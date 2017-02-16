package com.ider.cloudreader.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ider.cloudreader.R;
import com.ider.cloudreader.common.DateFormatter;
import com.ider.cloudreader.common.RegularExpression;
import com.ider.cloudreader.main.repostcomment.CommentActivity;
import com.ider.cloudreader.views.ArticalDetailView;
import com.ider.cloudreader.views.CircleHeaderImage;
import com.ider.cloudreader.views.ImageGridView;
import com.ider.cloudreader.views.RetweetStatusView;
import com.sina.weibo.sdk.openapi.models.Status;
import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/7.
 */

public class StatusAdapter extends RecyclerView.Adapter implements LoadStateInterfaceHolder.AdapterInterface {
    private static final String TAG = "StatusAdapter";

    private static final int TYPE_NORMAL = 100;
    private static final int TYPE_MORE = 101;
    private boolean showError;
    private int loadState = STATE_LOADING;

    private ArrayList<Status> statusList;
    private Context context;
    private View.OnClickListener retryClickListener;

    public StatusAdapter(Context context, ArrayList<Status> statusList, View.OnClickListener retryClickListener) {
        this.context = context;
        this.statusList = statusList;
        this.retryClickListener = retryClickListener;
    }

    @Override
    public int getItemCount() {
        // 尾部加载更多
        return statusList.size() + 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof StatusHolder) {
            StatusHolder statusHolder = (StatusHolder) holder;
            statusHolder.bindStatus(statusList.get(position));
        } else if (holder instanceof LoadMoreHolder) {
            ((LoadMoreHolder) holder).bind();

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(context).inflate(R.layout.status_item, parent, false);
            return new StatusHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.status_load_more, parent, false);

            return new LoadMoreHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position == statusList.size()) {
            return TYPE_MORE;
        }
        return TYPE_NORMAL;
    }

    public static class StatusDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if(view != parent.getChildAt(parent.getChildCount()-1)) {
                outRect.set(0, 30, 0, 0);
            }
        }
    }

    class LoadMoreHolder extends RecyclerView.ViewHolder implements LoadStateInterfaceHolder.HolderInterface {
        private ImageView image;
        private TextView text;
        public LoadMoreHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.status_load_more_image);
            text = (TextView) itemView.findViewById(R.id.status_load_more_text);
        }

        @Override
        public void bind() {
            switch (loadState) {
                case STATE_LOADING:
                    stateLoading();
                    break;
                case STATE_NOMORE:
                    stateNoMore();
                    break;
                case STATE_ERROR:
                    stateError();
                    break;
            }
        }

        @Override
        public void stateNoMore() {
            text.setText(R.string.status_load_no_more);
            image.setVisibility(View.GONE);
            text.setOnClickListener(null);
        }

        @Override
        public void stateError() {
            text.setText(R.string.status_load_retry);
            image.setVisibility(View.GONE);
            text.setOnClickListener(retryClickListener);
        }

        @Override
        public void stateLoading() {
            text.setText(R.string.status_loading);
            image.setVisibility(View.VISIBLE);
            ((AnimationDrawable)image.getDrawable()).start();
            text.setOnClickListener(null);
        }


    }


    class StatusHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Status status;

        private CircleHeaderImage userIcon;
        private TextView userName;
        private TextView subTitle;
        private TextView content;
        private ArticalDetailView shareDetail, commentDetail, likeDetail;
        private ImageGridView imageGrid;
        private RetweetStatusView retweetStatusView;

        public StatusHolder(View itemView) {
            super(itemView);
            this.userIcon = (CircleHeaderImage) itemView.findViewById(R.id.status_item_user_logo);
            this.userName = (TextView) itemView.findViewById(R.id.status_item_user_name);
            this.subTitle = (TextView) itemView.findViewById(R.id.status_item_user_subtitle);
            this.content = (TextView) itemView.findViewById(R.id.status_item_content);
            this.shareDetail = (ArticalDetailView) itemView.findViewById(R.id.status_item_share_count);
            this.commentDetail = (ArticalDetailView) itemView.findViewById(R.id.status_item_comment_count);
            this.likeDetail = (ArticalDetailView) itemView.findViewById(R.id.status_item_like_count);
            this.imageGrid = (ImageGridView) itemView.findViewById(R.id.status_item_image_grid);
            this.retweetStatusView = (RetweetStatusView) itemView.findViewById(R.id.status_item_retweeted_status);
            setListener();
        }

        public void bindStatus(Status status) {
            this.status = status;
            this.userIcon.setVerified(status.user.verified);
            Glide.with(context).load(status.user.avatar_large).into(this.userIcon);
            this.userName.setText(status.user.name);
            String interval = DateFormatter.getInterval(status.created_at);
            String statusSource = RegularExpression.getStatusSource(status.source);
            this.subTitle.setText(String.format(context.getString(R.string.status_source), interval, statusSource));
            this.content.setText(RegularExpression.checkText(context, status.text));
            this.content.setMovementMethod(LinkMovementMethod.getInstance());

            this.commentDetail.setCount(status.comments_count);
            this.shareDetail.setCount(status.reposts_count);
            this.likeDetail.setCount(status.attitudes_count);

            // 微博配图
            this.imageGrid.setPicUrls(status.pic_urls);

            // 原微博
            this.retweetStatusView.setStatus(status.retweeted_status);
        }

        public void setListener() {
            this.shareDetail.setOnClickListener(this);
            this.commentDetail.setOnClickListener(this);
            this.likeDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.status_item_comment_count:
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("status", new ParcelableStatus(status));
                    context.startActivity(intent);
                    break;
            }
        }
    }

//    public void showBottomError() {
//        showError = true;
//        notifyDataSetChanged();
//    }
//
//    public void showLoading() {
//        showError = false;
//        notifyDataSetChanged();
//    }

    @Override
    public void loadMore() {
        if(loadState != STATE_LOADING) {
            loadState = STATE_LOADING;
            notifyDataSetChanged();
        }
    }

    @Override
    public void noMoreItem() {
        loadState = STATE_NOMORE;
        notifyDataSetChanged();
    }

    @Override
    public void loadError() {
        loadState = STATE_ERROR;
        notifyDataSetChanged();
    }
}
