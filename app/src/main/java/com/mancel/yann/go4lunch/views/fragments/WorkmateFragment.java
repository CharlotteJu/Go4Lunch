package com.mancel.yann.go4lunch.views.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;
import com.mancel.yann.go4lunch.utils.InsetDivider;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModel;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModelFactory;
import com.mancel.yann.go4lunch.views.adapters.AdapterListener;
import com.mancel.yann.go4lunch.views.adapters.WorkmateAdapter;
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
public class WorkmateFragment extends BaseFragment implements AdapterListener {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.fragment_workmate_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_workmate_no_people)
    TextView mNoPeople;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private GoogleMapsAndFirestoreViewModel mViewModel;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private WorkmateAdapter mAdapter;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    public WorkmateFragment() {}

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_workmate;
    }

    @Override
    protected void configureDesign() {
        // UI
        this.configureRecyclerView();

        // ViewModel
        this.configureViewModel();

        // LiveData
        this.configureUsersLiveData();
    }

    // -- AdapterListener interface --

    @Override
    public void onDataChanged() {
        this.mNoPeople.setVisibility( (this.mAdapter.getItemCount() == 0) ? View.VISIBLE :
                                                                            View.GONE);
    }

    // -- Instances --

    /**
     * Gets a new instance of {@link WorkmateFragment}
     * @return a {@link WorkmateFragment}
     */
    @NonNull
    public static WorkmateFragment newInstance() {
        return new WorkmateFragment();
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
     * Configures the {@link LiveData} of {@link List<User>}
     */
    private void configureUsersLiveData() {
        // Bind between liveData of ViewModel and the Adapter of RecyclerView
        this.mViewModel.getUsers()
                       .observe(this.getActivity(),
                                users -> this.mAdapter.updateData(users));
    }

    // -- RecyclerView --

    /**
     * Configures the {@link RecyclerView}
     */
    private void configureRecyclerView() {
        // Adapter
        this.mAdapter = new WorkmateAdapter(this,
                                            Glide.with(this));

        // InsetDivider
        final RecyclerView.ItemDecoration divider = new InsetDivider.Builder(this.getContext())
                                                                    .orientation(InsetDivider.VERTICAL_LIST)
                                                                    .dividerHeight(getResources().getDimensionPixelSize(R.dimen.divider_height))
                                                                    .color(getResources().getColor(R.color.ColorSeparator))
                                                                    .insets(getResources().getDimensionPixelSize(R.dimen.divider_inset), 0)
                                                                    .overlay(true)
                                                                    .build();

        // RecyclerView
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.mRecyclerView.addItemDecoration(divider);
    }
}