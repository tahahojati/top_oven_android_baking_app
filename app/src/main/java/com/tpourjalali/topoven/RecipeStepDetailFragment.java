package com.tpourjalali.topoven;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerView;

public class RecipeStepDetailFragment extends Fragment{
    public PlayerView mPlayerView;
    public TextView mRecipeStepDetailTextView;
    public CardView mRecipeStepDetailCardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        mPlayerView = v.findViewById(R.id.player_view);
        mRecipeStepDetailCardView =  v.findViewById(R.id.recipe_step_detail_cardview);
        mRecipeStepDetailTextView = v.findViewById(R.id.recipe_step_details_textview);
        return v;
    }

    public interface CallBacks{

    }
}
