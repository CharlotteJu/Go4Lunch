package com.mancel.yann.go4lunch.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mancel.yann.go4lunch.R;

/**
 * Created by Yann MANCEL on 23/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 *
 * A {@link RecyclerView.ItemDecoration} subclass.
 */
public class InsetDivider extends RecyclerView.ItemDecoration {

    // Fetch from https://github.com/gejiaheng/Dividers-For-RecyclerView

    // FIELDS --------------------------------------------------------------------------------------

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Paint mPaint;
    // in pixel
    private int mDividerHeight;
    // left inset for vertical list, top inset for horizontal list
    private int mFirstInset;
    // right inset for vertical list, bottom inset for horizontal list
    private int mSecondInset;
    private int mColor;
    private int mOrientation;
    // set it to true to draw divider on the tile, or false to draw beside the tile.
    // if you set it to false and have inset at the same time, you may see the background of
    // the parent of RecyclerView.
    private boolean mOverlay;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Private constructor
     */
    private InsetDivider() {
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setStyle(Paint.Style.FILL);
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- RecyclerView.ItemDecoration --

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (this.mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (this.mOverlay) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }

        if (this.mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDividerHeight);
        } else {
            outRect.set(0, 0, mDividerHeight, 0);
        }
    }

    // -- Getters --

    public int getDividerHeight() {
        return this.mDividerHeight;
    }

    public int getFirstInset() {
        return this.mFirstInset;
    }

    public int getSecondInset() {
        return this.mSecondInset;
    }

    public int getColor() {
        return this.mColor;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public boolean getOverlay() {
        return this.mOverlay;
    }

    // -- Setters --

    public void setDividerHeight(int dividerHeight) {
        this.mDividerHeight = dividerHeight;
    }

    public void setFirstInset(int firstInset) {
        this.mFirstInset = firstInset;
    }

    public void setSecondInset(int secondInset) {
        this.mSecondInset = secondInset;
    }

    public void setColor(int color) {
        this.mColor = color;
        mPaint.setColor(color);
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    public void setOverlay(boolean overlay) {
        this.mOverlay = overlay;
    }

    // -- Draw --

    protected void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + this.mFirstInset;
        final int right = parent.getWidth() - parent.getPaddingRight() - this.mSecondInset;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (parent.getChildAdapterPosition(child) == (parent.getAdapter().getItemCount() - 1)) {
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int bottom;
            final int top;
            if (this.mOverlay) {
                bottom = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
                top = bottom - this.mDividerHeight;
            } else {
                top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
                bottom = top + this.mDividerHeight;
            }
            c.drawRect(left, top, right, bottom, this.mPaint);
        }
    }

    protected void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop() + this.mFirstInset;
        final int bottom = parent.getHeight() - parent.getPaddingBottom() - this.mSecondInset;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (parent.getChildAdapterPosition(child) == (parent.getAdapter().getItemCount() - 1)) {
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int right;
            final int left;
            if (this.mOverlay) {
                right = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
                left = right - this.mDividerHeight;
            } else {
                left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
                right = left + this.mDividerHeight;
            }
            c.drawRect(left, top, right, bottom, this.mPaint);
        }
    }

    // INNER CLASS ---------------------------------------------------------------------------------

    /**
     * Handy builder for creating {@link InsetDivider} instance.
     */
    public static class Builder {

        // FIELDS ----------------------------------------------------------------------------------

        private Context mContext;
        private int mDividerHeight;
        private int mFirstInset;
        private int mSecondInset;
        private int mColor;
        private int mOrientation;
        private boolean mOverlay = true; // set default to true to follow Material Design Guidelines

        // CONSTRUCTORS ----------------------------------------------------------------------------

        public Builder(Context context) {
            this.mContext = context;
        }

        // METHODS ---------------------------------------------------------------------------------

        public Builder dividerHeight(int dividerHeight) {
            this.mDividerHeight = dividerHeight;
            return this;
        }

        public Builder insets(int firstInset, int secondInset) {
            this.mFirstInset = firstInset;
            this.mSecondInset = secondInset;
            return this;
        }

        public Builder color(@ColorInt int color) {
            this.mColor = color;
            return this;
        }

        public Builder orientation(int orientation) {
            this.mOrientation = orientation;
            return this;
        }

        public Builder overlay(boolean overlay) {
            this.mOverlay = overlay;
            return this;
        }

        public InsetDivider build() {
            InsetDivider insetDivider = new InsetDivider();

            if (this.mDividerHeight == 0) {
                // Set default divider height to 1dp.
                insetDivider.setDividerHeight(this.mContext.getResources().getDimensionPixelSize(R.dimen.divider_height));
            } else if (this.mDividerHeight > 0) {
                insetDivider.setDividerHeight(this.mDividerHeight);
            } else {
                throw new IllegalArgumentException("Divider's height can't be negative.");
            }

            insetDivider.setFirstInset(this.mFirstInset < 0 ? 0 : this.mFirstInset);
            insetDivider.setSecondInset(this.mSecondInset < 0 ? 0 : this.mSecondInset);

            if (this.mColor == 0) {
                throw new IllegalArgumentException("Don't forget to set color");
            } else {
                insetDivider.setColor(this.mColor);
            }

            if (this.mOrientation != InsetDivider.HORIZONTAL_LIST && this.mOrientation != InsetDivider.VERTICAL_LIST) {
                throw new IllegalArgumentException("Invalid orientation");
            } else {
                insetDivider.setOrientation(this.mOrientation);
            }

            insetDivider.setOverlay(this.mOverlay);

            return insetDivider;
        }
    }
}