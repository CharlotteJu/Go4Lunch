package com.mancel.yann.go4lunch;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.mancel.yann.go4lunch.models.Follower;
import com.mancel.yann.go4lunch.models.UserInfos;
import com.mancel.yann.go4lunch.repositories.PlaceRepository;
import com.mancel.yann.go4lunch.repositories.PlaceRepositoryImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Yann MANCEL on 18/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch
 *
 * Test on {@link PlaceRepository}.
 */
@RunWith(AndroidJUnit4.class)
public class PlaceRepositoryTest {

    // FIELDS --------------------------------------------------------------------------------------

    private PlaceRepository mPlaceRepository;

    // METHODS -------------------------------------------------------------------------------------

    @Before
    public void setUp() {
        this.mPlaceRepository = new PlaceRepositoryImpl();
    }

    @Test
    public void shouldFetchUserFollowing() {
        // Creates Observable
        final Observable<List<Follower>> observable = this.mPlaceRepository.getStreamToFetchUserFollowing("JakeWharton");

        // Creates Observer
        final TestObserver<List<Follower>> observer = new TestObserver<>();

        // Create Stream
        observable.subscribeWith(observer)
                  .assertNoErrors()
                  .assertNoTimeout()
                  .awaitTerminalEvent();

        // Fetches the result
        final List<Follower> followers = observer.values().get(0);

        // TEST: List's size
        assertThat("Jake Wharton follows only 12 users.", followers.size() == 12);
    }

    @Test
    public void shouldFetchUserInfos() {
        // Creates Observable
        final Observable<UserInfos> observable = this.mPlaceRepository.getStreamToFetchUserInfos("JakeWharton");

        // Creates Observer
        final TestObserver<UserInfos> observer = new TestObserver<>();

        // Create Stream
        observable.subscribeWith(observer)
                  .assertNoErrors()
                  .assertNoTimeout()
                  .awaitTerminalEvent();

        // Fetches the result
        final UserInfos user = observer.values().get(0);

        // TEST: List's size
        assertThat("Jake Wharton GitHub's ID is 66577.", user.getId() == 66577);
    }
}
