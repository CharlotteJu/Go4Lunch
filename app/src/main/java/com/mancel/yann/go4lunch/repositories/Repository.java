package com.mancel.yann.go4lunch.repositories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * Created by Yann MANCEL on 12/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.repositories
 */
public interface Repository {

    // INTERFACES ----------------------------------------------------------------------------------

    interface UserRepository {

        // METHODS ---------------------------------------------------------------------------------

        // -- CollectionReference --

        /**
         * Gets the users collection
         * @return a {@link CollectionReference} that contains the reference of the users collection
         */
        @NonNull
        CollectionReference getUsersCollection();

        // -- Create --

        /**
         * Creates or sets a {@link com.mancel.yann.go4lunch.models.User} with its uid field
         * @param uid           a {@link String} that contains the uid
         * @param username      a {@link String} that contains the username
         * @param urlPicture    a {@link String} that contains the Url of the picture
         * @return a {@link Task} of {@link Void}
         */
        @NonNull
        Task<Void> createUser(@NonNull String uid, @NonNull String username, @Nullable String urlPicture);

        // -- Read --

        /**
         * Gets a {@link com.mancel.yann.go4lunch.models.User} with its uid field
         * @param uid a {@link String} that contains the uid
         * @return a {@link Task} of {@link DocumentSnapshot}
         */
        @NonNull
        Task<DocumentSnapshot> getUser(@NonNull String uid);

        /**
         * Gets all users into the collection
         * @return a {@link Query} taht contains the users
         */
        @NonNull
        Query getAllUsers();

        // -- Update --

        /**
         * Update the restaurant field of a {@link com.mancel.yann.go4lunch.models.User} with its uid field
         * @param uid           a {@link String} that contains the uid
         * @param restaurant    a {@link String} that contains the restaurant
         * @return a {@link Task} of {@link Void}
         */
        @NonNull
        Task<Void> updateRestaurant(@NonNull String uid, @Nullable String restaurant);

        // -- Delete --

        /**
         * Deletes a {@link com.mancel.yann.go4lunch.models.User} with its uid field
         * @param uid a {@link String} that contains the uid
         * @return a {@link Task} of {@link Void}
         */
        @NonNull
        Task<Void> deleteUser(@NonNull String uid);
    }
}