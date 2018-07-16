package com.tpourjalali.topoven.helpers;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tpourjalali.topoven.RecipeStepDetailFragment;
import com.tpourjalali.topoven.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Implement class methods
 */
public class RecipeStepDetailPagerAdapter extends FragmentStatePagerAdapter{

    private List<Recipe.RecipeStep> mStepsList = new ArrayList<>();

    public RecipeStepDetailPagerAdapter(FragmentManager fm) {
        super(fm);
        isViewFromObject()
    }

    @Override
    public Fragment getItem(int position) {
        return RecipeStepDetailFragment.newInstance(mStepsList.get(position));
    }

    @Override
    public int getCount() {
        return mStepsList.size();
    }

    public void updateData(@Nullable List<Recipe.RecipeStep> recipeStepList) {
        mStepsList = recipeStepList;
        notifyDataSetChanged();
    }
}
