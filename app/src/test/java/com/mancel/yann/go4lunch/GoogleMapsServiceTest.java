package com.mancel.yann.go4lunch;

import com.mancel.yann.go4lunch.apis.GoogleMapsService;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Yann MANCEL on 07/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch
 *
 * Test on {@link GoogleMapsService}.
 */
public class GoogleMapsServiceTest {

    // METHODS -------------------------------------------------------------------------------------

    @Test
    public void getPhoto_Should_Be_Success() {
        // Data
        final String photoReference = "dummy_photoReference";
        int maxWidth = 400;
        final String key = "dummy_key";

        final String urlExpected = "https://maps.googleapis.com/maps/api/place/photo?" + "photoreference=" + photoReference + "&" +
                                                                                         "maxwidth="       + maxWidth       + "&" +
                                                                                         "key="            + key;

        final String urlActual = GoogleMapsService.getPhoto(photoReference, maxWidth, key);

        // TEST: Equal
        assertEquals(urlExpected, urlActual);
    }
}
