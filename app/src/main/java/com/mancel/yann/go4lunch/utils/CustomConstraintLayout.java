package com.mancel.yann.go4lunch.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by Yann MANCEL on 28/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 *
 * A {@link ConstraintLayout} subclass.
 */
public class CustomConstraintLayout extends ConstraintLayout {

    // CONSTRUCTORS --------------------------------------------------------------------------------

    public CustomConstraintLayout(Context context) {
        super(context);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ConstraintLayout --

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        int childCount = this.getChildCount();

        for (int index = 0; index < childCount; ++index) {
            this.getChildAt(index).dispatchApplyWindowInsets(insets);
        }

        return insets;
    }
}
