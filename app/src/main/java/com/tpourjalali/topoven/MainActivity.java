package com.tpourjalali.topoven;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tpourjalali.topoven.model.Recipe;
import com.tpourjalali.topoven.model.RecipeRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Get the recipelist from the repo class, create an adapter to display it.
 * delegate to RecipeActivity()
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private static final String TAG = "MainActivity";
    private List<Recipe> mRecipeList;
    private RecipeRepo mRecipeRepo;
    private RecipeAdapter mAdapter = new RecipeAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        initViewFields();
        mRecipeRepo = RecipeRepo.getInstance(this);
        mRecipeList = mRecipeRepo.getRecipeList();
        if(mRecipeList.size()  == 0) {
            Toast.makeText(this, getString(R.string.error_norecipe_toast), Toast.LENGTH_LONG)
                .show();
            finish();
        }
        configureRecyclerView();
    }

    private void configureRecyclerView() {
        DisplayMetrics dm = getApplicationContext()
                .getResources().getDisplayMetrics();
        float screenWidth = dm.widthPixels / dm.xdpi;
        float screenHeight = dm.heightPixels / dm.ydpi;
        Log.d(TAG, "Device dimensions: "+screenWidth + " "+screenHeight);
        float min = Math.min(screenWidth, screenHeight);
        if(min > 600){
            //tablet.
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            //cellphone
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
        mAdapter.updateData(mRecipeList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initViewFields() {
        mRecyclerView = findViewById(R.id.recycler_view);
    }
    private void recipeClicked(int itemPosition) {
        mRecipeRepo.setLastViewedRecipeIndex(itemPosition);
        Intent intent = RecipeActivity.createIntent(itemPosition);
        startActivity(intent);
    }

    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Recipe mRecipe;
        private CardView mCardView;
        private TextView mRecipeTitleTextView;
        private int mItemPosition;
        public RecipeHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_view);
            mRecipeTitleTextView = itemView.findViewById(R.id.recipe_title);
            itemView.setOnClickListener(this);
        }
        public void bind(Recipe recipe, int position){
            mRecipe = recipe;
            mItemPosition = position;
            mRecipeTitleTextView.setText(mRecipe.getName());
        }

        @Override
        public void onClick(View v) {
            recipeClicked(mItemPosition);
        }
    }



    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder>{
        private ArrayList<Recipe> mRecipes = new ArrayList<>();
        @NonNull
        @Override
        public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater li = getLayoutInflater();
            View v = li.inflate(R.layout.recipe_list_item, parent, false);
            return  new RecipeHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
            Log.d(TAG, "mRecipes size: "+mRecipeList.size() +" position: "+position);
            holder.bind(mRecipeList.get(position), position);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "getItemCount returing: "+mRecipeList.size());
            return mRecipeList.size();
        }

        public void updateData(@Nullable List<Recipe> recipeList) {
            if(recipeList == null || recipeList.size() == 0)
                return;
            mRecipeList = new ArrayList<>(recipeList);
            notifyDataSetChanged();
        }
    }
}
