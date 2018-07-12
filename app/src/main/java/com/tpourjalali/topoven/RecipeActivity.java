
package com.tpourjalali.topoven;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * fetch the recipe from the repo, check if it has video and if we are connected to internet, if so, stream the video, else, disable the player.
 */
public class RecipeActivity extends AppCompatActivity implements RecipeStepDetailFragment.CallBacks, RecipeStepListFragment.CallBacks {

    private boolean mTwoPane = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_recipe);

        if(findViewById(R.id.container_layout) == null){
            //mobile device, only have the RecipeStepListFragment
            mTwoPane = false;
        } else {
            //tablet, have RecipeStepListFragment and RecipeStepDetailFragment
            mTwoPane = true;
        }


    }
}
