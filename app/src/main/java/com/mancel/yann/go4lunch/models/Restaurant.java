package com.mancel.yann.go4lunch.models;

import androidx.annotation.NonNull;

/**
 * Created by Yann MANCEL on 05/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.models
 */
public class Restaurant {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private Details mDetails;

    @NonNull
    private DistanceMatrix mDistanceMatrix;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with 2 arguments
     * @param details           a {@link Details}
     * @param distanceMatrix    a {@link DistanceMatrix}
     */
    public Restaurant(@NonNull Details details, @NonNull DistanceMatrix distanceMatrix) {
        this.mDetails = details;
        this.mDistanceMatrix = distanceMatrix;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- Getter --

    @NonNull
    public Details getDetails() {
        return this.mDetails;
    }

    @NonNull
    public DistanceMatrix getDistanceMatrix() {
        return this.mDistanceMatrix;
    }
}
