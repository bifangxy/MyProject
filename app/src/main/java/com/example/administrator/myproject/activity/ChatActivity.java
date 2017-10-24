package com.example.administrator.myproject.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myproject.R;
import com.example.administrator.myproject.adapter.ChatAdapter;
import com.example.administrator.myproject.data.MessageInfo;
import com.example.administrator.myproject.util.CameraAndPicUntil;
import com.example.administrator.myproject.util.Constants;
import com.example.administrator.myproject.util.KPSwitchConflictUtil;
import com.example.administrator.myproject.util.MediaManager;
import com.example.administrator.myproject.views.AudioRecorderTextView;
import com.example.administrator.myproject.widget.InputDetector;
import com.example.administrator.myproject.widget.KPSwitchPanelLinearLayout;
import com.jude.easyrecyclerview.EasyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Xieying on 2017/9/26.
 */
public class ChatActivity extends Activity implements View.OnClickListener {
    private static final String LOG_TAG = ChatActivity.class.getSimpleName();
    private static final String IMAGE_FILE_PATH = Environment.getExternalStorageDirectory() + "/Chat/images";
    private Context mContext;

    private View view_emotion;
    private View view_more;

    private KPSwitchPanelLinearLayout panel_root;
    private EasyRecyclerView rv_message;
    private ImageView iv_voice;
    private AudioRecorderTextView tv_audio_voice;
    private EditText et_message;
    private ImageView iv_emotion;
    private ImageView iv_more;
    private ImageView iv_send;
    private ViewPager vp_expression;
    private TextView tv_send_img;
    private TextView tv_photo;

    private ChatAdapter chatAdapter;
    private InputDetector mDetector;
    private LinearLayoutManager layoutManager;

    private List<MessageInfo> messageInfoList = new ArrayList<>();

    private int animationRes = 0;
    private int res = 0;
    private AnimationDrawable animationDrawable = null;
    private ImageView animView;

    private CameraAndPicUntil mCameraAndPicUntil;

    private Uri mUri;
    private Uri outUri;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = this;
        initView();
        initData();
        initListener();

    }

    private void initView() {
        rv_message = (EasyRecyclerView) findViewById(R.id.rv_message);
        et_message = (EditText) findViewById(R.id.et_message);
        iv_more = (ImageView) findViewById(R.id.iv_plus);
        iv_emotion = (ImageView) findViewById(R.id.iv_expression);
        tv_send_img = (TextView) findViewById(R.id.send_img_tv);
        tv_photo = (TextView) findViewById(R.id.tv_photo);
        panel_root = (KPSwitchPanelLinearLayout) findViewById(R.id.panel_root);
        view_emotion = panel_root.findViewById(R.id.view_emotion);
        view_more = panel_root.findViewById(R.id.view_more);
        iv_send = (ImageView) findViewById(R.id.iv_send);
        tv_audio_voice = (AudioRecorderTextView) findViewById(R.id.tv_audio_voice);
        vp_expression = (ViewPager) panel_root.findViewById(R.id.vp_emotion);
        iv_voice = (ImageView) findViewById(R.id.iv_voice);
    }

    private void initData() {
        messageInfoList = new ArrayList<>();
        EventBus.getDefault().register(this);
        mDetector = new InputDetector(this)
                .bindToEmotionView(view_emotion)
                .bindToMoreView(view_more)
                .bindToRecycleView(rv_message)
                .bindToPanelLayout(panel_root)
                .bindToVoiceImageView(iv_voice)
                .bindToAudioRecorderButton(tv_audio_voice)
                .bindToContentEditText(et_message)
                .bindToEmotionImageView(iv_emotion)
                .bindToMoreImageView(iv_more)
                .bindToSendButton(iv_send)
                .bindToEmotionViewPager(vp_expression)
                .init();
        initAdapter();
        mCameraAndPicUntil = new CameraAndPicUntil(this, IMAGE_FILE_PATH);
    }

    private void initListener() {
        tv_send_img.setOnClickListener(this);
        tv_photo.setOnClickListener(this);
    }


    private void initAdapter() {
        chatAdapter = new ChatAdapter(mContext);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_message.setLayoutManager(layoutManager);
        rv_message.setAdapter(chatAdapter);
        chatAdapter.addItemClickListener(itemClickListener);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final MessageInfo messageInfo) {
        messageInfo.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
        chatAdapter.add(messageInfo);
        messageInfoList.add(messageInfo);
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

    /**
     * item点击事件
     */
    private ChatAdapter.onItemClickListener itemClickListener = new ChatAdapter.onItemClickListener() {
        @Override
        public void onHeaderClick(int position) {
            Toast.makeText(ChatActivity.this, "onHeaderClick", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onImageClick(View view, int position) {

        }

        @Override
        public void onVoiceClick(final ImageView imageView, final int position) {
            if (animView != null) {
                animView.setImageResource(res);
                animView = null;
            }
            switch (messageInfoList.get(position).getType()) {
                case 1:
                    animationRes = R.drawable.voice_left;
                    res = R.mipmap.icon_voice_left3;
                    break;
                case 2:
                    animationRes = R.drawable.voice_right;
                    res = R.mipmap.icon_voice_right3;
                    break;
            }
            animView = imageView;
            animView.setImageResource(animationRes);
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
            MediaManager.playSound(messageInfoList.get(position).getFilepath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animView.setImageResource(res);
                }
            });
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_img_tv:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 909);
                } else {
                    selectCamera();
                }
                break;
            case R.id.tv_photo:
                selectPic();
                break;
        }
    }

    private void selectPic() {
        String file_name = new Date().getTime() + ".jpg";
        mCameraAndPicUntil.selectToPicture(1, file_name);
    }

    private void selectCamera() {
        String file_name = new Date().getTime() + ".jpg";
        outUri = mCameraAndPicUntil.selectToCamera(2, file_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Log.d(LOG_TAG, "----" + getRealPathFromURI(data.getData()) + "----");
                    mUri = mCameraAndPicUntil.cropRawPhoto(data.getData(), 3);
                    break;
                case 2:
                    Log.d(LOG_TAG, "----" + outUri.getPath() + "----");
                    mUri = mCameraAndPicUntil.cropRawPhoto(outUri, 3);
                    break;
                case 3:
                    Log.d(LOG_TAG, "----" + mUri.getPath() + "----");
                    File file = mCameraAndPicUntil.setPicToView(mUri, null);
                    Log.d(LOG_TAG, "----" + file.getPath() + "----");

                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setImageUrl(file.getPath());
                    EventBus.getDefault().post(messageInfo);
                    break;
            }
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
