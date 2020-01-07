package com.mancel.yann.go4lunch.utils;

import androidx.annotation.NonNull;

import java.util.List;

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
     * @param weekdayText a {@link List<String>}, each item corresponds to a day
     * @param dayOfWeek an integer that contains the day of the week
     * @return a {@link String} that displays if the restaurant is open and for how long
     */
    @NonNull
    public static String analyseOpeningHours(@NonNull final List<String> weekdayText, int dayOfWeek) {
        /*
            "weekday_text": [
                            "Monday: 12:00 – 1:30 PM",
                            "Tuesday: 12:00 – 1:30 PM, 7:00 – 9:30 PM",
                            "Wednesday: 12:00 – 1:30 PM, 7:00 – 9:30 PM",
                            "Thursday: 12:00 – 1:30 PM, 7:00 – 9:30 PM",
                            "Friday: 12:00 – 1:30 PM, 7:00 – 10:00 PM",
                            "Saturday: 12:00 – 1:30 PM, 7:00 – 10:00 PM",
                            "Sunday: Closed"
                            ]
         */

        // Get the opening hours for the current day
        //final String dayText = weekdayText.get(dayOfWeek);

        // TODO: 07/01/2020 to remove the day (ex Monday:)

        return weekdayText.get(dayOfWeek);
    }
}
