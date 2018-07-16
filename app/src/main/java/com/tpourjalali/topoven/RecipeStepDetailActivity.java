package com.tpourjalali.topoven;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.tpourjalali.topoven.model.Recipe;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tpourjalali.topoven.helpers.RecipeStepDetailPagerAdapter;
import com.tpourjalali.topoven.model.RecipeRepo;


public class RecipeStepDetailActivity extends AppCompatActivity implements RecipeStepDetailFragment.CallBacks, ViewPager.OnPageChangeListener, View.OnClickListener{
    private static final String INTENT_EXTRA_RECIPE_INDEX = "recipe_index";
    private static final String TAG = "RecipeStepDetailActivity";
    private static final String KEY_CURRENT_STEP = "current_step";
    private static final String PACKAGE_NAME = "com.tpourjalali.topoven";
    private ViewPager mViewPager;
    private ImageButton mNextButton, mPrevButton; //may be null
    private TextView mStepNumberTextView; //may be null
    private RecipeStepDetailPagerAdapter mPagerAdapter;
    private int mRecipeIndex = -1;
    private int mCurrentStep = 0;
    private Recipe mRecipe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate  " + (savedInstanceState == null ? "": savedInstanceState.toString()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        mNextButton = findViewById(R.id.next_page_image_button);
        mPrevButton = findViewById(R.id.previous_page_image_button);
        if(mNextButton != null && mPrevButton != null) {
            mNextButton.setOnClickListener(this);
            mPrevButton.setOnClickListener(this);
        }
        setValuesFromBundle(getIntent().getExtras());
        setValuesFromBundle(savedInstanceState);
        //NOTE: line above must have set mRecipeIndex to some positive value, else, there is an error
        mRecipeIndex = 0 ;
        if(mRecipeIndex < 0){
            finish();
        }
        //TODO: here you gotta fetch the data.
        mViewPager = findViewById(R.id.recipe_step_fragment_view_pager);
        mStepNumberTextView = findViewById(R.id.recipe_step_number_text_view);
        mPagerAdapter = new RecipeStepDetailPagerAdapter(getSupportFragmentManager());
        mRecipe = RecipeRepo.getInstance(this).getRecipe(mRecipeIndex);
        mPagerAdapter.updateData(mRecipe.getRecipeSteps());

        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(mCurrentStep, false);
        updateScrollViews();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_STEP, mCurrentStep);
    }

    public static Intent createIntent(int recipeIndex, int stepIndex){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tpourjalali.topoven", RecipeStepDetailActivity.class.getName()));
        intent.putExtra(INTENT_EXTRA_RECIPE_INDEX, recipeIndex);
        intent.putExtra(KEY_CURRENT_STEP, stepIndex);
        return intent;
    }
    private void setValuesFromBundle(@Nullable Bundle bundle){
        if(bundle == null) return ;
        if(bundle.containsKey(INTENT_EXTRA_RECIPE_INDEX)){
            mRecipeIndex = bundle.getInt(INTENT_EXTRA_RECIPE_INDEX, -1);
        }
        if(bundle.containsKey(KEY_CURRENT_STEP))
            mCurrentStep = bundle.getInt(KEY_CURRENT_STEP);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentStep = position;
        updateScrollViews();
    }

    private void updateScrollViews() {
        if(mPrevButton != null && mNextButton != null) {
            if (mCurrentStep == mPagerAdapter.getCount() - 1) {
                mNextButton.setVisibility(View.GONE);
            } else {
                mNextButton.setVisibility(View.VISIBLE);
            }
            if (mCurrentStep == 0) {
                mPrevButton.setVisibility(View.GONE);
            } else {
                mPrevButton.setVisibility(View.VISIBLE);
            }
        }
        if(mStepNumberTextView != null){
            mStepNumberTextView.setText(getString(R.string.pageview_page_number, mCurrentStep +1, mPagerAdapter.getCount()));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View v) {
        //for previous and next button clicks
        switch(v.getId()){
            case R.id.previous_page_image_button:
                mViewPager.arrowScroll(View.FOCUS_BACKWARD);
                break;
            case R.id.next_page_image_button:
                mViewPager.arrowScroll(View.FOCUS_FORWARD);
            default:
        }
    }
}
