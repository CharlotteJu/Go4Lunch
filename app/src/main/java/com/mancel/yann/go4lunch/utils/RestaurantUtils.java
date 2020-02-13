package com.mancel.yann.go4lunch.utils;

import androidx.annotation.NonNull;

import com.mancel.yann.go4lunch.models.Restaurant;
import com.mancel.yann.go4lunch.models.User;

import java.util.Iterator;
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
    public static float calculateRating(final float googleMapsRating) {
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
    public static String analyseOpeningHours(@NonNull final List<String> weekdayText,
                                             final int dayOfWeek) {
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

    /**
     * Updates a {@link List<Restaurant>} with a {@link List<User>}
     * @param restaurants   a {@link List<Restaurant>}
     * @param users         a {@link List<User>}
     * @return a {@link List<Restaurant>} that contains the couple {@link Restaurant} and the number of {@link User}
     */
    @NonNull
    public static synchronized List<Restaurant> updateRestaurantsWithUsers(@NonNull final List<Restaurant> restaurants,
                                                                           @NonNull final List<User> users) {
        final Iterator<Restaurant> iterator = restaurants.iterator();

        while (iterator.hasNext()) {
            final Restaurant restaurant = iterator.next();

            int nbUser = 0;
            for (User user : users) {
                // Place Id is null
                if (user.getPlaceIdOfRestaurant() == null) {
                    continue;
                }

                // Same Place Id
                if (user.getPlaceIdOfRestaurant().equals(restaurant.getDetails().getResult().getPlaceId())) {
                    nbUser++;
                }
            }

            restaurant.setNumberOfUsers(nbUser);
        }

        return restaurants;
    }
}
