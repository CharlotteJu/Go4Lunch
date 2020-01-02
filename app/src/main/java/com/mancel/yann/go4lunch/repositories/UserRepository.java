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
public interface UserRepository {

    // METHODS -------------------------------------------------------------------------------------

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
     * @return a {@link Query} that contains the users
     */
    @NonNull
    Query getAllUsers();

    /**
     * Gets all users  who have selected the same restaurant
     * If the argument is null, {@link Query} returns the users that do not selected a restaurant
     * @param placeIdOfRestaurant a {@link String} that contains the place_id of the restaurant
     * @return a {@link Query} that contains the users
     */
    @NonNull
    Query getAllUsersFromThisRestaurant(@Nullable String placeIdOfRestaurant);

    // -- Update --

    /**
     * Update the username field of a {@link com.mancel.yann.go4lunch.models.User} with its uid field
     * @param uid       a {@link String} that contains the uid
     * @param username  a {@link String} that contains the username
     * @return a {@link Task} of {@link Void}
     */
    @NonNull
    Task<Void> updateUsername(@NonNull String uid, @NonNull String username);

    /**
     * Update the restaurant field of a {@link com.mancel.yann.go4lunch.models.User} with its uid field
     * @param uid                   a {@link String} that contains the uid
     * @param placeIdOfRestaurant   a {@link String} that contains the place_id of the restaurant
     * @param nameOfRestaurant      a {@link String} that contains the name of the restaurant
     * @param foodTypeOfRestaurant  a {@link String} that contains the food type of the restaurant
     * @return a {@link Task} of {@link Void}
     */
    @NonNull
    Task<Void> updateRestaurant(@NonNull String uid, @Nullable String placeIdOfRestaurant,
                                @Nullable String nameOfRestaurant,@Nullable String foodTypeOfRestaurant);

    // -- Delete --

    /**
     * Deletes a {@link com.mancel.yann.go4lunch.models.User} with its uid field
     * @param uid a {@link String} that contains the uid
     * @return a {@link Task} of {@link Void}
     */
    @NonNull
    Task<Void> deleteUser(@NonNull String uid);
}
