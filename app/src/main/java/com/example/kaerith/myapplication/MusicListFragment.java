package com.example.kaerith.myapplication;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


public class MusicListFragment extends Fragment {

    @BindView(com.example.kaerith.myapplication.R.id.list_view_music)
    ListView mListViewFiles;

    MusicListAdapter mMusicAdapter;
    String mCurrentDirPath;

    private final static String[] MUSIC_EXTENSIONS = {".mp3", ".wav", ".flac", ".m4a", ".ogg", ".mid"};

    MusicPlaylist mMusicPlaylist;

    public void setmMusicPlayerFragment(MusicPlayerFragment mMusicPlayerFragment) {
        this.mMusicPlayerFragment = mMusicPlayerFragment;
    }

    private MusicPlayerFragment mMusicPlayerFragment;

    public MusicListFragment() {
        super();
    }

    public MusicListFragment(MusicPlaylist musicPlaylist) {
        super();
        mMusicPlaylist = musicPlaylist;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MusicPlayerFragment.MUSIC_LIST, mMusicPlaylist);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(com.example.kaerith.myapplication.R.layout.fragment_music_list, container, false);
        ButterKnife.bind(this, fragmentView);

        mCurrentDirPath = (Environment.getExternalStorageDirectory().getPath());
        loadFiles(new File(mCurrentDirPath));

        if(savedInstanceState != null) {
            mMusicPlaylist = savedInstanceState.getParcelable(MusicPlayerFragment.MUSIC_LIST);
        }

        return fragmentView;
    }

    @OnItemClick(com.example.kaerith.myapplication.R.id.list_view_music)
    public void onListViewMusicItemClick(int position) {
        File file = (File) mMusicAdapter.getItem(position);

        if (file.getName() == getResources().getString(com.example.kaerith.myapplication.R.string.parent_folder)) {

            mCurrentDirPath = mCurrentDirPath.substring(0, mCurrentDirPath.lastIndexOf("/"));
            loadFiles(new File(mCurrentDirPath));

        } else if (file.isDirectory() && file.canRead()) {

            mCurrentDirPath += "/" + file.getName();
            loadFiles(new File(mCurrentDirPath));
        } else  {
            mMusicPlaylist.add(file);
            if(mMusicPlayerFragment != null) {
                mMusicPlayerFragment.loadPlaylist();
            }
        }
    }

    private void loadFiles(File dir) {
        File[] files = filterDirsAndMusic(dir);

        File[] allFiles;

        if (mCurrentDirPath.equals(Environment.getExternalStorageDirectory().getPath())) {
            allFiles = files;
        } else {
            allFiles = new File[1 + files.length];
            allFiles[0] = new File(getResources().getString(com.example.kaerith.myapplication.R.string.parent_folder));

            for (int i = 0; i < files.length; i++) {
                allFiles[i + 1] = files[i];
            }
        }

        mMusicAdapter = new MusicListAdapter(allFiles);
        mListViewFiles.setAdapter(mMusicAdapter);
    }

    private File[] filterDirsAndMusic(File dir) {
        File[] temp = dir.listFiles();
        List<File> dirsAndMusic = new ArrayList<>();

        for (File file : temp) {
            if (isMusic(file) || file.isDirectory()) {
                dirsAndMusic.add(file);
            }
        }

        return dirsAndMusic.toArray(new File[0]);
    }

    public static boolean isMusic(File file) {
        for (String extension : MUSIC_EXTENSIONS) {
            if (file.getName().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
