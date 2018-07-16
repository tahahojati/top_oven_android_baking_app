package com.tpourjalali.topoven.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable{
    //TODO: remove all of these defaults.
    @SerializedName("name")
    private String mName = "Nutella Pie";
    @SerializedName("servings")
    private int mServings = 8;
    @SerializedName("image")
    private String mImage = "" ;
    @SerializedName("steps")
    private List<RecipeStep> mRecipeSteps = new ArrayList<>(3);
    @SerializedName("ingredients")
    private List<Ingredient> mIngredients = new ArrayList<>(2);
    public Recipe(){
        mRecipeSteps.add(new RecipeStep());
        mRecipeSteps.add(new RecipeStep());
        mRecipeSteps.add(new RecipeStep());
        mIngredients.add(new Ingredient());
        mIngredients.add(new Ingredient());
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getServings() {
        return mServings;
    }

    public void setServings(int servings) {
        mServings = servings;
    }
    @Nullable
    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    @NonNull
    public List<RecipeStep> getRecipeSteps() {
        return mRecipeSteps;
    }

    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        mRecipeSteps = recipeSteps;
    }

    @NonNull
    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public static class Ingredient implements Serializable {
        @SerializedName("quantity")
        private int mQuantity = 3;
        @SerializedName("measure")
        private String mMeasure = "CUP";
        @SerializedName("ingredient")
        private String mIngredint;

        public int getQuantity() {
            return mQuantity;
        }

        public void setQuantity(int quantity) {
            mQuantity = quantity;
        }

        public String getMeasure() {
            return mMeasure;
        }

        public void setMeasure(String measure) {
            mMeasure = measure;
        }

        public String getIngredint() {
            return mIngredint;
        }

        public void setIngredint(String ingredint) {
            mIngredint = ingredint;
        }
    }
    public static class RecipeStep implements Serializable {
        private int mId = 0;
        private String mShortDescription ="Recipe Introduction" ;
        private String mDescription = "Recipe Introduction";
        private String mVideoUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";
        private String mThumbnailUrl = "";

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public String getShortDescription() {
            return mShortDescription;
        }

        public void setShortDescription(String shortDescription) {
            mShortDescription = shortDescription;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        public String getVideoUrl() {
            return mVideoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            mVideoUrl = videoUrl;
        }

        public String getThumbnailUrl() {
            return mThumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            mThumbnailUrl = thumbnailUrl;
        }
    }
}
