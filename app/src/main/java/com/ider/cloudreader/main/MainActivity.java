package com.ider.cloudreader.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ider.cloudreader.R;
import com.ider.cloudreader.main.repostcomment.ICommentView;
import com.ider.cloudreader.main.repostcomment.IRepostView;
import com.ider.cloudreader.main.repostcomment.RepostPresenter;
import com.ider.cloudreader.main.repostcomment.ShareCommentInputActivity;
import com.ider.cloudreader.navigation.NavigationAdapter;
import com.ider.cloudreader.toolbar.MainTabHolder;
import com.ider.cloudreader.toolbar.OnTabItemClickListener;
import com.ider.cloudreader.weibo.user.LoginPresenter;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationAdapter.OnItemClickListener, ILoginView, IRepostView {
    private static final String TAG = "MainActivity";

    private static final int FRAGMENT_MAX = 3;
    private ViewPager mainViewPager;
    private MainTabHolder tabHolder;
    private NavigationAdapter navigationAdapter;
    private MainFragmentDiscovery fragmentDiscovery;
    private MainFragmentMusic fragmentMusic;
    private MainFragmentUser fragmentUser;
    private LoginPresenter loginPresenter;
    private RepostPresenter repostPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginPresenter = new LoginPresenter(this);

        setupToolBar();
        setupNavigationView();
        initViews();

        loginPresenter.checkExistsAccessToken();

    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.actionbar_list);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: toolbar onNavigationClicked");
                DrawerLayout mainDrawerLayout = (DrawerLayout) MainActivity.this.findViewById(R.id.main);
                mainDrawerLayout.openDrawer(Gravity.START);
            }
        });

        MainTabHolder tabHolder = (MainTabHolder) toolbar.findViewById(R.id.toolbar_tabholder);
        tabHolder.setTabClickListener(new OnTabItemClickListener() {
            @Override
            public void onTabItemClicke(int position) {
                if (position >= 0 || position < FRAGMENT_MAX) {
                    mainViewPager.setCurrentItem(position);

                }
            }
        });

        ImageView newStatues = (ImageView) toolbar.findViewById(R.id.actionbar_new_statues);
        newStatues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void setupNavigationView() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.navigationView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        navigationAdapter = new NavigationAdapter();
        navigationAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(navigationAdapter);
    }


    private void initViews() {
        tabHolder = (MainTabHolder) findViewById(R.id.toolbar_tabholder);
        mainViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mainViewPager.addOnPageChangeListener(onPageChangeListener);
        initFragment();
    }

    private void initFragment() {
        fragmentDiscovery = new MainFragmentDiscovery();
        fragmentMusic = new MainFragmentMusic();
        fragmentUser = new MainFragmentUser();
        ArrayList<Fragment> fragments = new ArrayList<>(3);
        fragments.add(fragmentDiscovery);
        fragments.add(fragmentMusic);
        fragments.add(fragmentUser);
        FragmentPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), fragments);
        mainViewPager.setAdapter(adapter);
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            tabHolder.setSelectPosition(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void login() {
        loginPresenter.login();
    }


    @Override
    public void onItemClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_header_login:
                login();
                break;
        }
    }


    @Override
    public void showUser(User user, Oauth2AccessToken accessToken) {
        navigationAdapter.showUser(user);
        fragmentUser.showUser(user);
        fragmentDiscovery.refreshStatues(accessToken, false, 0L);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout mainDrawerLayout = (DrawerLayout) findViewById(R.id.main);
        if (mainDrawerLayout.isDrawerOpen(Gravity.START)) {
            mainDrawerLayout.closeDrawers();
            return;
        }
        moveTaskToBack(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginPresenter.onAuthorizeCallback(requestCode, resultCode, data);
        if(requestCode == ShareCommentInputActivity.REPOST_REQUEST_CODE) {
            if(resultCode == ShareCommentInputActivity.RESULT_OK) {
                if(repostPresenter == null) {
                    repostPresenter = new RepostPresenter(MainActivity.this);
                }
                String statusId = data.getStringExtra("statusId");
                String text = data.getStringExtra("text");
                repostPresenter.repostStatus(statusId, text);
            }
        }
    }

    @Override
    public void reposting() {
        Toast.makeText(this, "正在发送", Toast.LENGTH_LONG).show();
    }

    @Override
    public void repostSuccess(String s) {
        Toast.makeText(this, "发送成功", Toast.LENGTH_LONG).show();
        try {
            JSONObject json = new JSONObject(s);
            Status status = Status.parse(json);
            fragmentDiscovery.addToFirst(status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void repostFailed(String message) {
        Log.i(TAG, "repostFailed: " + message);
        Toast.makeText(this, "发送失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
