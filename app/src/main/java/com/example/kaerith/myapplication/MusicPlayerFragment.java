package com.example.kaerith.myapplication;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.AudioManager;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.io.IOException;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


public class MusicPlayerFragment extends Fragment implements OnCompletionListener {


    MusicPlaylist mMusicPlaylist;
    public static final String MUSIC_LIST = "playlist";

    private MusicPlayerAdapter mMusicAdapter;

    @BindView(com.example.kaerith.myapplication.R.id.playlist_view_music)
    ListView mListViewPlaylist;

    public MusicPlayerFragment() {
        super();
    }

    public MusicPlayerFragment(MusicPlaylist musicPlaylist) {
        super();
        mMusicPlaylist = musicPlaylist;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

    }
    File[] mfiles;
    private Button button,btnNext,btnPlay,btnBackward,btS,btR,btnLast;
    private double startTime = 0;
    private double finalTime = 0;
    private MediaPlayer mediaPlayer;
    private Handler mHandler = new Handler();;
    private SeekBar songProgressBar;
    private TextView tx1,tx2,tx3,tx4;
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private Utilities utils;
    private boolean isPause=false;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MUSIC_LIST, mMusicPlaylist);
    }
    private class yourListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) {
                mediaPlayer.seekTo(progress);
                seekBar.setProgress(progress);
            }
        }
        /**
         * When user starts moving the progress handler
         * */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // remove message Handler from updating progress bar

        }

        /**
         * When user stops moving the progress hanlder
         * */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.removeCallbacks(mUpdateTimeTask);
            int totalDuration = mediaPlayer.getDuration();
            int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

            // forward or backward to certain seconds
            mediaPlayer.seekTo(currentPosition);

            // update timer progress again
            updateProgressBar();
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(com.example.kaerith.myapplication.R.layout.fragment_music_player, container, false);
        button = (Button)fragmentView.findViewById(com.example.kaerith.myapplication.R.id.btnForward);
        btnNext = (Button)fragmentView.findViewById(com.example.kaerith.myapplication.R.id.btnNext);
        btnPlay = (Button)fragmentView.findViewById(com.example.kaerith.myapplication.R.id.btnPlay);
        btnBackward = (Button)fragmentView.findViewById(com.example.kaerith.myapplication.R.id.btnBackward);
        btnLast = (Button)fragmentView.findViewById(com.example.kaerith.myapplication.R.id.btnLast );
        btR = (Button)fragmentView.findViewById(com.example.kaerith.myapplication.R.id.btR);
        btS = (Button)fragmentView.findViewById(com.example.kaerith.myapplication.R.id.btS);
      //  mfiles = mMusicPlaylist.getFiles().toArray(new File[mMusicPlaylist.getFiles().size()]);
        tx2 = (TextView)fragmentView.findViewById(com.example.kaerith.myapplication.R.id.tx2);
        tx3 = (TextView)fragmentView.findViewById(com.example.kaerith.myapplication.R.id.tx3);
        tx4 = (TextView)fragmentView.findViewById(com.example.kaerith.myapplication.R.id.textView4);

        mediaPlayer = new MediaPlayer();
       // songProgressBar.setOnSeekBarChangeListener(new yourListener());
        mediaPlayer.setOnCompletionListener(this);
        utils = new Utilities();
        songProgressBar = (SeekBar)fragmentView.findViewById(com.example.kaerith.myapplication.R.id.seekBar);
        songProgressBar.setOnSeekBarChangeListener(new yourListener());
       // seekbar.setClickable(false);
       // btnNext.setEnabled(false);


        ButterKnife.bind(this, fragmentView);
        if (savedInstanceState != null) {
            mMusicPlaylist = savedInstanceState.getParcelable(MUSIC_LIST);
        }

        loadPlaylist();

        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    mediaPlayer.seekTo(0);
                }

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if(isShuffle) {
                    // shuffle is on - play a random song
                    Random rand = new Random();

                        currentSongIndex = rand.nextInt((mMusicPlaylist.getFiles().size() - 1) - 0 + 1) + 0;
                        PlaySong(currentSongIndex);
                }
                else{
                    //play next song
                    if (currentSongIndex < (mMusicPlaylist.getFiles().size() - 1)) {
                        PlaySong(currentSongIndex + 1);
                        currentSongIndex = currentSongIndex + 1;
                    } else {
                        // play first song
                        PlaySong(0);
                        currentSongIndex = 0;
                    }
                }
                isPause=false;
            }
        });

        btnLast.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(currentSongIndex > 0){
                    PlaySong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                }else{
                    // play last song
                    PlaySong(mMusicPlaylist.getFiles().size() - 1);
                    currentSongIndex = mMusicPlaylist.getFiles().size() - 1;
                }
                isPause=false;

            }

        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition + seekForwardTime <= mediaPlayer.getDuration()){
                    // forward song
                    mediaPlayer.seekTo(currentPosition + seekForwardTime);
                }else{
                    // forward to end position
                    mediaPlayer.seekTo(mediaPlayer.getDuration());
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(isPause==false && mediaPlayer.isPlaying()) {
                    btnPlay.setText(">");
                    mediaPlayer.pause();
                    songProgressBar.setProgress(0);
                    songProgressBar.setMax(100);
                    // Updating progress bar
                    updateProgressBar();
                    isPause=true;
                }
                else if(isPause==true){
                    btnPlay.setText("||");
                    mediaPlayer.start();
                    songProgressBar.setProgress(0);
                    songProgressBar.setMax(100);
                    // Updating progress bar
                    updateProgressBar();
                    isPause=false;
                }
                else if(isPause==false && mediaPlayer.isPlaying()==false)
                {
                    btnPlay.setText("||");
                    PlaySong(0);
                    songProgressBar.setProgress(0);
                    songProgressBar.setMax(100);
                    // Updating progress bar
                    updateProgressBar();
                    isPause=false;
                }
            }
        });

        btR.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isRepeat){
                    isRepeat = false;
                    Toast.makeText(getActivity().getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();

                }else{
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getActivity().getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;

                }
            }
        });

        btS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(isShuffle){
                    isShuffle = false;
                    Toast.makeText(getActivity().getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();

                }else{
                    // make repeat to true
                    isShuffle= true;
                    Toast.makeText(getActivity().getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;

                }
            }
        });


        return fragmentView;
    }

    public void loadPlaylist() {

        if (mMusicPlaylist != null) {
            if (!mMusicPlaylist.getFiles().isEmpty()) {

                List<File> files = mMusicPlaylist.getFiles();
                mMusicAdapter = new MusicPlayerAdapter(files.toArray(new File[files.size()]));
                mListViewPlaylist.setAdapter(mMusicAdapter);
            }
        }
    }



    @OnItemClick(com.example.kaerith.myapplication.R.id.playlist_view_music)
    public void onListViewMusicItemClick(int position) {
        currentSongIndex=position;
        PlaySong(position);
    //    songProgressBar.setProgress(0);
      //
        int duration =mediaPlayer.getDuration();
        updateProgressBar();

    isPause=false;
    }
    /**
     * Update timer on seekbar
     * */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            int Duration = mediaPlayer.getDuration();
            //mFileDuration = mediaPlayer
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            tx3.setText(""+utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            tx2.setText(""+utils.milliSecondsToTimer(currentDuration));
            tx4.setText(""+mfiles[currentSongIndex].getName());
            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
           // Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };


    @Override
    public void onCompletion(MediaPlayer arg0) {

        // check for repeat is ON or OFF
        if(isRepeat){
            // repeat is on play same song again
            PlaySong(currentSongIndex);
        } else if(isShuffle){
            // shuffle is on - play a random song
           Random rand = new Random();
            currentSongIndex = rand.nextInt((mMusicPlaylist.getFiles().size() - 1) - 0 + 1) + 0;
            PlaySong(currentSongIndex);
        } else{
            // no repeat or shuffle ON - play next song
            if(currentSongIndex < (mMusicPlaylist.getFiles().size() - 1)){
                PlaySong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            }else{
                // play first song
                           PlaySong(0);
                currentSongIndex = 0;
            }
        }
    }


    public void PlaySong(int position){
     try {

       mfiles = mMusicPlaylist.getFiles().toArray(new File[mMusicPlaylist.getFiles().size()]);
    mediaPlayer.reset();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    mediaPlayer.setDataSource(mfiles[position].getPath());

    mediaPlayer.prepare();
    mediaPlayer.start();
    songProgressBar.setProgress(0);
     songProgressBar.setMax(100);
    // Updating progress bar
   //     updateProgressBar();
         btnPlay.setText("||");

} catch (IllegalArgumentException e) {
        e.printStackTrace();
        } catch (IllegalStateException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.release();
    }
}
