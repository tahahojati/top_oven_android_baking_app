package com.tpourjalali.topoven;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tpourjalali.topoven.model.Recipe;
import com.tpourjalali.topoven.model.RecipeRepo;

import java.util.List;

/**
 * Get the recipelist from the repo class, create an adapter to display it.
 * delegate to RecipeActivity()
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<Recipe> mRecipeList;
    private RecipeRepo mRecipeRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        initViewFields();
        mRecipeRepo = RecipeRepo.getInstance(this);
        mRecipeList = mRecipeRepo.getRecipeList();

    }

    private void initViewFields() {
        mRecyclerView = findViewById(R.id.recycler_view);
    }
}
