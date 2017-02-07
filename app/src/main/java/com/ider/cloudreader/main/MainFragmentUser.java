package com.ider.cloudreader.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ider.cloudreader.R;
import com.ider.cloudreader.weibo.user.AccessTokenKeeper;
import com.ider.cloudreader.weibo.user.UserKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by ider-eric on 2017/2/6.
 */

public class MainFragmentUser extends Fragment implements UserFunctionAdapter.OnItemClickListener {


    private MainActivity activity;
    private RecyclerView recyclerView;
    private UserFunctionAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setBackgroundColor(0xFFE8E8E8);
        initContent();
        return recyclerView;
    }

    private void initContent() {
        adapter = new UserFunctionAdapter(activity);
        adapter.setOnItemClickListener(this);
        if(checkExistUser() != null) {
            adapter.setUser(checkExistUser());
        }
        recyclerView.addItemDecoration(new UserFunctionAdapter.SpliteDecoration());
        recyclerView.setAdapter(adapter);

    }



    public User checkExistUser() {
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity);
        if(accessToken.getExpiresTime() > System.currentTimeMillis()) {
            User user = UserKeeper.readUser(activity);
            if (!user.name.isEmpty()) {
                return user;
            }
        }
        return null;
    }

    public void showUser(User user) {
        if(adapter != null) {
            adapter.setUser(user);
        }
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                activity.login();
                break;
        }
    }


}
