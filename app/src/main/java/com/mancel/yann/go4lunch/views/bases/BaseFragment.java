package com.mancel.yann.go4lunch.views.bases;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.bases
 *
 * A {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    // METHODS -------------------------------------------------------------------------------------

    protected abstract int getFragmentLayout();
    protected abstract void configureDesign();

    // -- Fragment --

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(this.getFragmentLayout(), container, false);

        // Using the ButterKnife library
        ButterKnife.bind(this, view);

        // Configures the design of the fragment
        this.configureDesign();

        return view;
    }
}
