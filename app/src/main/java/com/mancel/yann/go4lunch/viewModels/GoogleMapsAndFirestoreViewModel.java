package com.mancel.yann.go4lunch.viewModels;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.liveDatas.DetailsLiveData;
import com.mancel.yann.go4lunch.liveDatas.LocationLiveData;
import com.mancel.yann.go4lunch.liveDatas.NearbySearchLiveData;
import com.mancel.yann.go4lunch.liveDatas.POIsLiveData;
import com.mancel.yann.go4lunch.liveDatas.RestaurantsLiveData;
import com.mancel.yann.go4lunch.liveDatas.RestaurantsWithUsersLiveData;
import com.mancel.yann.go4lunch.liveDatas.UsersLiveData;
import com.mancel.yann.go4lunch.models.Details;
import com.mancel.yann.go4lunch.models.LocationData;
import com.mancel.yann.go4lunch.models.NearbySearch;
import com.mancel.yann.go4lunch.models.POI;
import com.mancel.yann.go4lunch.models.Restaurant;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.repositories.PlaceRepository;
import com.mancel.yann.go4lunch.repositories.UserRepository;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Yann MANCEL on 09/01/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.viewModels
 *
 * A {@link ViewModel} subclass.
 */
public class GoogleMapsAndFirestoreViewModel extends ViewModel {

    // FIELDS --------------------------------------------------------------------------------------

    // -- Repositories --

    @NonNull
    private final UserRepository mUserRepository;

    @NonNull
    private final PlaceRepository mPlaceRepository;

    // -- LiveData (Simple) --

    @Nullable
    private UsersLiveData mUsersLiveData = null;

    @Nullable
    private UsersLiveData mUsersLiveDataWithSameRestaurant = null;

    @Nullable
    private LocationLiveData mLocationLiveData = null;

    @Nullable
    private NearbySearchLiveData mNearbySearchLiveData = null;

    @Nullable
    private DetailsLiveData mDetailsLiveData = null;

    // -- LiveData (Complex) --

    @Nullable
    private POIsLiveData mPOIsLiveData = null;

    // *********************************************************************************************
    // ************************************** VERIFICATION *****************************************
    // *********************************************************************************************

    // TODO: 15/01/2020 add the LiveData which couples LocationLiveData and NearbySearchLiveData



    @Nullable
    private RestaurantsLiveData mRestaurantsLiveData = null;

    @Nullable
    private RestaurantsWithUsersLiveData mRestaurantsWithUsersLiveData = null;

    private static final String TAG = GoogleMapsAndFirestoreViewModel.class.getSimpleName();

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with 2 repositories
     * @param userRepository    a {@link UserRepository} for data from Firebase Firestore
     * @param placeRepository   a {@link PlaceRepository} for data from Google Maps
     */
    public GoogleMapsAndFirestoreViewModel(@NonNull final UserRepository userRepository,
                                           @NonNull final PlaceRepository placeRepository) {
        Log.d(TAG, "GoogleMapsAndFirestoreViewModel");

        this.mUserRepository = userRepository;
        this.mPlaceRepository = placeRepository;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ViewModel --

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared");
    }

    // -- UsersLiveData --

    /**
     * Gets all users from Firebase Firestore
     * @return a {@link LiveData} of {@link List<User>}
     */
    @NonNull
    public LiveData<List<User>> getUsers() {
        if (this.mUsersLiveData == null) {
            this.mUsersLiveData = new UsersLiveData(this.mUserRepository.getAllUsers());
        }

        return this.mUsersLiveData;
    }

    /**
     * Gets all users from Firebase Firestore which selected the same restaurant
     * @param placeIdOfRestaurant a {@link String} that contains the Place Id of restaurant
     * @return a {@link LiveData} of {@link List<User>}
     */
    @NonNull
    public LiveData<List<User>> getUsersWithSameRestaurant(@NonNull final String placeIdOfRestaurant) {
        if (this.mUsersLiveDataWithSameRestaurant == null) {
            this.mUsersLiveDataWithSameRestaurant = new UsersLiveData(this.mUserRepository.getAllUsersFromThisRestaurant(placeIdOfRestaurant));
        }

        return this.mUsersLiveDataWithSameRestaurant;
    }

    // -- LocationLiveData --

    /**
     * Gets the current location from Google Maps
     * @param context a {@link Context}
     * @return a {@link LiveData<LocationData>}
     */
    @NonNull
    public LiveData<LocationData> getLocation(@NonNull final Context context) {
        if (this.mLocationLiveData == null) {
            this.mLocationLiveData = new LocationLiveData(context);
        }

        return this.mLocationLiveData;
    }

    /**
     * Starts the location update from {@link LocationLiveData}
     */
    public void startLocationUpdate() {
        this.mLocationLiveData.requestUpdateLocation();
    }

    // -- NearbySearchLiveData --

    /**
     * Gets the nearby search from Google Maps
     * @param context       a {@link Context}
     * @param locationData  a {@link LocationData}
     * @param radius        a double that contains the radius of research
     * @return a {@link NearbySearchLiveData}
     */
    @NonNull
    public NearbySearchLiveData getNearbySearch(@NonNull final Context context,
                                                @Nullable final LocationData locationData,
                                                final double radius) {
        if (this.mNearbySearchLiveData == null) {
            this.mNearbySearchLiveData = new NearbySearchLiveData();
        }

        // Fetches the nearbySearch
        if (locationData != null) {
            this.fetchNearbySearch(context, locationData, radius);
        }

        return this.mNearbySearchLiveData;
    }

    /**
     * Loads the {@link NearbySearch}
     * @param context       a {@link Context}
     * @param locationData  a {@link LocationData}
     * @param radius        a double that contains the radius of research
     */
    public void fetchNearbySearch(@NonNull final Context context,
                                  @NonNull final LocationData locationData,
                                  final double radius) {
        // No location
        if (locationData.getLocation() == null) {
            return;
        }

        // Retrieves Google Maps Key
        final String key = context.getResources()
                                  .getString(R.string.google_maps_key);

        // Location
        final String location = locationData.getLocation().getLatitude() + "," +
                                locationData.getLocation().getLongitude();

        // Types
        final String types = "restaurant";

        // Observable
        final Observable<NearbySearch> observable;
        observable = this.mPlaceRepository.getStreamToFetchNearbySearch(location,
                                                                        radius,
                                                                        types,
                                                                        key);

        // Updates LiveData for the NearbySearch
        this.mNearbySearchLiveData.getNearbySearchWithObservable(observable);
    }

    // -- DetailsLiveData --

    /**
     * Gets the details from Google Maps
     * @param context               a {@link Context}
     * @param placeIdOfRestaurant   a {@link String} that contains the Place Id of restaurant
     * @return a {@link DetailsLiveData}
     */
    @NonNull
    public DetailsLiveData getDetails(@NonNull final Context context,
                                      @NonNull final String placeIdOfRestaurant) {
        if (this.mDetailsLiveData == null) {
            this.mDetailsLiveData = new DetailsLiveData();
        }

        // Fetches the details
        this.fetchDetails(context, placeIdOfRestaurant);

        return this.mDetailsLiveData;
    }

    /**
     * Loads the {@link Details}
     * @param context               a {@link Context}
     * @param placeIdOfRestaurant   a {@link String} that contains the Place Id of restaurant
     */
    public void fetchDetails(@NonNull final Context context,
                             @NonNull final String placeIdOfRestaurant) {
        // Retrieves Google Maps Key
        final String key = context.getResources()
                                  .getString(R.string.google_maps_key);

        // Observable
        final Observable<Details> observable;
        observable = this.mPlaceRepository.getStreamToFetchDetails(placeIdOfRestaurant, key);

        // Updates LiveData for the Details
        this.mDetailsLiveData.getDetailsWithObservable(observable);
    }

    // -- POIsLiveData --

    /**
     * Gets a {@link List<POI>} from Google Maps and Firebase Firestore
     * @param context       a {@link Context}
     * @param locationData  a {@link LocationData}
     * @param radius        a double that contains the radius of research
     * @return a {@link LiveData} of {@link List<POI>}
     */
    @NonNull
    public LiveData<List<POI>> getPOIs(@NonNull final Context context,
                                       @Nullable final LocationData locationData,
                                       final double radius) {
        if (this.mPOIsLiveData == null) {
            this.mPOIsLiveData = new POIsLiveData(this.getNearbySearch(context, locationData, radius),
                                                  this.getUsers());
        }

        return this.mPOIsLiveData;
    }


    // *********************************************************************************************
    // ************************************** VERIFICATION *****************************************
    // *********************************************************************************************

//    // -- SwitchMap: LocationLiveData then NearbySearchLiveData
//    @NonNull
//    public LiveData<NearbySearch> getNearbySearchFromLocation(@NonNull final Context context) {
//        // LocationLiveData -> NearbySearchLiveData
//        return Transformations.switchMap(this.getLocation(context), locationData ->
//            this.getNearbySearch(context, locationData)
//        );
//    }

    // -- RestaurantsLiveData --

    /**
     * Gets all restaurants from Google Maps
     * @param context a {@link Context}
     * @return a {@link RestaurantsLiveData}
     */
    @NonNull
    public RestaurantsLiveData getRestaurants(@NonNull final Context context) {
        if (this.mRestaurantsLiveData == null) {
            this.mRestaurantsLiveData = new RestaurantsLiveData();
        }

        // Fetches the restaurants
        this.loadRestaurants(context);

        return this.mRestaurantsLiveData;
    }

    /**
     * Loads the restaurant
     * @param context a {@link Context}
     */
    public void loadRestaurants(@NonNull final Context context) {
        // Retrieves Google Maps Key
        final String key = context.getResources()
                                  .getString(R.string.google_maps_key);

        // Observable
        final Observable<List<Restaurant>> observable;
        observable = this.mPlaceRepository.getStreamToFetchNearbySearchThenToFetchRestaurants("45.9922027,4.7176896",
                                                                                              200.0,
                                                                                              "restaurant",
                                                                                              "walking",
                                                                                              "metric",
                                                                                               key);

        // Updates LiveData for the restaurants
        this.mRestaurantsLiveData.getRestaurantsWithObservable(observable);
    }

    // -- RestaurantsWithUsersLiveData --

    /**
     * Gets all restaurants from Google Maps and associated with all users
     * @param context a {@link Context}
     * @return a {@link RestaurantsWithUsersLiveData}
     */
    @NonNull
    public RestaurantsWithUsersLiveData getRestaurantsWithUsers(@NonNull final Context context) {
        if (this.mRestaurantsWithUsersLiveData == null) {
            this.mRestaurantsWithUsersLiveData = new RestaurantsWithUsersLiveData(this.getRestaurants(context),
                                                                                  this.getUsers());
        }

        return this.mRestaurantsWithUsersLiveData;
    }

    // -- Create (User) --

    /**
     * Creates the current user (authenticated) of Firebase Firestore
     * thanks to {@link UserRepository}
     * @param currentUser a {@link FirebaseUser} that contains the data of the last authenticated
     * @throws Exception if the {@link FirebaseUser} is null
     */
    public void createUser(@Nullable final FirebaseUser currentUser) throws Exception {
        // FirebaseUser must not be null to create an user
        if (currentUser == null) throw new Exception("FirebaseUser is null");

        this.mUserRepository.getUser(currentUser.getUid())
                            .addOnSuccessListener( documentSnapshot -> {
                                final User user = documentSnapshot.toObject(User.class);

                                if (user != null) {
                                    // Do nothing because user is already present
                                }
                                else {
                                    // Firebase Firestore has checked that user is not present.
                                    // So we can create a new document (user) in the collection (users).
                                    this.mUserRepository.createUser(currentUser.getUid(),
                                                                    currentUser.getDisplayName(),
                                                                    currentUser.getPhotoUrl().toString())
                                                        .addOnSuccessListener( aVoid -> {
                                                            // Do nothing because user is created
                                                        })
                                                        .addOnFailureListener( e ->
                                                            Log.d(TAG, "--> createUser (onFailure): " + e.getMessage())
                                                        );
                                }
                            })
                            .addOnFailureListener( e ->
                                Log.e(TAG, "--> getUser (onFailure): " + e.getMessage())
                            );
    }

    // -- Read (User) --

    /**
     * Reads the current user (authenticated) of Firebase Firestore
     * thanks to {@link UserRepository}
     * @param currentUser a {@link FirebaseUser} that contains the data of the last authenticated
     * @throws Exception if the {@link FirebaseUser} is null
     */
    @NonNull
    public Task<DocumentSnapshot> getUser(@Nullable final FirebaseUser currentUser) throws Exception {
        // FirebaseUser must not be null to read an user
        if (currentUser == null) throw new Exception("FirebaseUser is null");

        return this.mUserRepository.getUser(currentUser.getUid())
                                   .addOnFailureListener( e ->
                                       Log.e(TAG, "--> getUser (onFailure): " + e.getMessage())
                                   );
    }

    // -- Update (User) --

    /**
     * Updates the username field of the current user (authenticated) of Firebase Firestore
     * @param currentUser   a {@link FirebaseUser} that contains the data of the last authenticated
     * @param username      a {@link String} that contains the username
     * @throws Exception if the {@link FirebaseUser} is null
     */
    public void updateUsername(@Nullable final FirebaseUser currentUser,
                               @NonNull final String username) throws Exception {
        // FirebaseUser must not be null to read an user
        if (currentUser == null) throw new Exception("FirebaseUser is null");

        this.mUserRepository.updateUsername(currentUser.getUid(), username)
                            .addOnSuccessListener( aVoid -> {
                                // Do nothing because username is updated
                            })
                            .addOnFailureListener( e ->
                                Log.e(TAG, "--> updateUsername (onFailure): " + e.getMessage())
                            );
    }

    /**
     * Updates the restaurant fields of the current user (authenticated) of Firebase Firestore
     * @param currentUser           a {@link FirebaseUser} that contains the data of the last authenticated
     * @param placeIdOfRestaurant   a {@link String} that contains the place_id of the restaurant
     * @param nameOfRestaurant      a {@link String} that contains the name of the restaurant
     * @param foodTypeOfRestaurant  a {@link String} that contains the food type of the restaurant
     * @throws Exception if the {@link FirebaseUser} is null
     */
    public void updateRestaurant(@Nullable final FirebaseUser currentUser,
                                 @Nullable final String placeIdOfRestaurant,
                                 @Nullable final String nameOfRestaurant,
                                 @Nullable final String foodTypeOfRestaurant) throws Exception {
        // FirebaseUser must not be null to read an user
        if (currentUser == null) throw new Exception("FirebaseUser is null");

        this.mUserRepository.updateRestaurant(currentUser.getUid(),
                                              placeIdOfRestaurant,
                                              nameOfRestaurant,
                                              foodTypeOfRestaurant)
                            .addOnSuccessListener( aVoid -> {
                                // Do nothing because restaurant is updated
                            })
                            .addOnFailureListener( e ->
                                Log.e(TAG, "--> updateRestaurant (onFailure): " + e.getMessage())
                            );
    }

    // -- Delete (User) --

    /**
     * Deletes the current user (authenticated) of Firebase Firestore
     * thanks to {@link UserRepository}
     * @param currentUser a {@link FirebaseUser} that contains the data of the last authenticated
     * @throws Exception if the {@link FirebaseUser} is null
     */
    public void deleteUser(@Nullable final FirebaseUser currentUser) throws Exception {
        // FirebaseUser must not be null to create an user
        if (currentUser == null) throw new Exception("FirebaseUser is null");

        this.mUserRepository.deleteUser(currentUser.getUid())
                            .addOnSuccessListener( aVoid -> {
                                // Do nothing because user is deleted
                            })
                            .addOnFailureListener( e ->
                                Log.e(TAG, "--> deleteUser (onFailure): " + e.getMessage())
                            );
    }
}