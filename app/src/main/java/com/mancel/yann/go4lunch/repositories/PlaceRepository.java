package com.mancel.yann.go4lunch.repositories;

import com.mancel.yann.go4lunch.models.Details;
import com.mancel.yann.go4lunch.models.DistanceMatrix;
import com.mancel.yann.go4lunch.models.NearbySearch;
import com.mancel.yann.go4lunch.models.Restaurant;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Yann MANCEL on 20/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.repositories
 */
public interface PlaceRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Simple streams --

    /**
     * Gets stream to Fetch the {@link NearbySearch}
     * @param location  a {@link String} that contains the latitude/longitude around which to retrieve place information
     * @param radius    a double that defines the distance (in meters) within which to return place results
     * @param types     a {@link String} that restricts the results to places matching the specified type
     * @param key       a {@link String} that contains your application's API key
     * @return an {@link Observable<NearbySearch>}
     */
    Observable<NearbySearch> getStreamToFetchNearbySearch(final String location,
                                                          double radius,
                                                          final String types,
                                                          final String key);

    /**
     * Gets stream to Fetch the {@link Details}
     * @param placeId   a {@link String} that contains the textual identifier that uniquely identifies a place
     * @param key       a {@link String} that contains your application's API key
     * @return an {@link Observable<Details>}
     */
    Observable<Details> getStreamToFetchDetails(final String placeId,
                                                final String key);

    /**
     * Gets stream to Fetch the {@link DistanceMatrix}
     * @param origins       a {@link String} that contains the starting point for calculating travel distance and time
     * @param destinations  a {@link String} that contains one or more locations to use as the finishing point for calculating travel distance and time
     * @param mode          a {@link String} that specifies the mode of transport to use when calculating distance
     * @param units         a {@link String} that specifies the unit system to use when expressing distance as text
     * @param key           a {@link String} that contains your application's API key
     * @return an {@link Observable<DistanceMatrix>}
     */
    Observable<DistanceMatrix> getStreamToFetchDistanceMatrix(final String origins,
                                                              final String destinations,
                                                              final String mode,
                                                              final String units,
                                                              final String key);

    // -- Complex streams --

    /**
     * Gets stream to Fetch the {@link Restaurant} thanks to {@link Details} and {@link DistanceMatrix}
     * @param location  a {@link String} that contains the latitude/longitude around which to retrieve place information
     * @param placeId   a {@link String} that contains the textual identifier that uniquely identifies a place
     * @param mode      a {@link String} that specifies the mode of transport to use when calculating distance
     * @param units     a {@link String} that specifies the unit system to use when expressing distance as text
     * @param key       a {@link String} that contains your application's API key
     * @return an {@link Observable<Restaurant>}
     */
    Observable<Restaurant> getStreamToFetchDetailsAndDistanceMatrix(final String location,
                                                                    final String placeId,
                                                                    final String mode,
                                                                    final String units,
                                                                    final String key);

    /**
     * Get stream to Fetch the {@link Details} thanks to a {@link NearbySearch}
     * @param location  a {@link String} that contains the latitude/longitude around which to retrieve place information
     * @param radius    a double that defines the distance (in meters) within which to return place results
     * @param types     a {@link String} that restricts the results to places matching the specified type
     * @param key       a {@link String} that contains your application's API key
     * @return an {@link Observable<Details>}
     */
    Observable<Details> getStreamToFetchNearbySearchThenToFetchDetailsForEachRestaurant(final String location,
                                                                                        double radius,
                                                                                        final String types,
                                                                                        final String key);

    /**
     * Get stream to Fetch the {@link Restaurant}
     * @param location  a {@link String} that contains the latitude/longitude around which to retrieve place information
     * @param radius    a double that defines the distance (in meters) within which to return place results
     * @param types     a {@link String} that restricts the results to places matching the specified type
     * @param mode      a {@link String} that specifies the mode of transport to use when calculating distance
     * @param units     a {@link String} that specifies the unit system to use when expressing distance as text
     * @param key       a {@link String} that contains your application's API key
     * @return an {@link Observable<Restaurant>}
     */
    Observable<Restaurant> getStreamToFetchNearbySearchThenToFetchRestaurant(final String location,
                                                                             double radius,
                                                                             final String types,
                                                                             final String mode,
                                                                             final String units,
                                                                             final String key);
    // TODO: 09/01/2020 Analyse the utility of the method above

    /**
     * Get stream to Fetch the {@link List<Restaurant>}
     * @param location  a {@link String} that contains the latitude/longitude around which to retrieve place information
     * @param radius    a double that defines the distance (in meters) within which to return place results
     * @param types     a {@link String} that restricts the results to places matching the specified type
     * @param mode      a {@link String} that specifies the mode of transport to use when calculating distance
     * @param units     a {@link String} that specifies the unit system to use when expressing distance as text
     * @param key       a {@link String} that contains your application's API key
     * @return an {@link Observable} of {@link List<Restaurant>}
     */
    Observable<List<Restaurant>> getStreamToFetchNearbySearchThenToFetchRestaurants(final String location,
                                                                                    double radius,
                                                                                    final String types,
                                                                                    final String mode,
                                                                                    final String units,
                                                                                    final String key);
}
