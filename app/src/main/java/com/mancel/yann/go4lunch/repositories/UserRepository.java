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
 * A class which implements {@link Repository.UserRepositoryInterface}.
 */
public class UserRepository implements Repository.UserRepositoryInterface {

    /*
        Firestore structure:

        users
        |
        +--- user1 (its uid value)
             |
             +--- uid
             +--- username
             +--- url_picture
             +--- selected_restaurant
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
        return this.getUsersCollection().orderBy("username")
                                        .limit(50);
    }

    // -- Update --

    @NonNull
    @Override
    public Task<Void> updateRestaurant(@NonNull String uid, @Nullable String restaurant) {
        return this.getUsersCollection().document(uid)
                                        .update("selected_restaurant", restaurant);
    }

    // -- Delete --

    @NonNull
    @Override
    public Task<Void> deleteUser(@NonNull String uid) {
        return this.getUsersCollection().document(uid)
                                        .delete();
    }
}
