package com.mancel.yann.go4lunch.repositories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.mancel.yann.go4lunch.models.User;

import javax.inject.Inject;

/**
 * Created by Yann MANCEL on 12/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.repositories
 *
 * A class which implements {@link UserRepository}.
 */
public class UserRepositoryImpl implements UserRepository {

    /*
        Firestore structure:

        users ........................................ [Collection]
        |
        +--- user1 (its uid value) ................... [Document]
             |
             +--- uid ................................ [Data]
             +--- username ........................... [Data]
             +--- url_picture ........................ [Data]
             +--- place_id_of_restaurant ............. [Data]
             +--- name_of_restaurant ................. [Data]
             +--- food_type_of_restaurant ............ [Data]
     */

    // FIELDS --------------------------------------------------------------------------------------

    private static final String COLLECTION_USERS = "users";

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor for Dagger 2
     */
    @Inject
    public UserRepositoryImpl() {}

    // METHODS -------------------------------------------------------------------------------------

    // -- CollectionReference --

    @NonNull
    @Override
    public CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance()
                                .collection(COLLECTION_USERS);
    }

    // -- Create --

    @NonNull
    @Override
    public Task<Void> createUser(@NonNull final String uid,
                                 @NonNull final String username,
                                 @Nullable final String urlPicture) {
        final User user = new User(uid, username, urlPicture);
        return this.getUsersCollection().document(uid)
                                        .set(user, SetOptions.merge());
    }

    // -- Read --

    @NonNull
    @Override
    public Task<DocumentSnapshot> getUser(@NonNull final String uid) {
        return this.getUsersCollection().document(uid)
                                        .get();
    }

    @NonNull
    @Override
    public Query getAllUsers() {
        return this.getUsersCollection().orderBy("username", Query.Direction.DESCENDING)
                                        .limit(50);
    }

    @NonNull
    @Override
    public Query getAllUsersFromThisRestaurant(@Nullable final String placeIdOfRestaurant) {
        return this.getUsersCollection().whereEqualTo("place_id_of_restaurant", placeIdOfRestaurant);
    }

    // -- Update --

    @NonNull
    @Override
    public Task<Void> updateUsername(@NonNull final String uid,
                                     @NonNull final String username) {
        return this.getUsersCollection().document(uid)
                                        .update("username", username);
    }

    @NonNull
    @Override
    public Task<Void> updateRestaurant(@NonNull final String uid,
                                       @Nullable final String placeIdOfRestaurant,
                                       @Nullable final String nameOfRestaurant,
                                       @Nullable final String foodTypeOfRestaurant) {
        return this.getUsersCollection().document(uid)
                                        .update("place_id_of_restaurant", placeIdOfRestaurant,
                                                "name_of_restaurant", nameOfRestaurant,
                                                "food_type_of_restaurant", foodTypeOfRestaurant);
    }

    // -- Delete --

    @NonNull
    @Override
    public Task<Void> deleteUser(@NonNull final String uid) {
        return this.getUsersCollection().document(uid)
                                        .delete();
    }
}
