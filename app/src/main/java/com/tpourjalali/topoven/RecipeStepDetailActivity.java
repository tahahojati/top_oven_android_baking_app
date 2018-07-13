package com.tpourjalali.topoven;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import com.tpourjalali.topoven.model.Recipe;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tpourjalali.topoven.helpers.RecipeStepDetailPagerAdapter;
import com.tpourjalali.topoven.model.RecipeRepo;

import java.util.List;

public class RecipeStepDetailActivity extends AppCompatActivity implements RecipeStepDetailFragment.CallBacks {
    private static final String INTENT_EXTRA_RECIPE_INDEX = "recipe_index";
    private static final String TAG = "RecipeStepDetailActivity";
    private static final String PACKAGE_NAME = "com.tpourjalali.topoven";
    private ViewPager mViewPager;
    private ImageButton mNextButton, mPrevButton;
    private TextView mStepNumberTextView;
    private RecipeStepDetailPagerAdapter mPagerAdapter;
    private int mRecipeIndex = -1;
    private List<Recipe.RecipeStep> mRecipeStepList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_recipe_step_detail);

        mNextButton = findViewById(R.id.next_page_image_button);
        mPrevButton = findViewById(R.id.previous_page_image_button);
        setValuesFromBuncle(getIntent().getExtras());
        //NOTE: line above must have set mRecipeIndex to some positive value, else, there is an error
        if(mRecipeIndex < 0){
            finish();
        }
        mViewPager = findViewById(R.id.recipe_step_fragment_view_pager);
        mStepNumberTextView = findViewById(R.id.recipe_step_number_text_view);
        mPagerAdapter = new RecipeStepDetailPagerAdapter(getSupportFragmentManager());
        mRecipeStepList = RecipeRepo.getInstance(this).getRecipe(mRecipeIndex).getRecipeSteps();
        mPagerAdapter.updateData(mRecipeStepList);

    }

    public static Intent createIntent(int recipeIndex){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tpourjalali.topoven", RecipeStepDetailActivity.class.getName()));
        intent.putExtra(INTENT_EXTRA_RECIPE_INDEX, recipeIndex);

        return intent;
    }
    private void setValuesFromBuncle(@Nullable Bundle bundle){
        if(bundle == null) return ;
        if(bundle.containsKey(INTENT_EXTRA_RECIPE_INDEX)){
            mRecipeIndex = bundle.getInt(INTENT_EXTRA_RECIPE_INDEX, -1);
        } else {
            mRecipeIndex = -1;
        }
    }
}
