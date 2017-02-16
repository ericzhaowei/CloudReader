package com.ider.cloudreader.main.repostcomment;

import android.graphics.drawable.AnimationDrawable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ider.cloudreader.R;
import com.ider.cloudreader.main.ParcelableStatus;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;

/**
 * Created by ider-eric on 2017/2/15.
 */

public class CommentFragment extends Fragment implements ICommentView, View.OnClickListener {



    private static final String TAG = "CommentFragment";

    private ImageView vLoading;
    private TextView vSort;
    private CommentAdapter adapter;
    private ArrayList<Comment> comments;
    private RecyclerView vComments;
    private CommentPresenter presenter;
    private Status status;
    private CommentListener commentListener;


    public interface CommentListener {
        void onCommentSuccess();
    }

    public void setCommentListener(CommentListener commentListener) {
        this.commentListener = commentListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CommentPresenter(this);

        Bundle bundle = getArguments();
        ParcelableStatus parcelableStatus = bundle.getParcelable("status");
        this.status = parcelableStatus.getStatus();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_list_fragment, container, false);

        vLoading = (ImageView) view.findViewById(R.id.comment_loading);
        AnimationDrawable loadAnim = (AnimationDrawable) vLoading.getDrawable();
        loadAnim.start();
        vSort = (TextView) view.findViewById(R.id.comment_sort_textview);
        vSort.setCompoundDrawablePadding(10);
        vComments = (RecyclerView) view.findViewById(R.id.comment_recyclerview);
        vComments.setLayoutManager(new LinearLayoutManager(getActivity()));
        setListener();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.getComments(status.id, 0);
    }

    private void setListener() {
        vComments.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                    if(lastVisiblePosition == comments.size() && !presenter.isLoading() && comments.size() != 0) {
                        long maxId = Long.parseLong(comments.get(comments.size()-1).id);
                        presenter.getComments(status.id, maxId-1);
                    }
                }
            }
        });
    }


    public void commitComment(String text) {
        presenter.commitComment(text, status.id);
    }


    @Override
    public void requestFailed(String message) {
        vLoading.setVisibility(View.GONE);
        if(this.adapter != null) {
            this.adapter.loadError();
        }
    }

    @Override
    public void requestSuccess(ArrayList<Comment> comments, int total) {
        vLoading.setVisibility(View.GONE);
        vSort.setVisibility(View.VISIBLE);

        Log.i(TAG, "requestSuccess: 新增评论 = " + comments.size());

        if(this.adapter == null) {
            this.comments = comments;
            this.adapter = new CommentAdapter(getContext(), comments, this);
            vComments.setAdapter(this.adapter);
        } else {
            this.comments.addAll(comments);
            this.adapter.notifyDataSetChanged();
            if(comments.size() == 0) {
                adapter.noMoreItem();
            }
        }

    }

    @Override
    public void commentFailed(String message) {
        vLoading.setVisibility(View.GONE);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void commentSuccess(Comment comment) {
        comments.add(0, comment);
        adapter.notifyDataSetChanged();
        vLoading.setVisibility(View.GONE);
        commentListener.onCommentSuccess();
    }

    @Override
    public void showTopLoading() {
        vLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.status_load_more_text:
                long maxId = Long.parseLong(comments.get(comments.size()-1).id);
                adapter.loadMore();
                presenter.getComments(status.id, maxId-1);
                break;
        }
    }
}
