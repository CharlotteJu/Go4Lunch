package com.mancel.yann.go4lunch.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * Created by Yann MANCEL on 27/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.repositories
 */
public interface LikeRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- CollectionReference --

    /**
     * Gets the likes collection
     * @param uid a {@link String} that contains the user's uid
     * @return a {@link CollectionReference} that contains the reference of the likes collection
     */
    @NonNull
    CollectionReference getLikesCollection(@NonNull final String uid);

    // -- Create --

    /**
     * Creates or sets a {@link com.mancel.yann.go4lunch.models.Like}
     * @param uid                   a {@link String} that contains the user's uid
     * @param placeIdOfRestaurant   a {@link String} that contains the place_id of the restaurant
     * @param rating                a double that contains the user's rating of the restaurant
     * @return a {@link Task} of {@link Void}
     */
    @NonNull
    Task<Void> createLike(@NonNull final String uid,
                          @NonNull final String placeIdOfRestaurant,
                          final double rating);

    // -- Read --

    /**
     * Gets a {@link com.mancel.yann.go4lunch.models.Like} for specific user and for an specific
     * restaurant
     * @param uid                   a {@link String} that contains the user's uid
     * @param placeIdOfRestaurant   a {@link String} that contains the place_id of the restaurant
     * @return a {@link Task} of {@link DocumentSnapshot}
     */
    @NonNull
    Task<DocumentSnapshot> getLikeForUser(@NonNull final String uid,
                                          @NonNull final String placeIdOfRestaurant);

    /**
     * Gets all likes into the collection
     * @param uid a {@link String} that contains the user's uid
     * @return a {@link Query} that contains the likes
     */
    @NonNull
    Query getAllLikesForUser(@NonNull final String uid);
}
