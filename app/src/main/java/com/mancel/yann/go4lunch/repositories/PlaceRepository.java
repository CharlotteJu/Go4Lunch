package com.mancel.yann.go4lunch.repositories;

import com.mancel.yann.go4lunch.models.Follower;
import com.mancel.yann.go4lunch.models.UserInfos;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Yann MANCEL on 20/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.repositories
 */
public interface PlaceRepository {

    // METHODS ---------------------------------------------------------------------------------

    /**
     * Get stream to Fetch the user infos
     * @param username a {@link String}
     * @return an {@link Observable} of {@link UserInfos}
     */
    Observable<UserInfos> getStreamToFetchUserInfos(final String username);

    /**
     * Get stream to Fetch the user following
     * @param username a {@link String}
     * @return an {@link Observable} of {@link List<Follower>}
     */
    Observable<List<Follower>> getStreamToFetchUserFollowing(final String username);

    /**
     * Get stream to Fetch the user infos from the first following
     * @param username a {@link String}
     * @return an {@link Observable} of {@link UserInfos}
     */
    Observable<UserInfos> getStreamToFetchUserInfosFromFirstFollowing(final String username);
}
