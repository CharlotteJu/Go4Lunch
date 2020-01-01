package com.mancel.yann.go4lunch.repositories;

import com.mancel.yann.go4lunch.models.NearbySearch;

import io.reactivex.Observable;

/**
 * Created by Yann MANCEL on 20/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.repositories
 */
public interface PlaceRepository {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Get stream to Fetch the {@link NearbySearch}
     * @param location  a {@link String} that contains the latitude/longitude around which to retrieve place information
     * @param radius    a double that defines the distance (in meters) within which to return place results
     * @param types      a {@link String} that restricts the results to places matching the specified type
     * @param key       a {@link String} that contains your application's API key
     * @return an {@link Observable<NearbySearch>}
     */
    Observable<NearbySearch> getStreamToFetchNearbySearch(final String location,
                                                          double radius,
                                                          final String types,
                                                          final String key);
}
