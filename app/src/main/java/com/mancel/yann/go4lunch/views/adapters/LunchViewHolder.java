package com.mancel.yann.go4lunch.views.adapters;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.Details;
import com.mancel.yann.go4lunch.models.Restaurant;
import com.mancel.yann.go4lunch.utils.RestaurantUtils;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 21/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A {@link RecyclerView.ViewHolder} subclass.
 */
class LunchViewHolder extends RecyclerView.ViewHolder {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.item_restaurant_name)
    TextView mName;
    @BindView(R.id.item_restaurant_type_and_address)
    TextView mTypeAndAddress;
    @BindView(R.id.item_restaurant_opening_hours)
    TextView mOpeningHours;
    @BindView(R.id.item_restaurant_distance)
    TextView mDistance;
    @BindView(R.id.item_restaurant_people_number)
    TextView mPeopleNumber;
    @BindView(R.id.item_restaurant_rating_bar)
    RatingBar mRatingBar;
    @BindView(R.id.item_restaurant_image)
    ImageView mImage;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor
     * @param itemView a {@link View}
     */
    LunchViewHolder(@NonNull View itemView) {
        super(itemView);

        // Using the ButterKnife library
        ButterKnife.bind(this, itemView);
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- Layout --

    /**
     * Returns the layout value
     * @return an integer that contains the layout value
     */
    static int getLayout() {
        return R.layout.item_restaurant;
    }

    // -- Update UI --

    /**
     * Updates the item
     * @param restaurant    a {@link Restaurant}
     * @param glide         a {@link RequestManager}
     */
    void updateLunch(@NonNull final Restaurant restaurant, @NonNull final RequestManager glide) {
        // Name
        this.mName.setText(restaurant.getDetails().getResult().getName());

        // Food type & Address
        this.updateFoodTypeAndAddress(restaurant.getDetails().getResult().getAddressComponents());

        // Opening hours
        this.updateOpeningHours(restaurant.getDetails().getResult().getOpeningHours());

        // Distance
        this.mDistance.setText(restaurant.getDistanceMatrix().getRows().get(0).getElements().get(0).getDistance().getText());

        // People
        // TODO: 06/01/2020 Add people number
        //this.mPeopleNumber;

        // Rating
        this.updateRating(restaurant.getDetails().getResult().getRating());

        // Image
        //this.mImage;
    }

    /**
     * Updates the {@link TextView} on food type and address
     * @param addressComponents a {@link List<Details.AddressComponent>} that contains the address data
     */
    private void updateFoodTypeAndAddress(@Nullable final List<Details.AddressComponent> addressComponents) {
        final StringBuilder stringBuilder = new StringBuilder();

        // TODO: 06/01/2020 Add food type
        stringBuilder.append("Food - ");


        // No addressComponents
        if (addressComponents == null) {
            stringBuilder.append(itemView.getContext().getString(R.string.no_address));

            this.mTypeAndAddress.setText(stringBuilder);

            return;
        }

        // Research street_number and route
        String streetNumber = null;
        String route = null;

        for (Details.AddressComponent addressComponent : addressComponents) {
            // street_number
            if (addressComponent.getTypes().get(0).contains("street_number")) {
                streetNumber = addressComponent.getShortName() + " ";
            }

            // route
            if (addressComponent.getTypes().get(0).contains("route")) {
                route = addressComponent.getShortName();
            }
        }

        // No streetNumber and no route
        if (streetNumber == null && route == null) {
            stringBuilder.append(itemView.getContext().getString(R.string.no_specific_address));

            this.mTypeAndAddress.setText(stringBuilder);

            return;
        }

        // streetNumber
        if (streetNumber != null) {
            stringBuilder.append(streetNumber);
        }

        // route
        if (route != null) {
            stringBuilder.append(route);
        }

        this.mTypeAndAddress.setText(stringBuilder);
    }

    /**
     * Updates the {@link TextView} on opening hours
     * @param openingHours a {@link Details.OpeningHours}
     */
    private void updateOpeningHours(@Nullable final Details.OpeningHours openingHours) {
        // No openingHours
        if (openingHours == null) {
            this.mOpeningHours.setText(itemView.getContext().getString(R.string.no_data_on_the_opening_hours));
            return;
        }

        // TODO: 06/01/2020 Finish this method 

        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                Log.d(LunchViewHolder.class.getSimpleName(), "updateOpeningHours: Day = MONDAY");
                break;

            case Calendar.TUESDAY:
                Log.d(LunchViewHolder.class.getSimpleName(), "updateOpeningHours: Day = TUESDAY");
                break;

            case Calendar.WEDNESDAY:
                Log.d(LunchViewHolder.class.getSimpleName(), "updateOpeningHours: Day = WEDNESDAY");
                break;

            case Calendar.THURSDAY:
                Log.d(LunchViewHolder.class.getSimpleName(), "updateOpeningHours: Day = THURSDAY");
                break;

            case Calendar.FRIDAY:
                Log.d(LunchViewHolder.class.getSimpleName(), "updateOpeningHours: Day = FRIDAY");
                break;

            case Calendar.SATURDAY:
                Log.d(LunchViewHolder.class.getSimpleName(), "updateOpeningHours: Day = SATURDAY");
                break;

            case Calendar.SUNDAY:
                Log.d(LunchViewHolder.class.getSimpleName(), "updateOpeningHours: Day = SUNDAY");
                break;

            default:
                Log.e(LunchViewHolder.class.getSimpleName(), "error to get the day of the week");
        }

        // weekday_text
        if (openingHours.getWeekdayText() != null) {
            // ...
        }

        // periods
        if (openingHours.getPeriods() != null) {
            // ...
        }

        // open_now
        if (openingHours.getOpenNow()) {
            // ...
        }
        else {
            // ...
        }

        /*
        "opening_hours": {
                    "open_now": false,
                    "periods": [
                        {
                            "close": {
                                "day": 1,
                                "time": "1330"
                            },
                            "open": {
                                "day": 1,
                                "time": "1200"
                            }
                        }
                    ],
                    "weekday_text": [
                        "Monday: 12:00 – 1:30 PM",
                        "Tuesday: 12:00 – 1:30 PM, 7:00 – 9:30 PM",
                        "Wednesday: 12:00 – 1:30 PM, 7:00 – 9:30 PM",
                        "Thursday: 12:00 – 1:30 PM, 7:00 – 9:30 PM",
                        "Friday: 12:00 – 1:30 PM, 7:00 – 10:00 PM",
                        "Saturday: 12:00 – 1:30 PM, 7:00 – 10:00 PM",
                        "Sunday: Closed"
                    ]
                },
         */

        this.mOpeningHours.setText(RestaurantUtils.AnalyseOpeningHours(openingHours));
    }

    /**
     * Updates the {@link RatingBar}
     * @param googleMapsRating a {@link Double} that contains the google maps value
     */
    private void updateRating(@Nullable final Double googleMapsRating) {
        // No Rating
        if (googleMapsRating == null) {
            this.mRatingBar.setVisibility(View.GONE);
            return;
        }

        float floatValue = RestaurantUtils.calculateRating(googleMapsRating.floatValue());

        // TODO: 06/01/2020 Change the number stars of RatingBar 
        //this.mRatingBar.setNumStars((int) floatValue);
        this.mRatingBar.setRating(floatValue);
    }
}
