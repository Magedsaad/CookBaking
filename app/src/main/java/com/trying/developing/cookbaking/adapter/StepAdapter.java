package com.trying.developing.cookbaking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trying.developing.cookbaking.R;
import com.trying.developing.cookbaking.pojo.Recipe;
import com.trying.developing.cookbaking.pojo.Steps;

import java.util.List;

/**
 * Created by developing on 2/20/2018.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

    Context mContext;
    List<Steps> data;


    public StepAdapter(Context mContext, List<Steps> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.step_row,parent,false);
        StepHolder holder= new StepHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(StepHolder holder, int position) {

        Steps steps=data.get(position);

        holder.ingerdient.setText(steps.getShortDescription());
        Log.d("stepsss",steps.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class StepHolder extends RecyclerView.ViewHolder {

        TextView ingerdient;

        public StepHolder(View itemView) {
            super(itemView);
            ingerdient = (TextView) itemView.findViewById(R.id.Desc);
        }
    }
}
