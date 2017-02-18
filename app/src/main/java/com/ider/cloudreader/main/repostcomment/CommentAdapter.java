package com.ider.cloudreader.main.repostcomment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
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
import com.ider.cloudreader.main.LoadStateInterfaceHolder;
import com.ider.cloudreader.views.CircleHeaderImage;
import com.sina.weibo.sdk.openapi.models.Comment;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/15.
 */

public class CommentAdapter extends RecyclerView.Adapter implements LoadStateInterfaceHolder.AdapterInterface {

    private static final String TAG = "CommentAdapter";
    private Context context;
    private ArrayList<Comment> commentList;
    private static final int TYPE_NORMAL = 100;
    private static final int TYPE_MORE = 101;

    private int loadState = STATE_NOMORE;
    private View.OnClickListener retryListener;

    public CommentAdapter(Context context, ArrayList<Comment> commentList, View.OnClickListener retryListener) {
        this.commentList = commentList;
        this.context = context;
        this.retryListener = retryListener;
    }

    @Override
    public int getItemCount() {
        return commentList.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
            return new CommentHolder(view);
        } else if(viewType == TYPE_MORE) {
            View view = LayoutInflater.from(context).inflate(R.layout.status_load_more, parent, false);
            return new LoadMoreHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CommentHolder) {
            Comment comment = commentList.get(position);
            CommentHolder commentHolder = (CommentHolder) holder;
            commentHolder.bindComment(comment);
        } else if(holder instanceof LoadMoreHolder) {
            LoadMoreHolder moreHolder = (LoadMoreHolder) holder;
            moreHolder.bind();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position == commentList.size()) {
            return TYPE_MORE;
        }
        return TYPE_NORMAL;
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        private CircleHeaderImage userIcon;
        private TextView userName;
        private TextView subTitle;
        private TextView text;

        public CommentHolder(View view) {
            super(view);
            userIcon = (CircleHeaderImage) view.findViewById(R.id.comment_item_user_icon);
            userName = (TextView) view.findViewById(R.id.comment_item_user_name);
            subTitle = (TextView) view.findViewById(R.id.comment_item_user_subtitle);
            text = (TextView) view.findViewById(R.id.comment_item_content);
        }

        private void bindComment(Comment comment) {
            Glide.with(context).load(comment.user.profile_image_url).into(userIcon);
            userName.setText(comment.user.name);
            Log.i(TAG, "bindComment: " + comment.text);
            if(comment.user.verified) {
                userName.setTextColor(context.getResources().getColor(R.color.colorAccent));
                userIcon.setVerified(true);
            } else {
                userName.setTextColor(Color.BLACK);
                userIcon.setVerified(false);
            }
            subTitle.setText(DateFormatter.getInterval(comment.created_at));
            SpannableString spannableString = RegularExpression.checkText(context, comment.text);
            text.setText(spannableString);
        }
    }

    class LoadMoreHolder extends RecyclerView.ViewHolder implements LoadStateInterfaceHolder.HolderInterface {
        private ImageView image;
        private TextView text;
        private LoadMoreHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.status_load_more_image);
            text = (TextView) view.findViewById(R.id.status_load_more_text);
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
        public void stateLoading() {
            image.setVisibility(View.VISIBLE);
            ((AnimationDrawable) image.getDrawable()).start();
            text.setText(R.string.status_loading);
            text.setOnClickListener(null);
        }

        @Override
        public void stateError() {
            image.setVisibility(View.GONE);
            text.setText(R.string.status_load_retry);
            text.setOnClickListener(retryListener);
        }

        @Override
        public void stateNoMore() {
            image.setVisibility(View.GONE);
            text.setText(R.string.comment_no_more_comment);
            text.setOnClickListener(null);
        }
    }


    @Override
    public void setLoadingState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }
}
