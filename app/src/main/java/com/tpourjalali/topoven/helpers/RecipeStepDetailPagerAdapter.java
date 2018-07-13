package com.tpourjalali.topoven.helpers;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tpourjalali.topoven.model.Recipe;

import java.util.List;

/**
 * TODO: Implement class methods
 */
public class RecipeStepDetailPagerAdapter extends FragmentStatePagerAdapter{

    public RecipeStepDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    public void updateData(@Nullable List<Recipe.RecipeStep> recipeStepList) {

    }
}
