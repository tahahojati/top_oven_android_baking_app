package com.tpourjalali.topoven.helpers;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tpourjalali.topoven.RecipeStepDetailFragment;
import com.tpourjalali.topoven.model.Recipe;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO: Implement class methods
 */
public class RecipeStepDetailPagerAdapter extends FragmentStatePagerAdapter{

    private List<Recipe.RecipeStep> mStepsList = new ArrayList<>();
    private ArrayList<WeakReference<RecipeStepDetailFragment>> mFragments;

    public RecipeStepDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecipeStepDetailFragment frag = (RecipeStepDetailFragment) super.instantiateItem(container, position);
        //make sure array list has enough items:
        mFragments.set(position, new WeakReference<>(frag));
        return frag;
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
        ArrayList<Recipe.RecipeStep> temp = new ArrayList<>();
        if(recipeStepList != null)
            temp.addAll(recipeStepList);
        reallocateFragmentList(temp.size());
        mStepsList = temp;
        notifyDataSetChanged();
    }

    private void reallocateFragmentList(int size) {
        mFragments = new ArrayList<>(size);
        for(int i =0; i < size; ++i)
            mFragments.add(null);
    }

    @Nullable
    public WeakReference<RecipeStepDetailFragment> getFragment(int index) {
        return mFragments.get(index);
    }
}
