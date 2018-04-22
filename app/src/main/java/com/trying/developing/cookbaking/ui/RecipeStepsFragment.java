package com.trying.developing.cookbaking.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trying.developing.cookbaking.R;
import com.trying.developing.cookbaking.adapter.StepAdapter;
import com.trying.developing.cookbaking.pojo.Recipe;

import java.lang.reflect.Type;
import java.util.List;


public class RecipeStepsFragment extends Fragment {

    RecyclerView recyclerView;
    StepAdapter Adapter;
    TextView ingredient,ingred;

    String Baking;
    int index;
    Gson gson,gson2;
    List<Recipe> recipes;
    Recipe recipe;


    ForTablet mListener;


    public RecipeStepsFragment() {
    }

    public void setNameListener(ForTablet forTablet){
        this.mListener=forTablet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        recyclerView=(RecyclerView) view.findViewById(R.id.lvNames);
        ingredient=(TextView) view.findViewById(R.id.textingredients);
        ingred=(TextView) view.findViewById(R.id.ingredientsId);




        Bundle bundle=getArguments();
        index=bundle.getInt("Index");

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("sharedPreferences",Context.MODE_PRIVATE);
        Baking=sharedPreferences.getString("baking","");

        gson=new Gson();
        gson2=new Gson();

        Type type=new TypeToken<List<Recipe>>(){}.getType();

        recipes=gson.fromJson(Baking,type);

        recipe=recipes.get(index);
        for(int i=0;i<recipe.getIngredients().size();i++){

            ingredient.append(recipe.getIngredients().get(i).getIngredient()+" :  " + recipe.getIngredients().get(i).getQuantity()+" "+recipe.getIngredients().get(i).getMeasure()+"\n");

        }

        Log.d("TAGss",recipe.getSteps().toString());

        Adapter=new StepAdapter(getActivity(),recipe.getSteps());

        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity(),getSpan());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(Adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        String videoURL=recipe.getSteps().get(position).getVideoURL();
                        String description=recipe.getSteps().get(position).getDescription();
                        String thumbnailUrl=recipe.getSteps().get(position).getThumbnailURL();
                        String stepsBean=gson2.toJson(recipe.getSteps());
                        int stepsSize=recipe.getSteps().size();
                        mListener.setSelectingBaking(videoURL,description,thumbnailUrl,stepsBean,position,stepsSize);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );




        return view;
    }


    private int getSpan() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 2;
        }
        return 1;
    }



}
