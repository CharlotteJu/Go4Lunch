package com.mancel.yann.go4lunch.views.fragments;

import androidx.annotation.NonNull;

/**
 * Created by Yann MANCEL on 20/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.fragments
 */
public interface FragmentListener {

    /**
     * Callback from {@link androidx.fragment.app.Fragment} to {@link android.app.Activity}
     * @param placeIdOfRestaurant a {@link String} that contains the Place Id value
     */
    void onSelectedRestaurant(@NonNull final String placeIdOfRestaurant);
}
