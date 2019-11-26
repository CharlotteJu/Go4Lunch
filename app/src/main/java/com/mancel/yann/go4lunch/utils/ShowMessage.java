package com.mancel.yann.go4lunch.utils;

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
    public static void showMessageWithSnackbar(@NonNull CoordinatorLayout coordinatorLayout, @NonNull String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}
