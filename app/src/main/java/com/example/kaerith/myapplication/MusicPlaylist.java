package com.example.kaerith.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MusicPlaylist implements Parcelable {

    protected MusicPlaylist(Parcel in) {
        mFiles = (List<File>) in.readSerializable();
    }

    public static final Creator<MusicPlaylist> CREATOR = new Creator<MusicPlaylist>() {
        @Override
        public MusicPlaylist createFromParcel(Parcel in) {
            return new MusicPlaylist(in);
        }

        @Override
        public MusicPlaylist[] newArray(int size) {
            return new MusicPlaylist[size];
        }
    };

    public List<File> getFiles() {
        return mFiles;
    }

    List<File> mFiles;

    public MusicPlaylist() {
        mFiles = new ArrayList<>();
    }

    public void add(File file) {
        if(mFiles != null) {
            mFiles.add(file);
        }
        else {
            mFiles = new ArrayList<>();
            mFiles.add(file);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable((Serializable) mFiles);
    }
}
