package com.example.administrator.myproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myproject.R;

import org.xutils.view.annotation.ContentView;

/**
 * Created by Administrator on 2017/8/16.
 */

@ContentView(R.layout.fragment_four)
public class FragmentFour extends FragmentBase {
    private static final String LOG_TAG = FragmentFour.class.getSimpleName();

    private Context mContext;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
