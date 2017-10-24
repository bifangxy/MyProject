package com.example.administrator.myproject.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.myproject.R;
import com.example.administrator.myproject.adapter.EmotionPagerAdapter;
import com.example.administrator.myproject.data.MessageInfo;
import com.example.administrator.myproject.util.EmotionViewUtils;
import com.example.administrator.myproject.util.GlobalOnItemClickManagerUtils;
import com.example.administrator.myproject.util.KPSwitchConflictUtil;
import com.example.administrator.myproject.util.KeyboardUtil;
import com.example.administrator.myproject.views.AudioRecorderTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */

public class InputDetector {
    private Activity mContext;

    private View view_emotion;

    private View view_more;

    private EasyRecyclerView rv_message;

    private KPSwitchPanelLinearLayout panel_root;

    private ImageView iv_voice;

    private AudioRecorderTextView tv_audio_voice;

    private EditText et_message_content;

    private ImageView iv_emotion;

    private ImageView iv_more;

    private ImageView iv_send;

    private ViewPager vp_expression;

    private TextView tv_send_img;
    private EmotionPagerAdapter emotionPagerAdapter;

    private boolean isKey = true;


    public InputDetector(Activity mContext) {
        this.mContext = mContext;
    }

    public InputDetector bindToEmotionView(View view) {
        this.view_emotion = view;
        return this;
    }

    public InputDetector bindToMoreView(View view) {
        this.view_more = view;
        return this;
    }

    public InputDetector bindToRecycleView(EasyRecyclerView easyRecyclerView) {
        this.rv_message = easyRecyclerView;
        return this;
    }

    public InputDetector bindToPanelLayout(KPSwitchPanelLinearLayout kpSwitchPanelLinearLayout) {
        this.panel_root = kpSwitchPanelLinearLayout;
        return this;
    }

    public InputDetector bindToVoiceImageView(ImageView imageView) {
        this.iv_voice = imageView;
        return this;
    }

    public InputDetector bindToAudioRecorderButton(AudioRecorderTextView audioRecorderTextView) {
        this.tv_audio_voice = audioRecorderTextView;
        return this;
    }

    public InputDetector bindToContentEditText(EditText editText) {
        this.et_message_content = editText;
        return this;
    }

    public InputDetector bindToEmotionImageView(ImageView imageView) {
        this.iv_emotion = imageView;
        return this;
    }

    public InputDetector bindToMoreImageView(ImageView imageView) {
        this.iv_more = imageView;
        return this;
    }

    public InputDetector bindToSendButton(ImageView imageView) {
        this.iv_send = imageView;
        return this;
    }

    public InputDetector bindToEmotionViewPager(ViewPager viewPager) {
        this.vp_expression = viewPager;
        return this;
    }

    public InputDetector init() {
        initKeyBoard();
        initEmotion();
        initListener();
        return this;
    }

    private void initListener() {
        et_message_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_message_content.getText().length() != 0) {
                    iv_more.setVisibility(View.GONE);
                    iv_send.setVisibility(View.VISIBLE);
                } else {
                    iv_more.setVisibility(View.VISIBLE);
                    iv_send.setVisibility(View.GONE);
                }
            }
        });

        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTextMessage();
            }
        });

        iv_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isKey) {
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(mContext.getWindow().getDecorView().getWindowToken(), 0);
                        }
                        if (panel_root.getVisibility() == View.VISIBLE) {
                            KPSwitchConflictUtil.hidePanelAndKeyboard(panel_root);
                        }
                        iv_voice.setImageResource(R.drawable.keyboard_bg);
                        et_message_content.setVisibility(View.GONE);
                        tv_audio_voice.setVisibility(View.VISIBLE);
                        isKey = false;
                    }
                } else {
                    iv_voice.setImageResource(R.drawable.voice_bg);
                    isKey = true;
                    et_message_content.setVisibility(View.VISIBLE);
                    tv_audio_voice.setVisibility(View.GONE);
                }

            }
        });
        tv_audio_voice.setFinishRecorderCallBack(new AudioRecorderTextView.AudioFinishRecorderCallBack() {
            @Override
            public void onFinish(long times, String filePath) {
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setFilepath(filePath);
                messageInfo.setVoiceTime(times);
                EventBus.getDefault().post(messageInfo);
            }
        });

    }


    private void sendTextMessage() {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setContent(et_message_content.getText().toString());
        EventBus.getDefault().post(messageInfo);
        et_message_content.setText("");
    }


    //防止键盘弹出和底部布局冲突
    private void initKeyBoard() {
        panel_root.setIgnoreRecommendHeight(true);
        KeyboardUtil.attach(mContext, panel_root);
        KPSwitchConflictUtil.attach(panel_root, et_message_content,
                new KPSwitchConflictUtil.SwitchClickListener() {
                    @Override
                    public void onClickSwitch(boolean switchToPanel) {
                        if (switchToPanel) {
                            et_message_content.clearFocus();
                        } else {
                            et_message_content.requestFocus();
                        }
                    }
                },
                new KPSwitchConflictUtil.SubPanelAndTrigger(view_emotion, iv_emotion),
                new KPSwitchConflictUtil.SubPanelAndTrigger(view_more, iv_more));
        rv_message.setLayoutManager(new LinearLayoutManager(mContext));
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

    /**
     * 初始化底部表情栏
     */
    private void initEmotion() {
        List<GridView> emotionViews = EmotionViewUtils.getEmotionGridView(mContext);
        emotionPagerAdapter = new EmotionPagerAdapter(emotionViews);
        vp_expression.setAdapter(emotionPagerAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(EmotionViewUtils.getScreenWidth(), EmotionViewUtils.getGvHeight());
        vp_expression.setLayoutParams(params);
        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(mContext);
        globalOnItemClickListener.attachToEditText(et_message_content);
    }


}
