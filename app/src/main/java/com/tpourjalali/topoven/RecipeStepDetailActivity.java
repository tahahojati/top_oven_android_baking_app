package com.tpourjalali.topoven;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RecipeStepDetailActivity extends AppCompatActivity implements RecipeStepDetailFragment.CallBacks {
    private ViewPager mViewPager;
    private ImageButton mNextButton, mPrevButton;
    private TextView mStepNumberTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_recipe_step_detail);

        mNextButton = findViewById(R.id.next_page_image_button);
        mPrevButton = findViewById(R.id.previous_page_image_button);
        mViewPager = findViewById(R.id.recipe_step_fragment_view_pager);
        mStepNumberTextView = findViewById(R.id.recipe_step_number_text_view);

    }
}
