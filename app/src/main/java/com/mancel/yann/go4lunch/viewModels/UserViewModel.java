package com.mancel.yann.go4lunch.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.repositories.UserRepository;

/**
 * Created by Yann MANCEL on 10/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.viewModels
 *
 * A {@link ViewModel} subclass.
 */
public class UserViewModel extends ViewModel {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private UserRepository mUserRepository;

    private static final String TAG = UserViewModel.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with an argument
     * @param userRepository a {@link UserRepository}
     */
    public UserViewModel(@NonNull UserRepository userRepository) {
        Log.d(TAG, "UserViewModel");

        this.mUserRepository = userRepository;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ViewModel --

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared");
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
                                    Log.d(TAG, "----> user is already present: " + user.toString());
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
     * @param uid       a {@link String} that contains the uid
     * @param restaurant  a {@link String} that contains the restaurant
     */
    public void updateRestaurant(@NonNull String uid, @NonNull String restaurant) {
        Log.d(TAG, "updateRestaurant: " + uid);
        this.mUserRepository.updateRestaurant(uid, restaurant)
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
