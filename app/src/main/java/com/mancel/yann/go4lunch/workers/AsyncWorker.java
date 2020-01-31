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
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.notifications.AlarmNotification;
import com.mancel.yann.go4lunch.repositories.UserRepository;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yann MANCEL on 31/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.workers
 *
 * A {@link ListenableWorker} subclass
 */
public class AsyncWorker extends ListenableWorker {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final Context mContext;

    @Nullable
    private String mUidOfCurrentUser = null;

    @NonNull
    private final UserRepository mUserRepository;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param context       a {@link Context}
     * @param workerParams  a {@link WorkerParameters}
     */
    public AsyncWorker(@NonNull Context context,
                       @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.mContext = context;
        this.mUserRepository = new UserRepositoryImpl();
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
                                      @Nullable final List<User> users) {
                    final String message;

                    // No restaurant selected for current user
                    if (users == null) {
                        // You have not selected a restaurant.
                        message = mContext.getString(R.string.no_restaurant_notification);
                    }
                    else if (users.size() == 1) {
                        // You are the only one who selected this restaurant.
                        message = mContext.getString(R.string.only_one_user_notification,
                                                     nameOfRestaurant);
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

                        message = mContext.getString(R.string.not_alone_notification,
                                nameOfRestaurant,
                                stringBuilder.toString());
                    }

                    // Notification
                    AlarmNotification.sendVisualNotification(mContext,
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
            this.FetchCurrentUser(this.mUidOfCurrentUser, callback);

            return callback;
        });
    }

    // -- User --

    /**
     * Fetches the current user in asynchronous way
     * @param uidOfUser a {@link String} that contains the Uid of user from Firebase Authentication
     * @param callback  a {@link CallbackWorker} interface
     */
    private void FetchCurrentUser(@Nullable final String uidOfUser,
                                  @NonNull final CallbackWorker callback) {
        // Uid of current user is null
        if (uidOfUser == null) {
            callback.onFailure(new Exception("AsyncWorker#getCurrentUser: Uid of current user is null"));
            return;
        }

        // Current user from Firebase Firestore
        this.mUserRepository.getUser(uidOfUser)
                            .addOnSuccessListener( documentSnapshot -> {
                                final User user = documentSnapshot.toObject(User.class);

                                if (user != null) {
                                    this.FetchAllUsersFromRestaurant(user.getPlaceIdOfRestaurant(),
                                                                     user.getNameOfRestaurant(),
                                                                     callback);
                                }
                                else {
                                    callback.onFailure(new Exception("AsyncWorker#getCurrentUser: current user is not present into Firebase Firestore"));
                                }

                            })
                            .addOnFailureListener( e ->
                                callback.onFailure(e)
                            );
    }

    /**
     * Fetches all users who have selected the restaurant in asynchronous way
     * @param placeIdOfRestaurant   a {@link String} that contains the Place Id of restaurant from Firebase Authentication
     * @param nameOfRestaurant      a {@link String} that contains the name of restaurant from Firebase Authentication
     * @param callback              a {@link CallbackWorker} interface
     */
    private void FetchAllUsersFromRestaurant(@Nullable final String placeIdOfRestaurant,
                                             @Nullable final String nameOfRestaurant,
                                             @NonNull final CallbackWorker callback) {
        // Place Id of restaurant is null
        if (placeIdOfRestaurant == null) {
            callback.onSuccess(nameOfRestaurant, null);
            return;
        }

        // All users who have selected the restaurant from Firebase Firestore
        this.mUserRepository.getAllUsersFromThisRestaurant(placeIdOfRestaurant)
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

                                callback.onSuccess(nameOfRestaurant, users);
                            })
                            .addOnFailureListener( e ->
                                callback.onFailure(e)
                            );
    }
}
