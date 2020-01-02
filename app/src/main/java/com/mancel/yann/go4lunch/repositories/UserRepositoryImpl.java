package com.mancel.yann.go4lunch.repositories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mancel.yann.go4lunch.models.User;

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

        users
        |
        +--- user1 (its uid value)
             |
             +--- uid
             +--- username
             +--- url_picture
             +--- place_id_of_restaurant
             +--- name_of_restaurant
             +--- food_type_of_restaurant
     */

    // FIELDS --------------------------------------------------------------------------------------

    private static final String COLLECTION_USERS = "users";

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
    public Task<Void> createUser(@NonNull String uid, @NonNull String username, @Nullable String urlPicture) {
        final User user = new User(uid, username, urlPicture);
        return this.getUsersCollection().document(uid)
                                        .set(user);
    }

    // -- Read --

    @NonNull
    @Override
    public Task<DocumentSnapshot> getUser(@NonNull String uid) {
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
    public Query getAllUsersFromThisRestaurant(@Nullable String placeIdOfRestaurant) {
        return this.getUsersCollection().whereEqualTo("place_id_of_restaurant", placeIdOfRestaurant);
    }

    // -- Update --

    @NonNull
    @Override
    public Task<Void> updateUsername(@NonNull String uid, @NonNull String username) {
        return this.getUsersCollection().document(uid)
                                        .update("username", username);
    }

    @NonNull
    @Override
    public Task<Void> updateRestaurant(@NonNull String uid, @Nullable String placeIdOfRestaurant,
                                       @Nullable String nameOfRestaurant,@Nullable String foodTypeOfRestaurant) {
        return this.getUsersCollection().document(uid)
                                        .update("place_id_of_restaurant", placeIdOfRestaurant,
                                                "name_of_restaurant", nameOfRestaurant,
                                                "food_type_of_restaurant", foodTypeOfRestaurant);
    }

    // -- Delete --

    @NonNull
    @Override
    public Task<Void> deleteUser(@NonNull String uid) {
        return this.getUsersCollection().document(uid)
                                        .delete();
    }
}
