package com.mancel.yann.go4lunch.liveDatas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.mancel.yann.go4lunch.models.Restaurant;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.utils.RestaurantUtils;

import java.util.List;

/**
 * Created by Yann MANCEL on 13/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.liveDatas
 *
 * A {@link MediatorLiveData} of {@link List<Restaurant>} subclass.
 */
public class RestaurantsWithUsersLiveData extends MediatorLiveData<List<Restaurant>> {

    // FIELDS --------------------------------------------------------------------------------------

    @Nullable
    private List<Restaurant> mRestaurants = null;

    @Nullable
    private List<User> mUsers = null;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with several {@link LiveData} in argument
     * @param restaurantsLiveData   a {@link LiveData} of {@link List<Restaurant>}
     * @param usersLiveData         a {@link LiveData} of {@link List<User>}
     */
    public RestaurantsWithUsersLiveData(@NonNull final LiveData<List<Restaurant>> restaurantsLiveData,
                                        @NonNull final LiveData<List<User>> usersLiveData) {
        this.addSourceRestaurants(restaurantsLiveData);
        this.addSourceUsers(usersLiveData);
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- LiveData --

    /**
     * Adds a source, here a {@link LiveData} of {@link List<Restaurant>}
     * @param restaurantsLiveData a {@link LiveData} of {@link List<Restaurant>}
     */
    private void addSourceRestaurants(@NonNull final LiveData<List<Restaurant>> restaurantsLiveData) {
        this.addSource(restaurantsLiveData, restaurants -> {
            // Initialises the restaurants
            this.mRestaurants = restaurants;

            // Build the Restaurant/User list
            this.createRestaurantsWithUsers();
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

            // Build the Restaurant/User list
            this.createRestaurantsWithUsers();
        });
    }

    // -- Build Restaurants with Users --

    /**
     * Creates a {@link List<Restaurant>} and notify
     */
    private void createRestaurantsWithUsers() {
        // Waits the result of all LiveData
        if (this.mRestaurants == null || this.mUsers == null) {
            return;
        }

        // Notify
        this.setValue(RestaurantUtils.updateRestaurantsWithUsers(this.mRestaurants,
                                                                 this.mUsers));
    }
}
