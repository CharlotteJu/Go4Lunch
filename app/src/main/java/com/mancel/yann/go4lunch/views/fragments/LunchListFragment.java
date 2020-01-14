package com.mancel.yann.go4lunch.views.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.Restaurant;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModel;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModelFactory;
import com.mancel.yann.go4lunch.views.adapters.AdapterListener;
import com.mancel.yann.go4lunch.views.adapters.LunchAdapter;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

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
    private GoogleMapsAndFirestoreViewModel mViewModel;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private LunchAdapter mAdapter;

    // CONSTRUCTORS --------------------------------------------------------------------------------

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

        // ViewModel
        this.configureViewModel();

        // LiveData
        this.configureRestaurantsWithUsersLiveData();
    }

    // -- AdapterListener interface --

    @Override
    public void onDataChanged() {
        this.mNoRestaurant.setVisibility( (this.mAdapter.getItemCount() == 0) ? View.VISIBLE :
                                                                                View.GONE);
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

    // -- GoogleMapsAndFirestoreViewModel --

    /**
     * Configures the {@link GoogleMapsAndFirestoreViewModel}
     */
    private void configureViewModel() {
        // TODO: 09/01/2020 UserRepository and PlaceRepository must be removed thanks to Dagger 2
        final GoogleMapsAndFirestoreViewModelFactory factory = new GoogleMapsAndFirestoreViewModelFactory(new UserRepositoryImpl(),
                                                                                                          new PlaceRepositoryImpl());

        this.mViewModel = ViewModelProviders.of(this.getActivity(), factory)
                                            .get(GoogleMapsAndFirestoreViewModel.class);
    }

    /**
     * Configures the {@link LiveData} of {@link List<Restaurant>}
     */
    private void configureRestaurantsWithUsersLiveData() {
        // Bind between liveData of ViewModel and the Adapter of RecyclerView
        this.mViewModel.getRestaurantsWithUsers(this.getContext())
                       .observe(this.getActivity(),
                                restaurants -> {
                                    // The action can take a long time
                                    this.mProgressBar.show();

                                    // Updates adapter
                                    this.mAdapter.updateData(restaurants);

                                    // The end of action
                                    this.mProgressBar.hide();
                                });
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
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                                                                       DividerItemDecoration.VERTICAL));
    }
}