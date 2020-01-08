package com.mancel.yann.go4lunch.views.fragments;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;
import com.mancel.yann.go4lunch.viewModels.UserViewModel;
import com.mancel.yann.go4lunch.viewModels.UserViewModelFactory;
import com.mancel.yann.go4lunch.views.adapters.AdapterListener;
import com.mancel.yann.go4lunch.views.adapters.WorkmateAdapter;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

import java.util.ArrayList;
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
    private UserViewModel mUserViewModel;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private WorkmateAdapter mAdapter;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private ListenerRegistration mListenerRegistration;

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
        this.configureUserViewModel();
        this.configureRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stop listening to changes
        this.mListenerRegistration.remove();
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

    // -- UserViewModel --

    /**
     * Configures the {@link UserViewModel}
     */
    private void configureUserViewModel() {
        // TODO: 23/12/2019 UserRepository must be removed thanks to Dagger 2
        final UserViewModelFactory factory = new UserViewModelFactory(new UserRepositoryImpl());

        this.mUserViewModel = ViewModelProviders.of(this.getActivity(), factory)
                                                .get(UserViewModel.class);
    }

    // -- RecyclerView --

    /**
     * Configures the {@link RecyclerView}
     */
    private void configureRecyclerView() {
        // Adapter
        this.mAdapter = new WorkmateAdapter(this,
                                            Glide.with(this));

        // ListenerRegistration: SnapshotListener of Query
        this.mListenerRegistration = this.mUserViewModel.getAllUsers()
                                                        .addSnapshotListener( (queryDocumentSnapshots, e) -> {
                                                            if (e != null) {
                                                                Log.e(TAG, "When addSnapshotListener to query (Update WorkmateAdapter): Listen failed.", e);
                                                                return;
                                                            }

                                                            List<User> users = new ArrayList<>();

                                                            if (queryDocumentSnapshots != null) {
                                                                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                                                    if (doc != null) {
                                                                        users.add(doc.toObject(User.class));
                                                                    }
                                                                }
                                                            }

                                                            this.mAdapter.updateData(users);
                                                        });

        // RecyclerView
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                                                                       DividerItemDecoration.VERTICAL));

        // TODO: 02/01/2020 Replace DividerItemDecoration by a new class (inset divider)
    }
}
