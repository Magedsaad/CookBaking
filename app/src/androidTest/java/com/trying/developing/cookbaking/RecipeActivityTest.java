package com.trying.developing.cookbaking;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.trying.developing.cookbaking.ui.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

/**
 * Created by developing on 2/22/2018.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {



    @Rule
    public ActivityTestRule<RecipeActivity> testRule=new
            ActivityTestRule<>(RecipeActivity.class);

    @Test
    public void ClickOnRecylerView(){
        testScenarioCheckToolbarTitle(0);
    }


    private void testScenarioCheckToolbarTitle(int position) {
        try {
            Thread.sleep(3000);
            onView(withId(R.id.recipe_recycleviewID)).perform(actionOnItemAtPosition(position,click()));
            onView(withId(R.id.ingredientsId)).check(matches(withText("Ingredients:")));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
