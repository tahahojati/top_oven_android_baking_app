package com.tpourjalali.topoven;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tpourjalali.topoven.model.Recipe;
import com.tpourjalali.topoven.model.Recipe.RecipeStep;
import com.tpourjalali.topoven.model.RecipeRepo;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepListFragment extends Fragment{
    private static final String KEY_RECIPE_INDEX = "recipe_index";
    private static final String KEY_SCROLL_POSITION = "scroll_position";

    private int mScrollPosition = 0;
    private int mRecipeIndex = -1;
    private RecipeRepo mRepo;
    private Recipe mRecipe;
    private RecyclerView mRecyclerView;
    private CallBacks mCallBacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof CallBacks){
            mCallBacks = (CallBacks) context;
        } else {
            throw new RuntimeException("Housing Activity must implement Callbacks Interface");
        }
    }

    @Override
    public void onDetach() {
        mCallBacks = null;
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCROLL_POSITION, mScrollPosition);
        outState.putInt(KEY_RECIPE_INDEX, mRecipeIndex);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setFromBundle(getArguments());
        setFromBundle(savedInstanceState);
        if(mRecipeIndex == -1)
            throw new RuntimeException("Recipe Index must be supplied in arguments");
        mRepo = RecipeRepo.getInstance(getActivity());
        View v = inflater.inflate(R.layout.recycler_view, container, false);

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(new StepAdapter(mRepo.getRecipe(mRecipeIndex).getRecipeSteps()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return v;
    }

    private void setFromBundle(@Nullable Bundle bundle){
        if(bundle == null)
            return;
        if(bundle.containsKey(KEY_RECIPE_INDEX))
            mRecipeIndex = bundle.getInt(KEY_RECIPE_INDEX);
        if(bundle.containsKey(KEY_SCROLL_POSITION))
            mScrollPosition = bundle.getInt(KEY_SCROLL_POSITION);
    }

    public static RecipeStepListFragment newInstance(int recipeIndex) {

        Bundle args = new Bundle();
        args.putInt(KEY_RECIPE_INDEX, recipeIndex);
        RecipeStepListFragment fragment = new RecipeStepListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public interface CallBacks {
        void onStepSelected(int stepNumber);
    }
    private class StepHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mRecipeTextView;
        private CardView mCardView;
        private RecipeStep mRecipeStep;
        private int mRecipeStepIndex = -1;

        public StepHolder(View itemView) {
            super(itemView);
            mRecipeTextView = itemView.findViewById(R.id.recipe_step);
            mCardView = itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(this);
            if(mCardView == null || mRecipeTextView == null){
                throw new RuntimeException("RecipeStep Holder require recipe_step_list item xml views");
            }
        }

        @Override
        public void onClick(View v) {
            mCallBacks.onStepSelected(mRecipeStepIndex);
        }

        public void bind(RecipeStep recipeStep, int recipeStepIndex) {
            mRecipeStep = recipeStep;
            mRecipeStepIndex = recipeStepIndex;
            mRecipeTextView.setText(mRecipeStep.getShortDescription());
        }
    }
    private class StepAdapter extends RecyclerView.Adapter<StepHolder>{

        private ArrayList<RecipeStep> mSteps;
        public StepAdapter(List<RecipeStep> steps){
            mSteps = new ArrayList<>(steps);
        }
        public void updateData(List<RecipeStep> steps){
            mSteps = new ArrayList<>(steps);
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return new StepHolder(li.inflate(R.layout.recipe_step_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull StepHolder holder, int position) {
            holder.bind(mSteps.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }
    }
}
