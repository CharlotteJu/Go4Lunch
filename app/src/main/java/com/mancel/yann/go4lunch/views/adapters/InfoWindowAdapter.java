package com.mancel.yann.go4lunch.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.apis.GoogleMapsService;
import com.mancel.yann.go4lunch.models.POI;
import com.mancel.yann.go4lunch.utils.CustomRelativeLayout;
import com.mancel.yann.go4lunch.utils.OnInfoWindowElemTouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 17/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.adapters
 *
 * A class which implements {@link GoogleMap.InfoWindowAdapter}
 */
public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.marker_info_window_Name)
    TextView mName;
    @BindView(R.id.marker_info_window_Image)
    ImageView mImage;
    @BindView(R.id.marker_info_window_Details)
    Button mDetailsButton;
    @BindView(R.id.marker_info_window_Way)
    Button mWayButton;

    @NonNull
    private final Context mContext;

    @NonNull
    private final CustomRelativeLayout mRelativeLayout;

    @NonNull
    private OnClickButtonInfoWindowListener mCallback;

    @NonNull
    private final View mView;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private OnInfoWindowElemTouchListener mDetailsListener;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private OnInfoWindowElemTouchListener mWayListener;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with an argument
     * @param context           a {@link Context}
     * @param relativeLayout    a {@link CustomRelativeLayout}
     * @param callback          a {@link OnClickButtonInfoWindowListener}
     */
    public InfoWindowAdapter(@NonNull final Context context,
                             @NonNull final CustomRelativeLayout relativeLayout,
                             @NonNull final OnClickButtonInfoWindowListener callback) {
        this.mContext = context;
        this.mRelativeLayout = relativeLayout;
        this.mCallback = callback;

        this.mView = ((Activity) this.mContext).getLayoutInflater()
                                               .inflate(this.getLayout(), null);

        // Using the ButterKnife library
        ButterKnife.bind(this, this.mView);

        // Listeners of Buttons
        this.configureButtonsAndListeners();
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- GoogleMap.InfoWindowAdapter --

    @Override
    public View getInfoWindow(Marker marker) {
        // Allows to provide a view that will be used for the entire info window

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Allows to just customize the contents of the window
        // but still keep the default info window frame and background

        // Updates UI
        this.updateUI(marker);

        return this.mView;
    }

    // -- Layout --

    /**
     * Gets the layout value
     * @return an integer that contains the layout value
     */
    private int getLayout() {
        return R.layout.marker_info_window;
    }

    // -- UI --

    /**
     * Updates the {@link View}
     * @param marker a {@link Marker}
     */
    private void updateUI(@NonNull final Marker marker) {
        // POI
        final POI poi = (POI) marker.getTag();

        // Url Photo
        final String urlPhoto = (poi.getPhotoUrl() == null) ? null :
                                                              GoogleMapsService.getPhoto(poi.getPhotoUrl(),
                                                                                        400,
                                                                                         this.mContext.getString(R.string.google_maps_key));

        // Image (using to Glide library)
        Glide.with(this.mContext)
             .load(urlPhoto)
             .fallback(R.drawable.ic_restaurant)
             .error(R.drawable.ic_close)
             .listener(new RequestListener<Drawable>() {
                 @Override
                 public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                     return false;
                 }

                 @Override
                 public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                     // Creates a time range to refresh the content of ImageView
                     new Handler().postDelayed( () -> {
                         // Refreshes the image
                         if (marker.isInfoWindowShown()) {
                             marker.showInfoWindow();
                         }
                     }, 100);

                     return false;
                 }
             })
             .into(this.mImage);

        // Name
        this.mName.setText(poi.getName());

        // Listeners
        this.mDetailsListener.setMarker(marker);
        this.mWayListener.setMarker(marker);

        // Event of CustomRelativeLayout (Info Window)
        this.mRelativeLayout.setMarkerWithInfoWindow(marker, this.mView);
    }

    // -- Button --

    /**
     * Configures several {@link Button} and {@link OnInfoWindowElemTouchListener}
     */
    private void configureButtonsAndListeners() {
        // Details button
        this.mDetailsListener = new OnInfoWindowElemTouchListener() {
            @Override
            protected void onClickConfirmed(@NonNull Marker marker) {
                // Callback for Details Button
                mCallback.onClickOnDetailsButton(marker);
            }
        };
        this.mDetailsButton.setOnTouchListener(this.mDetailsListener);

        // Way button
        this.mWayListener = new OnInfoWindowElemTouchListener() {
            @Override
            protected void onClickConfirmed(@NonNull Marker marker) {
                // Callback for Way Button
                mCallback.onClickOnWayButton(marker);
            }
        };
        this.mWayButton.setOnTouchListener(this.mWayListener);
    }
}