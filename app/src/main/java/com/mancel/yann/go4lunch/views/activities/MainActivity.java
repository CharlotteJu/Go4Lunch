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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;
import com.mancel.yann.go4lunch.utils.BlurTransformation;
import com.mancel.yann.go4lunch.utils.ShowMessage;
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

    @BindView(R.id.activity_main_CoordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.activity_main_bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

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
    public static final String INTENT_CURRENT_USER = "INTENT_CURRENT_USER";

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
        this.configureLunchMapFragment();
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

        // TODO: 24/01/2020 Make this method in BaseActivity and make startLocationUpdate in protected method in BaseFragment
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO: 24/01/2020 Make this method in BaseActivity and make startLocationUpdate in protected method in BaseFragment
        // Check settings to location
        if (requestCode == LunchMapFragment.RC_CHECK_SETTINGS_TO_LOCATION && resultCode == RESULT_OK) {
            this.mLunchMapFragment.startLocationUpdate();
        }
    }

    // -- FragmentListener --

    @Override
    public void onSelectedRestaurant(@NonNull final String placeIdOfRestaurant) {
        try {
            this.mViewModel.getUser(this.getCurrentUser())
                           .addOnSuccessListener( documentSnapshot -> {
                               final User user = documentSnapshot.toObject(User.class);

                               if (user != null) {
                                   final Intent intent = new Intent(this.getApplicationContext(), DetailsActivity.class);

                                   // EXTRA: String on [Place Id]
                                   intent.putExtra(INTENT_PLACE_ID, placeIdOfRestaurant);

                                   // EXTRA: Current user
                                   intent.putExtra(INTENT_CURRENT_USER, user);

                                   this.startActivity(intent);
                               }
                           });
        }
        catch (Exception e) {
            Log.e(TAG, "getUser: " + e.getMessage());
        }
    }

    // -- Actions --

    private boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            // NavigationView ----------------------------------------------------------------------

            // Lunch
            case R.id.menu_drawer_lunch:
                this.startDetailsOfLunch();
                break;

            // Setting
            case R.id.menu_drawer_setting:
                Log.e(this.getClass().getSimpleName(), "Setting");
                break;

            // Logout
            case R.id.menu_drawer_logout:
                this.logout();
                break;

            // BottomNavigationView ----------------------------------------------------------------

            // Maps fragment
            case R.id.bottom_navigation_menu_map:
                this.configureLunchMapFragment();
                break;

            // List fragment
            case R.id.bottom_navigation_menu_list:
                this.configureLunchListFragment();
                break;

            // Workmate fragment
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

        // ImageView: Background image (Using to Glide library)
        final ImageView imageBackgroundFromXML = headerView.findViewById(R.id.activity_main_header_drawer_image_background);
        Glide.with(this)
             .load(R.drawable.background_image)
             .transform(new MultiTransformation<>(new CenterCrop(),
                                                  new BlurTransformation(this)))
             .error(R.drawable.ic_close)
             .into(imageBackgroundFromXML);

        // From Firebase Authentication
        final FirebaseUser user = this.getCurrentUser();

        if (user != null) {
            // ImageView: User image (Using to Glide library)
            final ImageView imageUserFromXML = headerView.findViewById(R.id.activity_main_header_drawer_user_image);

            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                     .load(user.getPhotoUrl())
                     .circleCrop()
                     .fallback(R.drawable.ic_person)
                     .error(R.drawable.ic_close)
                     .into(imageUserFromXML);
            }

            // TextView: Username and email
            final TextView usernameFromXML = headerView.findViewById(R.id.activity_main_header_drawer_username);
            final TextView emailFromXML = headerView.findViewById(R.id.activity_main_header_drawer_email);

            final String username = TextUtils.isEmpty(user.getDisplayName()) ? this.getString(R.string.info_no_username_found) :
                                                                               user.getDisplayName();

            final String email = TextUtils.isEmpty(user.getEmail()) ? this.getString(R.string.info_no_email_found) :
                                                                      user.getEmail();

            usernameFromXML.setText(username);
            emailFromXML.setText(email);
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
     * Configures the {@link LunchMapFragment}
     */
    private void configureLunchMapFragment() {
        this.mFragmentType = FragmentType.MAP;

        if (this.mLunchMapFragment == null) {
            this.mLunchMapFragment = LunchMapFragment.newInstance(this);
        }

        this.replaceFragment(this.mLunchMapFragment, R.id.activity_main_frame_layout);


    }

    /**
     * Configures the {@link LunchListFragment}
     */
    private void configureLunchListFragment() {
        this.mFragmentType = FragmentType.LIST;

        if (this.mLunchListFragment == null) {
            this.mLunchListFragment = LunchListFragment.newInstance();
        }

        this.replaceFragment(this.mLunchListFragment, R.id.activity_main_frame_layout);

        this.mLunchMapFragment = null;
    }

    /**
     * Configures the {@link WorkmateFragment}
     */
    private void configureWorkmateFragment() {
        this.mFragmentType = FragmentType.WORKMATE;

        if (this.mWorkmateFragment == null) {
            this.mWorkmateFragment = WorkmateFragment.newInstance();
        }

        this.replaceFragment(this.mWorkmateFragment, R.id.activity_main_frame_layout);

        this.mLunchMapFragment = null;
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

    // -- Lunch --

    /**
     * Displays the details of selected restaurant
     */
    private void startDetailsOfLunch() {
        try {
            this.mViewModel.getUser(this.getCurrentUser())
                           .addOnSuccessListener( documentSnapshot -> {
                               final User user = documentSnapshot.toObject(User.class);

                               if (user != null) {
                                   // User has already selected a restaurant (-> place id is not null)
                                   if (user.getPlaceIdOfRestaurant() != null) {
                                       final Intent intent = new Intent(this.getApplicationContext(), DetailsActivity.class);

                                       // EXTRA: String on [Place Id]
                                       intent.putExtra(INTENT_PLACE_ID, user.getPlaceIdOfRestaurant());

                                       // EXTRA: Current user
                                       intent.putExtra(INTENT_CURRENT_USER, user);

                                       this.startActivity(intent);
                                   }
                                   else {
                                       ShowMessage.showMessageWithSnackbar(this.mCoordinatorLayout,
                                                                           this.getString(R.string.restaurant_no_selected));
                                   }
                               }
                           });
        }
        catch (Exception e) {
            Log.e(TAG, "getUser: " + e.getMessage());
        }
    }

    // -- Logout --

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
