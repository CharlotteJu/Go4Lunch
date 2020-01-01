package com.mancel.yann.go4lunch.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by Yann MANCEL on 29/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 */
public abstract class GeneratorBitmap {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Converts a resource (vector) to {@link Bitmap}
     * @param context       a {@link Context}
     * @param vectorResId   an integer that corresponds to the resource
     * @return  a {@link BitmapDescriptor}
     */
    @Nullable
    public static BitmapDescriptor bitmapDescriptorFromVector(@NonNull final Context context, int vectorResId) {
        final Drawable drawable = ContextCompat.getDrawable(context, vectorResId);

        if (drawable == null) {
            return null;
        }

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        final Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                                                  drawable.getIntrinsicHeight(),
                                                  Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
