package com.trying.developing.cookbaking.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trying.developing.cookbaking.R;

public class RecipeStepsActivity extends AppCompatActivity implements ForTablet {

    boolean mTwoPane=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        setTitle("Ingredients");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        RecipeStepsFragment recipeStepsFragment=new RecipeStepsFragment();

        recipeStepsFragment.setNameListener(RecipeStepsActivity.this);
        recipeStepsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.StepFrameLayout,recipeStepsFragment).commit();

        if(null != findViewById(R.id.flDetails)){

            mTwoPane=true;

        }

    }

    @Override
    public void setSelectingBaking(String videoURL, String Description, String thumbnailURL, String StepBean, int position, int StepSize) {

        if(!mTwoPane){
            Intent intent=new Intent(RecipeStepsActivity.this,BakingStepDetailsActivity.class);
            intent.putExtra("videoURL", videoURL);
            intent.putExtra("Description", Description);
            intent.putExtra("thumbnailURL", thumbnailURL);
            intent.putExtra("stepsBean", StepBean);
            intent.putExtra("position", position);
            intent.putExtra("stepsBeanSize", StepSize);

            startActivity(intent);


        }else {

            BakingStepDetailsFragment recipeStepDetailsFragment=new BakingStepDetailsFragment();
            Bundle bundle=new Bundle();

            bundle.putString("videoURL",videoURL);
            bundle.putString("Description",Description);
            bundle.putString("thumbnailURL",thumbnailURL);
            bundle.putString("stepsBean",StepBean);
            bundle.putInt("position",position);
            bundle.putInt("stepsBeanSize",StepSize);
            recipeStepDetailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.flDetails,recipeStepDetailsFragment).commit();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }
}
