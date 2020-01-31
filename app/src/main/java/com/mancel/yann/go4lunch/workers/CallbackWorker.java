package com.mancel.yann.go4lunch.workers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mancel.yann.go4lunch.models.User;

import java.util.List;

/**
 * Created by Yann MANCEL on 31/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.workers
 */
public interface CallbackWorker {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Method that is called when the action is a success
     * @param nameOfRestaurant  a {@link String} that contains the name of restaurant
     * @param users             a {@link List<User>} that contains all users who have selected the restaurant
     */
    void onSuccess(@Nullable final String nameOfRestaurant,
                   @Nullable final List<User> users);

    /**
     * Method that is called when the action is a fail
     * @param exception an {@link Exception}
     */
    void onFailure(@NonNull final Exception exception);
}
