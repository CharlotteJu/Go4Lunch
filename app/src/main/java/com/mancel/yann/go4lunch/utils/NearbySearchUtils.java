package com.mancel.yann.go4lunch.utils;

import androidx.annotation.NonNull;

import com.mancel.yann.go4lunch.models.NearbySearch;
import com.mancel.yann.go4lunch.models.POI;
import com.mancel.yann.go4lunch.models.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Yann MANCEL on 06/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 */
public abstract class NearbySearchUtils {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Updates a {@link List<POI>} with a {@link NearbySearch} and a {@link List<User>}
     * @param nearbySearch  a {@link NearbySearch}
     * @param users         a {@link List<User>}
     * @return a {@link List<POI>}
     */
    @NonNull
    public static synchronized List<POI> updatePOIs(@NonNull NearbySearch nearbySearch,
                                                    @NonNull List<User> users) {
        // Initialises list
        final List<POI> pois = new ArrayList<>();

        // No Result
        if (nearbySearch.getResults() == null || nearbySearch.getResults().size() == 0) {
            return pois;
        }

        final Iterator<NearbySearch.Result> iterator = nearbySearch.getResults().iterator();

        while (iterator.hasNext()) {
            final NearbySearch.Result result = iterator.next();

            final POI poi = new POI(result.getName(),
                                    result.getPlaceId(),
                                    result.getGeometry().getLocation().getLat(),
                                    result.getGeometry().getLocation().getLng(),
                                    (result.getPhotos() == null) ? null :
                                                                   result.getPhotos().get(0).getPhotoReference());

            for (User user : users) {
                // Restaurant is already selected by at least one user
                if (poi.getIsSelected()) {
                    break;
                }

                // Place Id is null
                if (user.getPlaceIdOfRestaurant() == null) {
                    continue;
                }

                // Same Place Id
                if (user.getPlaceIdOfRestaurant().equals(result.getPlaceId())) {
                    poi.setIsSelected(true);
                }
            }

            pois.add(poi);
        }

        return pois;
    }
}
