package com.mancel.yann.go4lunch.utils;

import androidx.annotation.NonNull;

import com.mancel.yann.go4lunch.models.Details;

/**
 * Created by Yann MANCEL on 06/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 */
public abstract class RestaurantUtils {

    // METHODS -------------------------------------------------------------------------------------

    // -- Rating --

    /**
     * Calculates the rating via the google maps rating (value on 5 stars)
     * @param googleMapsRating a float that contains the google maps rating (value on 5 stars)
     * @return a float that corresponds to the rating on 3 stars
     */
    public static float calculateRating(float googleMapsRating) {
        // Rating of Google Maps is on 5 stars

        //               4.6 on 5.0
        //                 ? on 3.0

        // So we have: ? = (3.0 * 4.6) / 5.0 = 2.76

        return (3.0F * googleMapsRating) / 5.0F;
    }

    // -- Opening hours --

    /**
     * Analyses the opening hours
     * @param openingHours a {@link Details.OpeningHours}
     * @return a {@link String} that displays if the restaurant is open and for how long
     */
    @NonNull
    public static String AnalyseOpeningHours(@NonNull final Details.OpeningHours openingHours) {

        return "test";
    }
}
