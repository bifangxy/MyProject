package com.example.administrator.myproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myproject.R;

import org.xutils.view.annotation.ContentView;

/**
 * Created by Administrator on 2017/8/16.
 */

@ContentView(R.layout.fragment_three)
public class FragmentThree extends FragmentBase {
    private static final String LOG_TAG = FragmentThree.class.getSimpleName();

    private Context mContext;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
