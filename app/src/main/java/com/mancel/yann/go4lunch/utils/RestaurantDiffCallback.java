package com.mancel.yann.go4lunch.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.mancel.yann.go4lunch.models.Restaurant;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Yann MANCEL on 08/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 *
 * A {@link DiffUtil.Callback} subclass.
 */
public class RestaurantDiffCallback extends DiffUtil.Callback {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final List<Restaurant> mOldRestaurants;

    @NonNull
    private final List<Restaurant> mNewRestaurants;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with the odl and new lists
     * @param oldRestaurants a {@link List<Restaurant>} that contains the old list
     * @param newRestaurants a {@link List<Restaurant>} that contains the new list
     */
    public RestaurantDiffCallback(@NonNull final List<Restaurant> oldRestaurants,
                                  @NonNull final List<Restaurant> newRestaurants) {
        this.mOldRestaurants = oldRestaurants;
        this.mNewRestaurants = newRestaurants;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- DiffUtil.Callback --

    @Override
    public int getOldListSize() {
        return this.mOldRestaurants.size();
    }

    @Override
    public int getNewListSize() {
        return this.mNewRestaurants.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Comparison based on the place Id of restaurant
        final String oldName = this.mOldRestaurants.get(oldItemPosition).getDetails().getResult().getPlaceId();
        final String newName = this.mNewRestaurants.get(newItemPosition).getDetails().getResult().getPlaceId();

        return oldName.equals(newName);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Comparison based on:
        //  - the restaurant's name
        //  - the rating
        //  - the photo (unlikely change)
        //  - the people number (from firestore)
        //  - the opening hours (unlikely change)

        return this.restaurantAnalyse(this.mOldRestaurants.get(oldItemPosition))
                   .equals(this.restaurantAnalyse(this.mNewRestaurants.get(newItemPosition)));
    }

    // -- restaurantAnalyse

    /**
     * Return a {@link String} which concatenates the restaurant's name, the rating, the photo (unlikely change),
     * the people number (from firestore) and the opening hours (unlikely change)
     * @param restaurant a {@link Restaurant} that contains the data
     * @return a {@link String}
     */
    @NonNull
    private String restaurantAnalyse(@NonNull final Restaurant restaurant) {
        // NAME
        final String name = restaurant.getDetails().getResult().getName();

        // RATING
        final Double rating = (restaurant.getDetails().getResult().getRating() == null) ?
                0.0 :
                restaurant.getDetails().getResult().getRating();

        // PHOTO
        final String photo = (restaurant.getDetails().getResult().getPhotos() == null) ?
                "Null" :
                restaurant.getDetails().getResult().getPhotos().get(0).getPhotoReference();

        // PEOPLE
        final int oldPeople = restaurant.getNumberOfUsers();

        // OPENING HOURS
        final String openingHours;
        if (restaurant.getDetails().getResult().getOpeningHours() != null) {
            if (restaurant.getDetails().getResult().getOpeningHours().getWeekdayText() != null) {
                final int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                openingHours = restaurant.getDetails().getResult().getOpeningHours().getWeekdayText().get(dayOfWeek - 2);
            }
            else {
                openingHours = restaurant.getDetails().getResult().getOpeningHours().getOpenNow() ?
                        "True" :
                        "False";
            }
        }
        else {
            openingHours = "";
        }

        // NAME + RATING + PHOTO + PEOPLE + OPENING HOURS
        return name + rating.toString() + photo + oldPeople + openingHours;
    }
}
