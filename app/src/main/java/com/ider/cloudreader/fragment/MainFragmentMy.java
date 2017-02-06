package com.ider.cloudreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ider.cloudreader.R;
import com.ider.cloudreader.main.MainActivity;
import com.ider.cloudreader.weibo.AccessTokenKeeper;
import com.ider.cloudreader.weibo.ILoginView;
import com.ider.cloudreader.weibo.LoginPresenter;
import com.ider.cloudreader.weibo.UserKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by ider-eric on 2017/1/12.
 */

public class MainFragmentMy extends Fragment {

    private View rootView;
    private TextView name, subTitle;
    private ImageView headIcon;
    private TextView statuses, friends, followers;
    private TextView statusesCount, friendsCount, followersCount;
    private Button login;
    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("MY", "onCreateView: ");
        if (rootView == null) {
//            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.mainfragment_my, container, false);
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_header, container, false);
            name = (TextView) rootView.findViewById(R.id.my_name);
            subTitle = (TextView) rootView.findViewById(R.id.my_subtitle);
            headIcon = (ImageView) rootView.findViewById(R.id.my_icon);
            statusesCount = (TextView) rootView.findViewById(R.id.statues_count);
            friendsCount = (TextView) rootView.findViewById(R.id.friends_count);
            followersCount = (TextView) rootView.findViewById(R.id.followers_count);
            statuses = (TextView) rootView.findViewById(R.id.my_statues);
            friends = (TextView) rootView.findViewById(R.id.my_friends);
            followers = (TextView) rootView.findViewById(R.id.my_followers);
            login = (Button) rootView.findViewById(R.id.my_login_button);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.login();
                }
            });
            checkExistUser();
        }
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    public void checkExistUser() {
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity);
        if(accessToken.getExpiresTime() > System.currentTimeMillis()) {
            User user = UserKeeper.readUser(activity);
            if (!user.name.isEmpty()) {
                showUser(user);
            }
        }

    }

    public void showUser(User user) {
        if(rootView != null) {
            login.setVisibility(View.GONE);
            Glide.with(getActivity()).load(user.avatar_large).into(headIcon);
            name.setText(user.name);
            statusesCount.setText(String.valueOf(user.statuses_count));
            friendsCount.setText(String.valueOf(user.friends_count));
            followersCount.setText(String.valueOf(user.followers_count));
            statuses.setVisibility(View.VISIBLE);
            friends.setVisibility(View.VISIBLE);
            followers.setVisibility(View.VISIBLE);
        }
    }

}
