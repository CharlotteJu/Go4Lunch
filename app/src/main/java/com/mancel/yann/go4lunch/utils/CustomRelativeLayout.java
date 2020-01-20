package com.mancel.yann.go4lunch.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Yann MANCEL on 19/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 *
 * A {@link RelativeLayout} subclass.
 */
public class CustomRelativeLayout extends RelativeLayout {

    // See http://androidlearningtutorials.com/blog_details.php?article_id=8

    // FIELDS --------------------------------------------------------------------------------------

    @Nullable
    private GoogleMap mMap = null;

    /**
     * Vertical offset in pixels between the bottom edge of our InfoWindow
     * and the marker position (by default it's bottom edge too).
     * It's a good idea to use custom markers and also the InfoWindow frame,
     * because we probably can't rely on the sizes of the default marker and frame.
     */
    private int mBottomOffsetPixels;

    @Nullable
    private Marker mMarker = null;

    @Nullable
    private View mInfoWindow = null;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- RelativeLayout --

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret = false;
        // Make sure that the mInfoWindow is shown and we have all the needed references
        if (this.mMarker != null && this.mMarker.isInfoWindowShown() && this.mMap != null && this.mInfoWindow != null) {
            // Get a marker position on the screen
            final Point point = this.mMap.getProjection().toScreenLocation(this.mMarker.getPosition());

            // Make a copy of the MotionEvent and adjust it's location
            // so it is relative to the mInfoWindow left top corner
            final MotionEvent copyEv = MotionEvent.obtain(ev);

            copyEv.offsetLocation(
                    -point.x + (this.mInfoWindow.getWidth() / 2),
                    -point.y + this.mInfoWindow.getHeight() + this.mBottomOffsetPixels);

            // Dispatch the adjusted MotionEvent to the mInfoWindow
            ret = this.mInfoWindow.dispatchTouchEvent(copyEv);
        }
        // If the mInfoWindow consumed the touch event, then just return true.
        // Otherwise pass this event to the super class and return it's result
        return ret || super.dispatchTouchEvent(ev);
    }

    // -- Init --

    /**
     * Must be called before we can route the touch events
     * @param map                   a {@link GoogleMap}
     * @param bottomOffsetPixels    Vertical offset in pixels between the bottom edge of our InfoWindow
     *                              and the marker position (by default it's bottom edge too).
     */
    public void init(@Nullable final GoogleMap map, int bottomOffsetPixels) {
        this.mMap = map;
        this.mBottomOffsetPixels = bottomOffsetPixels;
    }

    // -- Marker and InfoWindow --

    /**
     * Best to be called from either the {@link com.mancel.yann.go4lunch.views.adapters.InfoWindowAdapter#getInfoContents(Marker)}
     * or {@link com.mancel.yann.go4lunch.views.adapters.InfoWindowAdapter#getInfoWindow(Marker)}
     * @param marker        a {@link Marker}
     * @param infoWindow    a {@link View}
     */
    public void setMarkerWithInfoWindow(@Nullable final Marker marker,
                                        @Nullable final View infoWindow) {
        this.mMarker = marker;
        this.mInfoWindow = infoWindow;
    }
}