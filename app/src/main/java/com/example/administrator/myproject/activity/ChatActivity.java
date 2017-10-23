package com.example.administrator.myproject.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.utils.Utils;
import com.example.administrator.myproject.MyApplication;
import com.example.administrator.myproject.R;
import com.example.administrator.myproject.adapter.ChatAdapter;
import com.example.administrator.myproject.adapter.EmotionGridViewAdapter;
import com.example.administrator.myproject.adapter.EmotionPagerAdapter;
import com.example.administrator.myproject.data.MessageInfo;
import com.example.administrator.myproject.util.Constants;
import com.example.administrator.myproject.util.EmotionUtils;
import com.example.administrator.myproject.util.EmotionViewUtils;
import com.example.administrator.myproject.util.GlobalOnItemClickManagerUtils;
import com.example.administrator.myproject.util.KPSwitchConflictUtil;
import com.example.administrator.myproject.util.KeyboardUtil;
import com.example.administrator.myproject.widget.EmotionInputDetector;
import com.example.administrator.myproject.widget.KPSwitchPanelLinearLayout;
import com.jude.easyrecyclerview.EasyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xieying on 2017/9/26.
 */
public class ChatActivity extends Activity implements View.OnClickListener {
    private static final String LOG_TAG = ChatActivity.class.getSimpleName();
    private Context mContext;
    private EasyRecyclerView rv_message;
    private EditText et_message;
    private ImageView iv_plus;
    private ImageView iv_expression;
    private TextView tv_send_img;
    private Button bt_send;
    private TextView tv_send_void;
    private KPSwitchPanelLinearLayout panel_root;
    private ViewPager vp_expression;
    private View sub_panel_one;
    private View sub_panel_two;
    private EmotionPagerAdapter emotionPagerAdapter;
    private ImageView iv_voice;

    private ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;

    private EmotionInputDetector mDetector;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = this;
        initView();
        initData();
        initListener();

    }

    private void initListener() {
        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_message.getText().length() != 0) {
                    iv_plus.setVisibility(View.GONE);
                    bt_send.setVisibility(View.VISIBLE);
                } else {
                    iv_plus.setVisibility(View.VISIBLE);
                    bt_send.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initData() {

        initKeyBoard();
        initEmotion();
        EventBus.getDefault().register(this);
        mDetector = EmotionInputDetector.with(this)
                .bindToEditText(et_message)
                .bindToSendButton(bt_send)
                .bindToVoiceButton(iv_voice)
                .bindToVoiceText(tv_send_void)
                .build();
        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(et_message);

        chatAdapter = new ChatAdapter(mContext);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_message.setLayoutManager(layoutManager);
        rv_message.setAdapter(chatAdapter);
        MessageInfo messageInfo3 = new MessageInfo();
        messageInfo3.setContent("[微笑][色][色][色]");
        messageInfo3.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo3.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
        messageInfo3.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        chatAdapter.add(messageInfo3);
        MessageInfo messageInfo2 = new MessageInfo();
        messageInfo2.setContent("[微笑][色][色][色]");
        messageInfo2.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo2.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
        messageInfo2.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        chatAdapter.add(messageInfo2);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
    }


    //防止键盘弹出和底部布局冲突
    private void initKeyBoard() {
        panel_root.setIgnoreRecommendHeight(true);
        KeyboardUtil.attach(this, panel_root);
        KPSwitchConflictUtil.attach(panel_root, et_message,
                new KPSwitchConflictUtil.SwitchClickListener() {
                    @Override
                    public void onClickSwitch(boolean switchToPanel) {
                        if (switchToPanel) {
                            et_message.clearFocus();
                        } else {
                            et_message.requestFocus();
                        }
                    }
                },
                new KPSwitchConflictUtil.SubPanelAndTrigger(sub_panel_one, iv_plus),
                new KPSwitchConflictUtil.SubPanelAndTrigger(sub_panel_two, iv_expression));
        rv_message.setLayoutManager(new LinearLayoutManager(this));
        rv_message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KPSwitchConflictUtil.hidePanelAndKeyboard(panel_root);
                }
                return false;
            }
        });
    }

    private void initView() {
        rv_message = (EasyRecyclerView) findViewById(R.id.rv_message);
        et_message = (EditText) findViewById(R.id.et_message);
        iv_plus = (ImageView) findViewById(R.id.iv_plus);
        iv_expression = (ImageView) findViewById(R.id.iv_expression);
        tv_send_img = (TextView) findViewById(R.id.send_img_tv);
        panel_root = (KPSwitchPanelLinearLayout) findViewById(R.id.panel_root);
        sub_panel_one = panel_root.findViewById(R.id.sub_panel_one);
        sub_panel_two = panel_root.findViewById(R.id.sub_panel_two);
        bt_send = (Button) findViewById(R.id.bt_send);
        tv_send_void = (TextView) findViewById(R.id.tv_send_voice);
        vp_expression = (ViewPager) panel_root.findViewById(R.id.fragment_chat_vp);
        iv_voice = (ImageView) findViewById(R.id.iv_voice);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (panel_root.getVisibility() == View.VISIBLE) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(panel_root);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    private void initEmotion() {
        List<GridView> emotionViews = EmotionViewUtils.getEmotionGridView(mContext);
        emotionPagerAdapter = new EmotionPagerAdapter(emotionViews);
        vp_expression.setAdapter(emotionPagerAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(EmotionViewUtils.getScreenWidth(), EmotionViewUtils.getGvHeight());
        vp_expression.setLayoutParams(params);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_send:
                MessageInfo messageInfo3 = new MessageInfo();
                messageInfo3.setContent("asdsad");
                messageInfo3.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
                messageInfo3.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
                messageInfo3.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
                chatAdapter.add(messageInfo3);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final MessageInfo messageInfo) {
        messageInfo.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
        chatAdapter.add(messageInfo);
        rv_message.scrollToPosition(chatAdapter.getCount() - 1);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                chatAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }
}
