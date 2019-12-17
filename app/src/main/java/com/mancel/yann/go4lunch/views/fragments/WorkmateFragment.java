package com.mancel.yann.go4lunch.views.fragments;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.repositories.Repository;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;
import com.mancel.yann.go4lunch.views.adapters.WorkmateAdapter;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

import butterknife.BindView;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.fragments
 *
 * A {@link BaseFragment} subclass which implements {@link WorkmateAdapter.WorkmateAdapterListener}.
 */
public class WorkmateFragment extends BaseFragment implements WorkmateAdapter.WorkmateAdapterListener {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.fragment_workmate_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_workmate_no_people)
    TextView mNoPeople;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private WorkmateAdapter mAdapter;

    private static final String TAG = WorkmateFragment.class.getSimpleName();

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

    // -- WorkmateAdapter.WorkmateAdapterListener interface --

    @Override
    public void onDataChanged() {
        Log.d(TAG, "onDataChanged:");
        this.mNoPeople.setVisibility( (this.mAdapter.getItemCount() == 0) ? View.VISIBLE :
                                                                            View.GONE);
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
        // TODO: 16/12/2019 Update this method. Userrepository is just to test
        final Repository.UserRepository repo = new UserRepositoryImpl();

        // Adapter
        this.mAdapter = new WorkmateAdapter(this.generateOptionsForAdapter(repo.getAllUsers()),
                                            this,
                                            Glide.with(this),
                                            this.getContext());

        // RecyclerView
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                                                                       DividerItemDecoration.VERTICAL));
    }

    /**
     * Generates the {@link FirestoreRecyclerOptions} thanks to the {@link Query} in argument
     * @param query a {@link Query}
     * @return a {@link FirestoreRecyclerOptions} of {@link User}
     */
    @NonNull
    private FirestoreRecyclerOptions<User> generateOptionsForAdapter(final Query query) {
        return new FirestoreRecyclerOptions.Builder<User>()
                                           .setQuery(query, User.class)
                                           .setLifecycleOwner(this.getActivity())
                                           .build();
    }
}
