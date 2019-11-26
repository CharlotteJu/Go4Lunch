package com.mancel.yann.go4lunch.views.fragments;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.views.adapters.LunchAdapter;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

import butterknife.BindView;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.fragments
 *
 * A {@link BaseFragment} subclass.
 */
public class LunchListFragment extends BaseFragment {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.fragment_lunch_list_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_lunch_list_no_restaurant)
    TextView mNoRestaurant;

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
        this.configureRecyclerView();
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
        this.mAdapter = new LunchAdapter();

        // RecyclerView
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                                                                       DividerItemDecoration.VERTICAL));
    }
}
