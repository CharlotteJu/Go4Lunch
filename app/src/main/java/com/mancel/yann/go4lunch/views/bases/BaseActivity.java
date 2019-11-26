package com.mancel.yann.go4lunch.views.bases;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    // -- Fragment --

    /**
     * Replaces the fragment
     * @param fragment      a {@link Fragment}
     * @param idFrameLayout an integer that contains the id of the {@link android.widget.FrameLayout}
     */
    protected void replaceFragment(final Fragment fragment, final int idFrameLayout) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                                       .replace(idFrameLayout, fragment)
                                       .commit();
        }
    }
}
