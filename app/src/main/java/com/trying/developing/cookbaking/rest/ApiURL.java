package com.trying.developing.cookbaking.rest;

/**
 * Created by developing on 2/19/2018.
 */

public class ApiURL {
    public static final String BasrUrl="https://d17h27t6h515a5.cloudfront.net/";

    public static  APIService getService(){
        return   RestClient.getClient(BasrUrl).create(APIService.class);
    }

}
