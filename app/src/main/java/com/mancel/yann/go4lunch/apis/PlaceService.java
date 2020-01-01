package com.mancel.yann.go4lunch.apis;

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
public interface PlaceService {

    // FIELDS --------------------------------------------------------------------------------------

    Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://maps.googleapis.com/maps/api/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                    .build();

    // METHODS -------------------------------------------------------------------------------------

    // -- GET --

    /**
     * Gets a Nearby Search request is an HTTP URL in Json format
     * @param location  a {@link String} that contains the latitude/longitude around which to retrieve place information
     * @param radius    a double that defines the distance (in meters) within which to return place results
     * @param types      a {@link String} that restricts the results to places matching the specified type
     * @param key       a {@link String} that contains your application's API key
     * @return an {@link Observable<NearbySearch>}
     */
    @GET("place/nearbysearch/json?")
    Observable<NearbySearch> getNearbySearch(@Query("location") final String location,
                                             @Query("radius") double radius,
                                             @Query("types") final String types,
                                             @Query("key") final String key);

    // TODO: 31/12/2019 To take into account pagetoken
}
