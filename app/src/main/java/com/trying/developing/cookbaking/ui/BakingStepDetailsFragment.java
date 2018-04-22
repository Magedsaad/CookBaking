package com.trying.developing.cookbaking.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.trying.developing.cookbaking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BakingStepDetailsFragment extends Fragment {

    String videoURL, Description, thumbnailURL;
    TextView textDescription;
    ImageView thumbnailURLImageView;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private int currentWindow;
    private long playbackPosition;
    private BandwidthMeter bandwidthMeter;
    boolean landscapeMood;

    public BakingStepDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_baking_step_details, container, false);

        simpleExoPlayerView = (SimpleExoPlayerView) v.findViewById(R.id.player_view);
        textDescription = (TextView) v.findViewById(R.id.textDescription);
        thumbnailURLImageView = (ImageView) v.findViewById(R.id.thiumbnailURLImageView);
        landscapeMood = getResources().getBoolean(R.bool.landscapeMood);

        Bundle sentBundle = getArguments();

        videoURL = sentBundle.getString("videoURL");
        Description = sentBundle.getString("Description");
        thumbnailURL = sentBundle.getString("thumbnailURL");

        shouldAutoPlay = sentBundle.getBoolean("shouldAutoPlay");
        currentWindow = sentBundle.getInt("currentWindow");
        playbackPosition = sentBundle.getLong("playbackPosition",-1);




        if (savedInstanceState != null) {
            shouldAutoPlay = savedInstanceState.getBoolean("shouldAutoPlay");
            currentWindow = savedInstanceState.getInt("currentWindow");
        }

        if (!thumbnailURL.isEmpty())
            Picasso.with(getActivity()).load(thumbnailURL).into(thumbnailURLImageView);

        textDescription.append("\n" + Description);
        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();

        return v;
    }

    private void initializePlayer() {


            simpleExoPlayerView.requestFocus();

            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);

            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

            simpleExoPlayerView.setPlayer(player);

            player.setPlayWhenReady(shouldAutoPlay);
            if (playbackPosition == -1) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
                playbackPosition = sharedPreferences.getLong("pos", 0);
                Log.i("Taaag", "seek" + playbackPosition);
                player.seekTo(currentWindow, playbackPosition);
            }
            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
                    mediaDataSourceFactory, extractorsFactory, null, null);

            player.prepare(mediaSource, true, false);

    }
    private void releasePlayer() {

            if (player != null) {
                playbackPosition = player.getCurrentPosition();
                Log.d("Taaag", "dasd" + playbackPosition + "");
                currentWindow = player.getCurrentWindowIndex();
                shouldAutoPlay = player.getPlayWhenReady();
                player.release();
                player = null;
                trackSelector = null;
            }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Tag","Resume");
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {

        Log.i("Taaag","pause"+playbackPosition+"");
        if (Util.SDK_INT <= 23) {
            Log.i("Taaag","if"+playbackPosition+"");
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("shouldAutoPlay", shouldAutoPlay);
        outState.putInt("currentWindow", currentWindow);
        outState.putLong("playbackPosition", playbackPosition);
        if (Util.SDK_INT <= 23) {
            Log.i("Taaaag","if"+playbackPosition+"");
            releasePlayer();
        }
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("shared",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong("pos",playbackPosition);
        editor.commit();

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}
