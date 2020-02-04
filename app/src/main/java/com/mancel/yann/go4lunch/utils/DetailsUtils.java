package com.mancel.yann.go4lunch.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.Details;

import java.util.List;

/**
 * Created by Yann MANCEL on 21/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 */
public abstract class DetailsUtils {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Gets a couple of food type and address
     * @param context           a {@link Context}
     * @param addressComponents a {@link List < Details.AddressComponent>} that contains the address data
     * @return a {@link String} that contains the couple of food type and address
     */
    public static String createStringOfAddress(@NonNull final Context context,
                                               @Nullable final List<Details.AddressComponent> addressComponents) {
        final StringBuilder stringBuilder = new StringBuilder();

        // No addressComponents
        if (addressComponents == null) {
            stringBuilder.append(context.getString(R.string.no_address));

            return stringBuilder.toString();
        }

        // Research street_number and route
        String streetNumber = null;
        String route = null;

        for (Details.AddressComponent addressComponent : addressComponents) {
            // street_number
            if (addressComponent.getTypes().get(0).contains("street_number")) {
                streetNumber = addressComponent.getShortName() + " ";
            }

            // route
            if (addressComponent.getTypes().get(0).contains("route")) {
                route = addressComponent.getShortName();
            }
        }

        // No streetNumber and no route
        if (streetNumber == null && route == null) {
            stringBuilder.append(context.getString(R.string.no_specific_address));

            return stringBuilder.toString();
        }

        // streetNumber
        if (streetNumber != null) {
            stringBuilder.append(streetNumber);
        }

        // route
        if (route != null) {
            stringBuilder.append(route);
        }

        return stringBuilder.toString();
    }
}
