package com.mancel.yann.go4lunch.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.Details;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.notifications.Go4LunchNotification;
import com.mancel.yann.go4lunch.repositories.PlaceRepository;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.UserRepository;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;
import com.mancel.yann.go4lunch.utils.DetailsUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Yann MANCEL on 31/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.workers
 *
 * A {@link ListenableWorker} subclass
 */
public class AsyncWorker extends ListenableWorker {

    /*
        Call structure:

            BEGIN:  Current user
                        -> [place id and name of selected restaurant]
            THEN:   All users who ave selected this restaurant
                        -> [users list]
            THEN:   Details of restaurant
                        -> [address of this restaurant]
     */

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final UserRepository mUserRepository;

    @NonNull
    private final PlaceRepository mPlaceRepository;

    @Nullable
    private String mUidOfCurrentUser = null;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private List<User> mUsers;

    @Nullable
    private String mNameOfRestaurant = null;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private String mAddressOfRestaurant;

    @Nullable
    private Disposable mDisposable = null;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param context       a {@link Context}
     * @param workerParams  a {@link WorkerParameters}
     */
    public AsyncWorker(@NonNull Context context,
                       @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.mUserRepository = new UserRepositoryImpl();
        this.mPlaceRepository = new PlaceRepositoryImpl();
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ListenableWorker --

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture( completer -> {
            // Creates a callback interface to receive the user's data
            final CallbackWorker callback = new CallbackWorker() {
                @Override
                public void onSuccess(@Nullable final String nameOfRestaurant,
                                      @Nullable final String addressOfRestaurant,
                                      @Nullable final List<User> users) {
                    final String message;

                    // No restaurant selected for current user
                    if (nameOfRestaurant == null || addressOfRestaurant == null || users == null) {
                        message = getApplicationContext().getString(R.string.no_restaurant_notification);
                    }
                    else if (users.size() == 1) {
                        message = getApplicationContext().getString(R.string.only_one_user_notification,
                                                                    nameOfRestaurant,
                                                                    addressOfRestaurant);
                    }
                    else {
                        final StringBuilder stringBuilder = new StringBuilder();

                        for (User user : users) {
                            // All users except the current user
                            if (!user.getUid().equals(mUidOfCurrentUser)) {
                                final String username = "- " + user.getUsername() + "\n";
                                stringBuilder.append(username);
                            }
                        }

                        message = getApplicationContext().getString(R.string.not_alone_notification,
                                                                    nameOfRestaurant,
                                                                    addressOfRestaurant,
                                                                    stringBuilder.toString());
                    }

                    // Notification
                    Go4LunchNotification.sendVisualNotification(getApplicationContext(),
                                                             message);

                    completer.set(Result.success());
                }

                @Override
                public void onFailure(@NonNull final Exception exception) {
                    completer.setException(exception);
                }
            };

            // Data (Input)
            this.mUidOfCurrentUser = this.getInputData().getString(WorkerController.DATA_UID_CURRENT_USER);

            // Asynchronous method
            this.fetchCurrentUser(this.mUidOfCurrentUser, callback);

            return callback;
        });
    }

    // -- User --

    /**
     * Fetches the current user in asynchronous way from Firebase Firestore
     * @param uidOfUser a {@link String} that contains the Uid of user from Firebase Authentication
     * @param callback  a {@link CallbackWorker} interface
     */
    private void fetchCurrentUser(@Nullable final String uidOfUser,
                                  @NonNull final CallbackWorker callback) {
        // Uid of current user is null
        if (uidOfUser == null) {
            callback.onFailure(new Exception("AsyncWorker#fetchCurrentUser: Uid of current user is null"));
            return;
        }

        // Current user from Firebase Firestore
        this.mUserRepository.getUser(uidOfUser)
                            .addOnSuccessListener( documentSnapshot -> {
                                final User user = documentSnapshot.toObject(User.class);

                                if (user != null) {
                                    this.fetchAllUsersFromRestaurant(user,
                                                                     callback);
                                }
                                else {
                                    callback.onFailure(new Exception("AsyncWorker#fetchCurrentUser: current user is not present into Firebase Firestore"));
                                }

                            })
                            .addOnFailureListener(
                                callback::onFailure
                            );
    }

    /**
     * Fetches all users who have selected the restaurant in asynchronous way from Firebase Firestore
     * @param currentUser   a {@link User} that contains the current user
     * @param callback      a {@link CallbackWorker} interface
     */
    private void fetchAllUsersFromRestaurant(@NonNull final User currentUser,
                                             @NonNull final CallbackWorker callback) {
        // Place Id of restaurant is null
        if (currentUser.getPlaceIdOfRestaurant() == null) {
            callback.onSuccess(null, null, null);
            return;
        }

        this.mNameOfRestaurant = currentUser.getNameOfRestaurant();

        // All users who have selected the restaurant from Firebase Firestore
        this.mUserRepository.getAllUsersFromThisRestaurant(currentUser.getPlaceIdOfRestaurant())
                            .get()
                            .addOnSuccessListener( queryDocumentSnapshots -> {
                                List<User> users = new ArrayList<>();

                                if (queryDocumentSnapshots != null) {
                                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                        if (doc != null) {
                                            users.add(doc.toObject(User.class));
                                        }
                                    }
                                }

                                this.mUsers = users;

                                this.fetchDetailsOfRestaurant(currentUser.getPlaceIdOfRestaurant(),
                                                              callback);
                            })
                            .addOnFailureListener(
                                callback::onFailure
                            );
    }


    /**
     * Fetches the {@link Details} of the restaurant in asynchronous way from Google Maps
     * @param placeIdOfRestaurant   a {@link String} that contains the Place Id of the restaurant
     * @param callback              a {@link CallbackWorker} interface
     */
    private void fetchDetailsOfRestaurant(@Nullable final String placeIdOfRestaurant,
                                          @NonNull final CallbackWorker callback) {
        // Retrieves Google Maps Key
        final String key = this.getApplicationContext().getResources()
                                                       .getString(R.string.google_maps_key);

        // Observable
        final Observable<Details> observable;
        observable = this.mPlaceRepository.getStreamToFetchDetails(placeIdOfRestaurant, key);

        // Stream (Observable + observer)
        this.mDisposable = observable.subscribeWith(new DisposableObserver<Details>() {
                                                   @Override
                                                   public void onNext(Details details) {
                                                       // Avoid memory leaks
                                                       if (mDisposable != null && !mDisposable.isDisposed()) {
                                                           mDisposable.dispose();
                                                       }

                                                       // After-treatment of address of restaurant
                                                       mAddressOfRestaurant = DetailsUtils.createStringOfAddress(getApplicationContext(),
                                                                                                                 details.getResult().getAddressComponents());

                                                       callback.onSuccess(mNameOfRestaurant,
                                                                          mAddressOfRestaurant,
                                                                          mUsers);
                                                   }

                                                   @Override
                                                   public void onError(Throwable e) {
                                                       // Avoid memory leaks
                                                       if (mDisposable != null && !mDisposable.isDisposed()) {
                                                           mDisposable.dispose();
                                                       }

                                                       callback.onFailure(new Exception("AsyncWorker#fetchDetailsOfRestaurant: " + e.getMessage()));
                                                   }

                                                   @Override
                                                   public void onComplete() {
                                                       // Do nothing
                                                   }
                                               });
    }
}
