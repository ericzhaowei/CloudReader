package com.ider.cloudreader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ider.cloudreader.R;

/**
 * Created by ider-eric on 2017/1/12.
 */

public class MainFragmentDiscovery extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.mainfragment_discovery, container, false);
        return view;
    }
}
