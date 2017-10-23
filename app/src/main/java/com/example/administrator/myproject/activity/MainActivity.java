package com.example.administrator.myproject.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.administrator.myproject.R;
import com.example.administrator.myproject.fragment.FragmentFour;
import com.example.administrator.myproject.fragment.FragmentOne;
import com.example.administrator.myproject.fragment.FragmentThree;
import com.example.administrator.myproject.fragment.FragmentTwo;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends ActivityBase implements BottomNavigationBar.OnTabSelectedListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";

    private Context mContext;

    @ViewInject(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    private FragmentManager mFragmentManager;

    private Fragment currentFragment = new Fragment();

    private List<Fragment> fragmentList = new ArrayList<>();

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initData(savedInstanceState);

    }

    private void initData(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            // “内存重启”时调用
            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);
            fragmentList.removeAll(fragmentList);
            if (mFragmentManager.findFragmentByTag(0 + "") != null) {
                fragmentList.add(mFragmentManager.findFragmentByTag(0 + ""));
            } else {
                fragmentList.add(new FragmentOne());
            }

            if (mFragmentManager.findFragmentByTag(1 + "") != null) {
                fragmentList.add(mFragmentManager.findFragmentByTag(1 + ""));
            } else {
                fragmentList.add(new FragmentTwo());
            }
            if (mFragmentManager.findFragmentByTag(2 + "") != null) {
                fragmentList.add(mFragmentManager.findFragmentByTag(2 + ""));
            } else {
                fragmentList.add(new FragmentThree());
            }
            if (mFragmentManager.findFragmentByTag(3 + "") != null) {
                fragmentList.add(mFragmentManager.findFragmentByTag(3 + ""));
            } else {
                fragmentList.add(new FragmentFour());
            }
            //恢复fragment页面
            restoreFragment();
        } else {
            //正常启动时调用
            fragmentList.add(new FragmentOne());
            fragmentList.add(new FragmentTwo());
            fragmentList.add(new FragmentThree());
            fragmentList.add(new FragmentFour());
            showFragment();
        }

        mBottomNavigationBar.setTabSelectedListener(this);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setActiveColor(R.color.newBlue)
                .setBarBackgroundColor(R.color.gray);

        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.main_normal, R.string.main))
                .addItem(new BottomNavigationItem(R.mipmap.live_normal, R.string.live))
                .addItem(new BottomNavigationItem(R.mipmap.found_normal, R.string.found))
                .addItem(new BottomNavigationItem(R.mipmap.user_center_normal, R.string.user_center))
                .setFirstSelectedPosition(0)
                .initialise();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //“内存重启”时保存当前的fragment名字
        outState.putInt(CURRENT_FRAGMENT, currentIndex);
        super.onSaveInstanceState(outState);
    }


    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        //如果之前没有添加过
        if (!fragmentList.get(currentIndex).isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.layout, fragmentList.get(currentIndex), "" + currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag
        } else {
            transaction
                    .hide(currentFragment)
                    .show(fragmentList.get(currentIndex));
        }
        currentFragment = fragmentList.get(currentIndex);
        transaction.commit();
    }

    /**
     * 恢复fragment
     */
    private void restoreFragment() {

        FragmentTransaction mBeginTransaction = mFragmentManager.beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {
            if (i == currentIndex) {
                mBeginTransaction.show(fragmentList.get(i));
            } else {
                if (mFragmentManager.findFragmentByTag(i + "") != null)
                    mBeginTransaction.hide(fragmentList.get(i));
            }
        }
        mBeginTransaction.commit();
        //把当前显示的fragment记录下来
        currentFragment = fragmentList.get(currentIndex);
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                currentIndex = 0;
                break;
            case 1:
                currentIndex = 1;
                break;
            case 2:
                currentIndex = 2;
                break;
            case 3:
                currentIndex = 3;
                break;
        }
        showFragment();

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
