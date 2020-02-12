package com.mancel.yann.go4lunch.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.mancel.yann.go4lunch.models.Like;

/**
 * Created by Yann MANCEL on 27/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.repositories
 *
 * A class which implements {@link LikeRepository}.
 */
public class LikeRepositoryImpl implements LikeRepository {

    /*
        Firestore structure:

        users .......................................................... [Collection]
        |
        +--- user1 (its uid value) ..................................... [Document]
             |
             +--- uid .................................................. [Data]
             +--- username ............................................. [Data]
             +--- url_picture .......................................... [Data]
             +--- place_id_of_restaurant ............................... [Data]
             +--- name_of_restaurant ................................... [Data]
             +--- food_type_of_restaurant .............................. [Data]
             |
             +--- likes ................................................ [Collection]
                  |
                  +--- like1 (its place_id_of_restaurant value) ........ [Document]
                       |
                       +--- uid ........................................ [Data]
                       +--- place_id_of_restaurant ..................... [Data]
                       +--- rating_of_restaurant ....................... [Data]
     */

    // FIELDS --------------------------------------------------------------------------------------

    private static final String COLLECTION_USERS = "users";
    private static final String COLLECTION_LIKES = "likes";

    // METHODS -------------------------------------------------------------------------------------

    // -- CollectionReference --

    @NonNull
    @Override
    public CollectionReference getLikesCollection(@NonNull final String uid) {
        return FirebaseFirestore.getInstance()
                                .collection(COLLECTION_USERS)
                                .document(uid)
                                .collection(COLLECTION_LIKES);
    }

    // -- Create --

    @NonNull
    @Override
    public Task<Void> createLike(@NonNull final String uid,
                                 @NonNull final String placeIdOfRestaurant,
                                 final double rating) {
        final Like likeToCreate = new Like(uid, placeIdOfRestaurant, rating);
        return this.getLikesCollection(uid).document(placeIdOfRestaurant)
                                           .set(likeToCreate, SetOptions.merge());
    }

    // -- Read --

    @NonNull
    @Override
    public Query getAllLikesForUser(@NonNull final String uid) {
        return this.getLikesCollection(uid).limit(50);
    }
}
