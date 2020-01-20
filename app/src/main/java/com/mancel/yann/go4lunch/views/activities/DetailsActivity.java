package com.mancel.yann.go4lunch.views.activities;

import android.content.Intent;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModel;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModelFactory;
import com.mancel.yann.go4lunch.views.adapters.AdapterListener;
import com.mancel.yann.go4lunch.views.adapters.WorkmateAdapter;
import com.mancel.yann.go4lunch.views.bases.BaseActivity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Yann MANCEL on 21/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.activities
 *
 * A {@link BaseActivity} subclass which implements {@link AdapterListener}.
 */
public class DetailsActivity extends BaseActivity implements AdapterListener {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.activity_details_image)
    ImageView mImage;
    @BindView(R.id.activity_details_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_details_recycler_view)
    RecyclerView mRecyclerView;

    @Nullable
    private String mPlaceIdOfRestaurant = null;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private GoogleMapsAndFirestoreViewModel mViewModel;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private WorkmateAdapter mAdapter;

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseActivity --

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_details;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return this.mToolbar;
    }

    @Override
    protected void configureDesign() {
        // Intent
        this.fetchPlaceIdOfRestaurantFromIntent();

        // UI
        this.configureToolBar();
        this.configureUpButtonOfToolBar();
        this.configureRecyclerView();

        // ViewModel
        this.configureViewModel();

        // LiveData
        this.configureDetailsLiveData();
        this.configureUsersLiveData();
    }

    // -- AdapterListener --

    @Override
    public void onDataChanged() {
        // TODO: 20/01/2020 Add action
    }

    // -- Intent --

    /**
     * Fetches the Place Id of restaurant from {@link Intent}
     */
    private void fetchPlaceIdOfRestaurantFromIntent() {
        final Intent intent = this.getIntent();

        if (intent != null) {
            this.mPlaceIdOfRestaurant = intent.getStringExtra(MainActivity.INTENT_PLACE_ID);
        }
    }

    // -- RecyclerView --

    /**
     * Configures the {@link RecyclerView}
     */
    private void configureRecyclerView() {
        // Adapter
        this.mAdapter = new WorkmateAdapter(this,
                                             Glide.with(this));

        // RecyclerView
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getApplicationContext(),
                                                                       DividerItemDecoration.VERTICAL));

        // TODO: 02/01/2020 Replace DividerItemDecoration by a new class (inset divider)
    }

    // -- GoogleMapsAndFirestoreViewModel --

    /**
     * Configures the {@link GoogleMapsAndFirestoreViewModel}
     */
    private void configureViewModel() {
        // TODO: 20/01/2020 UserRepository and PlaceRepository must be removed thanks to Dagger 2
        final GoogleMapsAndFirestoreViewModelFactory factory = new GoogleMapsAndFirestoreViewModelFactory(new UserRepositoryImpl(),
                                                                                                          new PlaceRepositoryImpl());

        this.mViewModel = ViewModelProviders.of(this, factory)
                                            .get(GoogleMapsAndFirestoreViewModel.class);
    }

    /**
     * Configures the {@link LiveData} of {@link List<User>}
     */
    private void configureDetailsLiveData() {
        // TODO: 20/01/2020 add DetailsLiveData
    }

    /**
     * Configures the {@link LiveData} of {@link List<User>}
     */
    private void configureUsersLiveData() {
        // Bind between liveData of ViewModel and the Adapter of RecyclerView
        if (this.mPlaceIdOfRestaurant != null) {
            this.mViewModel.getUsersWithSameRestaurant(this.mPlaceIdOfRestaurant)
                           .observe(this,
                                     users -> this.mAdapter.updateData(users) );
        }
    }
}