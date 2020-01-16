package com.mancel.yann.go4lunch;

import com.mancel.yann.go4lunch.models.NearbySearch;
import com.mancel.yann.go4lunch.models.POI;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.utils.NearbySearchUtils;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Yann MANCEL on 13/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch
 *
 * Test on {@link NearbySearchUtils}.
 */
public class NearbySearchUtilsTest {

    // METHODS -------------------------------------------------------------------------------------

    @Test
    public void updatePOIs_Should_Be_Success() {
        // Location (No useful for the tests but required
        final NearbySearch.Location location = new NearbySearch.Location();
        location.setLat(0.0);
        location.setLng(0.0);

        // Geometry (No useful for the tests but required
        final NearbySearch.Geometry geometry = new NearbySearch.Geometry();
        geometry.setLocation(location);

        // Result 1
        final NearbySearch.Result result1 = new NearbySearch.Result();
        result1.setName("Name 1");
        result1.setPlaceId("Dummy PlaceId 1");
        result1.setGeometry(geometry);

        // Result 2
        final NearbySearch.Result result2 = new NearbySearch.Result();
        result2.setName("Name 2");
        result2.setPlaceId("Dummy PlaceId 2");
        result2.setGeometry(geometry);

        // Result 3
        final NearbySearch.Result result3 = new NearbySearch.Result();
        result3.setName("Name 3");
        result3.setPlaceId("Dummy PlaceId 3");
        result3.setGeometry(geometry);

        // Results
        final List<NearbySearch.Result> results = Arrays.asList(result1, result2, result3);

        // NearbySearch (Result class is in static to allow the NearbySearch construction)
        final NearbySearch nearbySearch = new NearbySearch();
        nearbySearch.setResults(results);

        // User with the same Place Id that the restaurant 1
        final User userWithSamePlaceId1 = new User();
        userWithSamePlaceId1.setPlaceIdOfRestaurant("Dummy PlaceId 1");

        // User with the same Place Id that the restaurant 3
        final User userWithSamePlaceId2 = new User();
        userWithSamePlaceId2.setPlaceIdOfRestaurant("Dummy PlaceId 3");

        // User with another Place Id that the restaurant
        final User userWithAnotherPlaceId = new User();
        userWithAnotherPlaceId.setPlaceIdOfRestaurant("------");

        // Users
        final List<User> users = Arrays.asList(userWithSamePlaceId1,
                                               userWithAnotherPlaceId,
                                               new User(),
                                               userWithSamePlaceId2);

        // POIs
        final List<POI> pois = NearbySearchUtils.updatePOIs(nearbySearch, users);

        // TEST: Same size
        assertEquals(nearbySearch.getResults().size(), pois.size());
        assertEquals(pois.size(), 3);

        // TEST: To know if at least one user has selected the restaurant
        assertTrue(pois.get(0).getIsSelected());
        assertFalse(pois.get(1).getIsSelected());
        assertTrue(pois.get(2).getIsSelected());
    }
}
