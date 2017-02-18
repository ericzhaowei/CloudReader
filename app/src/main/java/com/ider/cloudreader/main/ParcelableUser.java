package com.ider.cloudreader.main;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by ider-eric on 2017/2/18.
 */

public class ParcelableUser implements Parcelable {
    private User user;

    public User getUser() {
        return this.user;
    }

    public ParcelableUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putString("id", user.id);
        bundle.putString("name", user.name);
        bundle.putString("description", user.description);
        bundle.putString("avatar_large", user.avatar_large);
        bundle.putBoolean("verified", user.verified);
        bundle.putInt("friends_count", user.friends_count);
        bundle.putInt("followers_count", user.followers_count);
        bundle.putInt("statuses_count", user.statuses_count);
        bundle.putString("location", user.location);
        bundle.putString("profile_url", user.profile_url);
        bundle.putString("gender", user.gender);
        parcel.writeBundle(bundle);

    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public ParcelableUser createFromParcel(Parcel parcel) {
            Bundle bundle = parcel.readBundle(getClass().getClassLoader());
            User user = new User();
            user.id = bundle.getString("id");
            user.name = bundle.getString("name");
            user.description = bundle.getString("description");
            user.avatar_large = bundle.getString("avatar_large");
            user.verified = bundle.getBoolean("verified");
            user.friends_count = bundle.getInt("friends_count");
            user.followers_count = bundle.getInt("followers_count");
            user.statuses_count = bundle.getInt("statuses_count");
            user.location = bundle.getString("location");
            user.profile_url = bundle.getString("profile_url");
            user.gender = bundle.getString("gender");
            return new ParcelableUser(user);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };
}
