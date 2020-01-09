package com.mancel.yann.go4lunch.apis;

import androidx.annotation.NonNull;

import com.mancel.yann.go4lunch.models.Details;
import com.mancel.yann.go4lunch.models.DistanceMatrix;
import com.mancel.yann.go4lunch.models.NearbySearch;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Yann MANCEL on 17/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.apis
 */
public interface GoogleMapsService {

    // FIELDS --------------------------------------------------------------------------------------

    String baseURL = "https://maps.googleapis.com/maps/api/";

    Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(baseURL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                    .build();

    // METHODS -------------------------------------------------------------------------------------

    // -- GET --

    /**
     * Gets a Nearby Search request is an HTTP URL in Json format
     *
     * @param location a {@link String} that contains the latitude/longitude around which to retrieve place information
     * @param radius   a double that defines the distance (in meters) within which to return place results
     * @param types    a {@link String} that restricts the results to places matching the specified type
     * @param key      a {@link String} that contains your application's API key
     * @return an {@link Observable<NearbySearch>}
     */
    @GET("place/nearbysearch/json?")
    Observable<NearbySearch> getNearbySearch(@Query("location") final String location,
                                             @Query("radius") double radius,
                                             @Query("types") final String types,
                                             @Query("key") final String key);

    /**
     * Gets a Details request is an HTTP URL in Json format
     *
     * @param placeId a {@link String} that contains the textual identifier that uniquely identifies a place
     * @param key     a {@link String} that contains your application's API key
     * @return an {@link Observable<Details>}
     */
    @GET("place/details/json?")
    Observable<Details> getDetails(@Query("place_id") final String placeId,
                                   @Query("key") final String key);

    /**
     * Gets a Distance Matrix request is an HTTP URL in Json format
     *
     * @param origins      a {@link String} that contains the starting point for calculating travel distance and time
     * @param destinations a {@link String} that contains one or more locations to use as the finishing point for calculating travel distance and time
     * @param mode         a {@link String} that specifies the mode of transport to use when calculating distance
     * @param units        a {@link String} that specifies the unit system to use when expressing distance as text
     * @param key          a {@link String} that contains your application's API key
     * @return an {@link Observable<DistanceMatrix>}
     */
    @GET("distancematrix/json?")
    Observable<DistanceMatrix> getDistanceMatrix(@Query("origins") final String origins,
                                                 @Query("destinations") final String destinations,
                                                 @Query("mode") final String mode,
                                                 @Query("units") final String units,
                                                 @Query("key") final String key);

    // STATIC METHODS ------------------------------------------------------------------------------

    /**
     * Gets a photo request URL
     * @param photoReference    a {@link String} that contains a string identifier that uniquely identifies a photo
     * @param maxWidth          an integer that specifies the maximum desired width, in pixels, of the image
     * @param key               a {@link String} that contains your application's API key
     * @return a {@link String} that contains the photo request URL
     */
    @NonNull
    static String getPhoto(@NonNull final String photoReference,
                           int maxWidth,
                           @NonNull final String key) {
        return baseURL + "place/photo?" + "photoreference=" + photoReference + "&" +
                                          "maxwidth="       + maxWidth       + "&" +
                                          "key="            + key;
    }
}
