package com.trying.developing.cookbaking.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by developing on 2/19/2018.
 */

public class RestClient {

    private static Retrofit retrofit;
    public static Retrofit getClient(String baseUrl){
        retrofit=new Retrofit.Builder().baseUrl(baseUrl).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        return retrofit;
    }
}
