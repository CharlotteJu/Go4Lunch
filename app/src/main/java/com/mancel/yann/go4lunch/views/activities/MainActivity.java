package com.mancel.yann.go4lunch.views.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.views.bases.BaseActivity;
import com.mancel.yann.go4lunch.views.fragments.LunchListFragment;
import com.mancel.yann.go4lunch.views.fragments.LunchMapFragment;
import com.mancel.yann.go4lunch.views.fragments.WorkmateFragment;

import butterknife.BindView;

/**
 * Created by Yann MANCEL on 18/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.activities
 *
 * A {@link BaseActivity} subclass.
 */
public class MainActivity extends BaseActivity {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.activity_main_bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    private LunchMapFragment mLunchMapFragment;
    private LunchListFragment mLunchListFragment;
    private WorkmateFragment mWorkmateFragment;

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseActivity --

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return this.mToolbar;
    }

    @Override
    protected void configureDesign() {
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomNavigationView();
        this.configureLunchMapFragment();
    }

    // -- Activity --

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creates a MenuInflater to add the menu xml file into the Toolbar
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu_search:
                Log.e(this.getClass().getSimpleName(), "Search");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    // -- Actions --

    private boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            // NavigationView
            case R.id.menu_drawer_lunch:
                Log.e(this.getClass().getSimpleName(), "Lunch");
                break;
            case R.id.menu_drawer_setting:
                Log.e(this.getClass().getSimpleName(), "Setting");
                break;
            case R.id.menu_drawer_logout:
                Log.e(this.getClass().getSimpleName(), "Logout");
                break;

            // BottomNavigationView
            case R.id.bottom_navigation_menu_map:
                this.configureLunchMapFragment();
                break;
            case R.id.bottom_navigation_menu_list:
                this.configureLunchListFragment();
                break;
            case R.id.bottom_navigation_menu_workmate:
                this.configureWorkmateFragment();
                break;

            default:
                Log.e(this.getClass().getSimpleName(), "onNavigationItemSelected: none of ids selected among the list");
        }

        // Closes the NavigationView at the end of the user action
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    // -- DrawerLayout --

    /**
     * Configures the {@link DrawerLayout}
     */
    private void configureDrawerLayout() {
        // Creates the Hamburger button of the toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                                                                 this.mDrawerLayout,
                                                                 this.getToolbar(),
                                                                 R.string.navigation_drawer_open,
                                                                 R.string.navigation_drawer_close);

        // Adds the listener (the "Hamburger" button) to the DrawerLayout field
        this.mDrawerLayout.addDrawerListener(toggle);

        // Synchronization
        toggle.syncState();
    }

    // -- NavigationView --

    /**
     * Configures the {@link NavigationView}
     */
    private void configureNavigationView() {
        this.mNavigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    // -- BottomNavigationView --

    /**
     * Configures the {@link BottomNavigationView}
     */
    private void configureBottomNavigationView() {
        this.mBottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    // -- Fragment --

    /**
     * Configures the {@link LunchMapFragment}
     */
    private void configureLunchMapFragment() {
        if (this.mLunchMapFragment == null) {
            this.mLunchMapFragment = LunchMapFragment.newInstance();
        }

        this.replaceFragment(this.mLunchMapFragment, R.id.activity_main_frame_layout);
    }

    /**
     * Configures the {@link LunchListFragment}
     */
    private void configureLunchListFragment() {
        if (this.mLunchListFragment == null) {
            this.mLunchListFragment = LunchListFragment.newInstance();
        }

        this.replaceFragment(this.mLunchListFragment, R.id.activity_main_frame_layout);
    }

    /**
     * Configures the {@link WorkmateFragment}
     */
    private void configureWorkmateFragment() {
        if (this.mWorkmateFragment == null) {
            this.mWorkmateFragment = WorkmateFragment.newInstance();
        }

        this.replaceFragment(this.mWorkmateFragment, R.id.activity_main_frame_layout);
    }
}
