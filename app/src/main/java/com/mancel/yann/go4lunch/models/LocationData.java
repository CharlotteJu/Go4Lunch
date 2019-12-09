package com.mancel.yann.go4lunch.models;

import android.location.Location;

import androidx.annotation.Nullable;

/**
 * Created by Yann MANCEL on 09/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.models
 */
public class LocationData {

    // FIELDS --------------------------------------------------------------------------------------

    @Nullable
    private Location mLocation;
    @Nullable
    private Exception mException;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param location  a {@link Location}
     * @param exception an {@link Exception}
     */
    public LocationData(@Nullable Location location, @Nullable Exception exception) {
        this.mLocation = location;
        this.mException = exception;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- Getter --

    @Nullable
    public Location getLocation() {
        return this.mLocation;
    }
    @Nullable
    public Exception getException() {
        return this.mException;
    }
}
