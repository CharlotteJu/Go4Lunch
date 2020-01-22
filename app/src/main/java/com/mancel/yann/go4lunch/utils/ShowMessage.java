package com.mancel.yann.go4lunch.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;

/**
 * Created by Yann MANCEL on 18/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 */
public abstract class ShowMessage {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Shows a {@link Snackbar} with a message
     * @param coordinatorLayout a {@link CoordinatorLayout} that contains the view
     * @param message           a {@link String} that contains the message to display
     */
    public static void showMessageWithSnackbar(@NonNull final CoordinatorLayout coordinatorLayout,
                                               @NonNull final String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT)
                .show();
    }

    /**
     * Shows a {@link Snackbar} with a message and a {@link android.widget.Button}
     * @param coordinatorLayout a {@link CoordinatorLayout} that contains the view
     * @param message           a {@link String} that contains the message to display
     * @param textButton        a {@link String} that contains the message to display into button
     * @param clickListener     a {@link android.view.View.OnClickListener} for the button click event
     */
    public static void showMessageWithSnackbarWithButton(@NonNull final CoordinatorLayout coordinatorLayout,
                                                         @NonNull final String message,
                                                         @NonNull final String textButton,
                                                         @NonNull final View.OnClickListener clickListener) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT)
                .setAction(textButton, clickListener)
                .show();
    }
}
