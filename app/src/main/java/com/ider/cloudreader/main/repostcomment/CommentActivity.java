package com.ider.cloudreader.main.repostcomment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ider.cloudreader.R;
import com.ider.cloudreader.main.MainPagerAdapter;
import com.ider.cloudreader.main.ParcelableStatus;
import com.sina.weibo.sdk.openapi.models.Status;

import org.w3c.dom.Comment;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/15.
 */

public class CommentActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, CommentFragment.CommentListener {

    private static final String TAG = "CommentActivity";
    private TextView vRepostCount, vCommentCount;
    private TextView vLikeCount;
    private TextView vRepost, vComment, vLike;
    private ImageView vCountTab;
    private Status status;
    private ViewPager pager;
    private CommentFragment commentFragment;
    private ArrayList<Fragment> fragments;
    private int repostWidth, commentWidth, tabWidth; // 转发数、评论数、tab的宽度
    private boolean measured; // 只计算一次的标记
    private int tabPositionStart; // tab初始位置
    private int tabPathWidth; // tab滑动距离
    private int initPosition = 1; // 初始页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_comment_activity);
        fragments = new ArrayList<>();
        Intent intent = getIntent();
        ParcelableStatus parcelableStatus = intent.getParcelableExtra("status");
        this.status = parcelableStatus.getStatus();

        initViews();

        setListener();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !measured) {
            repostWidth = vRepostCount.getMeasuredWidth();
            commentWidth = vCommentCount.getMeasuredWidth();
            tabWidth = vCountTab.getMeasuredWidth();
            measured = true;
            tabPositionStart = (repostWidth - tabWidth) / 2;
            tabPathWidth = (repostWidth + commentWidth) / 2;

            setCountTabPosition(tabPositionStart + tabPathWidth);
            if(initPosition == 0) {
                vRepostCount.setSelected(true);
            } else if(initPosition == 1) {
                vCommentCount.setSelected(true);
            }
        }
    }

    private void initViews() {
        vCountTab = (ImageView) findViewById(R.id.comment_count_tab);
        vRepostCount = (TextView) findViewById(R.id.status_comment_reweeted_count);
        vCommentCount = (TextView) findViewById(R.id.status_comment_comment_count);
        vLikeCount = (TextView) findViewById(R.id.status_comment_liked_count);
        vRepostCount.setText(String.format(getString(R.string.comment_reposts_count), status.reposts_count));
        vCommentCount.setText(String.format(getString(R.string.comment_comments_count), status.comments_count));
        vLikeCount.setText(String.format(getString(R.string.comment_liked_count), status.attitudes_count));
        vRepost = (TextView) findViewById(R.id.comment_bottom_repost);
        vComment = (TextView) findViewById(R.id.comment_bottom_comment);
        vLike = (TextView) findViewById(R.id.comment_bottom_like);

        pager = (ViewPager) findViewById(R.id.status_comment_pager);
        // 转发
        RepostFragment repostFragment = new RepostFragment();
        fragments.add(repostFragment);

        // 评论
        commentFragment = new CommentFragment();
        // 传递status到commentFragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("status", new ParcelableStatus(status));
        commentFragment.setArguments(bundle);
        fragments.add(commentFragment);


        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(adapter);
        pager.setCurrentItem(initPosition);
        pager.setOnPageChangeListener(this);

    }

    private void setListener() {
        vRepost.setOnClickListener(this);
        vComment.setOnClickListener(this);
        vLike.setOnClickListener(this);
        commentFragment.setCommentListener(this);
    }

    public void back(View view) {
        this.finish();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if(positionOffset == 0 || positionOffset == 1) {
            return;
        }
        int currentTabPosition = (int) (tabPositionStart + tabPathWidth * positionOffset);
        setCountTabPosition(currentTabPosition);

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0) {
            vRepostCount.setSelected(true);
            vCommentCount.setSelected(false);
        } else if(position == 1) {
            vRepostCount.setSelected(false);
            vCommentCount.setSelected(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 停止滚动
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            int position = pager.getCurrentItem();
            if (position == 0) {
                setCountTabPosition(tabPositionStart);
            } else if (position == 1) {
                setCountTabPosition(tabPositionStart + tabPathWidth);
            }
        }

    }

    private void setCountTabPosition(int currentTabPosition) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) vCountTab.getLayoutParams();
        lp.setMarginStart(currentTabPosition);
        vCountTab.setLayoutParams(lp);

    }

    @Override
    public void onClick(View view) {
        if(view == vComment) {
            Intent intent = new Intent(this, ShareCommentInputActivity.class);
            intent.putExtra(ShareCommentInputActivity.TYPE_KEY, ShareCommentInputActivity.TYPE_COMMENT);
            intent.putExtra(ShareCommentInputActivity.STATUS_ID_KEY, status.id);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100) {
            if(resultCode == 100) {
                String commentText = data.getStringExtra("text");
                CommentFragment commentFragment = (CommentFragment) fragments.get(1);
                commentFragment.commitComment(commentText);
            }
        }
    }

    @Override
    public void onCommentSuccess() {
        status.comments_count += 1;
        vCommentCount.setText(String.format(getString(R.string.comment_comments_count), status.comments_count));
    }
}
