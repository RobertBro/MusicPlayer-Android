package com.example.kaerith.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kaerith.myapplication.*;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kaerith on 06.12.2016.
 */

public class MusicPlayerAdapter extends BaseAdapter {

    static class Holder {
        @BindView(com.example.kaerith.myapplication.R.id.text_view_playlist_file)
        TextView name;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private File[] mPlaylistFiles;

    public MusicPlayerAdapter(File[] files) {
        mPlaylistFiles = files;
    }

    @Override
    public int getCount() {
        return mPlaylistFiles.length;
    }

    @Override
    public Object getItem(int position) {
        return mPlaylistFiles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parentView) {
        Holder holder;
        if (convertView != null) {
            holder = (Holder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(parentView.getContext()).inflate(com.example.kaerith.myapplication.R.layout.music_player_list_view_element_layout, parentView, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }

        File file = mPlaylistFiles[position];
        holder.name.setText(file.getName());

        return convertView;
    }
}
