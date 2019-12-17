package com.mancel.yann.go4lunch.apis;

import com.mancel.yann.go4lunch.models.Follower;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
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
                                                        .build();

    // METHODS -------------------------------------------------------------------------------------

    //"https://api.github.com/users/JakeWharton/following"
    @GET("users/{username}/following")
    Call<List<Follower>> getFollowing(@Path("username") final String username);

}
