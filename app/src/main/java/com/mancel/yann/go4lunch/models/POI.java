package com.mancel.yann.go4lunch.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Yann MANCEL on 16/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.models
 */
public class POI {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final String mName;

    @NonNull
    private final String mPlaceId;

    private double mLatitude, mLongitude;

    @Nullable
    private final String mPhotoUrl;

    private boolean mIsSelected = false;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param name      a {@link String} that contains the name
     * @param placeId   a {@link String} that contains the place Id
     * @param latitude  a double that contains the latitude value
     * @param longitude a double that contains the longitude value
     * @param photoUrl  a {@link String} that contains the url of photo
     */
    public POI(@NonNull String name,
               @NonNull String placeId,
               double latitude,
               double longitude,
               @Nullable String photoUrl) {
        this.mName = name;
        this.mPlaceId = placeId;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mPhotoUrl = photoUrl;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- Getters --

    @NonNull
    public String getName() {
        return this.mName;
    }

    @NonNull
    public String getPlaceId() {
        return this.mPlaceId;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    @Nullable
    public String getPhotoUrl() {
        return this.mPhotoUrl;
    }

    public boolean getIsSelected() {
        return this.mIsSelected;
    }

    // -- Setters --

    public void setIsSelected(boolean selected) {
        this.mIsSelected = selected;
    }
}