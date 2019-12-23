package com.mancel.yann.go4lunch.views.fragments;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.liveDatas.LocationLiveData;
import com.mancel.yann.go4lunch.models.LocationData;
import com.mancel.yann.go4lunch.viewModels.MapViewModel;
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

    @SuppressWarnings("NullableProblems")
    @NonNull
    private MapViewModel mMapViewModel;

    private GoogleMap mGoogleMap;
    private LocationLiveData mLocationLiveData;

    public static final int RC_PERMISSION_LOCATION_UPDATE_LOCATION = 100;
    public static final int RC_CHECK_SETTINGS_TO_LOCATION = 1000;

    private static final String TAG = LunchMapFragment.class.getSimpleName();

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
        this.configureMapViewModel();
        this.configureLocationLiveData();
        this.configureSupportMapFragment();
    }

    // -- MapViewModel --

    /**
     * Configures the {@link }
     */
    private void configureMapViewModel() {
        this.mMapViewModel = ViewModelProviders.of(this.getActivity())
                                               .get(MapViewModel.class);
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

        if (locationData.getLocation() == null) {
            return;
        }

        if (this.mGoogleMap != null) {
            LatLng currentPlace = new LatLng(locationData.getLocation().getLatitude(),
                                             locationData.getLocation().getLongitude());

            this.mGoogleMap.addMarker(new MarkerOptions().position(currentPlace)
                           .title("Home"));

            //this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPlace));
            this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace, 18));

            //this.mGoogleMap.getProjection().getVisibleRegion().latLngBounds.northeast;
        }

        //Log.e("LunchMapFragment", "[LiveData] onChangedLocationData: " + locationData.getLocation());
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
                                .add(R.id.fragment_lunch_map_fragment, this.mMapFragment)
                                .commit();
        }

        this.mMapFragment.getMapAsync(this::onMapReady);
    }

    /**
     * Updates the {@link GoogleMap}
     * @param googleMap a {@link GoogleMap}
     */
    private void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        // Configure the style of the GoogleMap
        this.configureGoogleMapStyle();
    }

    /**
     * Configures the style of the {@link GoogleMap}
     */
    private void configureGoogleMapStyle() {
        // STYLE
        try {
            // Customises the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = this.mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.getContext(),
                                                                                               R.raw.google_maps_style_json));

            if (!success) {
                Log.e(TAG, "configureGoogleMapStyle: Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "configureGoogleMapStyle: Can't find style. Error: ", e);
        }

        // GESTURES
        final UiSettings uiSettings = this.mGoogleMap.getUiSettings();
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        // MIN ZOOM LEVELS
        this.mGoogleMap.setMinZoomPreference( (this.mGoogleMap.getMinZoomLevel() > 10.0F) ? this.mGoogleMap.getMinZoomLevel() :
                                                                                            10.0F);

        // MAX ZOOM LEVELS
        this.mGoogleMap.setMaxZoomPreference( (this.mGoogleMap.getMaxZoomLevel() < 25.0F) ? this.mGoogleMap.getMaxZoomLevel() :
                                                                                            25.0F);

        // TODO: 23/12/2019 Add a FAB or use this.mGoogleMap.setMyLocationEnabled(true);
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
