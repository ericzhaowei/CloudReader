package com.ider.cloudreader.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.ider.cloudreader.R;
import com.ider.cloudreader.weibo.user.AccessTokenKeeper;
import com.ider.cloudreader.weibo.user.Constants;
import com.ider.cloudreader.weibo.user.UserKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.constant.WBPageConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by ider-eric on 2017/2/13.
 */

public class ContactsActivity extends Activity {
    private static final String TAG = "ContactsActivity";

    private ImageView vBack;
    private ListView vContacts;
    private static final String CONTACT_URL = "https://api.weibo.com/2/friendships/friends.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity_layout);

        vBack = (ImageView) findViewById(R.id.contact_navigation_back);
        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactsActivity.this.finish();
            }
        });
        vContacts = (ListView) findViewById(R.id.contact_list);
        setupList();
    }

    private void setupList() {
        User user = UserKeeper.readUser(this);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(this);
        WeiboParameters wbparams = new WeiboParameters(Constants.APP_KEY);
        wbparams.put("access_token", accessToken.getToken());
        wbparams.put("uid", user.id);
        Log.i(TAG, "setupList: token = " + accessToken.getToken());
        Log.i(TAG, "setupList: uid = " + user.id);
        new AsyncWeiboRunner(this).requestAsync(CONTACT_URL, wbparams, "GET", mListener);
    }


    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String s) {

        }

        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
        }
    };
}
