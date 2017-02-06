package com.ider.cloudreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.cloudreader.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ider-eric on 2017/2/6.
 */

public class MainFragmentUser extends Fragment {

    private List<UserFunction> funList = Arrays.asList(new UserFunction[] {
            new UserHeader()
    });
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rootView = new RecyclerView(getActivity());
        rootView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rootView.setBackgroundColor(0xff0000ff);
        return rootView;
    }

    class UserFunction {

    }

    class UserHeader extends UserFunction {

    }

    class MyAdapter extends RecyclerView.Adapter {

        private static final int TYPE_HEADER = 0;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(TYPE_HEADER == viewType) {
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_header, parent, false);
                ImageView icon = (ImageView) headerView.findViewById(R.id.my_icon);
            }
            return null;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return funList.size();
        }

        @Override
        public int getItemViewType(int position) {
            UserFunction function = funList.get(position);
            if(function instanceof UserHeader) {
                return TYPE_HEADER;
            }

            return -1;
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private ImageView icon;
        private TextView name;
        private TextView subTitle;

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }




}
