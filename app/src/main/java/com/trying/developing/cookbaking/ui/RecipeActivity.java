package com.trying.developing.cookbaking.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trying.developing.cookbaking.R;
import com.trying.developing.cookbaking.adapter.RecipeAdapter;
import com.trying.developing.cookbaking.pojo.Recipe;
import com.trying.developing.cookbaking.rest.APIService;
import com.trying.developing.cookbaking.rest.ApiURL;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity {



    APIService apiService;
    RecyclerView RecyclerViewer;
    RecipeAdapter recipeAdapter;
    List<Recipe> data;
    Gson gson;
    String Baking="";

    ProgressDialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        if(internet_connection()) {
            RecyclerViewer = (RecyclerView) findViewById(R.id.recipe_recycleviewID);
            gson = new Gson();
            apiService = ApiURL.getService();
            dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Loading....");


            if (savedInstanceState != null) {
                Baking = savedInstanceState.getString("baking");
                Type type = new TypeToken<List<Recipe>>() {
                }.getType();
                data = gson.fromJson(Baking, type);
                recipeAdapter = new RecipeAdapter(RecipeActivity.this, data);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(RecipeActivity.this, getSpan());
                RecyclerViewer.setLayoutManager(layoutManager);
                RecyclerViewer.setItemAnimator(new DefaultItemAnimator());
                RecyclerViewer.setAdapter(recipeAdapter);

                RecyclerViewer.addOnItemTouchListener(new RecyclerItemClickListener(RecipeActivity.this, RecyclerViewer, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(RecipeActivity.this, RecipeStepsActivity.class);
                                intent.putExtra("Index", position);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })

                );

            } else {
                getBaking();
            }
        }else {

            Toast.makeText(RecipeActivity.this,"there's no connection",Toast.LENGTH_LONG).show();

        }


    }

    private int getSpan() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 2;
        }
        return 1;
    }


    public void getBaking(){
        dialog.show();

        apiService.getRecipe().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if(response.isSuccessful()){
                    data=response.body();
                    Baking=gson.toJson(data);
                    SharedPreferences sharedPreferences=RecipeActivity.this.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("baking",Baking);
                    editor.apply();

                    recipeAdapter=new RecipeAdapter(RecipeActivity.this,data);
                    RecyclerView.LayoutManager layoutManager=new GridLayoutManager(RecipeActivity.this,getSpan());
                    RecyclerViewer.setLayoutManager(layoutManager);
                    RecyclerViewer.setItemAnimator(new DefaultItemAnimator());
                    RecyclerViewer.setAdapter(recipeAdapter);

                    RecyclerViewer.addOnItemTouchListener(new RecyclerItemClickListener(RecipeActivity.this, RecyclerViewer, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                    Intent intent=new Intent(RecipeActivity.this,RecipeStepsActivity.class);
                                    intent.putExtra("Index",position);
                                    startActivity(intent);

                                }

                                @Override
                                public void onLongItemClick(View view, int position) {

                                }
                            })

                    );


                }

                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(RecipeActivity.this,"there's no internet connection",Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences=RecipeActivity.this.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
                Baking=sharedPreferences.getString("baking","");
                Type type=new TypeToken<List<Recipe>>(){
                }.getType();
                data=gson.fromJson(Baking,type);
                recipeAdapter=new RecipeAdapter(RecipeActivity.this,data);
                RecyclerView.LayoutManager layoutManager=new GridLayoutManager(RecipeActivity.this,getSpan());
                RecyclerViewer.setLayoutManager(layoutManager);
                RecyclerViewer.setItemAnimator(new DefaultItemAnimator());
                RecyclerViewer.setAdapter(recipeAdapter);

                RecyclerViewer.addOnItemTouchListener(new RecyclerItemClickListener(RecipeActivity.this, RecyclerViewer, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Intent intent=new Intent(RecipeActivity.this,RecipeStepsActivity.class);
                                intent.putExtra("Index",position);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })

                );
                dialog.dismiss();

            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("baking",Baking);
    }


    boolean internet_connection(){
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
