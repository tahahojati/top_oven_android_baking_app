package com.tpourjalali.topoven;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.tpourjalali.topoven.model.Recipe;
import com.tpourjalali.topoven.model.RecipeRepo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {
    private static final String RECIPE_CLICK_ACTION = "recipe Click";
    private static final String TAG = BakingWidgetProvider.class.getName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(TAG, "updateAppWidget was called");
        RecipeRepo repo = RecipeRepo.getInstance(context);
        if(!repo.isInitialized())
            repo.updateRepo(false);
        // Construct the RemoteViews object
        Recipe recipe = repo.getLastRecipe();
        List<String> ingredientList = recipe.getIngredients().stream().map(x -> x.getIngredint()).collect(Collectors.toList());

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        if(recipe == null){
            views.setViewVisibility(R.id.no_recipe_error_tv, View.VISIBLE);
            views.setViewVisibility(R.id.tv_widget_ingredient_list, View.GONE);
        } else {
            views.setViewVisibility(R.id.no_recipe_error_tv, View.GONE);
            views.setViewVisibility(R.id.tv_widget_ingredient_list, View.VISIBLE);
            CharSequence cs = TextUtils.join("\n", ingredientList);
            views.setTextViewText(R.id.tv_widget_ingredient_list, cs);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() == RECIPE_CLICK_ACTION){
            Log.d(TAG, "Recipe was clicked!");
        } else
            super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

