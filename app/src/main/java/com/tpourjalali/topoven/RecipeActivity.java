
package com.tpourjalali.topoven;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
