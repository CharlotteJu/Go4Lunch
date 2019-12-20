package com.mancel.yann.go4lunch.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
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
                                    Log.e(TAG, "----> user is already present: " + user.toString());
                                }
                                else {
                                    Log.e(TAG, "----> user is not present");

                                    // Firebase Firestore has checked that user is not present.
                                    // So we can create a new document (user) in the collection (users).
                                    this.mUserRepository.createUser(currentUser.getUid(),
                                                                    currentUser.getDisplayName(),
                                                                    currentUser.getPhotoUrl().toString())
                                                        .addOnSuccessListener( aVoid ->
                                                            Log.e(TAG, "--> createUser (onSuccess)")
                                                        )
                                                        .addOnFailureListener( e ->
                                                            Log.e(TAG, "--> createUser (onFailure): " + e.getMessage())
                                                        );

                                }
                            })
                            .addOnFailureListener( e ->
                                Log.e(TAG, "--> getUser (onFailure): " + e.getMessage())
                            );
    }

    // -- Read (User) --

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
                                Log.e(TAG, "--> deleteUser (onSuccess)")
                            )
                            .addOnFailureListener( e ->
                                Log.e(TAG, "--> deleteUser (onFailure): " + e.getMessage())
                            );
    }
}
