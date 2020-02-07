package com.mancel.yann.go4lunch.views.adapters;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * Created by Yann MANCEL on 03/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 */
public interface AdapterListener {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * the callback method is activated with the updateData method
     */
    void onDataChanged();

    /**
     * the callback method is activated with the user click on
     * the {@link androidx.cardview.widget.CardView}
     * @param view a {@link View}
     */
    void onCardViewClicked(@NonNull final View view);
}
