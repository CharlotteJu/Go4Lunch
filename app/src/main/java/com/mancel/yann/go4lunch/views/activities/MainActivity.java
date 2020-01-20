package com.mancel.yann.go4lunch.views.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;
import com.mancel.yann.go4lunch.utils.BlurTransformation;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModel;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModelFactory;
import com.mancel.yann.go4lunch.views.bases.BaseActivity;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;
import com.mancel.yann.go4lunch.views.fragments.FragmentListener;
import com.mancel.yann.go4lunch.views.fragments.LunchListFragment;
import com.mancel.yann.go4lunch.views.fragments.LunchMapFragment;
import com.mancel.yann.go4lunch.views.fragments.WorkmateFragment;

import butterknife.BindView;

/**
 * Created by Yann MANCEL on 18/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.activities
 *
 * A {@link BaseActivity} subclass which implements {@link FragmentListener}.
 */
public class MainActivity extends BaseActivity implements FragmentListener {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.activity_main_bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    private ImageView mBackgroundImageFromHeader;
    private ImageView mUserImageFromHeader;
    private TextView mUsernameFromHeader;
    private TextView mEmailFromHeader;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private GoogleMapsAndFirestoreViewModel mViewModel;

    private enum FragmentType {MAP, LIST, WORKMATE}

    @NonNull
    private FragmentType mFragmentType = FragmentType.MAP;

    private LunchMapFragment mLunchMapFragment;
    private LunchListFragment mLunchListFragment;
    private WorkmateFragment mWorkmateFragment;

    public static final String INTENT_PLACE_ID = "INTENT_PLACE_ID";

    private static final String TAG = MainActivity.class.getSimpleName();

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
        // UI
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomNavigationView();

        // ViewModel
        this.configureViewModel();

        // User
        this.createUser();

        // Fragment
        this.configureFragment(this.mFragmentType);
    }

    // -- Activity --

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creates a MenuInflater to add the menu xml file into the Toolbar
        this.getMenuInflater().inflate(R.menu.toolbar_menu, menu);
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
            // TODO: 10/01/2020 call logout method and not super.onBackPressed();
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            // Access update location
            case BaseFragment.RC_PERMISSION_LOCATION_UPDATE_LOCATION:
                // No permission
                if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Log.e("MainActivity", "No permission to access fine location");
                }

                // According to the fragment type
                switch (this.mFragmentType) {
                    case MAP:
                        this.mLunchMapFragment.startLocationUpdate();
                        break;
                    case LIST:
                        break;
                    case WORKMATE:
                        break;
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check settings to location
        if (requestCode == LunchMapFragment.RC_CHECK_SETTINGS_TO_LOCATION && resultCode == RESULT_OK) {
            this.mLunchMapFragment.startLocationUpdate();
        }
    }

    // -- FragmentListener --

    @Override
    public void onSelectedRestaurant(@NonNull String placeIdOfRestaurant) {
        final Intent intent = new Intent(this.getApplicationContext(), DetailsActivity.class);

        // Place Id
        intent.putExtra(INTENT_PLACE_ID, placeIdOfRestaurant);

        this.startActivity(intent);
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
                this.logout();
                break;

            // BottomNavigationView
            case R.id.bottom_navigation_menu_map:
                this.mFragmentType = FragmentType.MAP;
                this.configureLunchMapFragment();
                break;
            case R.id.bottom_navigation_menu_list:
                this.mFragmentType = FragmentType.LIST;
                this.configureLunchListFragment();
                break;
            case R.id.bottom_navigation_menu_workmate:
                this.mFragmentType = FragmentType.WORKMATE;
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
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
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

        this.configureHeaderOfNavigationView();
    }

    /**
     * Configures the header of {@link NavigationView}
     */
    private void configureHeaderOfNavigationView() {
        // Gets the only header of NavigationView
        final View headerView = this.mNavigationView.getHeaderView(0);

        // Header of XML file
        this.mBackgroundImageFromHeader = headerView.findViewById(R.id.activity_main_header_drawer_image_background);
        this.mUserImageFromHeader = headerView.findViewById(R.id.activity_main_header_drawer_user_image);
        this.mUsernameFromHeader = headerView.findViewById(R.id.activity_main_header_drawer_username);
        this.mEmailFromHeader = headerView.findViewById(R.id.activity_main_header_drawer_email);

        // Using to Glide library
        Glide.with(this)
             .load(R.drawable.background_image)
             .transform(new MultiTransformation<>(new CenterCrop(),
                                                  new BlurTransformation(this)))
             .error(R.drawable.ic_close)
             .into(this.mBackgroundImageFromHeader);

        // From Firebase
        final FirebaseUser user = this.getCurrentUser();

        if (user != null) {
            // ImageView: User image
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                     .load(user.getPhotoUrl())
                     .circleCrop()
                     .fallback(R.drawable.ic_person)
                     .error(R.drawable.ic_close)
                     .into(this.mUserImageFromHeader);
            }

            // TextView: Username and email
            final String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) :
                                                                               user.getDisplayName();

            final String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) :
                                                                      user.getEmail();

            this.mUsernameFromHeader.setText(username);
            this.mEmailFromHeader.setText(email);
        }
    }

    // -- BottomNavigationView --

    /**
     * Configures the {@link BottomNavigationView}
     */
    private void configureBottomNavigationView() {
        this.mBottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    // -- GoogleMapsAndFirestoreViewModel --

    /**
     * Configures the {@link GoogleMapsAndFirestoreViewModel}
     */
    private void configureViewModel() {
        // TODO: 10/01/2020 UserRepository and PlaceRepository must be removed thanks to Dagger 2
        final GoogleMapsAndFirestoreViewModelFactory factory = new GoogleMapsAndFirestoreViewModelFactory(new UserRepositoryImpl(),
                                                                                                          new PlaceRepositoryImpl());

        this.mViewModel = ViewModelProviders.of(this, factory)
                                            .get(GoogleMapsAndFirestoreViewModel.class);
    }

    // -- Fragment --

    /**
     * Configures the {@link androidx.fragment.app.Fragment}
     * @param fragmentType a {@link FragmentType}
     */
    private void configureFragment(@NonNull final FragmentType fragmentType) {
        // TODO: 10/01/2020 refactor and delete field not useful
        switch (fragmentType) {
            case MAP:
                this.configureLunchMapFragment();
                break;

            case LIST:
                this.configureLunchListFragment();
                break;

            case WORKMATE:
                this.configureWorkmateFragment();
                break;
        }
    }

    /**
     * Configures the {@link LunchMapFragment}
     */
    private void configureLunchMapFragment() {
        if (this.mLunchMapFragment == null) {
            this.mLunchMapFragment = LunchMapFragment.newInstance(this);
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

    // -- User --

    /**
     * Creates the user if it does not exist
     */
    private void createUser() {
        try {
            this.mViewModel.createUser(this.getCurrentUser());
        }
        catch (Exception e) {
            Log.e(TAG, "createUser: " + e.getMessage());
        }
    }

    /**
     * Logout the current user
     */
    private void logout() {
        // TODO: 10/01/2020 Create Dialog to ask if the user would like really logout
        this.signOutCurrentUser();
        this.startAnotherActivity(AuthActivity.class);

        // Deletes this activity
        this.finish();
    }
}
