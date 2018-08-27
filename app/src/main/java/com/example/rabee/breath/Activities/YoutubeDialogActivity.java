package com.example.rabee.breath.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.rabee.breath.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeDialogActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {
    String API_KEY = "AIzaSyCHH7wadSVxjRklA5ebZHLIofaiYj_pgeE";
    String video_id = "";
    private YouTubePlayerView youTubePlayerFragment;
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };
    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {


        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_dialog_activiy);
        Bundle b = getIntent().getExtras();
        String youtubeSongUrl = "";
        if (b != null) {
            youtubeSongUrl = b.getString("youtubeSongUrl");
            if (youtubeSongUrl != null) {
                String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
                Pattern compiledPattern = Pattern.compile(pattern);
                Matcher matcher = compiledPattern.matcher(youtubeSongUrl);
                if (matcher.find()) {
                    video_id = matcher.group();
                }
            }
        }
        youTubePlayerFragment = (YouTubePlayerView) findViewById(R.id.youtubeplayerfragment);
        youTubePlayerFragment = new YouTubePlayerView(YoutubeDialogActivity.this);
        youTubePlayerFragment.initialize(API_KEY, YoutubeDialogActivity.this);
        addContentView(youTubePlayerFragment, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        youTubePlayerFragment.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.cueVideo(video_id);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
