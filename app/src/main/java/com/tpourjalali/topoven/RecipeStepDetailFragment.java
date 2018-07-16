package com.tpourjalali.topoven;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player.DefaultEventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.tpourjalali.topoven.model.Recipe;

import okhttp3.Call;
import okhttp3.internal.Util;

public class RecipeStepDetailFragment extends Fragment implements FragmentHideListener{
    private static final String TAG = RecipeStepDetailFragment.class.getName();
    private Recipe.RecipeStep mRecipeStep = null;
    private DefaultEventListener mListener = new DefaultEventListener(){

    };
    private long mCurrentVideoPosition = 0;

    private static final String KEY_RECIPE_STEP = "recipe_step";
    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_CURRENT_POSITION = "current_playback_position";
    public PlayerView mPlayerView;
    public TextView mRecipeStepDetailTextView;
    public CardView mRecipeStepDetailCardView;
    private SimpleExoPlayer mExoPlayer;
    private boolean mPlayWhenReady = false;
    private CallBacks mCallBacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
        if(context instanceof CallBacks){
            mCallBacks = (CallBacks) context;
        } else {
            throw new RuntimeException("Housing Activity must implement Callbacks Interface");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        mCallBacks = null;
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFromBundle(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(KEY_PLAY_WHEN_READY, mPlayWhenReady);
        outState.putLong(KEY_CURRENT_POSITION, mExoPlayer.getCurrentPosition());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate View" );
        View v = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        mPlayerView = v.findViewById(R.id.player_view);
        mRecipeStepDetailCardView =  v.findViewById(R.id.recipe_step_detail_cardview);
        mRecipeStepDetailTextView = v.findViewById(R.id.recipe_step_details_textview);

        initFromBundle(getArguments());
        if(mRecipeStep == null){
            throw new RuntimeException("You must provide a recipe step as argument to this fragment");
        }
        if(mRecipeStepDetailTextView != null)
            mRecipeStepDetailTextView.setText(mRecipeStep.getDescription());
        return v;
    }

    private void initFromBundle(@Nullable Bundle arguments) {
        if(arguments == null)
            return;
        if(arguments.containsKey(KEY_RECIPE_STEP))
            mRecipeStep = (Recipe.RecipeStep) arguments.getSerializable(KEY_RECIPE_STEP);
        if(arguments.containsKey(KEY_PLAY_WHEN_READY))
            mPlayWhenReady = arguments.getBoolean(KEY_PLAY_WHEN_READY);
        if(arguments.containsKey(KEY_CURRENT_POSITION))
            mCurrentVideoPosition = arguments.getLong(KEY_CURRENT_POSITION);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        if(Build.VERSION.SDK_INT > 23){
            initializeVideoPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if(Build.VERSION.SDK_INT <= 23){
            initializeVideoPlayer();
        }
        onShow();
    }

    @Override
    public void onPause() {
        onHide();
        if(Build.VERSION.SDK_INT <= 23){
            releaseVideoPlayer();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if(Build.VERSION.SDK_INT > 23){
            releaseVideoPlayer();
        }
        super.onStop();
    }

    private void initializeVideoPlayer() {
        String url = mRecipeStep.getVideoUrl();
        if(url == null ) {
            mPlayerView.setVisibility(View.GONE);
            mRecipeStepDetailTextView.setVisibility(View.VISIBLE);
            return;
        }
        Uri videoUri = Uri.parse(url);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()), new DefaultTrackSelector(), new DefaultLoadControl());
        mPlayerView.setPlayer(mExoPlayer);
        MediaSource mediaSource = buildMediaSource(videoUri);
        mExoPlayer.prepare(mediaSource, true, false);
        mExoPlayer.seekTo(mCurrentVideoPosition);
        Log.d(TAG, "Play when ready: "+ mPlayWhenReady);
        mExoPlayer.addListener(new DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                mPlayWhenReady = playWhenReady;
                Log.d(TAG, "Video State listener, playwhen ready: "+mPlayWhenReady);
            }
        });
        mExoPlayer.setPlayWhenReady(mPlayWhenReady);

    }

    private MediaSource buildMediaSource(@NonNull Uri videoUri) {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("exoplayer"))
                .createMediaSource(videoUri);
    }

    private void releaseVideoPlayer() {
        if(mExoPlayer != null)
            mExoPlayer.release();
    }

    public static RecipeStepDetailFragment newInstance(@NonNull Recipe.RecipeStep recipeStep) {
        if(recipeStep == null)
            return null;
        Bundle args = new Bundle();
        args.putSerializable(KEY_RECIPE_STEP, recipeStep);
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onHide() {
        if (mExoPlayer != null){
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onShow() {
        if(mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
        }
    }

    public interface CallBacks{

    }

}
