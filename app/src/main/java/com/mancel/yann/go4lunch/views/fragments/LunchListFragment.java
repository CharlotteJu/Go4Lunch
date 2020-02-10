package com.mancel.yann.go4lunch.views.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.LocationData;
import com.mancel.yann.go4lunch.models.Restaurant;
import com.mancel.yann.go4lunch.views.adapters.AdapterListener;
import com.mancel.yann.go4lunch.views.adapters.LunchAdapter;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.fragments
 *
 * A {@link BaseFragment} subclass which implements {@link AdapterListener}.
 */
public class LunchListFragment extends BaseFragment implements AdapterListener {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.fragment_lunch_list_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_lunch_list_no_restaurant)
    TextView mNoRestaurant;
    @BindView(R.id.fragment_lunch_list_ProgressBar)
    ContentLoadingProgressBar mProgressBar;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private LunchAdapter mAdapter;

    private static final double NEARBY_SEARCH_RADIUS = 200.0;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Default constructor
     */
    public LunchListFragment() {}

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_lunch_list;
    }

    @Override
    protected void configureDesign() {
        // UI
        this.configureRecyclerView();

        // LiveData
        this.configureLocationLiveData();
        this.configureRestaurantsWithUsersLiveData();
    }

    // -- AdapterListener interface --

    @Override
    public void onDataChanged() {
        this.mNoRestaurant.setVisibility( (this.mAdapter.getItemCount() == 0) ? View.VISIBLE :
                                                                                View.GONE);
    }

    @Override
    public void onCardViewClicked(@NonNull final View view) {
        if (view.getTag() != null) {
            final String placeIdOfRestaurant = (String) view.getTag();
            this.mCallbackFromFragmentToActivity.onSelectedRestaurant(placeIdOfRestaurant);
        }
    }

    // -- Instances --

    /**
     * Gets a new instance of {@link LunchListFragment}
     * @return a {@link LunchListFragment}
     */
    @NonNull
    public static LunchListFragment newInstance() {
        return new LunchListFragment();
    }

    // -- RecyclerView --

    /**
     * Configures the {@link RecyclerView}
     */
    private void configureRecyclerView() {
        // Adapter
        this.mAdapter = new LunchAdapter(this, Glide.with(this));

        // RecyclerView
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),
                                                                       DividerItemDecoration.VERTICAL));
    }

    // -- LiveData --

    /**
     * Configures the {@link LiveData<LocationData>}
     */
    private void configureLocationLiveData() {
        // Bind between liveData of ViewModel and the Fragment
        this.mViewModel.getLocation(this.getContext())
                       .observe(this.getViewLifecycleOwner(), this::onChangedLocationData);
    }
    /**
     * Configures the {@link LiveData} of {@link List<Restaurant>}
     */
    private void configureRestaurantsWithUsersLiveData() {
        // Bind between liveData of ViewModel and the Adapter of RecyclerView
        this.mViewModel.getRestaurantsWithUsers(this.getContext(), null, NEARBY_SEARCH_RADIUS)
                       .observe(this.getViewLifecycleOwner(),
                                restaurants -> {
                                    // The action can take a long time
                                    this.mProgressBar.show();

                                    // Sorts the list from A to Z
                                    Collections.sort(restaurants, new Restaurant.AZComparator());

                                    // Updates adapter
                                    this.mAdapter.updateData(restaurants);

                                    // The end of action
                                    this.mProgressBar.hide();
                                });
    }

    // -- Update Location --

    /**
     * Method to replace the {@link androidx.lifecycle.Observer} of {@link LocationData}
     * @param locationData a {@link LocationData}
     */
    private void onChangedLocationData (@NonNull final LocationData locationData) {
        // Exception
        if (this.handleLocationException(locationData.getException())) {
            return;
        }

        // No Location
        if (locationData.getLocation() == null) {
            return;
        }

        // Fetches Restaurants
        // TODO: 05/02/2020 analyse if really useful to update at each location update
        this.mViewModel.fetchRestaurants(this.getContext(),
                                         locationData,
                                         NEARBY_SEARCH_RADIUS);
    }
}