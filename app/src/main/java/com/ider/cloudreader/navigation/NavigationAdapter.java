package com.ider.cloudreader.navigation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ider.cloudreader.R;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.Arrays;
import java.util.List;

public class NavigationAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private static final String TAG = "NavigationAdapter";

    private static final int NAV_TYPE_HEADER = 0;
    private static final int NAV_TYPE_NORMAL = 1;
    private static final int NAV_TYPE_DIVER = 2;

    private OnItemClickListener onItemClickListener;

    private List<NavItem> dataList = Arrays.asList(new NavHeader(),
            new NavNormal(R.drawable.menu_message, R.string.nav_message),
            new NavNormal(R.drawable.menu_vip, R.string.nav_vip),
            new NavNormal(R.drawable.menu_store, R.string.nav_store),
            new NavNormal(R.drawable.menu_freemusic_online, R.string.nav_freemusic_online),
            new NavDiver(),
            new NavNormal(R.drawable.menu_checkmusic, R.string.nav_checkmusic),
            new NavNormal(R.drawable.menu_theme, R.string.nav_theme, "官方红"),
            new NavNormal(R.drawable.menu_night, R.string.nav_night, true),
            new NavNormal(R.drawable.menu_stopdelay, R.string.nav_stopdelay),
            new NavNormal(R.drawable.menu_qrscan, R.string.nav_qrscan),
            new NavNormal(R.drawable.menu_musicclock, R.string.nav_musicclock),
            new NavNormal(R.drawable.menu_car, R.string.nav_car));


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        NavItem item = dataList.get(position);
        if (item instanceof NavHeader) {
            return NAV_TYPE_HEADER;
        } else if (item instanceof NavNormal) {
            return NAV_TYPE_NORMAL;
        } else if (item instanceof NavDiver) {
            return NAV_TYPE_DIVER;
        }

        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case NAV_TYPE_HEADER:
                view = inflater.inflate(R.layout.nav_header, parent, false);
                view.findViewById(R.id.nav_header_login).setOnClickListener(this);
                holder = new HeaderViewHolder(view);
                break;
            case NAV_TYPE_NORMAL:
                view = inflater.inflate(R.layout.nav_normal, parent, false);
                holder = new NormalViewHolder(view);
                break;
            case NAV_TYPE_DIVER:
                view = inflater.inflate(R.layout.nav_line, parent, false);
                holder = new DiverViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder: " + position);
        NavItem item = dataList.get(position);
        if (item instanceof NavNormal) {
            bindNormalViewHolder((NormalViewHolder) holder, (NavNormal) item);
        } else if (item instanceof NavHeader) {
            bindHeaderViewHolder((HeaderViewHolder) holder, (NavHeader) item);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class NormalViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView text;
        private View itemView;
        private int childCount;

        NormalViewHolder(View itemView) {
            super(itemView);
            this.icon = (ImageView) itemView.findViewById(R.id.nav_normal_icon);
            this.text = (TextView) itemView.findViewById(R.id.nav_normal_text);
            if (itemView instanceof ViewGroup) {
                this.childCount = ((ViewGroup) itemView).getChildCount();
            }
            this.itemView = itemView;
        }

        private View getView() {
            return this.itemView;
        }
    }

    private class DiverViewHolder extends RecyclerView.ViewHolder {
        DiverViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void bindNormalViewHolder(NormalViewHolder holder, NavNormal item) {
        holder.icon.setImageResource(item.getIconRes());
        holder.text.setText(item.getTitleRes());

        LinearLayout root = (LinearLayout) holder.getView();
        // 每次bind时，清空item.obj代表的subView,并重新添加，防止重复
        if (root.getChildCount() > holder.childCount) {
            for (int i = holder.childCount; i < root.getChildCount(); i++) {
                root.removeViewAt(i);
            }
        }

        if (item.getObj() == null) {
            return;
        }

        if (item.getObj() instanceof String) {
            addNavSubTitle(root, item.getObj().toString());
        } else if (item.getObj() instanceof Boolean) {
            addSwitchButton(root, (Boolean) item.getObj());
        }
    }


    private void addNavSubTitle(LinearLayout navView, String subTitle) {
        Context context = navView.getContext();
        TextView textView = new TextView(context);
        textView.setTextColor(context.getResources().getColor(R.color.liteWhite));
        textView.setTextSize(12);
        textView.setText(subTitle);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL;
        lp.setMarginEnd(40);
        textView.setLayoutParams(lp);
        navView.addView(textView);
    }

    private void addSwitchButton(LinearLayout navView, boolean switchOn) {
        Context context = navView.getContext();
        SwitchButton switchButton = new SwitchButton(context);
        switchButton.setEnable(switchOn);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL;
        lp.setMarginEnd(40);
        switchButton.setLayoutParams(lp);
        navView.addView(switchButton);
    }

    private void bindHeaderViewHolder(HeaderViewHolder holder, NavHeader header) {
        User user = header.getUser();
        if (user != null) {
            View view = holder.itemView;
            view.findViewById(R.id.header_login_layer).setVisibility(View.GONE);
            LinearLayout headerUser = (LinearLayout) view.findViewById(R.id.header_user_layer);
            headerUser.setVisibility(View.VISIBLE);
            ImageView userIcon = (ImageView) headerUser.findViewById(R.id.usericon);
            TextView username = (TextView) headerUser.findViewById(R.id.username);
            if (user.name != null) {
                username.setText(user.name);
            }
            Glide.with(view.getContext()).load(user.avatar_large).into(userIcon);
        }
    }


    @Override
    public void onClick(View view) {

        onItemClickListener.onItemClicked(view);

    }


    public interface OnItemClickListener {
        void onItemClicked(View view);
    }

    public void showUser(User user) {
        if (dataList.get(0) instanceof NavHeader) {
            NavHeader header = (NavHeader) dataList.get(0);
            header.setUser(user);
            this.notifyDataSetChanged();
        }
    }
}
