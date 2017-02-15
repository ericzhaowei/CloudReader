package com.ider.cloudreader.main;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ider.cloudreader.R;
import com.sina.weibo.sdk.openapi.models.User;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ider-eric on 2017/2/7.
 */

public class UserFunctionAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;

    private UserHeader header = new UserHeader(0);
    private List<UserFunction> funList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public UserFunctionAdapter(Context context) {
        this.context = context;
        funList = Arrays.asList(
                header,
                new UserNormal(R.drawable.messagescenter_at, R.string.user_function_message_at, 30),
                new UserNormal(R.drawable.messagescenter_comments, R.string.user_function_message_comments, 2),
                new UserNormal(R.drawable.messagescenter_good, R.string.user_function_message_fabulous, 2),
                new UserNormal(R.drawable.user_article, R.string.user_function_ariticle, 30),
                new UserNormal(R.drawable.user_collection, R.string.user_function_collection, 2),
                new UserNormal(R.drawable.user_more, R.string.user_function_more, 30));
    }

    class UserFunction {
        public int topMargin;
        public UserFunction(int topMargin) {
            this.topMargin = topMargin;
        }
    }

    class UserHeader extends UserFunction {
        private User user;

        public UserHeader(int topMargin) {
            super(topMargin);
        }

        public void setUser(User user) {

            this.user = user;
        }
    }

    class UserNormal extends UserFunction {

        private int drawableRes;
        private int titleRes;
        private String subTitle;
        private int rightDrawableRes;

        public UserNormal(int drawableRes, int titleRes, int topMargin) {
            super(topMargin);
            this.drawableRes = drawableRes;
            this.titleRes = titleRes;
        }

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if(TYPE_HEADER == viewType) {
            View headerView = LayoutInflater.from(context).inflate(R.layout.fragment_my_header, parent, false);
            holder = new HeaderHolder(headerView);
        } else if (TYPE_NORMAL == viewType) {
            View normalItem = LayoutInflater.from(context).inflate(R.layout.fragment_user_normal_item, parent, false);
            holder = new NormalHolder(normalItem);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if(TYPE_HEADER == type) {
            UserHeader header = (UserHeader) funList.get(position);
            ((HeaderHolder) holder).bindHeader(header);
            holder.itemView.setTag(R.id.key_top_margin, header.topMargin);
        } else if(TYPE_NORMAL == type) {
            UserNormal normal = (UserNormal) funList.get(position);
            ((NormalHolder) holder).bindNormal(normal);
            holder.itemView.setTag(R.id.key_top_margin, normal.topMargin);
        }
    }

    @Override
    public int getItemCount() {
        return funList.size();
    }

    @Override
    public int getItemViewType(int position) {
        UserFunction function = funList.get(position);
        if (function instanceof UserHeader) {
            return TYPE_HEADER;
        } else if (function instanceof UserNormal) {
            return TYPE_NORMAL;
        }

        return -1;
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView icon;
        private TextView name;
        private TextView subTitle;
        private TextView statuesCount, friendsCount, followersCount;
        private Button loginButton;

        public HeaderHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            icon = (ImageView) itemView.findViewById(R.id.my_icon);
            name = (TextView) itemView.findViewById(R.id.my_name);
            subTitle = (TextView) itemView.findViewById(R.id.my_subtitle);
            statuesCount = (TextView) itemView.findViewById(R.id.statues_count);
            friendsCount = (TextView) itemView.findViewById(R.id.friends_count);
            followersCount = (TextView) itemView.findViewById(R.id.followers_count);
            loginButton = (Button) itemView.findViewById(R.id.login_button);
            loginButton.setOnClickListener(UserFunctionAdapter.this);
        }

        private void bindHeader(UserHeader header) {
            User user = header.user;
            if(user != null) {
                Glide.with(context).load(user.avatar_large).into(icon);
                name.setText(user.name);
                String description = (user.description == null || user.description.isEmpty())
                        ? context.getString(R.string.my_header_nodescription) : user.description;
                subTitle.setText(String.format(context.getString(R.string.my_header_description), description));
                statuesCount.setText(String.valueOf(user.statuses_count));
                friendsCount.setText(String.valueOf(user.friends_count));
                followersCount.setText(String.valueOf(user.followers_count));

                loginButton.setVisibility(View.GONE);
                itemView.findViewById(R.id.my_statues).setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.my_friends).setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.my_followers).setVisibility(View.VISIBLE);
            }
        }
    }


    class NormalHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView icon;
        TextView title, subTitle;
        ImageView rightDrawable;
        public NormalHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            icon = (ImageView) itemView.findViewById(R.id.user_normal_item_icon);
            title = (TextView) itemView.findViewById(R.id.user_normal_item_title);
            subTitle = (TextView) itemView.findViewById(R.id.user_normal_item_subtitle);
            rightDrawable = (ImageView) itemView.findViewById(R.id.user_normal_item_right_icon);
        }

        public void bindNormal(UserNormal normal) {
            icon.setImageResource(normal.drawableRes);
            title.setText(normal.titleRes);
            if(normal.subTitle != null) {
                subTitle.setText(normal.subTitle);
            }
            if(normal.rightDrawableRes != 0) {
                rightDrawable.setImageResource(normal.rightDrawableRes);
            }
        }
    }

    public void setUser(User user) {
        header.setUser(user);
        this.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if(onItemClickListener == null) {
            return;
        }
        onItemClickListener.onItemClick(view);
    }

    public static class SpliteDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, (Integer) view.getTag(R.id.key_top_margin), 0, 0);
        }
    }


}
