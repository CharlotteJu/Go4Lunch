package com.mancel.yann.go4lunch.apis;

import com.mancel.yann.go4lunch.models.Follower;
import com.mancel.yann.go4lunch.models.UserInfos;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Yann MANCEL on 17/12/2019.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.apis
 */
public interface PlaceService {

    // FIELDS --------------------------------------------------------------------------------------

    public static final Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl("https://api.github.com/")
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                                        .build();

    // METHODS -------------------------------------------------------------------------------------

    // -- GET --

    //"https://api.github.com/users/{username}"
    @GET("users/{username}")
    Observable<UserInfos> getUserInfos(@Path("username") final String username);

    //"https://api.github.com/users/{username}/following"
    @GET("users/{username}/following")
    Observable<List<Follower>> getFollowing(@Path("username") final String username);
}
