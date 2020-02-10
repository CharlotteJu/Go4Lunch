package com.mancel.yann.go4lunch.views.fragments;

import android.content.res.Resources;
import android.location.Location;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.LocationData;
import com.mancel.yann.go4lunch.models.POI;
import com.mancel.yann.go4lunch.utils.CustomRelativeLayout;
import com.mancel.yann.go4lunch.utils.GeneratorBitmap;
import com.mancel.yann.go4lunch.views.adapters.InfoWindowAdapter;
import com.mancel.yann.go4lunch.views.adapters.OnClickButtonInfoWindowListener;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Yann MANCEL on 19/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.fragments
 *
 * A {@link BaseFragment} subclass which implements {@link OnMapReadyCallback},
 * {@link GoogleMap.OnCameraMoveStartedListener}, {@link GoogleMap.OnCameraIdleListener},
 * {@link GoogleMap.OnMarkerClickListener} and {@link OnClickButtonInfoWindowListener}.
 */
public class LunchMapFragment extends BaseFragment implements OnMapReadyCallback,
                                                              GoogleMap.OnCameraMoveStartedListener,
                                                              GoogleMap.OnCameraIdleListener,
                                                              GoogleMap.OnMarkerClickListener,
                                                              OnClickButtonInfoWindowListener {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.fragment_lunch_map_RelativeLayout)
    CustomRelativeLayout mRelativeLayout;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private GoogleMap mGoogleMap;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private LiveData<LocationData> mLocationDataLiveData;

    private boolean mIsFirstLocation = true;
    private boolean mIsLocatedOnUser = true;
    private boolean mIsSelectedMarker;

    private static final float DEFAULT_ZOOM = 17F;
    private static final double NEARBY_SEARCH_RADIUS = 200.0;

    private static final String TAG = LunchMapFragment.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Default constructor
     */
    public LunchMapFragment() {}

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseFragment --

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_lunch_map;
    }

    @Override
    protected void configureDesign() {
        // UI
        this.configureSupportMapFragment();

        // LiveData
        this.configureLocationLiveData();
        this.configurePOIsLiveData();
    }

    @Override
    public void onSuccessOfAutocomplete(@NonNull final Place place) {
        Log.d(TAG, "onAutocompleteListener: " + place.getLatLng() );
    }

    // -- Fragment --

    @Override
    public void onPause() {
        super.onPause();

        // Resets the configurations when the user navigates with the BottomNavigationView
        // and go back to this Fragment
        this.mIsFirstLocation = true;
        this.mIsLocatedOnUser = true;
    }

    // -- Actions --

    @OnClick(R.id.fragment_lunch_map_FAB)
    public void onFABClicked(@NonNull final View view) {
        // Focusing on vision against the current position
        if (!this.mIsLocatedOnUser) {
            this.animateCamera();

            // Reset: Camera focus on user
            this.mIsLocatedOnUser = true;
        }
    }

    // -- OnMapReadyCallback interface --

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        // Camera
        this.mGoogleMap.setOnCameraMoveStartedListener(this);
        this.mGoogleMap.setOnCameraIdleListener(this);

        // Markers
        this.mGoogleMap.setOnMarkerClickListener(this);

        // Event of CustomRelativeLayout (Info Window)
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        final float scale = this.getContext().getResources().getDisplayMetrics().density;
        this.mRelativeLayout.init(this.mGoogleMap, (int) ((39.0F + 20.0F) * scale + 0.5F));

        // Info window
        this.mGoogleMap.setInfoWindowAdapter(new InfoWindowAdapter(this.getContext(), this.mRelativeLayout, this));
    }

    // -- GoogleMap.OnCameraMoveStartedListener interface --

    @Override
    public void onCameraMoveStarted(int reason) {
        // By default, none of marker will be selected
        this.mIsSelectedMarker = false;
        
        switch (reason) {
            // The user gestured on the map (ex: Zoom or Rotation)
            case GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE:
                // No data in liveData
                if (this.mLocationDataLiveData.getValue() == null ||
                        this.mLocationDataLiveData.getValue().getLocation() == null) {
                    return;
                }

                // Location
                final Location location = this.mLocationDataLiveData.getValue()
                                                                    .getLocation();

                // Projection's Center (visible region) = current location of user (ex: zoom or rotation)
                if (this.mGoogleMap.getProjection().getVisibleRegion().latLngBounds.getCenter().latitude == location.getLatitude() &&
                    this.mGoogleMap.getProjection().getVisibleRegion().latLngBounds.getCenter().longitude == location.getLongitude()) {
                    // Do nothing because same center
                }
                else {
                    this.mIsLocatedOnUser = false;
                }

                break;

            // The user tapped something on the map (ex: tap on marker)
            case GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION:
                this.mIsLocatedOnUser = false;
                this.mIsSelectedMarker = true;
                break;

            // The app moved the camera (ex: GoogleMap#moveCamera of GoogleMap#animateCamera)
            case GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION:
                // Do nothing
        }
    }

    // -- GoogleMap.OnCameraIdleListener interface --

    @Override
    public void onCameraIdle() {
        // When the camera has stopped moving

        // Test on Radius for nearby search
        // TODO: 05/02/2020 analyse if really useful to update at each location update
        if (!this.mIsLocatedOnUser && !this.mIsSelectedMarker) {
            // Location
            final Location location = new Location("Actual Location");
            location.setLatitude(this.mGoogleMap.getProjection().getVisibleRegion().latLngBounds.getCenter().latitude);
            location.setLongitude(this.mGoogleMap.getProjection().getVisibleRegion().latLngBounds.getCenter().longitude);

            // LocationData
            final LocationData locationData = new LocationData(location, null);

            // Fetches POIs
            this.mViewModel.fetchNearbySearch(this.getContext(),
                                              locationData,
                                              NEARBY_SEARCH_RADIUS);
        }
    }

    // -- GoogleMap.OnMarkerClickListener interface --

    @Override
    public boolean onMarkerClick(Marker marker) {
        // The default behavior (return false) for a marker click event is to show its info window (if available)
        // and move the camera such that the marker is centered on the map.
        return false;
    }

    // -- OnClickButtonInfoWindowListener interface --

    @Override
    public void onClickOnDetailsButton(@Nullable final Marker marker) {
        // No marker
        if (marker == null) {
            return;
        }

        // InfoWindow
        marker.hideInfoWindow();

        // POI
        final POI poi = (POI) marker.getTag();

        // Place Id
        final String placeId = poi.getPlaceId();

        // Callback to Activity
        this.mCallbackFromFragmentToActivity.onSelectedRestaurant(placeId);
    }

    @Override
    public void onClickOnWayButton(@Nullable final Marker marker) {
        // No marker
        if (marker == null) {
            return;
        }

        // InfoWindow
        marker.hideInfoWindow();

        Log.d(TAG, "onClickOnWayButton: WAY");
        // TODO: 23/01/2020 Pass by Road API
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

    // -- Google Maps --

    /**
     * Configures the {@link SupportMapFragment}
     */
    private void configureSupportMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.fragment_lunch_map_fragment);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();

            this.getChildFragmentManager().beginTransaction()
                                          .add(R.id.fragment_lunch_map_fragment, mapFragment)
                                          .commit();
        }

        mapFragment.getMapAsync(this);
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
        this.mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        this.mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);

        // SCROLL
        this.mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        this.mGoogleMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(true);

        // MIN ZOOM LEVELS
        this.mGoogleMap.setMinZoomPreference( (this.mGoogleMap.getMinZoomLevel() > 10.0F) ? this.mGoogleMap.getMinZoomLevel() :
                                                                                            10.0F);

        // MAX ZOOM LEVELS
        this.mGoogleMap.setMaxZoomPreference( (this.mGoogleMap.getMaxZoomLevel() < 21.0F) ? this.mGoogleMap.getMaxZoomLevel() :
                                                                                            21.0F);

        // MY LOCATION
        this.mGoogleMap.setMyLocationEnabled(true);
        this.mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

        // TOOLBAR
        this.mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    /**
     * Configures the camera of {@link GoogleMap}
     */
    private void configureCamera() {
        // No data in liveData
        if (this.mLocationDataLiveData.getValue() == null ||
            this.mLocationDataLiveData.getValue().getLocation() == null) {
            return;
        }

        // Location
        final Location location = this.mLocationDataLiveData.getValue()
                                                            .getLocation();

        // Location to LatLng
        final LatLng currentLatLng = new LatLng(location.getLatitude(),
                                                location.getLongitude());

        // Camera
        this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                                                                     DEFAULT_ZOOM));
    }

    /**
     * Animates the camera of {@link GoogleMap}
     */
    private void animateCamera() {
        // No data in liveData
        if (this.mLocationDataLiveData.getValue() == null ||
            this.mLocationDataLiveData.getValue().getLocation() == null) {
            return;
        }

        // Location
        final Location location = this.mLocationDataLiveData.getValue()
                                                            .getLocation();

        // Location to LatLng -> Target
        final LatLng target = new LatLng(location.getLatitude(),
                                         location.getLongitude());

        // CameraPosition
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                                                                .target(target)
                                                                .zoom(DEFAULT_ZOOM)
                                                                .build();

        // Camera
        this.mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    // -- LiveData --

    /**
     * Configures the {@link LiveData<LocationData>}
     */
    private void configureLocationLiveData() {
        // Bind between liveData of ViewModel and the SupportMapFragment
        this.mLocationDataLiveData = this.mViewModel.getLocation(this.getContext());
        this.mLocationDataLiveData.observe(this.getViewLifecycleOwner(), this::onChangedLocationData);
    }

    /**
     * Method to replace the {@link androidx.lifecycle.Observer} of {@link LocationData}
     * @param locationData a {@link LocationData}
     */
    private void onChangedLocationData (@NonNull final LocationData locationData) {
        // Exception
        if (this.handleLocationException(locationData.getException())) {
            return;
        }

        // No Location
        if (locationData.getLocation() == null) {
            return;
        }

        // Focus on the current location of user
        if (this.mIsLocatedOnUser) {
            // First Location
            if (this.mIsFirstLocation) {
                this.mIsFirstLocation = false;

                // This method is put in place here and not in onMapReady method
                // because the MyLocation parameter of GoogleMap must have the permission.ACCESS_FINE_LOCATION.
                // This permission is already asked with LocationLiveData.
                this.configureGoogleMapStyle();

                this.configureCamera();
            }
            else {
                this.animateCamera();
            }

            // Fetches POIs
            // TODO: 05/02/2020 analyse if really useful to update at each location update
            this.mViewModel.fetchNearbySearch(this.getContext(),
                                              locationData,
                                              NEARBY_SEARCH_RADIUS);
        }
    }

    /**
     * Configures the {@link LiveData} of {@link List<POI>}
     */
    private void configurePOIsLiveData() {
        // Bind between liveData of ViewModel and the SupportMapFragment
        this.mViewModel.getPOIs(this.getContext(), null, NEARBY_SEARCH_RADIUS)
                       .observe(this.getViewLifecycleOwner(), this::onChangedPOIsData);
    }

    /**
     * Method to replace the {@link androidx.lifecycle.Observer} of {@link LiveData} of {@link List<POI>}
     * @param poiList a {@link List<POI>}
     */
    private void onChangedPOIsData(@NonNull final List<POI> poiList) {
        // Remove Markers
        this.mGoogleMap.clear();

        // Adds Markers
        for (POI poi : poiList) {
            int drawableValue = poi.getIsSelected() ? R.drawable.ic_location_user_select :
                                                      R.drawable.ic_location_user_unselect;

            final BitmapDescriptor bitmapDescriptor = GeneratorBitmap.bitmapDescriptorFromVector(this.getContext(),
                                                                                                 drawableValue);

            final Marker marker = this.mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(poi.getLatitude(),
                                                                                                    poi.getLongitude()))
                                                                               .icon(bitmapDescriptor));

            // TAG
            marker.setTag(poi);
        }
    }
}