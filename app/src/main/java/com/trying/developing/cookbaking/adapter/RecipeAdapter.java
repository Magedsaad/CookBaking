package com.trying.developing.cookbaking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.trying.developing.cookbaking.R;
import com.trying.developing.cookbaking.pojo.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developing on 2/18/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe>data;
    private Context mContext;




    public RecipeAdapter(Context mContext,List<Recipe> data) {
        this.data = data;
        this.mContext = mContext;
    }


    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflate=LayoutInflater.from(parent.getContext());
        View view=inflate.inflate(R.layout.recipe_row,parent,false);
        RecipeViewHolder recipeHolder=new RecipeViewHolder(view);

        return recipeHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

      final  Recipe recipe=data.get(position);


        holder.name.setText(recipe.getName());
       holder.serving.setText(recipe.getResolvedServings());

        holder.bindImage(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

   public class RecipeViewHolder extends RecyclerView.ViewHolder {


       public TextView name;

       public TextView serving;

       public ImageView CakeImage;


       public RecipeViewHolder(View itemView) {
           super(itemView);
           name = (TextView) itemView.findViewById(R.id.recipe_name);
           serving = (TextView) itemView.findViewById(R.id.recipe_servings);
           CakeImage = (ImageView) itemView.findViewById(R.id.recipe_photo);

       }

       public void bindImage(final Recipe mdata){
           String imag=mdata.getImage();
           if(imag.isEmpty()) {
               Picasso.with(itemView.getContext()).load(getImage(mdata.getId())).into(CakeImage);
           } else {
               Picasso.with(itemView.getContext()).
                       load(mdata.getImage()).
                       placeholder(R.drawable.placeholder).into(CakeImage);
           }

       }

       public int getImage(int ImageId) {

           switch (ImageId) {

               case 1:
                   return R.drawable.nutella_pie;
               case 2:
                   return R.drawable.brownis;
               case 3:
                   return R.drawable.yellow_cake;
               case 4:
                   return R.drawable.cheesecake;

               default:
                   return R.drawable.placeholder;
           }


       }

       }


   }

