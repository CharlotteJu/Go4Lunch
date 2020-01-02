package com.mancel.yann.go4lunch;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mancel.yann.go4lunch.models.Details;
import com.mancel.yann.go4lunch.models.DistanceMatrix;
import com.mancel.yann.go4lunch.models.NearbySearch;
import com.mancel.yann.go4lunch.repositories.PlaceRepository;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Yann MANCEL on 18/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch
 *
 * Test on {@link PlaceRepository}.
 */
@RunWith(AndroidJUnit4.class)
public class PlaceRepositoryTest {

    // FIELDS --------------------------------------------------------------------------------------

    private PlaceRepository mPlaceRepository = new PlaceRepositoryImpl();

    // METHODS -------------------------------------------------------------------------------------

    @Test
    public void should_Fetch_NearbySearch() {
        // Retrieves Google Maps Key
        final String key = InstrumentationRegistry.getInstrumentation()
                                                  .getTargetContext()
                                                  .getResources()
                                                  .getString(R.string.google_maps_key);

        // Creates Observable
        final Observable<NearbySearch> observable = this.mPlaceRepository.getStreamToFetchNearbySearch("45.9922027,4.7176896",
                                                                                                       200.0,
                                                                                                       "restaurant",
                                                                                                        key);

        // Creates Observer
        final TestObserver<NearbySearch> observer = new TestObserver<>();

        // Creates Stream
        observable.subscribeWith(observer)
                  .assertNoErrors()
                  .assertNoTimeout()
                  .awaitTerminalEvent();

        // Fetches the result
        final NearbySearch nearbySearch = observer.values().get(0);

        // TEST: results's size
        assertEquals("results: Number of restaurant equals to 13", nearbySearch.getResults().size(), 13);

        // TEST: results[0]
        assertEquals("results[0] > [place_id]", nearbySearch.getResults().get(0).getPlaceId(), "ChIJn_OajiGF9EcRQFJ9o2prZ3w");
        assertEquals("results[0] > [name]", nearbySearch.getResults().get(0).getName(), "Charcutier Traiteur Vigne");
        assertTrue("results[0] > [rating]", nearbySearch.getResults().get(0).getRating() == 4.0);
        assertEquals("results[0] > [vicinity]", nearbySearch.getResults().get(0).getVicinity(), "269 Rue nationale, Villefranche-sur-Saône");
        assertTrue("results[0] > geometry > location > [lat]", nearbySearch.getResults().get(0).getGeometry().getLocation().getLat() == 45.992902);
        assertTrue("results[0] > geometry > location > [lng]", nearbySearch.getResults().get(0).getGeometry().getLocation().getLng() == 4.718997);

        // TEST: [status]
        assertEquals("[status]", nearbySearch.getStatus(), "OK");
    }

    @Test
    public void should_Fetch_Details() {
        // Retrieves Google Maps Key
        final String key = InstrumentationRegistry.getInstrumentation()
                                                  .getTargetContext()
                                                  .getResources()
                                                  .getString(R.string.google_maps_key);

        // Creates Observable
        final Observable<Details> observable = this.mPlaceRepository.getStreamToFetchDetails("ChIJp7JQEyKF9EcR72wJu3P1fUw",
                                                                                              key);

        // Creates Observer
        final TestObserver<Details> observer = new TestObserver<>();

        // Creates Stream
        observable.subscribeWith(observer)
                  .assertNoErrors()
                  .assertNoTimeout()
                  .awaitTerminalEvent();

        // Fetches the result
        final Details details = observer.values().get(0);

        // TEST: results > [name]
        assertEquals("results > [name]", details.getResult().getName(), "Pizzeria Al Dente");

        // TEST: results > geometry > location > [lat & lng]
        assertTrue("results > geometry > location > [lat]", details.getResult().getGeometry().getLocation().getLat() == 45.99138300000001);
        assertTrue("results > geometry > location > [lng]", details.getResult().getGeometry().getLocation().getLng() == 4.718076);

        // TEST: results > address_components[0 & 1] > [short_name]
        assertEquals("results > address_components[0] > [short_name]", details.getResult().getAddressComponents().get(0).getShortName(), "166");
        assertEquals("results > address_components[1] > [short_name]", details.getResult().getAddressComponents().get(1).getShortName(), "Rue Dechavanne");

        // TEST: results > [international_phone_number]
        assertEquals("results > [international_phone_number]", details.getResult().getInternationalPhoneNumber(), "+33 4 74 65 38 74");

        // TEST: results > opening_hours > periods[0] > open > [time]
        assertEquals("results > periods[0] > open > [time]", details.getResult().getOpeningHours().getPeriods().get(0).getOpen().getTime(), "1200");

        // TEST: results > [rating]
        assertTrue("results > [rating]", details.getResult().getRating() == 4.6);

        // TEST: results > [website]
        assertEquals("results > [website]", details.getResult().getWebsite(), "http://pizzeria-villefranche-sur-saone.fr/");

        // TEST: [status]
        assertEquals("[status]", details.getStatus(), "OK");
    }

    @Test
    public void should_Fetch_DistanceMatrix() {
        // Retrieves Google Maps Key
        final String key = InstrumentationRegistry.getInstrumentation()
                                                  .getTargetContext()
                                                  .getResources()
                                                  .getString(R.string.google_maps_key);

        // Creates Observable
        final Observable<DistanceMatrix> observable = this.mPlaceRepository.getStreamToFetchDistanceMatrix("45.9922027,4.7176896",
                                                                                                           "45.99138300000001,4.718076",
                                                                                                           "walking",
                                                                                                           "metric",
                                                                                                           key);

        // Creates Observer
        final TestObserver<DistanceMatrix> observer = new TestObserver<>();

        // Creates Stream
        observable.subscribeWith(observer)
                  .assertNoErrors()
                  .assertNoTimeout()
                  .awaitTerminalEvent();

        // Fetches the result
        final DistanceMatrix distanceMatrix = observer.values().get(0);

        // TEST: results > rows[0] > elements[0] > distance > [text]
        assertEquals("distance > [text]", distanceMatrix.getRows().get(0).getElements().get(0).getDistance().getText(), "90 m");

        // TEST: results > rows[0] > elements[0] > duration > [text]
        assertEquals("duration > [text]", distanceMatrix.getRows().get(0).getElements().get(0).getDuration().getText(), "1 min");

        // TEST: [status]
        assertEquals("[status]", distanceMatrix.getStatus(), "OK");
    }
}
