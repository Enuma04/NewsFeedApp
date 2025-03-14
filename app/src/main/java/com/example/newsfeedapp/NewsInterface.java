package com.example.newsfeedapp;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsInterface {

    @GET("v1/news/top")
    Call<ResponseWrapper> getTopNews(
            @Query("api_token") String apiToken,
            @Query("locale") String locale,
            @Query("limit") int limit
    );
}
