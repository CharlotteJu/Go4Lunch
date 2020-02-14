package com.mancel.yann.go4lunch.repositories;

import androidx.annotation.NonNull;

import com.mancel.yann.go4lunch.apis.GoogleMapsService;
import com.mancel.yann.go4lunch.models.Details;
import com.mancel.yann.go4lunch.models.DistanceMatrix;
import com.mancel.yann.go4lunch.models.NearbySearch;
import com.mancel.yann.go4lunch.models.Restaurant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Yann MANCEL on 18/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.repositories
 *
 * A class which implements {@link PlaceRepository}.
 */
public class PlaceRepositoryImpl implements PlaceRepository {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private GoogleMapsService mGoogleMapsService = GoogleMapsService.retrofit
                                                                    .create(GoogleMapsService.class);

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor for Dagger 2
     */
    @Inject
    public PlaceRepositoryImpl() {}

    // METHODS -------------------------------------------------------------------------------------

    // -- Simple streams --

    @Override
    public Observable<NearbySearch> getStreamToFetchNearbySearch(final String location,
                                                                 final double radius,
                                                                 final String types,
                                                                 final String key) {
        return this.mGoogleMapsService.getNearbySearch(location,
                                                       radius,
                                                       types,
                                                       key)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .timeout(10, TimeUnit.SECONDS);
    }

    @Override
    public Observable<Details> getStreamToFetchDetails(final String placeId,
                                                       final String key) {
        return this.mGoogleMapsService.getDetails(placeId, key)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .timeout(10, TimeUnit.SECONDS);
    }

    @Override
    public Observable<DistanceMatrix> getStreamToFetchDistanceMatrix(final String origins,
                                                                     final String destinations,
                                                                     final String mode,
                                                                     final String units,
                                                                     final String key) {
        return this.mGoogleMapsService.getDistanceMatrix(origins, destinations, mode, units, key)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .timeout(10, TimeUnit.SECONDS);
    }

    // -- Complex streams --

    @Override
    public Observable<Restaurant> getStreamToFetchDetailsAndDistanceMatrix(final String location,
                                                                           final String placeId,
                                                                           final String mode,
                                                                           final String units,
                                                                           final String key) {
        return Observable.zip(this.getStreamToFetchDetails(placeId, key),
                              this.getStreamToFetchDistanceMatrix(location, "place_id:" + placeId, mode, units, key),
                              (details, distanceMatrix) -> new Restaurant(details, distanceMatrix));
    }

    @Override
    public Observable<List<Restaurant>> getStreamToFetchNearbySearchThenToFetchRestaurants(final String location,
                                                                                           final double radius,
                                                                                           final String types,
                                                                                           final String mode,
                                                                                           final String units,
                                                                                           final String key) {
        return this.getStreamToFetchNearbySearch(location, radius, types, key)
                   .map( nearbySearch -> nearbySearch.getResults() )
                   .flatMapIterable( result -> result )
                   .flatMap( result -> this.getStreamToFetchDetailsAndDistanceMatrix(location, result.getPlaceId(), mode, units, key))
                   .toList()
                   .toObservable();
    }
}
