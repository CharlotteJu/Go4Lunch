package com.mancel.yann.go4lunch.views.fragments;

import android.support.annotation.NonNull;

import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.fragments
 *
 * A {@link BaseFragment} subclass.
 */
public class WorkmateFragment extends BaseFragment {

    // FIELDS --------------------------------------------------------------------------------------


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
}
