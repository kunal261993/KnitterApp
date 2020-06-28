package com.example.knitter;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/top_rated")
    Call<PostResponse> getTopRatedMovies(@Query("api_key") String apiKey);
    @GET("posts")
    Call<PostResponse> getNewPosts(@Query("page") int pageIndex, @Query("_format") String format, @Query("access-token") String apiKey);
    @GET("movie/{id}")
    Call<PostResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
