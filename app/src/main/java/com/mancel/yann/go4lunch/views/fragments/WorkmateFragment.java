package com.mancel.yann.go4lunch.views.fragments;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.views.adapters.WorkmateAdapter;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

import butterknife.BindView;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.fragments
 *
 * A {@link BaseFragment} subclass.
 */
public class WorkmateFragment extends BaseFragment {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.fragment_workmate_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_workmate_no_people)
    TextView mNoPeople;

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
        this.configureRecyclerView();
    }

    // -- Instances --

    /**
     * Gets a new instance of {@link WorkmateFragment}
     *
     * @return a {@link WorkmateFragment}
     */
    @NonNull
    public static WorkmateFragment newInstance() {
        return new WorkmateFragment();
    }

    // -- RecyclerView --

    /**
     * Configures the {@link RecyclerView}
     */
    private void configureRecyclerView() {
        // Adapter
        this.mAdapter = new WorkmateAdapter();

        // RecyclerView
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                                                                       DividerItemDecoration.VERTICAL));
    }
}
