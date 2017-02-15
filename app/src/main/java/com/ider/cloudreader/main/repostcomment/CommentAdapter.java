package com.ider.cloudreader.main.repostcomment;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ider.cloudreader.R;
import com.ider.cloudreader.common.RegularExpression;
import com.ider.cloudreader.views.CircleHeaderImage;
import com.sina.weibo.sdk.openapi.models.Comment;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/15.
 */

public class CommentAdapter extends RecyclerView.Adapter{

    private static final String TAG = "CommentAdapter";
    
    private Context context;
    private ArrayList<Comment> commentList;

    public CommentAdapter(Context context, ArrayList<Comment> commentList) {
        this.commentList = commentList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        CommentHolder commentHolder = (CommentHolder) holder;
        commentHolder.bindComment(comment);
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
            subTitle.setText(comment.created_at);
            SpannableString spannableString = RegularExpression.checkText(context, comment.text);
            text.setText(spannableString);
        }
    }


}
