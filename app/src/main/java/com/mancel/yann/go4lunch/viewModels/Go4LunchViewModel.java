package com.mancel.yann.go4lunch.viewModels;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mancel.yann.go4lunch.R;
import com.mancel.yann.go4lunch.liveDatas.DetailsLiveData;
import com.mancel.yann.go4lunch.liveDatas.FirestoreQueryLiveData;
import com.mancel.yann.go4lunch.liveDatas.LocationLiveData;
import com.mancel.yann.go4lunch.liveDatas.NearbySearchLiveData;
import com.mancel.yann.go4lunch.liveDatas.POIsLiveData;
import com.mancel.yann.go4lunch.liveDatas.RestaurantsLiveData;
import com.mancel.yann.go4lunch.liveDatas.RestaurantsWithUsersLiveData;
import com.mancel.yann.go4lunch.models.Details;
import com.mancel.yann.go4lunch.models.LocationData;
import com.mancel.yann.go4lunch.models.Message;
import com.mancel.yann.go4lunch.models.NearbySearch;
import com.mancel.yann.go4lunch.models.POI;
import com.mancel.yann.go4lunch.models.Restaurant;
import com.mancel.yann.go4lunch.models.User;
import com.mancel.yann.go4lunch.repositories.LikeRepository;
import com.mancel.yann.go4lunch.repositories.MessageRepository;
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
public class Go4LunchViewModel extends ViewModel {

    /*
        Summary of LiveData for the fragments: *****************************************************

            LunchMapFragment:
            |
            + --- LocationLiveData
            |
            + --- POIsLiveData
                  |
                  + -- NearbySearchLiveData
                  |
                  + -- FirestoreQueryLiveData (User)

            LunchListFragment:
            |
            + --- LocationLiveData
            |
            + --- RestaurantsWithUsersLiveData
                  |
                  + -- RestaurantsLiveData
                  |
                  + -- FirestoreQueryLiveData (User)

            WorkmateFragment:
            |
            + --- FirestoreQueryLiveData (User)

        Summary of LiveData for the activities: ****************************************************

            ChatActivity:
            |
            + --- FirestoreQueryLiveData (Message)

            DetailsActivity:
            |
            + --- DetailsLiveData
            |
            + --- FirestoreQueryLiveData (User)
     */

    // FIELDS --------------------------------------------------------------------------------------

    // -- Repositories --

    @NonNull
    private final UserRepository mUserRepository;

    @NonNull
    private final LikeRepository mLikeRepository;

    @NonNull
    private final MessageRepository mMessageRepository;

    @NonNull
    private final PlaceRepository mPlaceRepository;

    // -- LiveData (Simple) --

    @Nullable
    private LiveData<List<User>> mUsersLiveData = null;

    @Nullable
    private LiveData<List<User>> mUsersLiveDataWithSameRestaurant = null;

    @Nullable
    private LiveData<List<Message>> mMessagesLiveData = null;

    @Nullable
    private LocationLiveData mLocationLiveData = null;

    @Nullable
    private NearbySearchLiveData mNearbySearchLiveData = null;

    @Nullable
    private DetailsLiveData mDetailsLiveData = null;

    // -- LiveData (Complex) --

    @Nullable
    private POIsLiveData mPOIsLiveData = null;

    @Nullable
    private RestaurantsLiveData mRestaurantsLiveData = null;

    @Nullable
    private RestaurantsWithUsersLiveData mRestaurantsWithUsersLiveData = null;

    // CONSTRUCTORS --------------------------------------------------------------------------------

    /**
     * Constructor with 2 repositories
     * @param userRepository    a {@link UserRepository} for data from Firebase Firestore
     * @param likeRepository    a {@link LikeRepository} for data from Firebase Firestore
     * @param messageRepository a {@link MessageRepository} for data from Firebase Firestore
     * @param placeRepository   a {@link PlaceRepository} for data from Google Maps
     */
    public Go4LunchViewModel(@NonNull final UserRepository userRepository,
                             @NonNull final LikeRepository likeRepository,
                             @NonNull final MessageRepository messageRepository,
                             @NonNull final PlaceRepository placeRepository) {
        this.mUserRepository = userRepository;
        this.mLikeRepository = likeRepository;
        this.mMessageRepository = messageRepository;
        this.mPlaceRepository = placeRepository;
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ViewModel --

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    // -- FirestoreQueryLiveData --

    /**
     * Gets all users from Firebase Firestore
     * @return a {@link LiveData} of {@link List<User>}
     */
    @NonNull
    public LiveData<List<User>> getUsers() {
        if (this.mUsersLiveData == null) {
            this.mUsersLiveData = new FirestoreQueryLiveData<>(User.class,
                                                               this.mUserRepository.getAllUsers());
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
            this.mUsersLiveDataWithSameRestaurant = new FirestoreQueryLiveData<>(User.class,
                                                                                 this.mUserRepository.getAllUsersFromThisRestaurant(placeIdOfRestaurant));
        }

        return this.mUsersLiveDataWithSameRestaurant;
    }

    /**
     * Gets all messages from Firebase Firestore
     * @return a {@link LiveData} of {@link List<Message>}
     */
    @NonNull
    public LiveData<List<Message>> getMessages() {
        if (this.mMessagesLiveData == null) {
            this.mMessagesLiveData = new FirestoreQueryLiveData<>(Message.class,
                                                                  this.mMessageRepository.getAllMessages());
        }

        return this.mMessagesLiveData;
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
     * Fetches the {@link NearbySearch}
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
     * Fetches the {@link Details}
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

    // -- RestaurantsLiveData --

    /**
     * Gets all restaurants from Google Maps
     * @param context       a {@link Context}
     * @param locationData  a {@link LocationData}
     * @param radius        a double that contains the radius of research
     * @return a {@link RestaurantsLiveData}
     */
    @NonNull
    public RestaurantsLiveData getRestaurants(@NonNull final Context context,
                                              @Nullable final LocationData locationData,
                                              final double radius) {
        if (this.mRestaurantsLiveData == null) {
            this.mRestaurantsLiveData = new RestaurantsLiveData();
        }

        // Fetches the restaurants
        if (locationData != null) {
            this.fetchRestaurants(context, locationData, radius);
        }

        return this.mRestaurantsLiveData;
    }

    /**
     * Fetches the {@link Restaurant}
     * @param context       a {@link Context}
     * @param locationData  a {@link LocationData}
     * @param radius        a double that contains the radius of research
     */
    public void fetchRestaurants(@NonNull final Context context,
                                 @Nullable final LocationData locationData,
                                 final double radius) {
        // Retrieves Google Maps Key
        final String key = context.getResources()
                                  .getString(R.string.google_maps_key);

        // Location
        final String location = locationData.getLocation().getLatitude() + "," +
                                locationData.getLocation().getLongitude();

        // Types
        final String types = "restaurant";

        // Observable
        final Observable<List<Restaurant>> observable;
        observable = this.mPlaceRepository.getStreamToFetchNearbySearchThenToFetchRestaurants(location,
                                                                                              radius,
                                                                                              types,
                                                                                             "walking",
                                                                                             "metric",
                                                                                              key);

        // Updates LiveData for the restaurants
        this.mRestaurantsLiveData.getRestaurantsWithObservable(observable);
    }

    // -- RestaurantsWithUsersLiveData --

    /**
     * Gets all restaurants from Google Maps and associated with all users
     * @param context       a {@link Context}
     * @param locationData  a {@link LocationData}
     * @param radius        a double that contains the radius of research
     * @return a {@link RestaurantsWithUsersLiveData}
     */
    @NonNull
    public RestaurantsWithUsersLiveData getRestaurantsWithUsers(@NonNull final Context context,
                                                                @Nullable final LocationData locationData,
                                                                final double radius) {
        if (this.mRestaurantsWithUsersLiveData == null) {
            this.mRestaurantsWithUsersLiveData = new RestaurantsWithUsersLiveData(this.getRestaurants(context, locationData, radius),
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
                                                            Crashlytics.log(Log.ERROR,
                                                                            Go4LunchViewModel.class.getSimpleName(),
                                                                           "createUser (onFailure): " + e.getMessage())
                                                        );
                                }
                            })
                            .addOnFailureListener( e ->
                                Crashlytics.log(Log.ERROR,
                                                Go4LunchViewModel.class.getSimpleName(),
                                               "getUser (onFailure): " + e.getMessage())
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
                                       Crashlytics.log(Log.ERROR,
                                                       Go4LunchViewModel.class.getSimpleName(),
                                                      "getUser (onFailure): " + e.getMessage())
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
                                Crashlytics.log(Log.ERROR,
                                                Go4LunchViewModel.class.getSimpleName(),
                                               "updateUsername (onFailure): " + e.getMessage())
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
                                Crashlytics.log(Log.ERROR,
                                                Go4LunchViewModel.class.getSimpleName(),
                                               "updateRestaurant (onFailure): " + e.getMessage())
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
                                Crashlytics.log(Log.ERROR,
                                                Go4LunchViewModel.class.getSimpleName(),
                                               "deleteUser (onFailure): " + e.getMessage())
                            );
    }

    // -- Create (Like) --

    /**
     * Creates the like into Firebase Firestore
     * @param currentUser           a {@link FirebaseUser} that contains the data of the last authenticated
     * @param placeIdOfRestaurant   a {@link String} that contains the place_id of the restaurant
     * @param rating                a double that contains the user's rating of the restaurant
     * @throws Exception if the {@link FirebaseUser} is null
     */
    public void createLike(@Nullable final FirebaseUser currentUser,
                           @NonNull final String placeIdOfRestaurant,
                           final double rating) throws Exception {
        // FirebaseUser must not be null to create an user
        if (currentUser == null) throw new Exception("FirebaseUser is null");

        this.mLikeRepository.createLike(currentUser.getUid(),
                                        placeIdOfRestaurant,
                                        rating)
                            .addOnSuccessListener( aVoid -> {
                                // Do nothing because the like is created
                            })
                            .addOnFailureListener( e ->
                                Crashlytics.log(Log.ERROR,
                                                Go4LunchViewModel.class.getSimpleName(),
                                               "createLike (onFailure): " + e.getMessage())
                            );
    }

    // -- Read (Like) --

    /**
     * Reads the like of the restaurant for the current user (authenticated) of Firebase Firestore
     * @param currentUser           a {@link FirebaseUser} that contains the data of the last authenticated
     * @param placeIdOfRestaurant   a {@link String} that contains the place_id of the restaurant
     * @throws Exception if the {@link FirebaseUser} is null
     */
    @NonNull
    public Task<DocumentSnapshot> getLikeForUser(@Nullable final FirebaseUser currentUser,
                                                 @NonNull final String placeIdOfRestaurant) throws Exception {
        // FirebaseUser must not be null to read an user
        if (currentUser == null) throw new Exception("FirebaseUser is null");

        return this.mLikeRepository.getLikeForUser(currentUser.getUid(), placeIdOfRestaurant)
                   .addOnFailureListener( e ->
                       Crashlytics.log(Log.ERROR,
                                       Go4LunchViewModel.class.getSimpleName(),
                                      "getLikeForUser (onFailure): " + e.getMessage())
                   );
    }

    // -- Create (Message) --

    /**
     * Creates the message into Firebase Firestore
     * @param message       a {@link String} that contains the message
     * @param user          a {@link User} that contains the user who has created the message
     */
    public void createMessage(@NonNull final String message,
                              @NonNull final User user) {
        this.mMessageRepository.createMessage(message, user)
                .addOnSuccessListener( aVoid -> {
                    // Do nothing because message is created
                })
                .addOnFailureListener( e ->
                    Crashlytics.log(Log.ERROR,
                                    Go4LunchViewModel.class.getSimpleName(),
                                   "createMessage (onFailure): " + e.getMessage())
                );
    }
}