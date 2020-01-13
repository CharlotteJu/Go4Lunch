package com.mancel.yann.go4lunch;

import com.mancel.yann.go4lunch.models.Details;
import com.mancel.yann.go4lunch.models.DistanceMatrix;
import com.mancel.yann.go4lunch.models.Restaurant;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.utils.RestaurantUtils;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Yann MANCEL on 13/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch
 *
 * Test on {@link RestaurantUtils}.
 */
public class RestaurantUtilsTest {

    // METHODS -------------------------------------------------------------------------------------

    @Test
    public void updateRestaurantsWithUsers_Should_Be_Success() {
        // Details (Result class is in static to allow the Details construction)
        final Details details = new Details();
        details.setResult(new Details.Result());
        details.getResult().setPlaceId("DummyPlaceId");

        // Restaurants
        final List<Restaurant> restaurants = Collections.singletonList(new Restaurant(details, new DistanceMatrix()));

        // User with the same Place Id that the restaurant (singleton)
        final User userWithSamePlaceId = new User();
        userWithSamePlaceId.setPlaceIdOfRestaurant("DummyPlaceId");

        // User with another Place Id that the restaurant (singleton)
        final User userWithAnotherPlaceId = new User();
        userWithAnotherPlaceId.setPlaceIdOfRestaurant("------");

        // Users
        final List<User> users = Arrays.asList(userWithSamePlaceId,
                                               userWithAnotherPlaceId,
                                               new User());

        // Result
        final List<Restaurant> results = RestaurantUtils.updateRestaurantsWithUsers(restaurants, users);

        // TEST: Number of users
        assertEquals(results.get(0).getNumberOfUsers(), 1);
    }
}
