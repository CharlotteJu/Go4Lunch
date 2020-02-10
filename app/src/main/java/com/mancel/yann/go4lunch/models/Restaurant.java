package com.mancel.yann.go4lunch.models;

import androidx.annotation.NonNull;

import java.util.Comparator;

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

    int mNumberOfUsers = 0;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with 2 arguments
     * @param details           a {@link Details}
     * @param distanceMatrix    a {@link DistanceMatrix}
     */
    public Restaurant(@NonNull final Details details,
                      @NonNull final DistanceMatrix distanceMatrix) {
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

    public int getNumberOfUsers() {
        return this.mNumberOfUsers;
    }

    // -- Setter --

    public void setNumberOfUsers(int numberOfUsers) {
        this.mNumberOfUsers = numberOfUsers;
    }

    // INNER CLASSES -------------------------------------------------------------------------------

    /**
     * A class which implements {@link Comparator<Restaurant>}.
     */
    public static class AZComparator implements Comparator<Restaurant> {

        // METHODS ---------------------------------------------------------------------------------

        @Override
        public int compare(Restaurant o1, Restaurant o2) {
            // Comparison on the restaurant's name

            final String nameO1 = o1.mDetails.getResult().getName();
            final String nameO2 = o2.mDetails.getResult().getName();

            return nameO1.compareToIgnoreCase(nameO2);
        }
    }
}
