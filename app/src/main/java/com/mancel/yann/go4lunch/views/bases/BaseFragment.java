package com.mancel.yann.go4lunch.views.bases;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.ResolvableApiException;
import com.mancel.yann.go4lunch.repositories.MessageRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;
import com.mancel.yann.go4lunch.viewModels.Go4LunchViewModel;
import com.mancel.yann.go4lunch.viewModels.Go4LunchViewModelFactory;
import com.mancel.yann.go4lunch.views.fragments.FragmentListener;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.bases
 *
 * A {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    // FIELDS --------------------------------------------------------------------------------------

    @SuppressWarnings("NullableProblems")
    @NonNull
    protected FragmentListener mCallbackFromFragmentToActivity;

    @SuppressWarnings("NullableProblems")
    @NonNull
    protected Go4LunchViewModel mViewModel;

    private static final int REQUEST_CODE_PERMISSION_LOCATION = 1000;
    private static final int REQUEST_CODE_CHECK_SETTINGS_TO_LOCATION = 2000;

    private static final String TAG = BaseFragment.class.getSimpleName();

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Gets the integer value of the fragment layout
     * @return an integer that corresponds to the fragment layout
     */
    protected abstract int getFragmentLayout();

    /**
     * Configures the design of each daughter class
     */
    protected abstract void configureDesign();

    // -- Fragment --

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Configures the callback to the parent activity
        try {
            this.mCallbackFromFragmentToActivity = (FragmentListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragmentListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(this.getFragmentLayout(), container, false);

        // Using the ButterKnife library
        ButterKnife.bind(this, view);

        // ViewModel
        this.configureViewModel();

        // Configures the design of the fragment
        this.configureDesign();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // ACCESS_FINE_LOCATION
        if (requestCode == REQUEST_CODE_PERMISSION_LOCATION) {
            // No permission
            if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Log.e(TAG, "No permission to access fine location");
            }

            this.mViewModel.startLocationUpdate();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check settings to location
        if (requestCode == REQUEST_CODE_CHECK_SETTINGS_TO_LOCATION && resultCode == RESULT_OK) {
            this.mViewModel.startLocationUpdate();
        }
    }

    // -- ViewModel --

    /**
     * Configures the {@link Go4LunchViewModel}
     */
    private void configureViewModel() {
        // TODO: 07/02/2020 Repositories must be removed thanks to Dagger 2
        final Go4LunchViewModelFactory factory = new Go4LunchViewModelFactory(new UserRepositoryImpl(),
                                                                              new MessageRepositoryImpl(),
                                                                              new PlaceRepositoryImpl());

        this.mViewModel = new ViewModelProvider(this, factory).get(Go4LunchViewModel.class);
    }

    // -- Permissions --

    /**
     * Checks the permission: ACCESS_FINE_LOCATION
     * @param requestCode an integer that contains the request code
     * @return a boolean [true for PERMISSION_GRANTED - false to PERMISSION_DENIED]
     */
    private boolean checkLocationPermission(final int requestCode) {
        // ACCESS_FINE_LOCATION
        final int permissionResult = ContextCompat.checkSelfPermission(this.getContext(),
                                                                       Manifest.permission.ACCESS_FINE_LOCATION);

        // PERMISSION_DENIED
        if (permissionResult != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
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
            this.checkLocationPermission(REQUEST_CODE_PERMISSION_LOCATION);
        }

        if (exception instanceof ResolvableApiException) {
            // Location settings are not satisfied, but this can be fixed
            // by showing the user a dialog.
            try {
                // Show the dialog by calling startResolutionForResult(),
                // and check the result in onActivityResult().
                final ResolvableApiException resolvable = (ResolvableApiException) exception;
                resolvable.startResolutionForResult(this.getActivity(),
                                                    REQUEST_CODE_CHECK_SETTINGS_TO_LOCATION);
            }
            catch (IntentSender.SendIntentException sendEx) {
                // Ignore the error.
            }
        }

        return true;
    }
}
