package com.mancel.yann.go4lunch.views.bases;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;

/**
 * Created by Yann MANCEL on 18/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.bases
 *
 * A {@link AppCompatActivity} subclass.
 */
public abstract class BaseActivity extends AppCompatActivity {

    // METHODS -------------------------------------------------------------------------------------

    protected abstract int getActivityLayout();
    @Nullable
    protected abstract Toolbar getToolbar();
    protected abstract void configureDesign();

    // -- Activity --

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associates the layout file to this class
        setContentView(this.getActivityLayout());

        // Using the ButterKnife library
        ButterKnife.bind(this);

        // Configures the design of the activity
        this.configureDesign();
    }

    // -- Toolbar --

    /**
     * Configures the {@link Toolbar} field
     */
    protected void configureToolBar() {
        // If ToolBar exists
        if (this.getToolbar() != null) {
            setSupportActionBar(this.getToolbar());
        }
    }
}