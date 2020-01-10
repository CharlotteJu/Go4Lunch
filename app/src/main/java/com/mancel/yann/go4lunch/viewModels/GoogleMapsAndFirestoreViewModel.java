package com.mancel.yann.go4lunch.viewModels;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.liveDatas.RestaurantsLiveData;
import com.mancel.yann.go4lunch.liveDatas.UsersLiveData;
import com.mancel.yann.go4lunch.models.Restaurant;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.repositories.PlaceRepository;
import com.mancel.yann.go4lunch.repositories.UserRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Yann MANCEL on 09/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.viewModels
 *
 * A {@link ViewModel} subclass.
 */
public class GoogleMapsAndFirestoreViewModel extends ViewModel {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final UserRepository mUserRepository;

    @NonNull
    private final PlaceRepository mPlaceRepository;

    @Nullable
    private UsersLiveData mUsersLiveData = null;

    @Nullable
    private RestaurantsLiveData mRestaurantsLiveData = null;

    private static final String TAG = GoogleMapsAndFirestoreViewModel.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with 2 repositories
     * @param userRepository    a {@link UserRepository} for data from Firebase Firestore
     * @param placeRepository   a {@link PlaceRepository} for data from Google Maps
     */
    public GoogleMapsAndFirestoreViewModel(@NonNull final UserRepository userRepository,
                                           @NonNull final PlaceRepository placeRepository) {
        Log.d(TAG, "GoogleMapsAndFirestoreViewModel");

        this.mUserRepository = userRepository;
        this.mPlaceRepository = placeRepository;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ViewModel --

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared");
    }

    // -- UsersLiveData --

    /**
     * Gets all users from Firebase Firestore
     * @return a {@link UsersLiveData}
     */
    @NonNull
    public UsersLiveData getUsers() {
        if (this.mUsersLiveData == null) {
            this.mUsersLiveData = new UsersLiveData(this.mUserRepository.getAllUsers());
        }

        return this.mUsersLiveData;
    }

    // -- RestaurantsLiveData --

    /**
     * Gets all restaurants from Google Maps
     * @param context a {@link Context}
     * @return a {@link RestaurantsLiveData}
     */
    @NonNull
    public RestaurantsLiveData getRestaurants(@NonNull final Context context) {
        if (this.mRestaurantsLiveData == null) {
            this.mRestaurantsLiveData = new RestaurantsLiveData();
        }

        // Fetches the restaurants
        this.loadRestaurants(context);

        return this.mRestaurantsLiveData;
    }

    /**
     * Loads the restaurant
     * @param context a {@link Context}
     */
    public void loadRestaurants(@NonNull final Context context) {
        // Retrieves Google Maps Key
        final String key = context.getResources()
                                  .getString(R.string.google_maps_key);

        // Observable
        final Observable<List<Restaurant>> observable;
        observable = this.mPlaceRepository.getStreamToFetchNearbySearchThenToFetchRestaurants("45.9922027,4.7176896",
                                                                                              200.0,
                                                                                              "restaurant",
                                                                                              "walking",
                                                                                              "metric",
                                                                                               key);

        // Updates LiveData for the restaurants
        this.mRestaurantsLiveData.getRestaurantsWithObservable(observable);
    }

    // -- Create (User) --

    /**
     * Creates the current user (authenticated) of Firebase Firestore
     * thanks to {@link UserRepository}
     * @param currentUser a {@link FirebaseUser} that contains the data of the last authenticated
     * @throws Exception if the {@link FirebaseUser} is null
     */
    public void createUser(@Nullable final FirebaseUser currentUser) throws Exception {
        // FirebaseUser must not be null to create an user
        if (currentUser == null) throw new Exception("FirebaseUser is null");

        Log.d(TAG, "createUser: " + currentUser.getUid());
        this.mUserRepository.getUser(currentUser.getUid())
                            .addOnSuccessListener( documentSnapshot -> {
                                Log.d(TAG, "--> getUser (onSuccess)");

                                final User user = documentSnapshot.toObject(User.class);

                                if (user != null) {
                                    Log.d(TAG, "----> user is already present");
                                }
                                else {
                                    Log.d(TAG, "----> user is not present");

                                    // Firebase Firestore has checked that user is not present.
                                    // So we can create a new document (user) in the collection (users).
                                    this.mUserRepository.createUser(currentUser.getUid(),
                                                                    currentUser.getDisplayName(),
                                                                    currentUser.getPhotoUrl().toString())
                                                        .addOnSuccessListener( aVoid ->
                                                                Log.d(TAG, "--> createUser (onSuccess)")
                                                        )
                                                        .addOnFailureListener( e ->
                                                                Log.d(TAG, "--> createUser (onFailure): " + e.getMessage())
                                                        );

                                }
                            })
                            .addOnFailureListener( e ->
                                    Log.e(TAG, "--> getUser (onFailure): " + e.getMessage())
                            );
    }

    // -- Read (User) --

    /**
     * Gets a {@link User} with its uid field
     * @param uid a {@link String} that contains the uid
     * @return a {@link User}
     */
    @Nullable
    public User getUser(@NonNull String uid) {
        final User user = new User();

        Log.d(TAG, "getUser: " + uid);
        this.mUserRepository.getUser(uid)
                            .addOnSuccessListener( documentSnapshot -> {
                                Log.d(TAG, "--> deleteUser (onSuccess)");

                                final User userFromFirestore = documentSnapshot.toObject(User.class);

                                user.copy(userFromFirestore);
                            })
                            .addOnFailureListener( e ->
                                    Log.e(TAG, "--> getUser (onFailure): " + e.getMessage())
                            );

        return user;
    }

    /**
     * Gets all users into the collection
     * @return a {@link Query} that contains the users
     */
    @NonNull
    public Query getAllUsers() {
        return this.mUserRepository.getAllUsers();
    }

    /**
     * Gets all users  who have selected the same restaurant
     * If the argument is null, {@link Query} returns the users that do not selected a restaurant
     * @param placeIdOfRestaurant a {@link String} that contains the place_id of the restaurant
     * @return a {@link Query} that contains the users
     */
    @NonNull
    public Query getAllUsersFromThisRestaurant(@Nullable String placeIdOfRestaurant) {
        return this.mUserRepository.getAllUsersFromThisRestaurant(placeIdOfRestaurant);
    }

    // -- Update (User) --

    /**
     * Update the username field of a {@link com.mancel.yann.go4lunch.models.User} with its uid field
     * @param uid       a {@link String} that contains the uid
     * @param username  a {@link String} that contains the username
     */
    public void updateUsername(@NonNull String uid, @NonNull String username) {
        Log.d(TAG, "updateUsername: " + uid);
        this.mUserRepository.updateUsername(uid, username)
                            .addOnSuccessListener( aVoid ->
                                    Log.d(TAG, "--> updateUsername (onSuccess)")
                            )
                            .addOnFailureListener( e ->
                                    Log.e(TAG, "--> updateUsername (onFailure): " + e.getMessage())
                            );
    }

    /**
     * Update the restaurant field of a {@link com.mancel.yann.go4lunch.models.User} with its uid field
     * @param uid                   a {@link String} that contains the uid
     * @param placeIdOfRestaurant   a {@link String} that contains the place_id of the restaurant
     * @param nameOfRestaurant      a {@link String} that contains the name of the restaurant
     * @param foodTypeOfRestaurant  a {@link String} that contains the food type of the restaurant
     */
    public void updateRestaurant(@NonNull String uid, @Nullable String placeIdOfRestaurant,
                                 @Nullable String nameOfRestaurant,@Nullable String foodTypeOfRestaurant) {
        Log.d(TAG, "updateRestaurant: " + uid);
        this.mUserRepository.updateRestaurant(uid, placeIdOfRestaurant, nameOfRestaurant, foodTypeOfRestaurant)
                            .addOnSuccessListener( aVoid ->
                                    Log.d(TAG, "--> updateRestaurant (onSuccess)")
                            )
                            .addOnFailureListener( e ->
                                    Log.e(TAG, "--> updateRestaurant (onFailure): " + e.getMessage())
                            );
    }

    // -- Delete (User) --

    /**
     * Delete the current user (authenticated) of Firebase Firestore
     * thanks to {@link UserRepository}
     * @param currentUser a {@link FirebaseUser} that contains the data of the last authenticated
     * @throws Exception if the {@link FirebaseUser} is null
     */
    public void deleteUser(@Nullable final FirebaseUser currentUser) throws Exception {
        // FirebaseUser must not be null to create an user
        if (currentUser == null) throw new Exception("FirebaseUser is null");

        Log.d(TAG, "deleteUser: " + currentUser.getUid());
        this.mUserRepository.deleteUser(currentUser.getUid())
                            .addOnSuccessListener( aVoid ->
                                    Log.d(TAG, "--> deleteUser (onSuccess)")
                            )
                            .addOnFailureListener( e ->
                                    Log.e(TAG, "--> deleteUser (onFailure): " + e.getMessage())
                            );
    }










}
