package com.ider.cloudreader.main.repostcomment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ider.cloudreader.R;
import com.ider.cloudreader.common.SpellUtil;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/14.
 */

public class ContactAdapter extends BaseAdapter {

    private ArrayList<User> contacts;
    private Context context;

    public ContactAdapter(Context context, ArrayList<User> contacts) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int i) {
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.contact_item, viewGroup, false);
            holder.firstLetter = (TextView) view.findViewById(R.id.contact_item_first_letter);
            holder.icon = (ImageView) view.findViewById(R.id.contact_item_icon);
            holder.name = (TextView) view.findViewById(R.id.contact_item_name);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String name = contacts.get(i).name;
        String imageUrl = contacts.get(i).avatar_large;
        Glide.with(context).load(imageUrl).into(holder.icon);
        holder.name.setText(name);

        String firstLetter = SpellUtil.getFirstLetter(name);
        if(firstLetter != null) {
            holder.firstLetter.setText(firstLetter);
            if(i != 0) {
                String lastFirstLetter = SpellUtil.getFirstLetter(contacts.get(i-1).name);
                if(lastFirstLetter.equals(firstLetter)) {
                    holder.firstLetter.setVisibility(View.GONE);
                }
            }
        }
        return view;
    }

    class ViewHolder {
        TextView firstLetter;
        ImageView icon;
        TextView name;
    }

}


