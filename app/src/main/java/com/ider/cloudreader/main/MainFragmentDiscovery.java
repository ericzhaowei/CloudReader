package com.ider.cloudreader.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
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

public class MainFragmentDiscovery extends Fragment implements IStatusView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainFragmentDiscovery";

    private View rootView;
    private StatusPresenter presenter;
    private ImageView welcomeImage;
    private SwipeRefreshLayout refreshContainer;
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
            refreshContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.discovery_refresh_container);
            refreshContainer.setOnRefreshListener(this);
            refreshContainer.setRefreshing(true);
            contentView = (RecyclerView) rootView.findViewById(R.id.discovery_content);
            setupRecyclerView();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRecyclerView() {
        StatusAdapter.StatusDecoration decoration = new StatusAdapter.StatusDecoration();
        adapter = new StatusAdapter(getActivity(), statusList, this);
        contentView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contentView.addItemDecoration(decoration);
        contentView.setAdapter(adapter);
        contentView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                    if(lastVisiblePosition == statusList.size() && !presenter.isLoading() && statusList.size() != 0) {
                        long lastId = Long.valueOf(statusList.get(statusList.size()-1).id);
                        refreshStatues(null, true, lastId-1);
                    }
                }
            }
        });
    }

    public void refreshStatues(Oauth2AccessToken accessToken, boolean append, long maxId) {
        if(presenter != null) {
            presenter.refreshStatues(accessToken, append, maxId);
        }
    }

    @Override
    public void displayLatestStatus(ArrayList<Status> latestList, boolean append) {
        if(refreshContainer.isRefreshing()) {
            refreshContainer.setRefreshing(false);
        }

        if(welcomeImage.getVisibility() == View.VISIBLE) {
            welcomeImage.setVisibility(View.GONE);
        }

        if(!append) {
            this.statusList = latestList;
            adapter = new StatusAdapter(getActivity(), statusList, this);
            contentView.setAdapter(adapter);
        } else {
            statusList.addAll(latestList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadError() {
        adapter.setLoadingState(LoadStateInterfaceHolder.AdapterInterface.STATE_ERROR);
    }

    @Override
    public void loadNoMore() {
        adapter.setLoadingState(LoadStateInterfaceHolder.AdapterInterface.STATE_NOMORE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.status_load_more_text:
                long maxId = Long.parseLong(statusList.get(statusList.size()-1).id);
                presenter.refreshStatues(null, true, maxId);
                adapter.setLoadingState(LoadStateInterfaceHolder.AdapterInterface.STATE_LOADING);
                break;
        }
    }

    @Override
    public void onRefresh() {
        refreshStatues(null, false, 0);
    }


    public void addToFirst(Status status) {
        statusList.add(0, status);
        adapter.notifyDataSetChanged();
    }

}
