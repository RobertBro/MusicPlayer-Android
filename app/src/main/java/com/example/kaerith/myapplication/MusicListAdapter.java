package com.example.kaerith.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MusicListAdapter extends BaseAdapter{

    static class Holder {
        @BindView(com.example.kaerith.myapplication.R.id.text_view_file)
        TextView name;
        @BindView(com.example.kaerith.myapplication.R.id.image_view_icon)
        ImageView icon;
        @BindView(com.example.kaerith.myapplication.R.id.image_view_plus)
        ImageView plus;

        public Holder(View view) {
            ButterKnife.bind(this,view);
        }
    }

    private File[] mFiles;
    private List<File> mPlaylist;

    public MusicListAdapter(File[] files) {
        mFiles = files;
    }

    @Override
    public int getCount() {
        return mFiles.length;
    }

    @Override
    public Object getItem(int position) {
        return mFiles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parentView) {
        Holder holder;
        if(convertView != null){
            holder = (Holder) convertView.getTag();
        }
        else {
            convertView = LayoutInflater.from(parentView.getContext()).inflate(com.example.kaerith.myapplication.R.layout.list_view_layout, parentView, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }

        mPlaylist = new ArrayList<File>();

        File file = mFiles[position];
        holder.name.setText(file.getName());

        if(MusicListFragment.isMusic(file)) {
            holder.icon.setImageResource(com.example.kaerith.myapplication.R.drawable.note);
            holder.plus.setVisibility(View.VISIBLE);
        } else {
            holder.icon.setImageResource(com.example.kaerith.myapplication.R.drawable.folder);
            holder.plus.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
