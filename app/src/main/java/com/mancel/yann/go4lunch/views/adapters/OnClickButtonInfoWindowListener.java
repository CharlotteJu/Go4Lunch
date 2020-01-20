package com.mancel.yann.go4lunch.views.adapters;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Yann MANCEL on 20/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 */
public interface OnClickButtonInfoWindowListener {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Callback at the click of Detail {@link android.widget.Button}
     * @param marker a {@link Marker}
     */
    void onClickOnDetailsButton(@Nullable final Marker marker);

    /**
     * Callback at the click of Way {@link android.widget.Button}
     * @param marker a {@link Marker}
     */
    void onClickOnWayButton(@Nullable final Marker marker);
}
