package com.ider.cloudreader.main.userinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.ider.cloudreader.R;
import com.ider.cloudreader.main.ParcelableUser;
import com.ider.cloudreader.views.UserInfoHeader;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by ider-eric on 2017/2/18.
 */

public class UserInfoActivity extends AppCompatActivity {

    private UserInfoHeader header;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);

        ParcelableUser parcelableUser = getIntent().getParcelableExtra("user");

        user = parcelableUser.getUser();

        header = (UserInfoHeader) findViewById(R.id.user_info_header);
        header.setUser(user);
    }

}
