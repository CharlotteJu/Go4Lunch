package com.mancel.yann.go4lunch.views.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mancel.yann.go4lunch.R;

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
     */
    void updateLunch() {

    }
}
