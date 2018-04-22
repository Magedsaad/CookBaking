package com.trying.developing.cookbaking.ui;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trying.developing.cookbaking.R;
import com.trying.developing.cookbaking.pojo.Steps;

import java.lang.reflect.Type;
import java.util.List;

public class BakingStepDetailsActivity extends AppCompatActivity {

    int position, stepsBeanSize;
    String videoURL, stepsBean;
    Gson gson;
    List<Steps> stepsBeen;

    private boolean shouldAutoPlay;
    private int currentWindow;
    private long playbackPosition=0;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_step_details);

        Intent sentIntent = getIntent();
        Bundle sentBundle = sentIntent.getExtras();

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("position");
            videoURL = savedInstanceState.getString("videoURL");
            stepsBean = savedInstanceState.getString("stepsBean");
            stepsBeanSize = savedInstanceState.getInt("stepsBeanSize");
            shouldAutoPlay = savedInstanceState.getBoolean("shouldAutoPlay");
            currentWindow = savedInstanceState.getInt("currentWindow");
            playbackPosition = savedInstanceState.getLong("playbackPosition");
            Log.i("Taaaag","AA"+playbackPosition+"");
        } else {
            position = sentBundle.getInt("position");
            videoURL = sentBundle.getString("videoURL");
            stepsBean = sentBundle.getString("stepsBean");
            stepsBeanSize = sentBundle.getInt("stepsBeanSize");
        }

        gson = new Gson();
        Type type = new TypeToken<List<Steps>>() {
        }.getType();
        stepsBeen = gson.fromJson(stepsBean, type);

        BakingStepDetailsFragment bakingStepDetailsFragment=new BakingStepDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("videoURL", stepsBeen.get(position).getVideoURL());
        bundle.putString("Description", stepsBeen.get(position).getDescription());
        bundle.putString("thumbnailURL", stepsBeen.get(position).getThumbnailURL());

        bundle.putBoolean("shouldAutoPlay", shouldAutoPlay);
        bundle.putInt("currentWindow", currentWindow);
        Log.i("Taaaag","BB"+playbackPosition+"");
        bundle.putLong("playbackPosition", playbackPosition);

        bakingStepDetailsFragment.setArguments(sentBundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.flDetails,bakingStepDetailsFragment).commit();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);
        outState.putString("videoURL", stepsBeen.get(position).getVideoURL());
        outState.putString("Description", stepsBeen.get(position).getDescription());
        outState.putString("thumbnailURL", stepsBeen.get(position).getThumbnailURL());
        outState.putString("stepsBean", stepsBean);
        outState.putInt("stepsBeanSize", stepsBeanSize);
        outState.putBoolean("shouldAutoPlay", shouldAutoPlay);
        outState.putInt("currentWindow", currentWindow);

    }
}
