package com.example.administrator.myproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.myproject.R;
import com.example.administrator.myproject.activity.ChatActivity;
import com.example.administrator.myproject.adapter.RollPageAdapter;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/8/16.
 */
@ContentView(R.layout.fragment_one)
public class FragmentOne extends FragmentBase {
    private static final String LOG_TAG = FragmentOne.class.getSimpleName();

    private Context mContext;

    private int[] images;

    @ViewInject(R.id.roll_page_view)
    RollPagerView rollPagerView;

    @Override
    public void onStart() {
        mContext = getActivity();
        initData();
        super.onStart();
    }


    private void initData() {
        images = new int[]{
                R.mipmap.index_one, R.mipmap.index_two, R.mipmap.index_three, R.mipmap.index_four
        };
        rollPagerView.setAdapter(new RollPageAdapter(rollPagerView, images));

        rollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(LOG_TAG, "position = " + position);
                mContext.startActivity(new Intent(mContext, ChatActivity.class));
            }
        });
    }
}
