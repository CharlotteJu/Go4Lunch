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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.liveDatas.LocationLiveData;
import com.mancel.yann.go4lunch.models.LocationData;
import com.mancel.yann.go4lunch.utils.GeneratorBitmap;
import com.mancel.yann.go4lunch.viewModels.MapViewModel;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.fragments
 *
 * A {@link BaseFragment} subclass which implements {@link OnMapReadyCallback},
 * {@link GoogleMap.OnCameraMoveStartedListener}, {@link GoogleMap.OnCameraMoveListener},
 * {@link GoogleMap.OnCameraMoveCanceledListener} and {@link GoogleMap.OnCameraIdleListener}.
 */
public class LunchMapFragment extends BaseFragment implements OnMapReadyCallback,
                                                              GoogleMap.OnCameraMoveStartedListener,
                                                              GoogleMap.OnCameraMoveListener,
                                                              GoogleMap.OnCameraMoveCanceledListener,
                                                              GoogleMap.OnCameraIdleListener {

    // FIELDS --------------------------------------------------------------------------------------

    @Nullable
    private SupportMapFragment mMapFragment;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private MapViewModel mMapViewModel;

    private GoogleMap mGoogleMap;
    private LocationLiveData mLocationLiveData;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private LatLng mCurrentLatLngUser;

    private boolean mIsFirstLocation = true;
    private boolean mIsLocatedOnUser = true;

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
        this.configureSupportMapFragment();
        this.configureMapViewModel();
        this.configureLocationLiveData();
    }

    // -- OnMapReadyCallback interface --

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        // Camera
        this.mGoogleMap.setOnCameraMoveStartedListener(this);
        this.mGoogleMap.setOnCameraMoveListener(this);
        this.mGoogleMap.setOnCameraMoveCanceledListener(this);
        this.mGoogleMap.setOnCameraIdleListener(this);

        // POIs
//        this.mGoogleMap.setOnPoiClickListener(this);

        // Configure the style of the GoogleMap
        this.configureGoogleMapStyle();
    }

    // -- GoogleMap.OnCameraMoveStartedListener interface --

    @Override
    public void onCameraMoveStarted(int reason) {
        Log.d(TAG, "onCameraMoveStarted: The camera move started.");
    }

    // -- GoogleMap.OnCameraMoveListener interface --

    @Override
    public void onCameraMove() {
        Log.d(TAG, "onCameraMove: The camera is moving.");

    }

    // -- GoogleMap.OnCameraMoveCanceledListener interface --

    @Override
    public void onCameraMoveCanceled() {
        Log.d(TAG, "onCameraMoveCanceled: Camera movement canceled.");
    }

    // -- GoogleMap.OnCameraIdleListener interface --

    @Override
    public void onCameraIdle() {
        Log.d(TAG, "onCameraIdle: The camera has stopped moving.");

        final CameraPosition camera = this.mGoogleMap.getCameraPosition();
        Log.d(TAG, "zoom " + camera.zoom);
        Log.d(TAG, "bearing " + camera.bearing);
        Log.d(TAG, "tilt " + camera.tilt);
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

        this.mMapFragment.getMapAsync(this);
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
        this.mGoogleMap.setMaxZoomPreference( (this.mGoogleMap.getMaxZoomLevel() < 21.0F) ? this.mGoogleMap.getMaxZoomLevel() :
                                                                                            21.0F);

        // TODO: 23/12/2019 Add a FAB or use this.mGoogleMap.setMyLocationEnabled(true);
    }

    // -- MapViewModel --

    /**
     * Configures the {@link MapViewModel}
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
        // Exception
        if (this.handleLocationException(locationData.getException())) {
            return;
        }

        // No Location
        if (locationData.getLocation() == null) {
            return;
        }

        if (this.mGoogleMap != null) {
            // Current location
            this.mCurrentLatLngUser = new LatLng(locationData.getLocation().getLatitude(),
                                                 locationData.getLocation().getLongitude());

            // First location
            if (this.mIsFirstLocation) {
                this.mIsFirstLocation = false;

//                this.mMapViewModel.getPOIs(this.mCurrentLatLngUser.latitude,
//                                           this.mCurrentLatLngUser.longitude);

                this.mGoogleMap.addMarker(new MarkerOptions().position(this.mCurrentLatLngUser)
                                                             .title("Current position"));


                final BitmapDescriptor bitmapDescriptor = GeneratorBitmap.bitmapDescriptorFromVector(this.getContext(), R.drawable.ic_star);

                this.mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(this.mCurrentLatLngUser.latitude + 0.0002,
                                                                                 this.mCurrentLatLngUser.longitude + 0.0002))
                                                             .title("test1")
                                                             .snippet("test to add marker")
                                                             .icon(bitmapDescriptor));

                //this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPlace));
                this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.mCurrentLatLngUser, 17));

                return;
            }

            if (this.mIsLocatedOnUser) {
                // TODO: 29/12/2019 to follow user when none of gesture has been done
            }

            //final LatLngBounds lngBounds = this.mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
            //Log.e(TAG, "[northeast]: " + lngBounds.northeast + " & [southwest]: " + lngBounds.southwest);
        }
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
