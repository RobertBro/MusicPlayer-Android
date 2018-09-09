package com.example.kaerith.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.kaerith.myapplication.MusicListFragment;
import com.example.kaerith.myapplication.MusicPlayerFragment;
import com.example.kaerith.myapplication.MusicPlaylist;
import com.example.kaerith.myapplication.PagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity{

    private Context mContext;
    @BindView(R.id.view_pager_music_player)
    ViewPager mPager;
    @BindView(R.id.tab_layout_music_player)
    TabLayout mTabLayout;

    private final static int PERMISSION_REQUEST_CODE = 1;

    MusicPlaylist mMusicPlaylist;
    MusicListFragment mMusicListFragment;
    MusicPlayerFragment mMusicPlayerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(savedInstanceState != null) {
            mMusicPlaylist = savedInstanceState.getParcelable(MusicPlayerFragment.MUSIC_LIST);
        }
        else {
            mMusicPlaylist = new MusicPlaylist();
        }

        mMusicPlayerFragment = new MusicPlayerFragment(mMusicPlaylist);
        mMusicListFragment = new MusicListFragment(mMusicPlaylist);
        mMusicListFragment.setmMusicPlayerFragment(mMusicPlayerFragment);

        if(isStoragePermissionGranted()) {
            initComponents();
        }
        else {
            Toast.makeText(this, "You have to grant permissions.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }

    private void grantPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //resume tasks needing this permission
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MusicPlayerFragment.MUSIC_LIST, mMusicPlaylist);
    }

    private void initComponents() {
        mContext = this;
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), mContext, mMusicListFragment,mMusicPlayerFragment);
        mPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mPager);
    }

}
