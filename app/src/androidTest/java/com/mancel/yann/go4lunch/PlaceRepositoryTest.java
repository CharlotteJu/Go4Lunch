package com.mancel.yann.go4lunch;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

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

        // TEST: Results's size
        assertEquals("Results: Number of restaurant equals to 13", nearbySearch.getResults().size(), 13);

        // TEST: First item of Results
        assertEquals("Results[0]: place_id equals to ChIJn_OajiGF9EcRQFJ9o2prZ3w", nearbySearch.getResults().get(0).getPlaceId(), "ChIJn_OajiGF9EcRQFJ9o2prZ3w");
        assertEquals("Results[0]: name equals to Charcutier Traiteur Vigne", nearbySearch.getResults().get(0).getName(), "Charcutier Traiteur Vigne");
        assertTrue("Results[0]: rating equals to 4", nearbySearch.getResults().get(0).getRating() == 4.0);
        assertEquals("Results[0]: vicinity equals to 269 Rue nationale, Villefranche-sur-Saône", nearbySearch.getResults().get(0).getVicinity(), "269 Rue nationale, Villefranche-sur-Saône");
        assertTrue("Results[0]: geometry > location > lat equals to 45.992902", nearbySearch.getResults().get(0).getGeometry().getLocation().getLat() == 45.992902);
        assertTrue("Results[0]: geometry > location > lng equals to 4.718997", nearbySearch.getResults().get(0).getGeometry().getLocation().getLng() == 4.718997);

        // TEST: Status
        assertEquals("Status: OK.", nearbySearch.getStatus(), "OK");
    }
}
