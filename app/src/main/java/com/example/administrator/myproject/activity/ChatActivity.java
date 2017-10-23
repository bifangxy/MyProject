package com.example.administrator.myproject.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.utils.Utils;
import com.example.administrator.myproject.MyApplication;
import com.example.administrator.myproject.R;
import com.example.administrator.myproject.adapter.EmotionGridViewAdapter;
import com.example.administrator.myproject.adapter.EmotionPagerAdapter;
import com.example.administrator.myproject.util.EmotionUtils;
import com.example.administrator.myproject.util.GlobalOnItemClickManagerUtils;
import com.example.administrator.myproject.util.KPSwitchConflictUtil;
import com.example.administrator.myproject.util.KeyboardUtil;
import com.example.administrator.myproject.widget.KPSwitchPanelLinearLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xieying on 2017/9/26.
 */
@ContentView(R.layout.activity_chat)
public class ChatActivity extends Activity {
    private static final String LOG_TAG = ChatActivity.class.getSimpleName();

    private Context mContext;

    /*   @ViewInject(R.id.rv_message)
       RecyclerView rv_message;
       @ViewInject(R.id.et_message)
       EditText et_message;
       @ViewInject(R.id.iv_plus)
       ImageView iv_plus;
       @ViewInject(R.id.send_img_tv)
       TextView tv_send_img;
       @ViewInject(R.id.panel_root)
       KPSwitchPanelLinearLayout panel_root;*/
    private RecyclerView rv_message;
    private EditText et_message;
    private ImageView iv_plus;
    private ImageView iv_expression;
    private TextView tv_send_img;
    private KPSwitchPanelLinearLayout panel_root;
    private ViewPager vp_expression;
    private View sub_panel_one;
    private View sub_panel_two;
    private EmotionPagerAdapter emotionPagerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = this;
        initView();
        initData();


    }

    private void initData() {

        initKeyBoard();
        initEmotion();
        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(et_message);
    }


    private void initKeyBoard() {
        panel_root.setIgnoreRecommendHeight(true);
        KeyboardUtil.attach(this, panel_root,
                // Add keyboard showing state callback, do like this when you want to listen in the
                // keyboard's show/hide change.
                new KeyboardUtil.OnKeyboardShowingListener() {
                    @Override
                    public void onKeyboardShowing(boolean isShowing) {
                        Log.d(LOG_TAG, String.format("Keyboard is %s", isShowing ? "showing" : "hiding"));
                    }
                });


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
                new KPSwitchConflictUtil.SubPanelAndTrigger(sub_panel_one, iv_expression),
                new KPSwitchConflictUtil.SubPanelAndTrigger(sub_panel_two, iv_plus));


        tv_send_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mock start the translucent full screen activity.
                Log.d(LOG_TAG, "---------点击了");
//                startActivity(new Intent(ChattingResolvedActivity.this, TranslucentActivity.class));
            }
        });

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
        rv_message = (RecyclerView) findViewById(R.id.rv_message);
        et_message = (EditText) findViewById(R.id.et_message);
        iv_plus = (ImageView) findViewById(R.id.iv_plus);
        iv_expression = (ImageView) findViewById(R.id.iv_expression);
        tv_send_img = (TextView) findViewById(R.id.send_img_tv);
        panel_root = (KPSwitchPanelLinearLayout) findViewById(R.id.panel_root);
        sub_panel_one = panel_root.findViewById(R.id.sub_panel_one);
        sub_panel_two = panel_root.findViewById(R.id.sub_panel_two);
        vp_expression = (ViewPager) panel_root.findViewById(R.id.fragment_chat_vp);
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
        // 获取屏幕宽度
        int screenWidth = MyApplication.screenWidth;
        // item的间距
        int spacing = Utils.dp2px(mContext, 12);
        // 动态计算item的宽度和高度
        int itemWidth = (screenWidth - spacing * 8) / 7;
        //动态计算gridview的总高度
        int gvHeight = itemWidth * 3 + spacing * 6;

        List<GridView> emotionViews = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>();
        // 遍历所有的表情的key
        for (String emojiName : EmotionUtils.EMOTION_STATIC_MAP.keySet()) {
            emotionNames.add(emojiName);
            // 每20个表情作为一组,同时添加到ViewPager对应的view集合中
            if (emotionNames.size() == 23) {
                GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
                emotionViews.add(gv);
                // 添加完一组表情,重新创建一个表情名字集合
                emotionNames = new ArrayList<>();
            }
        }

        // 判断最后是否有不足23个表情的剩余情况
        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
            emotionViews.add(gv);
        }

        emotionPagerAdapter = new EmotionPagerAdapter(emotionViews);
        vp_expression.setAdapter(emotionPagerAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
        vp_expression.setLayoutParams(params);


    }

    /**
     * 创建显示表情的GridView
     */
    private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
        GridView gv = new GridView(mContext);
        gv.setSelector(android.R.color.transparent);
        gv.setNumColumns(8);
        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding * 2);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);
        EmotionGridViewAdapter adapter = new EmotionGridViewAdapter(mContext, emotionNames, itemWidth);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(GlobalOnItemClickManagerUtils.getInstance(mContext).getOnItemClickListener());
        return gv;
    }

}
