package com.example.kaerith.myapplication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {

    Context mContext;
    MusicListFragment mMusicListFragment;
    MusicPlayerFragment mMusicPlayerFragment;

    public PagerAdapter(FragmentManager fm, Context context,
                        MusicListFragment musicListFragment,
                        MusicPlayerFragment musicPlayerFragment) {
        super(fm);
        mContext = context;
        mMusicListFragment = musicListFragment;
        mMusicPlayerFragment = musicPlayerFragment;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return mContext.getResources().getString(com.example.kaerith.myapplication.R.string.files);
            case 1:
                return mContext.getResources().getString(com.example.kaerith.myapplication.R.string.player);
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mMusicListFragment;
            case 1:
                return mMusicPlayerFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
