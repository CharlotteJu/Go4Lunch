package com.mancel.yann.go4lunch.views.bases;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.ResolvableApiException;

import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.bases
 *
 * A {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    // FIELDS --------------------------------------------------------------------------------------

    public static final int RC_PERMISSION_LOCATION_UPDATE_LOCATION = 1000;
    public static final int RC_CHECK_SETTINGS_TO_LOCATION = 2000;

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

    // -- Permissions --

    /**
     * Checks the permission: ACCESS_FINE_LOCATION
     * @param requestCode an integer that contains the request code
     * @return a boolean [true for PERMISSION_GRANTED - false to PERMISSION_DENIED]
     */
    private boolean checkLocationPermission(int requestCode) {
        int permissionResult = ContextCompat.checkSelfPermission(this.getContext(), // this.getApplicationContext()
                                                                 Manifest.permission.ACCESS_FINE_LOCATION);

        // PackageManager.PERMISSION_DENIED
        if (permissionResult != PackageManager.PERMISSION_GRANTED) {
            final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

            ActivityCompat.requestPermissions(this.getActivity(),
                                              permissions,
                                              requestCode);

            return false;
        }
        else {
            return true;
        }
    }

    // -- Exceptions --

    /**
     * Handles the location {@link Exception}
     * @param exception an {@link Exception}
     * @return a boolean that is true if there is an {@link Exception}
     */
    protected boolean handleLocationException(@Nullable final Exception exception) {
        // No exception
        if (exception == null) {
            return false;
        }

        if (exception instanceof SecurityException) {
            this.checkLocationPermission(RC_PERMISSION_LOCATION_UPDATE_LOCATION);
        }

        if (exception instanceof ResolvableApiException) {
            // Location settings are not satisfied, but this can be fixed
            // by showing the user a dialog.
            try {
                // Show the dialog by calling startResolutionForResult(),
                // and check the result in onActivityResult().
                final ResolvableApiException resolvable = (ResolvableApiException) exception;
                resolvable.startResolutionForResult(this.getActivity(),
                                                    RC_CHECK_SETTINGS_TO_LOCATION);
            }
            catch (IntentSender.SendIntentException sendEx) {
                // Ignore the error.
            }
        }

        return true;
    }
}
