package com.tpourjalali.topoven;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

/**
 * Get the recipelist from the repo class, create an adapter to display it.
 * delegate to RecipeActivity()
 */
public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        initViewFields();
    }

    private void initViewFields() {
        mRecyclerView = findViewById(R.id.recycler_view);
    }
}
