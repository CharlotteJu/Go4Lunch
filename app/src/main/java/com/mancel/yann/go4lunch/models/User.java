package com.mancel.yann.go4lunch.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.PropertyName;

import java.util.Objects;

/**
 * Created by Yann MANCEL on 12/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.models
 *
 * Pojo class to Firebase Firestore.
 */
public class User {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private String mUid;
    @NonNull
    private String mUsername;
    @Nullable
    private String mUrlPicture;
    @Nullable
    private String mSelectedRestaurant;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor by default
     * It is this constructor that Firebase Firestore uses.
     */
    public User() {
        this.mUid = "";
        this.mUsername = "";
        this.mUrlPicture = null;
        this.mSelectedRestaurant = null;
    }

    /**
     * Constructor
     * @param uid           a {@link String} that contains the uid
     * @param username      a {@link String} that contains the username
     * @param urlPicture    a {@link String} that contains the Url of the picture
     */
    public User(@NonNull String uid,
                @NonNull String username,
                @Nullable String urlPicture) {
        this.mUid = uid;
        this.mUsername = username;
        this.mUrlPicture = urlPicture;
        this.mSelectedRestaurant = null;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- Getter --

    @PropertyName("uid")
    @NonNull
    public String getUid() {
        return this.mUid;
    }

    @PropertyName("username")
    @NonNull
    public String getUsername() {
        return this.mUsername;
    }

    @PropertyName("url_picture")
    @Nullable
    public String getUrlPicture() {
        return this.mUrlPicture;
    }

    @PropertyName("selected_restaurant")
    @Nullable
    public String getSelectedRestaurant() {
        return this.mSelectedRestaurant;
    }

    // -- Setter --

    @PropertyName("uid")
    public void setUid(@NonNull String uid) {
        this.mUid = uid;
    }

    @PropertyName("username")
    public void setUsername(@NonNull String username) {
        this.mUsername = username;
    }

    @PropertyName("url_picture")
    public void setUrlPicture(@Nullable String urlPicture) {
        this.mUrlPicture = urlPicture;
    }

    @PropertyName("selected_restaurant")
    public void setSelectedRestaurant(@Nullable String selectedRestaurant) {
        this.mSelectedRestaurant = selectedRestaurant;
    }

    // -- Comparison --

    @Override
    public boolean equals(@Nullable Object obj) {
        // Same address
        if (this == obj) return true;

        // Null or the class is different
        if (obj == null || getClass() != obj.getClass()) return false;

        // Cast Object to Meeting
        final User user = (User) obj;

        return Objects.equals(this.mUid, user.mUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mUid);
    }

    // -- Display --

    @NonNull
    @Override
    public String toString() {
        return "User [" + "mUid=" + this.mUid +
                          ", mUsername=" + this.mUsername +
                          ", mUrlPicture=" + this.mUrlPicture +
                          ", mSelectedRestaurant=" + this.mSelectedRestaurant +
                "]";
    }
}
