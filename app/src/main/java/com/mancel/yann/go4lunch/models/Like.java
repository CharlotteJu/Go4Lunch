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
public class Like {

    /*
        Firestore annotation:
            - PropertyName:     Marks a field to be renamed when serialized.
     */

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private String mUid;

    @NonNull
    private String mPlaceIdOfRestaurant;

    private double mRatingOfRestaurant = 0.0;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor by default
     * It is this constructor that Firebase Firestore uses.
     */
    public Like() {
        this.mUid = "";
        this.mPlaceIdOfRestaurant = "";
    }

    /**
     * Constructor
     * @param uid                   a {@link String} that contains the user's uid
     * @param placeIdOfRestaurant   a {@link String} that contains the place_id of the restaurant
     * @param rating                an integer that contains the user's rating of the restaurant
     */
    public Like(@NonNull final String uid,
                @NonNull final String placeIdOfRestaurant,
                final double rating) {
        this.mUid = uid;
        this.mPlaceIdOfRestaurant = placeIdOfRestaurant;
        this.mRatingOfRestaurant = rating;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- Getter --

    @NonNull
    @PropertyName("uid")
    public String getUid() {
        return this.mUid;
    }

    @NonNull
    @PropertyName("place_id_of_restaurant")
    public String getPlaceIdOfRestaurant() {
        return this.mPlaceIdOfRestaurant;
    }

    @PropertyName("rating_of_restaurant")
    public double getRatingOfRestaurant() {
        return this.mRatingOfRestaurant;
    }

    // -- Setter --

    @PropertyName("uid")
    public void setUid(@NonNull final String uid) {
        this.mUid = uid;
    }

    @PropertyName("place_id_of_restaurant")
    public void setPlaceIdOfRestaurant(@NonNull final String placeIdOfRestaurant) {
        this.mPlaceIdOfRestaurant = placeIdOfRestaurant;
    }

    @PropertyName("rating_of_restaurant")
    public void setRatingOfRestaurant(final double ratingOfRestaurant) {
        this.mRatingOfRestaurant = ratingOfRestaurant;
    }

    // -- Comparison --

    @Override
    public boolean equals(@Nullable Object obj) {
        // Same address
        if (this == obj) return true;

        // Null or the class is different
        if (obj == null || getClass() != obj.getClass()) return false;

        // Cast Object to Like
        final Like like = (Like) obj;

        return Objects.equals(this.mUid, like.mUid)                                 &&
               Objects.equals(this.mPlaceIdOfRestaurant, like.mPlaceIdOfRestaurant) &&
               Objects.equals(this.mRatingOfRestaurant, like.mRatingOfRestaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mUid, this.mPlaceIdOfRestaurant);
    }
}
