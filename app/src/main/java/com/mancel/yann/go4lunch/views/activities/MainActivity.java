package com.mancel.yann.go4lunch.views.activities;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.utils.BlurTransformation;
import com.mancel.yann.go4lunch.utils.SaveUtils;
import com.mancel.yann.go4lunch.utils.ShowMessage;
import com.mancel.yann.go4lunch.views.bases.BaseActivity;
import com.mancel.yann.go4lunch.views.bases.BaseFragment;
import com.mancel.yann.go4lunch.views.dialogs.DialogListener;
import com.mancel.yann.go4lunch.views.dialogs.SettingsDialogFragment;
import com.mancel.yann.go4lunch.views.fragments.FragmentListener;
import com.mancel.yann.go4lunch.workers.WorkerController;

import java.util.Arrays;

import butterknife.BindView;

/**
 * Created by Yann MANCEL on 18/11/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.activities
 *
 * A {@link BaseActivity} subclass which implements {@link FragmentListener} and
 * {@link DialogListener}.
 */
public class MainActivity extends BaseActivity implements FragmentListener,
                                                          DialogListener {

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

    public static final String INTENT_PLACE_ID = "INTENT_PLACE_ID";
    public static final String INTENT_CURRENT_USER = "INTENT_CURRENT_USER";

    public static final int REQUEST_CODE_AUTOCOMPLETE = 2020;

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
    protected void configureDesign(@Nullable Bundle savedInstanceState) {
        // User
        this.createUser();

        // UI
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        // Fragment Navigation
        this.configureFragmentNavigation();

        // WorkManager
        this.configureWorkManager();
    }

    // -- Activity --

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creates a MenuInflater to add the menu xml file into the Toolbar
        this.getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Behavior of Toolbar
        this.configureBehaviorOfToolBar();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // SEARCH
        if (item.getItemId() == R.id.toolbar_menu_search) {
            // Autocomplete of Place of Google Maps
            Places.initialize(this.getApplicationContext(),
                              this.getString(R.string.google_maps_key));

            // Start the autocomplete intent.
            final Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                                                                 Arrays.asList(Place.Field.ID,
                                                                               Place.Field.NAME,
                                                                               Place.Field.LAT_LNG))
                                                  .setTypeFilter(TypeFilter.ESTABLISHMENT)
                                                  .build(this.getApplicationContext());

            this.startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);

            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // AUTOCOMPLETE
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Data
                final Place place = Autocomplete.getPlaceFromIntent(data);

                // NavController
                final NavController navController = Navigation.findNavController(this,
                                                                                  R.id.activity_main_nav_host_fragment);

                // Retrieves the current fragment
                switch (navController.getCurrentDestination().getId()) {

                    // MAP or LIST
                    case R.id.navigation_lunchMapFragment:
                    case R.id.navigation_lunchListFragment:
                        this.retrieveCurrentFragmentFromNavigation(place);
                        break;

                    // WORKMATE
                    case R.id.navigation_workmateFragment:
                        // Not possible see configureBehaviorOfToolBar method
                        break;

                    default:
                        Crashlytics.log(Log.ERROR,
                                        MainActivity.class.getSimpleName(),
                                       "onActivityResult: The Id of the current destination is not good.");
                }
            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                final Status status = Autocomplete.getStatusFromIntent(data);

                Crashlytics.log(Log.ERROR,
                                MainActivity.class.getSimpleName(),
                               "onActivityResult: " + status.getStatusMessage());
            }
            else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                ShowMessage.showMessageWithSnackbar(this.mCoordinatorLayout,
                                                    this.getString(R.string.cancelled_search));
            }
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

    // -- FragmentListener interface --

    @Override
    public void onSelectedRestaurant(@NonNull final String placeIdOfRestaurant) {
        // Fetches the data of current user
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
            Crashlytics.log(Log.ERROR,
                            MainActivity.class.getSimpleName(),
                           "onSelectedRestaurant: " + e.getMessage());
        }
    }

    // -- DialogListener interface --

    @Override
    public void onClickOnPositiveButton(final float newRating) {
        // Do nothing
    }

    @Override
    public void onClickOnPositiveButton(final boolean isChecked) {
        // From SettingsDialogFragment
        if (isChecked) {
            WorkerController.startWorkRequestIntoWorkManager(this.getApplicationContext(),
                                                             this.getCurrentUser().getUid());
        }
        else {
            WorkerController.stopWorkRequestIntoWorkManager(this.getApplicationContext());
        }
    }

    // -- Actions --

    /**
     * Actions for the {@link NavigationView}
     * @param menuItem a {@link MenuItem}
     * @return a boolean
     */
    private boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            // NavigationView ----------------------------------------------------------------------

            // Lunch
            case R.id.menu_drawer_lunch:
                this.startDetailsOfLunch();
                break;

            // Setting
            case R.id.menu_drawer_setting:
                SettingsDialogFragment.newInstance()
                                      .show(this.getSupportFragmentManager(),
                                           "Settings Dialog Fragment");
                break;

            // Chat
            case R.id.menu_drawer_chat:
                this.startAnotherActivity(ChatActivity.class);
                break;

            // Logout
            case R.id.menu_drawer_logout:
                this.logout();
                break;

            default:
                Log.e(this.getClass().getSimpleName(), "onNavigationItemSelected: None of ids selected among the list");
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

    // -- Navigation Component --

    /**
     * Configures the {@link androidx.fragment.app.Fragment} navigation component
     */
    private void configureFragmentNavigation() {
        // NavController
        final NavController navController = Navigation.findNavController(this,
                                                                          R.id.activity_main_nav_host_fragment);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        final AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_lunchMapFragment,
                                                                                        R.id.navigation_lunchListFragment,
                                                                                        R.id.navigation_workmateFragment)
                                                                               .build();

        // Navigation component
        NavigationUI.setupActionBarWithNavController(this,
                                                      navController,
                                                      appBarConfiguration);

        NavigationUI.setupWithNavController(this.mBottomNavigationView, navController);
    }

    /**
     * Configures the behavior of the {@link Toolbar}
     */
    private void configureBehaviorOfToolBar() {
        // Listener to NavController
        Navigation.findNavController(this,
                                      R.id.activity_main_nav_host_fragment)
                  .addOnDestinationChangedListener( ((controller, destination, arguments) -> {
                      // Search item
                      final MenuItem searchItem = this.mToolbar.getMenu()
                                                      .findItem(R.id.toolbar_menu_search);

                      // Retrieves the current fragment
                      switch (destination.getId()) {

                          // MAP or LIST
                          case R.id.navigation_lunchMapFragment:
                          case R.id.navigation_lunchListFragment:
                              searchItem.setEnabled(true);
                              searchItem.setIcon(R.drawable.ic_search_enable);
                              break;

                          // WORKMATE
                          case R.id.navigation_workmateFragment:
                              searchItem.setEnabled(false);
                              searchItem.setIcon(R.drawable.ic_search_disable);
                              break;

                          default:
                              Crashlytics.log(Log.ERROR,
                                              MainActivity.class.getSimpleName(),
                                             "addOnDestinationChangedListener: The Id of the current destination is not good.");
                      }
                  }));
    }

    /**
     * Retrieves the current {@link Fragment} from the navigation component
     * @param place a {@link Place} that contains all data of the autocomplete system
     */
    private void retrieveCurrentFragmentFromNavigation(@NonNull final Place place) {
        final BaseFragment fragment = (BaseFragment) this.getSupportFragmentManager()
                                                         .findFragmentById(R.id.activity_main_nav_host_fragment)
                                                         .getChildFragmentManager()
                                                         .getFragments()
                                                         .get(0);

        // Method of BaseFragment
        fragment.onSuccessOfAutocomplete(place);

        // Shows message
        ShowMessage.showMessageWithSnackbar(this.mCoordinatorLayout,
                                            this.getString(R.string.validated_search,
                                                           place.getName()));
    }

    // -- WorkManager --

    /**
     * Configures the {@link androidx.work.WorkManager}
     */
    private void configureWorkManager() {
        if (SaveUtils.loadBooleanFromSharedPreferences(this.getApplicationContext(),
                                                       SettingsDialogFragment.BUNDLE_SWITCH_NOTIFICATION)) {
            WorkerController.startWorkRequestIntoWorkManager(this.getApplicationContext(),
                                                             this.getCurrentUser().getUid());
        }
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
            Crashlytics.log(Log.ERROR,
                            MainActivity.class.getSimpleName(),
                           "createUser: " + e.getMessage());
        }
    }

    // -- Lunch --

    /**
     * Displays the details of selected restaurant
     */
    private void startDetailsOfLunch() {
        // Fetches the data of current user
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
            Crashlytics.log(Log.ERROR,
                            MainActivity.class.getSimpleName(),
                           "startDetailsOfLunch: " + e.getMessage());
        }
    }

    // -- Logout --

    /**
     * Logout the current user
     */
    private void logout() {
        // DIALOG
        new AlertDialog.Builder(this) // this instead of this.getApplicationContext() for the theme
                       .setTitle(this.getString(R.string.title_alertdialog))
                       .setMessage(this.getString(R.string.message_alertdialog))
                       .setPositiveButton(this.getString(R.string.positive_button_alertdialog), (dialog, which) -> {
                           this.signOutCurrentUser();
                           this.startAnotherActivity(AuthActivity.class);

                           // Deletes this activity
                           this.finish();
                       })
                       .setNegativeButton(this.getString(R.string.negative_button_alertdialog), (dialog, which) -> {
                           // Do nothing
                       })
                       .create()
                       .show();
    }
}
