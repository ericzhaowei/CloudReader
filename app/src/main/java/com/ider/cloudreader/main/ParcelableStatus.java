package com.ider.cloudreader.main;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.sina.weibo.sdk.openapi.models.Status;

/**
 * Created by ider-eric on 2017/2/15.
 */

public class ParcelableStatus implements Parcelable {

    private Status status;

    public Status getStatus() {
        return this.status;
    }


    public ParcelableStatus(Status status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        Bundle bundle = new Bundle();
        bundle.putString("created_at", status.created_at);
        bundle.putString("id", status.id);
        bundle.putString("mid", status.mid);
        bundle.putString("idstr", status.idstr);
        bundle.putString("text", status.text);
        bundle.putString("source", status.source);
        bundle.putBoolean("favorited", status.favorited);
        bundle.putBoolean("truncated", status.truncated);
        bundle.putString("thumbnail_pic", status.thumbnail_pic);
        bundle.putString("bmiddle_pic", status.bmiddle_pic);
        bundle.putString("original_pic", status.original_pic);
        bundle.putInt("reposts_count", status.reposts_count);
        bundle.putInt("comments_count", status.comments_count);
        bundle.putInt("attitudes_count", status.attitudes_count);
        parcel.writeBundle(bundle);

    }

    public static final Creator<ParcelableStatus> CREATOR = new Creator<ParcelableStatus>() {
        @Override
        public ParcelableStatus createFromParcel(Parcel parcel) {

            Bundle bundle = parcel.readBundle(getClass().getClassLoader());
            Status status = new Status();
            status.created_at = bundle.getString("created_at");
            status.id = bundle.getString("id");
            status.mid = bundle.getString("mid");
            status.idstr = bundle.getString("idstr");
            status.text = bundle.getString("text");
            status.source = bundle.getString("source");
            status.favorited = bundle.getBoolean("favorited");
            status.truncated = bundle.getBoolean("truncated");
            status.thumbnail_pic = bundle.getString("thumbnail_pic");
            status.bmiddle_pic = bundle.getString("bmiddle_pic");
            status.original_pic = bundle.getString("original_pic");
            status.reposts_count = bundle.getInt("reposts_count");
            status.comments_count = bundle.getInt("comments_count");
            status.attitudes_count = bundle.getInt("attitudes_count");

            return new ParcelableStatus(status);
        }

        @Override
        public ParcelableStatus[] newArray(int i) {
            return new ParcelableStatus[i];
        }
    };
}
