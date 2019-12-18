package com.mancel.yann.go4lunch.repositories;

import com.mancel.yann.go4lunch.apis.PlaceService;
import com.mancel.yann.go4lunch.models.Follower;
import com.mancel.yann.go4lunch.models.UserInfos;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Yann MANCEL on 18/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.repositories
 *
 * A classs which implements {@link Repository.PlaceRepository}.
 */
public class PlaceRepositoryImpl implements Repository.PlaceRepository {

    // METHODS -------------------------------------------------------------------------------------

    @Override
    public Observable<UserInfos> getStreamToFetchUserInfos(String username) {
        PlaceService placeService = PlaceService.retrofit.create(PlaceService.class);

        return placeService.getUserInfos(username)
                           .subscribeOn(Schedulers.io())
                           .observeOn(AndroidSchedulers.mainThread())
                           .timeout(10, TimeUnit.SECONDS);
    }

    @Override
    public Observable<List<Follower>> getStreamToFetchUserFollowing(String username) {
        PlaceService placeService = PlaceService.retrofit.create(PlaceService.class);

        return placeService.getFollowing(username)
                           .subscribeOn(Schedulers.io())
                           .observeOn(AndroidSchedulers.mainThread())
                           .timeout(10, TimeUnit.SECONDS);
    }

    @Override
    public Observable<UserInfos> getStreamToFetchUserInfosFromFirstFollowing(String username) {
        return this.getStreamToFetchUserFollowing("JakeWharton")
                   .map(new Function<List<Follower>, Follower>() {
                       @Override
                       public Follower apply(List<Follower> followers) throws Exception {
                           return followers.get(0);
                       }
                   })
                   .flatMap(new Function<Follower, Observable<UserInfos>>() {
                       @Override
                       public Observable<UserInfos> apply(Follower follower) throws Exception {
                           return getStreamToFetchUserInfos(follower.getLogin());
                       }
                   });
    }
}
