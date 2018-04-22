package com.trying.developing.cookbaking.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trying.developing.cookbaking.R;
import com.trying.developing.cookbaking.adapter.RecipeAdapter;
import com.trying.developing.cookbaking.pojo.Recipe;
import com.trying.developing.cookbaking.rest.APIService;
import com.trying.developing.cookbaking.rest.ApiURL;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The configuration screen for the {@link NewAppWidget NewAppWidget} AppWidget.
 */
public class NewAppWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.trying.developing.cookbaking.ui.NewAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    APIService mAPIService;
    ProgressDialog pdialog;
    RecyclerView recycler_view;
    RecipeAdapter adapter;
    String BakingData = "";
    boolean tabletMood = false;
    int noItems = 1;
    Gson gson;
    List<Recipe> bakingResponses;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public NewAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.new_app_widget_configure);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        tabletMood = getResources().getBoolean(R.bool.tabletMood);
        if (tabletMood)
            noItems = 2;
        else
            noItems = 1;

        gson = new Gson();
        mAPIService = ApiURL.getService();

        pdialog = new ProgressDialog(NewAppWidgetConfigureActivity.this);
        pdialog.setIndeterminate(true);
        pdialog.setCancelable(false);
        pdialog.setMessage("Loading. Please wait...");

        if (icicle != null) {
            BakingData = icicle.getString("baking");
            Type type = new TypeToken<List<Recipe>>() {
            }.getType();
            bakingResponses = gson.fromJson(BakingData, type);

            adapter = new RecipeAdapter(NewAppWidgetConfigureActivity.this, bakingResponses);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(NewAppWidgetConfigureActivity.this, noItems);
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(adapter);

            recycler_view.addOnItemTouchListener(
                    new RecyclerItemClickListener(NewAppWidgetConfigureActivity.this, recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            String widgetText = "Ingredients: \n";
                            // When the button is clicked, store the string locally
                            for (int index = 0; index < bakingResponses.get(position).getIngredients().size(); index++)
                                widgetText += bakingResponses.get(position).getIngredients().get(index).getIngredient() + " : " + bakingResponses.get(position).getIngredients().get(index).getQuantity() + " " + bakingResponses.get(position).getIngredients().get(index).getMeasure() + "\n";
                            saveTitlePref(NewAppWidgetConfigureActivity.this, mAppWidgetId, widgetText);

                            // It is the responsibility of the configuration activity to update the app widget
                            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(NewAppWidgetConfigureActivity.this);
                            NewAppWidget.updateAppWidget(NewAppWidgetConfigureActivity.this, appWidgetManager, mAppWidgetId);

                            // Make sure we pass back the original appWidgetId
                            Intent resultValue = new Intent();
                            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                            setResult(RESULT_OK, resultValue);
                            finish();
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            // do whatever
                        }
                    })
            );
        } else {
            getBakingGET();
        }

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    public void getBakingGET() {
        pdialog.show();
        mAPIService.getRecipe().enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if (response.isSuccessful()) {

                    bakingResponses = response.body();
                    BakingData = gson.toJson(bakingResponses);
                    SharedPreferences sharedPreferences = NewAppWidgetConfigureActivity.this.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("baking", BakingData);
                    editor.apply();

                    adapter = new RecipeAdapter(NewAppWidgetConfigureActivity.this, bakingResponses);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(NewAppWidgetConfigureActivity.this, noItems);
                    recycler_view.setLayoutManager(mLayoutManager);
                    recycler_view.setItemAnimator(new DefaultItemAnimator());
                    recycler_view.setAdapter(adapter);

                    recycler_view.addOnItemTouchListener(
                            new RecyclerItemClickListener(NewAppWidgetConfigureActivity.this, recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                    String widgetText = "Ingredients: \n";
                                    // When the button is clicked, store the string locally
                                    for (int index = 0; index < bakingResponses.get(position).getIngredients().size(); index++)
                                        widgetText += bakingResponses.get(position).getIngredients().get(index).getIngredient() + " : " + bakingResponses.get(position).getIngredients().get(index).getQuantity() + " " + bakingResponses.get(position).getIngredients().get(index).getMeasure() + "\n";
                                    saveTitlePref(NewAppWidgetConfigureActivity.this, mAppWidgetId, widgetText);

                                    // It is the responsibility of the configuration activity to update the app widget
                                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(NewAppWidgetConfigureActivity.this);
                                    NewAppWidget.updateAppWidget(NewAppWidgetConfigureActivity.this, appWidgetManager, mAppWidgetId);

                                    // Make sure we pass back the original appWidgetId
                                    Intent resultValue = new Intent();
                                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                                    setResult(RESULT_OK, resultValue);
                                    finish();
                                }

                                @Override
                                public void onLongItemClick(View view, int position) {
                                    // do whatever
                                }
                            })
                    );
                }
                pdialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(NewAppWidgetConfigureActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = NewAppWidgetConfigureActivity.this.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
                BakingData = sharedPreferences.getString("baking", "");

                Type type = new TypeToken<List<Recipe>>() {
                }.getType();
                bakingResponses = gson.fromJson(BakingData, type);

                adapter = new RecipeAdapter(NewAppWidgetConfigureActivity.this, bakingResponses);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(NewAppWidgetConfigureActivity.this, noItems);
                recycler_view.setLayoutManager(mLayoutManager);
                recycler_view.setItemAnimator(new DefaultItemAnimator());
                recycler_view.setAdapter(adapter);

                recycler_view.addOnItemTouchListener(
                        new RecyclerItemClickListener(NewAppWidgetConfigureActivity.this, recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                String widgetText = "Ingredients: \n";
                                // When the button is clicked, store the string locally
                                for (int index = 0; index < bakingResponses.get(position).getIngredients().size(); index++)
                                    widgetText += bakingResponses.get(position).getIngredients().get(index).getIngredient() + "\n";
                                saveTitlePref(NewAppWidgetConfigureActivity.this, mAppWidgetId, widgetText);

                                // It is the responsibility of the configuration activity to update the app widget
                                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(NewAppWidgetConfigureActivity.this);
                                NewAppWidget.updateAppWidget(NewAppWidgetConfigureActivity.this, appWidgetManager, mAppWidgetId);

                                // Make sure we pass back the original appWidgetId
                                Intent resultValue = new Intent();
                                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                                setResult(RESULT_OK, resultValue);
                                finish();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                // do whatever
                            }
                        })
                );

                pdialog.dismiss();
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("baking", BakingData);
    }
}

