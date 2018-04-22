package com.trying.developing.cookbaking.rest;

import com.trying.developing.cookbaking.pojo.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by developing on 2/19/2018.
 */

public interface APIService {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipe();

}
