package com.ider.cloudreader.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ider.cloudreader.R;
import com.ider.cloudreader.weibo.statues.StatusPresenter;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/1/12.
 */

public class MainFragmentDiscovery extends Fragment implements IStatusView {

    private static final String TAG = "MainFragmentDiscovery";

    private View rootView;
    private StatusPresenter presenter;
    private ImageView welcomeImage;
    private RecyclerView contentView;
    private StatusAdapter adapter;
    private ArrayList<Status> statusList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new StatusPresenter(getActivity(), this);
        statusList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.mainfragment_discovery, container, false);
            welcomeImage = (ImageView) rootView.findViewById(R.id.discovery_welcome);
            contentView = (RecyclerView) rootView.findViewById(R.id.discovery_content);
            setupRecyclerView();
            refreshStatues(null);
        }
        return rootView;
    }

    private void setupRecyclerView() {
        StatusAdapter.StatusDecoration decoration = new StatusAdapter.StatusDecoration();
        adapter = new StatusAdapter(getActivity(), statusList);
        contentView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contentView.addItemDecoration(decoration);
        contentView.setAdapter(adapter);
    }

    public void refreshStatues(Oauth2AccessToken accessToken) {
        if(presenter != null) {
            presenter.requestLatestStatues(accessToken);
        }
    }

    @Override
    public void displayLatestStatus(ArrayList<Status> latestList) {
        if(welcomeImage.getVisibility() == View.VISIBLE) {
            welcomeImage.setVisibility(View.GONE);
        }

        if(latestList.size() > 0) {
            statusList.addAll(0, latestList);
        }
        adapter.notifyDataSetChanged();

    }

}
