package com.mancel.yann.go4lunch.utils;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Yann MANCEL on 19/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 *
 * A class which implements {@link View.OnTouchListener}.
 */
public abstract class OnInfoWindowElemTouchListener implements View.OnTouchListener {

    // FIELDS --------------------------------------------------------------------------------------

    @SuppressWarnings("NullableProblems")
    @NonNull
    private Marker mMarker;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor by default
     */
    public OnInfoWindowElemTouchListener() {}

    // METHODS -------------------------------------------------------------------------------------

    protected abstract void onClickConfirmed(@NonNull final Marker marker);

    // -- View.OnTouchListener --

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Limits to an only one action to avoid the multiple event
        // (ex: click on button is in reality 2 events)
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            this.onClickConfirmed(this.mMarker);
        }

        return false;
    }

    // -- Marker --

    public void setMarker(@NonNull final Marker marker) {
        this.mMarker = marker;
    }
}