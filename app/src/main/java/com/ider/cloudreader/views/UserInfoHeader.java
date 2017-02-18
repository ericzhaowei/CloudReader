package com.ider.cloudreader.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ider.cloudreader.R;
import com.ider.cloudreader.common.CountFormatter;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by ider-eric on 2017/2/18.
 */

public class UserInfoHeader extends ConstraintLayout {

    private static final String TAG = "UserInfoHeader";

    private User user;

    private CircleHeaderImage vImage;
    private TextView vName;
    private TextView vFriends;
    private TextView vFollowers;
    private ImageView vGender;

    public UserInfoHeader(Context context) {
        this(context, null);
    }

    public UserInfoHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.user_info_header, this);
        vImage = (CircleHeaderImage) findViewById(R.id.user_info_icon);
        vName = (TextView) findViewById(R.id.user_info_name);
        vFriends = (TextView) findViewById(R.id.user_info_friends);
        vFollowers = (TextView) findViewById(R.id.user_info_followers);
        vGender = (ImageView) findViewById(R.id.user_info_gender);
    }

    public void setUser(User user) {
        this.user = user;
        Glide.with(getContext()).load(user.avatar_large).into(this.vImage);
        vName.setText(user.name);
        vImage.setVerified(user.verified);
        vFriends.setText(String.format(getResources().getString(R.string.user_info_friends_count), CountFormatter.formatCount(user.friends_count)));
        vFollowers.setText(String.format(getResources().getString(R.string.user_info_followers_count), CountFormatter.formatCount(user.followers_count)));
        switch (user.gender) {
            case "m":
                vGender.setImageResource(R.drawable.userinfo_icon_male);
                break;
            case "f":
                vGender.setImageResource(R.drawable.userinfo_icon_female);
                break;
            case "n":
                vGender.setImageResource(R.drawable.userinfo_icon_male_unchecked);
                break;
        }

    }


}
