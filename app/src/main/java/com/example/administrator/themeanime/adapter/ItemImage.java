package com.example.administrator.themeanime.adapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 27/12/2017.
 */

public class ItemImage implements Parcelable {
    private String img;
    private boolean like;

    public ItemImage(Parcel in) {
        String[] data = new String[2];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.img = data[0];
        this.like = Boolean.parseBoolean(data[1]);
    }

    public static final Creator<ItemImage> CREATOR = new Creator<ItemImage>() {
        @Override
        public ItemImage createFromParcel(Parcel in) {
            return new ItemImage(in);
        }

        @Override
        public ItemImage[] newArray(int size) {
            return new ItemImage[size];
        }
    };

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public ItemImage(String img, boolean like) {

        this.img = img;
        this.like = like;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                this.img, String.valueOf(this.like)
        });
    }
}
