package com.ider.cloudreader.main;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ider.cloudreader.R;
import com.ider.cloudreader.views.CircleHeader;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/7.
 */

public class StatusAdapter extends RecyclerView.Adapter {

    private ArrayList<Status> statusList;
    private Context context;

    public StatusAdapter(Context context, ArrayList<Status> statusList) {
        this.context = context;
        this.statusList = statusList;
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StatusHolder statusHolder = (StatusHolder) holder;
        statusHolder.bindStatus(statusList.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.status_item, parent, false);
        return new StatusHolder(view);
    }


    public static class StatusDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 30, 0, 0);
        }
    }

    class StatusHolder extends RecyclerView.ViewHolder {

        private CircleHeader userIcon;
        private TextView userName;
        private TextView subTitle;
        private TextView content;

        public StatusHolder(View itemView) {
            super(itemView);
            this.userIcon = (CircleHeader) itemView.findViewById(R.id.status_item_user_logo);
            this.userName = (TextView) itemView.findViewById(R.id.status_item_user_name);
            this.subTitle = (TextView) itemView.findViewById(R.id.status_item_user_subtitle);
            this.content = (TextView) itemView.findViewById(R.id.status_item_content);
        }

        public void bindStatus(Status status) {
            this.userIcon.setVerified(status.user.verified);
            Glide.with(context).load(status.user.avatar_large).into(this.userIcon);
            this.userName.setText(status.user.name);
            this.subTitle.setText(status.created_at);
            this.content.setText(status.text);
        }


    }

}
