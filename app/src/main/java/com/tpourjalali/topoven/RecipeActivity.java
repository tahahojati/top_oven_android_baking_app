
package com.tpourjalali.topoven;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.tpourjalali.topoven.model.Recipe;
import com.tpourjalali.topoven.model.RecipeRepo;


/**
 * fetch the recipe from the repo, check if it has video and if we are connected to internet, if so, stream the video, else, disable the player.
 */
public class RecipeActivity extends AppCompatActivity implements RecipeStepDetailFragment.CallBacks, RecipeStepListFragment.CallBacks {

    private boolean mTwoPane = false;
    private int mRecipeIndex = -1;
    private int mCurrentStep = 0;
    private Recipe mRecipe;
    private static final String KEY_RECIPE_INDEX = "recipe_index";
    private static final String KEY_RECIPE_STEP_INDEX = "recipe step";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        setValuesFromBundle(getIntent().getExtras());
        setValuesFromBundle(savedInstanceState);
        mRecipeIndex = 0 ; //TODO:remove this line!
        if(mRecipeIndex == -1){
            finish();
        }

        if(findViewById(R.id.container_layout) == null){
            //mobile device, only have the RecipeStepListFragment
            mTwoPane = false;
        } else {
            //tablet, have RecipeStepListFragment and RecipeStepDetailFragment
            mTwoPane = true;
        }
        mRecipe = RecipeRepo.getInstance(this).getRecipe(mRecipeIndex);

        setupMasterFragment();
        setupDetailFragment();


    }

    private void setupDetailFragment() {
        if(!mTwoPane)
            return;
        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentById(R.id.step_detail_fragment) == null)
            fm.beginTransaction()
                    .replace(R.id.step_detail_fragment, RecipeStepDetailFragment.newInstance(mRecipe.getRecipeSteps().get(mCurrentStep)))
                    .commit();
    }

    private void setupMasterFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentById(R.id.recipe_step_list_fragment) == null)
            fm.beginTransaction()
                .replace(R.id.recipe_step_list_fragment, RecipeStepListFragment.newInstance(mRecipeIndex))
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_RECIPE_STEP_INDEX, mCurrentStep);
//        outState.putInt(KEY_RECIPE_INDEX, mRecipeIndex); //no need, this is already saved in the intent.
    }

    private void setValuesFromBundle(@Nullable Bundle bundle){
        if(bundle == null) return ;
        if(bundle.containsKey(KEY_RECIPE_INDEX)){
            mRecipeIndex = bundle.getInt(KEY_RECIPE_INDEX);
        }
        if(bundle.containsKey(KEY_RECIPE_STEP_INDEX))
            mCurrentStep = bundle.getInt(KEY_RECIPE_STEP_INDEX);
    }


    private void goToDetailActivity(int stepIndex){
        Intent intent = RecipeStepDetailActivity.createIntent(mRecipeIndex, stepIndex);
        startActivity(intent);
    }
    private void replaceDetailFragment(int stepIndex){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.step_detail_fragment, RecipeStepDetailFragment.newInstance(mRecipe.getRecipeSteps().get(stepIndex)))
                .commit();
    }

    @Override
    public void onStepSelected(int stepNumber) {
        mCurrentStep = stepNumber;
        if(mTwoPane){
            replaceDetailFragment(stepNumber);
        } else {
            goToDetailActivity(stepNumber);
        }
    }

    public static Intent createIntent(int recipeIndex) {
        return createIntent(recipeIndex, 0);
    }

    public static Intent createIntent(int recipeIndex, int recipeStepIndex) {
        Intent intent = new Intent();
        intent.setClassName("com.tpourjalali.topoven", RecipeActivity.class.getName());
        intent.putExtra(KEY_RECIPE_INDEX, recipeIndex);
        intent.putExtra(KEY_RECIPE_STEP_INDEX, recipeStepIndex);
        return intent;
    }
}
