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
public class LunchListFragment extends BaseFragment {

    // FIELDS --------------------------------------------------------------------------------------


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
}
