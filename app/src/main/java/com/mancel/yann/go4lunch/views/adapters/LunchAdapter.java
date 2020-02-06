package com.mancel.yann.go4lunch.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.apis.GoogleMapsService;
import com.mancel.yann.go4lunch.models.Details;
import com.mancel.yann.go4lunch.models.Restaurant;
import com.mancel.yann.go4lunch.utils.DetailsUtils;
import com.mancel.yann.go4lunch.utils.RestaurantDiffCallback;
import com.mancel.yann.go4lunch.utils.RestaurantUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 21/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A {@link RecyclerView.Adapter} subclass.
 */
public class LunchAdapter extends RecyclerView.Adapter<LunchAdapter.LunchViewHolder> {

    // FIELDS --------------------------------------------------------------------------------------

    @NonNull
    private final AdapterListener mCallback;

    @NonNull
    private final RequestManager mGlide;

    @NonNull
    private List<Restaurant> mRestaurants;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor by default
     */
    public LunchAdapter(@NonNull final AdapterListener callback,
                        @NonNull final RequestManager glide) {
        this.mCallback = callback;
        this.mGlide = glide;
        this.mRestaurants = new ArrayList<>();
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- RecyclerView.Adapter --

    @NonNull
    @Override
    public LunchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Creates a Context to the LayoutInflater
        final Context context = viewGroup.getContext();
        final LayoutInflater layoutInflater = LayoutInflater.from(context);

        // Creates the View thanks to the inflater
        final View view = layoutInflater.inflate(LunchAdapter.LunchViewHolder.getLayout(), viewGroup, false);

        return new LunchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LunchViewHolder lunchViewHolder, int position) {
        lunchViewHolder.updateLunch(this.mRestaurants.get(position), this.mGlide);
    }

    @Override
    public int getItemCount() {
        return this.mRestaurants.size();
    }

    // -- Restaurants --

    /**
     * Updates the data thanks to the {@link List<Restaurant>} in argument
     * @param newRestaurants a {@link List<Restaurant>} that contains the new data
     */
    public void updateData(@NonNull final List<Restaurant> newRestaurants) {
        // Optimizes the performances of RecyclerView
        final RestaurantDiffCallback diffCallback = new RestaurantDiffCallback(this.mRestaurants, newRestaurants);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // New data
        this.mRestaurants = newRestaurants;

        // Notifies adapter
        diffResult.dispatchUpdatesTo(this);

        // Callback to update UI
        this.mCallback.onDataChanged();
    }

    // INNER CLASSES -------------------------------------------------------------------------------

    /**
     * A {@link RecyclerView.ViewHolder} subclass.
     */
    public static class LunchViewHolder extends RecyclerView.ViewHolder {

        // FIELDS ----------------------------------------------------------------------------------

        @BindView(R.id.item_restaurant_name)
        TextView mName;
        @BindView(R.id.item_restaurant_address)
        TextView mAddress;
        @BindView(R.id.item_restaurant_opening_hours)
        TextView mOpeningHours;
        @BindView(R.id.item_restaurant_distance)
        TextView mDistance;
        @BindView(R.id.item_restaurant_people_image)
        ImageView mImagePeople;
        @BindView(R.id.item_restaurant_people_number)
        TextView mPeopleNumber;
        @BindView(R.id.item_restaurant_rating_bar)
        RatingBar mRatingBar;
        @BindView(R.id.item_restaurant_image)
        ImageView mImage;

        private static final String TAG = LunchViewHolder.class.getSimpleName();

        // CONSTRUCTORS ----------------------------------------------------------------------------

        /**
         * Constructor
         * @param itemView a {@link View}
         */
        public LunchViewHolder(@NonNull View itemView) {
            super(itemView);

            // Using the ButterKnife library
            ButterKnife.bind(this, itemView);
        }

        // METHODS ---------------------------------------------------------------------------------

        // -- Layout --

        /**
         * Returns the layout value
         * @return an integer that contains the layout value
         */
        public static int getLayout() {
            return R.layout.item_restaurant;
        }

        // -- Update UI --

        /**
         * Updates the item
         * @param restaurant    a {@link Restaurant}
         * @param glide         a {@link RequestManager}
         */
        public void updateLunch(@NonNull final Restaurant restaurant, @NonNull final RequestManager glide) {
            // Name
            this.mName.setText(restaurant.getDetails().getResult().getName());

            // Address
            this.mAddress.setText(DetailsUtils.createStringOfAddress(itemView.getContext(),
                                                                     restaurant.getDetails().getResult().getAddressComponents()));

            // Opening hours
            this.updateOpeningHours(restaurant.getDetails().getResult().getOpeningHours());

            // Distance
            this.mDistance.setText(restaurant.getDistanceMatrix().getRows().get(0).getElements().get(0).getDistance().getText());

            // People
            this.updatePeople(restaurant.getNumberOfUsers());

            // Rating
            this.updateRating(restaurant.getDetails().getResult().getRating());

            // Image
            this.updateImage(glide, restaurant.getDetails().getResult().getPhotos());
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

            // Retrieves the day of the week
            int dayOfWeek = -1;
            switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    dayOfWeek = 0;
                    break;

                case Calendar.TUESDAY:
                    dayOfWeek = 1;
                    break;

                case Calendar.WEDNESDAY:
                    dayOfWeek = 2;
                    break;

                case Calendar.THURSDAY:
                    dayOfWeek = 3;
                    break;

                case Calendar.FRIDAY:
                    dayOfWeek = 4;
                    break;

                case Calendar.SATURDAY:
                    dayOfWeek = 5;
                    break;

                case Calendar.SUNDAY:
                    dayOfWeek = 6;
                    break;

                default:
                    Log.e(TAG, "Error to get the day of the week");
            }

            // weekday_text
            if (openingHours.getWeekdayText() != null) {
                final String openingHoursForThisDay = RestaurantUtils.analyseOpeningHours(openingHours.getWeekdayText(), dayOfWeek);
                this.mOpeningHours.setText(openingHoursForThisDay);

                return;
            }

            // open_now
            this.mOpeningHours.setText(openingHours.getOpenNow() ? itemView.getContext().getString(R.string.currently_open) :
                                                                   itemView.getContext().getString(R.string.not_currently_open));
        }

        /**
         * Updates the {@link TextView} on people and the {@link ImageView} associated
         * @param numberOfUser an integer that contains the number of user who has selected the restaurant
         */
        private void updatePeople(int numberOfUser) {
            // No user
            if (numberOfUser == 0) {
                this.mImagePeople.setVisibility(View.GONE);
                this.mPeopleNumber.setVisibility(View.GONE);

                return;
            }

            this.mPeopleNumber.setText("(" + numberOfUser + ")");
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

        /**
         * Updates the {@link ImageView}
         * @param glide     a {@link RequestManager}
         * @param photos    a {@link List<Details.Photo>}
         */
        private void updateImage(@NonNull final RequestManager glide, @Nullable final List<Details.Photo> photos) {
            // Url Photo
            final String urlPhoto = (photos == null) ? null :
                    GoogleMapsService.getPhoto(photos.get(0).getPhotoReference(),
                                              400,
                                               itemView.getContext().getString(R.string.google_maps_key));

            // Image (using to Glide library)
            glide.load(urlPhoto)
                 .fallback(R.drawable.ic_restaurant2)
                 .error(R.drawable.ic_close)
                 .into(this.mImage);
        }
    }
}
