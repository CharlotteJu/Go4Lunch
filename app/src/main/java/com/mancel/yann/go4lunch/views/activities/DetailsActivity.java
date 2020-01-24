package com.mancel.yann.go4lunch.views.activities;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.apis.GoogleMapsService;
import com.mancel.yann.go4lunch.models.Details;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;
import com.mancel.yann.go4lunch.repositories.UserRepositoryImpl;
import com.mancel.yann.go4lunch.utils.DetailsUtils;
import com.mancel.yann.go4lunch.utils.InsetDivider;
import com.mancel.yann.go4lunch.utils.RestaurantUtils;
import com.mancel.yann.go4lunch.utils.ShowMessage;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModel;
import com.mancel.yann.go4lunch.viewModels.GoogleMapsAndFirestoreViewModelFactory;
import com.mancel.yann.go4lunch.views.adapters.AdapterListener;
import com.mancel.yann.go4lunch.views.adapters.WorkmateAdapter;
import com.mancel.yann.go4lunch.views.bases.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Yann MANCEL on 21/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.views.activities
 *
 * A {@link BaseActivity} subclass which implements {@link AdapterListener}.
 */
public class DetailsActivity extends BaseActivity implements AdapterListener {

    // FIELDS --------------------------------------------------------------------------------------

    @BindView(R.id.activity_details_CoordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_details_image)
    ImageView mImage;
    @BindView(R.id.activity_details_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.activity_details_name)
    TextView mName;
    @BindView(R.id.activity_details_rating_bar)
    RatingBar mRatingBar;
    @BindView(R.id.activity_details_food_type_and_address)
    TextView mFoodTypeAndAddress;
    @BindView(R.id.activity_details_call_button)
    Button mCallButton;
    @BindView(R.id.activity_details_like_button)
    Button mLikeButton;
    @BindView(R.id.activity_details_website_button)
    Button mWebsiteButton;
    @BindView(R.id.activity_details_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_details_FAB)
    FloatingActionButton mFAB;

    @Nullable
    private String mPlaceIdOfRestaurant = null;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private GoogleMapsAndFirestoreViewModel mViewModel;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private WorkmateAdapter mAdapter;

    @Nullable
    private String mPhoneNumber = null;

    @Nullable
    private String mWebsite = null;

    @Nullable
    private User mLastUser = null;

    @Nullable
    private User mCurrentUser = null;

    private static final String TAG = DetailsActivity.class.getSimpleName();

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseActivity --

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_details;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return this.mToolbar;
    }

    @Override
    protected void configureDesign() {
        // Intent
        this.fetchPlaceIdOfRestaurantFromIntent();

        // UI
        this.configureToolBar();
        this.configureUpButtonOfToolBar();
        this.configureRecyclerView();

        // ViewModel
        this.configureViewModel();

        // Current User [warning it is asynchronous so warning with DetailsLiveData]
        this.configureCurrentUser();

        // LiveData
        this.configureDetailsLiveData();
        this.configureUsersLiveData();
    }

    // -- AdapterListener --

    @Override
    public void onDataChanged() {
        // TODO: 20/01/2020 Add action
    }

    // -- Actions --

    @OnClick({R.id.activity_details_call_button,
              R.id.activity_details_like_button,
              R.id.activity_details_website_button,
              R.id.activity_details_FAB})
    public void onFABClicked(@NonNull final View view) {
        // According to the View's Id [Buttons are only enable if their data are not null]
        switch (view.getId()) {
            // CALL
            case R.id.activity_details_call_button:
                this.startPhoneCall(this.mPhoneNumber);
                break;

            // LIKE
            case R.id.activity_details_like_button:
                ShowMessage.showMessageWithSnackbarWithButton(this.mCoordinatorLayout,
                                                             "LIKE",
                                                             "Undo",
                                                              (v) ->{Log.d(TAG, "UNDO");});
                break;

            // WEBSITE
            case R.id.activity_details_website_button:
                this.startOpenWebsite(this.mWebsite);
                break;

            // FAB
            case R.id.activity_details_FAB:
                this.configureEvenOfFAB();
        }
    }

    // -- Intent --

    /**
     * Fetches the Place Id of restaurant from {@link Intent}
     */
    private void fetchPlaceIdOfRestaurantFromIntent() {
        final Intent intent = this.getIntent();

        if (intent != null) {
            this.mPlaceIdOfRestaurant = intent.getStringExtra(MainActivity.INTENT_PLACE_ID);
        }
    }

    // -- Current User --

    /**
     * Configure the current {@link User}
     */
    private void configureCurrentUser() {
        // Current User from Firebase Firestore
        try {
            this.mViewModel.getUser(this.getCurrentUser())
                           .addOnSuccessListener( documentSnapshot -> {
                               this.mCurrentUser = documentSnapshot.toObject(User.class);
                                        Log.d(TAG, "configureCurrentUser: " + this.mCurrentUser.getPlaceIdOfRestaurant());
                                   }
                           );
        }
        catch (Exception e) {
            Log.e(TAG, "getUser: " + e.getMessage());
        }
    }

    // -- RecyclerView --

    /**
     * Configures the {@link RecyclerView}
     */
    private void configureRecyclerView() {
        // Adapter
        this.mAdapter = new WorkmateAdapter(this,
                                             Glide.with(this));

        // InsetDivider
        final RecyclerView.ItemDecoration divider = new InsetDivider.Builder(this.getApplicationContext())
                                                                    .orientation(InsetDivider.VERTICAL_LIST)
                                                                    .dividerHeight(getResources().getDimensionPixelSize(R.dimen.divider_height))
                                                                    .color(getResources().getColor(R.color.ColorSeparator))
                                                                    .insets(getResources().getDimensionPixelSize(R.dimen.divider_inset), 0)
                                                                    .overlay(true)
                                                                    .build();

        // RecyclerView
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        this.mRecyclerView.addItemDecoration(divider);
    }

    // -- GoogleMapsAndFirestoreViewModel --

    /**
     * Configures the {@link GoogleMapsAndFirestoreViewModel}
     */
    private void configureViewModel() {
        // TODO: 20/01/2020 UserRepository and PlaceRepository must be removed thanks to Dagger 2
        final GoogleMapsAndFirestoreViewModelFactory factory = new GoogleMapsAndFirestoreViewModelFactory(new UserRepositoryImpl(),
                                                                                                          new PlaceRepositoryImpl());

        this.mViewModel = ViewModelProviders.of(this, factory)
                                            .get(GoogleMapsAndFirestoreViewModel.class);
    }

    /**
     * Configures the {@link LiveData} of {@link List<User>}
     */
    private void configureDetailsLiveData() {
        // Bind between liveData of ViewModel and the UI
        if (this.mPlaceIdOfRestaurant != null) {
            this.mViewModel.getDetails(this.getApplicationContext(), this.mPlaceIdOfRestaurant)
                           .observe(this,
                                     this::updateDetails);
        }
    }

    /**
     * Configures the {@link LiveData} of {@link List<User>}
     */
    private void configureUsersLiveData() {
        // Bind between liveData of ViewModel and the Adapter of RecyclerView
        if (this.mPlaceIdOfRestaurant != null) {
            this.mViewModel.getUsersWithSameRestaurant(this.mPlaceIdOfRestaurant)
                           .observe(this,
                                     users -> this.mAdapter.updateData(users));
        }
    }

    // -- Details --

    /**
     * Updates the UI against {@link Details}
     * @param details a {@link Details}
     */
    private void updateDetails(@NonNull final Details details) {
        // Url Photo
        final String urlPhoto = (details.getResult().getPhotos() == null) ? null :
                                                                            GoogleMapsService.getPhoto(details.getResult().getPhotos().get(0).getPhotoReference(),
                                                                                                      400,
                                                                                                       this.getString(R.string.google_maps_key));

        // Image (using to Glide library)
        Glide.with(this)
             .load(urlPhoto)
             .fallback(R.drawable.ic_restaurant)
             .error(R.drawable.ic_close)
             .into(this.mImage);

        // Name
        this.mName.setText(details.getResult().getName());

        // Rating
        if (details.getResult().getRating() == null) {
            this.mRatingBar.setVisibility(View.GONE);
        }
        else {
            float floatValue = RestaurantUtils.calculateRating(details.getResult().getRating().floatValue());

            // TODO: 22/01/2020 Change the number stars of RatingBar
            //this.mRatingBar.setNumStars((int) floatValue);
            this.mRatingBar.setRating(floatValue);
        }

        // Food type & Address
        this.mFoodTypeAndAddress.setText(DetailsUtils.createStringOfFoodTypeAndAddress(this.getApplicationContext(),
                                                                                       details.getResult().getAddressComponents()));

        // Phone
        if (details.getResult().getInternationalPhoneNumber() == null || details.getResult().getInternationalPhoneNumber().isEmpty()) {
            this.mCallButton.setEnabled(false);
        }
        else {
            this.mPhoneNumber = details.getResult().getInternationalPhoneNumber();
        }

        // Website
        if (details.getResult().getWebsite() == null || details.getResult().getWebsite().isEmpty()) {
            this.mWebsiteButton.setEnabled(false);
        }
        else {
            this.mWebsite = details.getResult().getWebsite();
        }

        Log.d(TAG, "updateDetails: "+ (this.mCurrentUser == null));
        this.mCurrentUser = new User();

        // FAB [warning user data are asynchronous]
        this.mFAB.setImageResource( (this.mCurrentUser.getPlaceIdOfRestaurant() != null &&
                                     this.mPlaceIdOfRestaurant.equals(this.mCurrentUser.getPlaceIdOfRestaurant()) ?
                R.drawable.ic_check :
                R.drawable.ic_add));
    }

    // -- FAB --

    /**
     * Configures the enevnts of {@link FloatingActionButton}
     */
    private void configureEvenOfFAB() {
        // Check if the restaurant is selected
        boolean isChecked = false;

        // Last User
        this.mLastUser = this.mCurrentUser;

        // No selected restaurant or different restaurant
        if (this.mCurrentUser.getPlaceIdOfRestaurant() == null ||
            !this.mCurrentUser.getPlaceIdOfRestaurant().equals(this.mPlaceIdOfRestaurant)) {
            // Update restaurant data on current user
            try {
                this.mViewModel.updateRestaurant(this.getCurrentUser(),
                                                 this.mPlaceIdOfRestaurant,
                                                 this.mName.getText().toString(),
                                                null);
            }
            catch (Exception e) {
                Log.e(TAG, "updateRestaurant: " + e.getMessage());
            }

            isChecked = true;
        }
        else {
            // Removes restaurant data on current user
            try {
                this.mViewModel.updateRestaurant(this.getCurrentUser(),
                                                null,
                                                null,
                                                null);
            }
            catch (Exception e) {
                Log.e(TAG, "updateRestaurant: " + e.getMessage());
            }
        }

        // Current User (Update restaurant data) [warning it is asynchronous]
        this.configureCurrentUser();

        // FAB
        this.mFAB.setImageResource( isChecked ? R.drawable.ic_check :
                                                R.drawable.ic_add);

        // Snackbar
        ShowMessage.showMessageWithSnackbarWithButton(this.mCoordinatorLayout,
                                                      isChecked ? this.getString(R.string.restaurant_selected) :
                                                                  this.getString(R.string.restaurant_no_selected),
                                                      this.getString(R.string.undo),
                                                      (v) ->{
            // Update restaurant data on current user
            try {
                this.mViewModel.updateRestaurant(this.getCurrentUser(),
                                                 this.mLastUser.getPlaceIdOfRestaurant(),
                                                 this.mLastUser.getNameOfRestaurant(),
                                                 this.mLastUser.getFoodTypeOfRestaurant());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Current User
            this.mCurrentUser = this.mLastUser;

            // FAB
            this.mFAB.setImageResource( (this.mLastUser.getPlaceIdOfRestaurant() != null &&
                                         this.mLastUser.getPlaceIdOfRestaurant().equals(this.mPlaceIdOfRestaurant) ?
                    R.drawable.ic_check :
                    R.drawable.ic_add));
        });
    }
}