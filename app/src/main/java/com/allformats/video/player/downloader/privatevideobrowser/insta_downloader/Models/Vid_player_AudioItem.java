package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models;

import android.os.Parcel;
import android.os.Parcelable;


public class Vid_player_AudioItem implements Parcelable {
    public static final Creator<Vid_player_AudioItem> CREATOR = new Creator<Vid_player_AudioItem>() {
        
        @Override 
        public Vid_player_AudioItem createFromParcel(Parcel parcel) {
            return new Vid_player_AudioItem(parcel);
        }

        
        @Override 
        public Vid_player_AudioItem[] newArray(int i) {
            return new Vid_player_AudioItem[i];
        }
    };
    private String id;
    String path;
    private String title;

    @Override 
    public int describeContents() {
        return 0;
    }

    public Vid_player_AudioItem(String str, String str2, String str3) {
        this.title = str2;
        this.id = str;
        this.path = str3;
    }

    public Vid_player_AudioItem() {
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public Vid_player_AudioItem(String str, String str2) {
        this.title = str2;
        this.id = str;
    }

    protected Vid_player_AudioItem(Parcel parcel) {
        this.id = parcel.readString();
        this.title = parcel.readString();
        this.path = parcel.readString();
    }

    @Override 
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.path);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }
}
