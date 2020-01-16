package com.mancel.yann.go4lunch.liveDatas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.mancel.yann.go4lunch.models.NearbySearch;
import com.mancel.yann.go4lunch.models.POI;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.utils.NearbySearchUtils;

import java.util.List;

/**
 * Created by Yann MANCEL on 16/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.liveDatas
 *
 * A {@link MediatorLiveData} of {@link List<POI>} subclass.
 */
public class POIsLiveData extends MediatorLiveData<List<POI>> {

    // FIELDS --------------------------------------------------------------------------------------

    @Nullable
    private NearbySearch mNearbySearch = null;

    @Nullable
    private List<User> mUsers = null;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with several {@link LiveData} in argument
     * @param nearbySearchLiveData  a {@link LiveData<NearbySearch>}
     * @param usersLiveData         a {@link LiveData} of {@link List<User>}
     */
    public POIsLiveData(@NonNull final LiveData<NearbySearch> nearbySearchLiveData,
                        @NonNull final LiveData<List<User>> usersLiveData) {
        this.addSourceNearbySearch(nearbySearchLiveData);
        this.addSourceUsers(usersLiveData);
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- LiveData --

    /**
     * Adds a source, here a {@link LiveData<NearbySearch>}
     * @param nearbySearchLiveData a {@link LiveData<NearbySearch>}
     */
    private void addSourceNearbySearch(@NonNull final LiveData<NearbySearch> nearbySearchLiveData) {
        this.addSource(nearbySearchLiveData, nearbySearch -> {
            // Initialises the nearby search
            this.mNearbySearch = nearbySearch;

            // Build the POI list
            this.createPOIs();
        });
    }

    /**
     * Adds a source, here a {@link LiveData} of {@link List<User>}
     * @param usersLiveData a {@link LiveData} of {@link List<User>}
     */
    private void addSourceUsers(@NonNull final LiveData<List<User>> usersLiveData) {
        this.addSource(usersLiveData, users -> {
            // Initialises the users
            this.mUsers = users;

            // Build the POI list
            this.createPOIs();
        });
    }

    // -- Build POIs --

    /**
     * Creates a {@link List<POI>} and notify
     */
    private void createPOIs() {
        // Waits the result of all LiveData
        if (this.mNearbySearch == null || this.mUsers == null) {
            return;
        }

        // Notify
        this.setValue(NearbySearchUtils.updatePOIs(this.mNearbySearch,
                                                   this.mUsers));
    }
}
