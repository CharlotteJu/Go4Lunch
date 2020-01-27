package com.mancel.yann.go4lunch.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.PropertyName;

import java.util.Objects;

/**
 * Created by Yann MANCEL on 12/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.models
 *
 * Pojo class to Firebase Firestore which implements {@link Parcelable}.
 */
public class User implements Parcelable {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private String mUid;
    @NonNull
    private String mUsername;
    @Nullable
    private String mUrlPicture = null;
    @Nullable
    private String mPlaceIdOfRestaurant = null;
    @Nullable
    private String mNameOfRestaurant = null;
    @Nullable
    private String mFoodTypeOfRestaurant = null;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor by default
     * It is this constructor that Firebase Firestore uses.
     */
    public User() {
        this.mUid = "";
        this.mUsername = "";
    }

    /**
     * Constructor
     * @param uid           a {@link String} that contains the uid
     * @param username      a {@link String} that contains the username
     * @param urlPicture    a {@link String} that contains the Url of the picture
     */
    public User(@NonNull final String uid,
                @NonNull final String username,
                @Nullable final String urlPicture) {
        this.mUid = uid;
        this.mUsername = username;
        this.mUrlPicture = urlPicture;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- Parcelable interface --

    protected User(Parcel in) {
        this.mUid = in.readString();
        this.mUsername = in.readString();
        this.mUrlPicture = in.readString();
        this.mPlaceIdOfRestaurant = in.readString();
        this.mNameOfRestaurant = in.readString();
        this.mFoodTypeOfRestaurant = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUid);
        dest.writeString(this.mUsername);
        dest.writeString(this.mUrlPicture);
        dest.writeString(this.mPlaceIdOfRestaurant);
        dest.writeString(this.mNameOfRestaurant);
        dest.writeString(this.mFoodTypeOfRestaurant);
    }

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

    @PropertyName("place_id_of_restaurant")
    @Nullable
    public String getPlaceIdOfRestaurant() {
        return this.mPlaceIdOfRestaurant;
    }

    @PropertyName("name_of_restaurant")
    @Nullable
    public String getNameOfRestaurant() {
        return this.mNameOfRestaurant;
    }

    @PropertyName("food_type_of_restaurant")
    @Nullable
    public String getFoodTypeOfRestaurant() {
        return this.mFoodTypeOfRestaurant;
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

    @PropertyName("place_id_of_restaurant")
    public void setPlaceIdOfRestaurant(@Nullable String placeIdOfRestaurant) {
        this.mPlaceIdOfRestaurant = placeIdOfRestaurant;
    }

    @PropertyName("name_of_restaurant")
    public void setNameOfRestaurant(@Nullable String nameOfRestaurant) {
        this.mNameOfRestaurant = nameOfRestaurant;
    }

    @PropertyName("food_type_of_restaurant")
    public void setFoodTypeOfRestaurant(@Nullable String foodTypeOfRestaurant) {
        this.mFoodTypeOfRestaurant = foodTypeOfRestaurant;
    }

    // -- Comparison --

    @Override
    public boolean equals(@Nullable Object obj) {
        // Same address
        if (this == obj) return true;

        // Null or the class is different
        if (obj == null || getClass() != obj.getClass()) return false;

        // Cast Object to User
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
                          ", mPlaceIdOfRestaurant=" + this.mPlaceIdOfRestaurant +
                          ", mNameOfRestaurant=" + this.mNameOfRestaurant +
                          ", mFoodTypeOfRestaurant=" + this.mFoodTypeOfRestaurant +
                "]";
    }

    // -- Copy --

    /**
     * Copies the fields of the {@link Object} in argument
     * @param obj a {@link Object}
     * @return a boolean that is true if the copy is performed
     */
    public boolean copy(@Nullable Object obj) {
        // Same address
        if (this == obj) return false;

        // Null or the class is different
        if (obj == null || getClass() != obj.getClass()) return false;

        // Cast Object to User
        final User user = (User) obj;

        // Copy
        this.mUid = user.mUid;
        this.mUsername = user.mUsername;
        this.mUrlPicture = user.mUrlPicture;
        this.mPlaceIdOfRestaurant = user.mPlaceIdOfRestaurant;
        this.mNameOfRestaurant = user.mNameOfRestaurant;
        this.mFoodTypeOfRestaurant = user.mFoodTypeOfRestaurant;

        return true;
    }
}
