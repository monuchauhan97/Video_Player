package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Vid_player_ImageDataModel implements Parcelable {
    public static final Creator<Vid_player_ImageDataModel> CREATOR = new Creator<Vid_player_ImageDataModel>() {

        @Override
        public Vid_player_ImageDataModel createFromParcel(Parcel parcel) {
            return new Vid_player_ImageDataModel(parcel);
        }


        @Override
        public Vid_player_ImageDataModel[] newArray(int i) {
            return new Vid_player_ImageDataModel[i];
        }
    };
    public String folderName;
    String date;
    long dateValue;
    String fileName;
    String filePath;
    String id;
    boolean isCheckboxVisible;
    boolean isFavorite;
    boolean isSelected;
    long size;

    protected Vid_player_ImageDataModel(Parcel parcel) {
        this.folderName = "";
        this.date = null;
        boolean z = false;
        this.isCheckboxVisible = false;
        this.isFavorite = false;
        this.id = parcel.readString();
        this.fileName = parcel.readString();
        this.filePath = parcel.readString();
        this.folderName = parcel.readString();
        this.size = parcel.readLong();
        this.date = parcel.readString();
        this.dateValue = parcel.readLong();
        this.isCheckboxVisible = parcel.readByte() != 0;
        this.isSelected = parcel.readByte() != 0;
        this.isFavorite = parcel.readByte() != 0 ? true : z;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long j) {
        this.size = j;
    }

    public String toString() {
        return "ImageData{id='" + this.id + "', fileName='" + this.fileName + "', filePath='" + this.filePath + "', folderName='" + this.folderName + "', size=" + this.size + ", date='" + this.date + "', isCheckboxVisible=" + this.isCheckboxVisible + ", isSelected;=" + this.isSelected + ", isFavorite;=" + this.isFavorite + ", dateValue;=" + this.dateValue + '}';
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.fileName);
        parcel.writeString(this.filePath);
        parcel.writeString(this.folderName);
        parcel.writeLong(this.size);
        parcel.writeString(this.date);
        parcel.writeLong(this.dateValue);
        parcel.writeByte(this.isCheckboxVisible ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
    }
}
