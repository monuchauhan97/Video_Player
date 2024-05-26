package com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Vid_player_StatusModel implements Parcelable {
    public static final Creator<Vid_player_StatusModel> CREATOR = new Creator<Vid_player_StatusModel>() {
        
        @Override 
        public Vid_player_StatusModel createFromParcel(Parcel parcel) {
            return new Vid_player_StatusModel(parcel);
        }

        
        @Override 
        public Vid_player_StatusModel[] newArray(int i) {
            return new Vid_player_StatusModel[i];
        }
    };
    private String filepath;

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    private Uri fileUri;
    public boolean selected = false;

    @Override 
    public int describeContents() {
        return 0;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean z) {
        this.selected = z;
    }

    public Vid_player_StatusModel(String str) {
        this.filepath = str;
    }

    protected Vid_player_StatusModel(Parcel parcel) {
        this.filepath = parcel.readString();
    }

    public String getFilePath() {
        return this.filepath;
    }


    @Override 
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.filepath);
    }
}
