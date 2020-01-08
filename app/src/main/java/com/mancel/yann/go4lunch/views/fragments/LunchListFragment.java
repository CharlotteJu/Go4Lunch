package com.mancel.yann.go4lunch.views.fragments;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.Restaurant;
import com.mancel.yann.go4lunch.repositories.PlaceRepository;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;
import com.mancel.yann.go4lunch.views.adapters.AdapterListener;
import com.mancel.yann.go4lunch.views.adapters.LunchAdapter;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

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

    @Nullable
    private Disposable mDisposable = null;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private List<Restaurant> mRestaurants;

    private static final String TAG = LunchListFragment.class.getSimpleName();

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
        this.configureRecyclerView();
        this.configureRestaurants();
    }

    // -- Fragment --

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
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

    // -- Java Rx --

    /**
     * Disposes the {@link Disposable} when {@link Fragment#onDestroy()} method is called
     */
    private void disposeWhenDestroy() {
        Log.e(TAG, "disposeWhenDestroy");

        if (this.mDisposable != null && !this.mDisposable.isDisposed()) {
            this.mDisposable.dispose();
        }
    }

    /**
     * Configures the {@link List<Restaurant>}
     */
    private void configureRestaurants() {
        // The action can take a long time
        this.mProgressBar.show();

        // Initializes the list
        this.mRestaurants = new ArrayList<>();

        // TODO: 03/01/2020 PlaceRepository must be removed
        PlaceRepository placeRepository = new PlaceRepositoryImpl();

        // Retrieves Google Maps Key
        final String key = getContext().getResources()
                                       .getString(R.string.google_maps_key);

        // Creates stream
        this.mDisposable = placeRepository.getStreamToFetchNearbySearchThenToFetchRestaurant("45.9922027,4.7176896",
                                                                                             200.0,
                                                                                             "restaurant",
                                                                                             "walking",
                                                                                             "metric",
                                                                                              key)
                                          .subscribeWith(new DisposableObserver<Restaurant>() {
                                              @Override
                                              public void onNext(Restaurant restaurant) {
                                                  // Add item
                                                  mRestaurants.add(restaurant);
                                              }

                                              @Override
                                              public void onError(Throwable e) {
                                                  Log.e(TAG, "onError: " + e.getMessage());
                                              }

                                              @Override
                                              public void onComplete() {
                                                  // Update data of adapter
                                                  mAdapter.updateData(mRestaurants);

                                                  // The end of action
                                                  mProgressBar.hide();
                                              }
                                          });
    }
}
