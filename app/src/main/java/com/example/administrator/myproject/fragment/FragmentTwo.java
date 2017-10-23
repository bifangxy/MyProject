package com.example.administrator.myproject.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.myproject.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/8/16.
 */

@ContentView(R.layout.fragment_two)
public class FragmentTwo extends FragmentBase {
    private static final String LOG_TAG = FragmentTwo.class.getSimpleName();

    private Context mContext;

    private View view;

    @ViewInject(R.id.bt_test)
    Button bt_test;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Event(type = View.OnClickListener.class, value = R.id.bt_test)
    private void testInjectOnClick(View v) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("请稍候...");
        progressDialog.show();
        RequestParams params = new RequestParams("http://api.k780.com:88/?app=idcard.get");
        params.addQueryStringParameter("appkey", "10003");
        params.addQueryStringParameter("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
        params.addQueryStringParameter("format", "json");
        params.addQueryStringParameter("idcard", "110101199001011114");
        x.http().get(params, new Callback.CommonCallback() {

            @Override
            public void onSuccess(Object result) {
                Log.d(LOG_TAG, "---" + result);
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(LOG_TAG, "-----onError----");
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
                Log.d(LOG_TAG, "-----onCancelled----");
            }

            @Override
            public void onFinished() {
                Log.d(LOG_TAG, "-----onFinished----");
                progressDialog.cancel();
            }
        });
    }
}
