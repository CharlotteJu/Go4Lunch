package com.mancel.yann.go4lunch.views.fragments;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.liveDatas.LocationLiveData;
import com.mancel.yann.go4lunch.models.LocationData;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.fragments
 *
 * A {@link BaseFragment} subclass.
 */
public class LunchMapFragment extends BaseFragment {

    // FIELDS --------------------------------------------------------------------------------------

    @Nullable
    private SupportMapFragment mMapFragment;

    private LocationLiveData mLocationLiveData;

    public static final int RC_PERMISSION_LOCATION_UPDATE_LOCATION = 100;
    public static final int RC_CHECK_SETTINGS_TO_LOCATION = 1000;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    public LunchMapFragment() {}

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_lunch_map;
    }

    @Override
    protected void configureDesign() {
        this.configureLocationLiveData();
        this.configureSupportMapFragment();
    }

    // -- LocationLiveData --

    /**
     * Configures the {@link LocationLiveData}
     */
    private void configureLocationLiveData() {
        this.mLocationLiveData = new LocationLiveData(getContext());
        this.mLocationLiveData.observe(getActivity(), this::onChangedLocationData);
    }

    /**
     * Method to replace the {@link androidx.lifecycle.Observer} of {@link LocationData}
     * @param locationData a {@link LocationData}
     */
    private void onChangedLocationData (LocationData locationData) {
        if (this.handleLocationException(locationData.getException())) {
            return;
        }

        Log.e("LunchMapFragment", "[LiveData] onChangedLocationData: " + locationData.getLocation());
    }

    /**
     * Handles the location {@link Exception}
     * @param exception an {@link Exception}
     * @return a boolean that is true if there is an {@link Exception}
     */
    private boolean handleLocationException(@Nullable Exception exception) {
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
                ResolvableApiException resolvable = (ResolvableApiException) exception;
                resolvable.startResolutionForResult(getActivity(),
                                                    RC_CHECK_SETTINGS_TO_LOCATION);
            } catch (IntentSender.SendIntentException sendEx) {
                // Ignore the error.
            }
        }

        return true;
    }

    /**
     * Starts the location update
     */
    public void startLocationUpdate() {
        this.mLocationLiveData.requestUpdateLocation();
    }

    // -- Permissions --

    /**
     * Checks the permission: ACCESS_FINE_LOCATION
     * @param requestCode an integer that contains the request code
     * @return a boolean [true for PERMISSION_GRANTED - false to PERMISSION_DENIED]
     */
    private boolean checkLocationPermission(int requestCode) {
        int permissionResult = ContextCompat.checkSelfPermission(getContext(),
                                                                 Manifest.permission.ACCESS_FINE_LOCATION);

        // PackageManager.PERMISSION_DENIED
        if (permissionResult != PackageManager.PERMISSION_GRANTED) {
            final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

            ActivityCompat.requestPermissions(getActivity(),
                                              permissions,
                                              requestCode);

            return false;
        }
        else {
            return true;
        }
    }

    // -- Google Maps --

    /**
     * Configures the {@link SupportMapFragment}
     */
    private void configureSupportMapFragment() {
        this.mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_lunch_map_fragment);

        if (this.mMapFragment == null) {
            this.mMapFragment = SupportMapFragment.newInstance();

            getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_lunch_map_fragment, this.mMapFragment)
                                .commit();
        }

        this.mMapFragment.getMapAsync(this::onMapReady);
    }

    /**
     * Updates the {@link GoogleMap}
     * @param googleMap a {@link GoogleMap}
     */
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                                               .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    // -- Instances --

    /**
     * Gets a new instance of {@link LunchMapFragment}
     * @return a {@link LunchMapFragment}
     */
    @NonNull
    public static LunchMapFragment newInstance() {
        return new LunchMapFragment();
    }
}
