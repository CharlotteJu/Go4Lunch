package com.mancel.yann.go4lunch.views.dialogs;

/**
 * Created by Yann MANCEL on 12/02/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.dialogs
 */
public interface DialogListener {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Callback from {@link androidx.fragment.app.DialogFragment} to {@link android.app.Activity}
     * @param newRating a double that contains the rating value
     */
    void onClickOnPositiveButton(final float newRating);

    /**
     * Callback from {@link androidx.fragment.app.DialogFragment} to {@link android.app.Activity}
     * @param isChecked a boolean that contains the On/Off value of the notification
     */
    void onClickOnPositiveButton(final boolean isChecked);
}
