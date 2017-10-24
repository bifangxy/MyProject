package com.example.administrator.myproject.adapter.holder;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.myproject.MyApplication;
import com.example.administrator.myproject.R;
import com.example.administrator.myproject.adapter.ChatAdapter;
import com.example.administrator.myproject.data.MessageInfo;
import com.example.administrator.myproject.util.Constants;
import com.example.administrator.myproject.util.Utils;
import com.example.administrator.myproject.views.GifTextView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.io.File;


/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class ChatSendViewHolder extends BaseViewHolder<MessageInfo> {

    TextView chatItemDate;
    ImageView chatItemHeader;
    GifTextView chatItemContentText;
    ImageView chatItemContentImage;
    ImageView chatItemFail;
    ProgressBar chatItemProgress;
    ImageView chatItemVoice;
    LinearLayout chatItemLayoutContent;
    TextView chatItemVoiceTime;
    private ChatAdapter.onItemClickListener onItemClickListener;
    private Handler handler;
    private Context mContext;

    public ChatSendViewHolder(ViewGroup parent, ChatAdapter.onItemClickListener onItemClickListener, Handler handler, Context context) {
        super(parent, R.layout.item_chat_send);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
        this.mContext = context;
        chatItemDate = $(R.id.chat_item_date);
        chatItemHeader = $(R.id.chat_item_header);
        chatItemContentText = $(R.id.chat_item_content_text);
        chatItemContentImage = $(R.id.chat_item_content_image);
        chatItemFail = $(R.id.chat_item_fail);
        chatItemProgress = $(R.id.chat_item_progress);
        chatItemVoice = $(R.id.chat_item_voice);
        chatItemLayoutContent = $(R.id.chat_item_layout_content);
        chatItemVoiceTime = $(R.id.chat_item_voice_time);
    }


    @Override
    public void setData(MessageInfo data) {
        chatItemDate.setText(data.getTime() != null ? data.getTime() : "");
        chatItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("----", "----" + getAdapterPosition() + "------" + getDataPosition());
                onItemClickListener.onHeaderClick(getDataPosition());
            }
        });
        Picasso.with(mContext).load(data.getHeader()).into(chatItemHeader);
        if (data.getContent() != null) {
            chatItemContentText.setSpanText(handler, data.getContent(), true);
            chatItemVoice.setVisibility(View.GONE);
            chatItemContentText.setVisibility(View.VISIBLE);
            chatItemLayoutContent.setVisibility(View.VISIBLE);
            chatItemVoiceTime.setVisibility(View.GONE);
            chatItemContentImage.setVisibility(View.GONE);
        } else if (data.getImageUrl() != null) {
            chatItemVoice.setVisibility(View.GONE);
            chatItemLayoutContent.setVisibility(View.GONE);
            chatItemVoiceTime.setVisibility(View.GONE);
            chatItemContentText.setVisibility(View.GONE);
            chatItemContentImage.setVisibility(View.VISIBLE);
            File file = new File(data.getImageUrl());
            Picasso.with(mContext).load(file).into(chatItemContentImage);
//            Picasso.with(getContext()).load(data.getImageUrl()).into(chatItemContentImage);
            chatItemContentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onImageClick(chatItemContentImage, getDataPosition());
                }
            });
        } else if (data.getFilepath() != null) {
            chatItemVoice.setVisibility(View.VISIBLE);
            chatItemLayoutContent.setVisibility(View.VISIBLE);
            chatItemContentText.setVisibility(View.GONE);
            chatItemVoiceTime.setVisibility(View.VISIBLE);
            chatItemContentImage.setVisibility(View.GONE);
            chatItemVoiceTime.setText(Utils.formatTime(data.getVoiceTime()));
            chatItemLayoutContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onVoiceClick(chatItemVoice, getDataPosition());
                }
            });
        }
        switch (data.getSendState()) {
            case Constants.CHAT_ITEM_SENDING:
                chatItemProgress.setVisibility(View.VISIBLE);
                chatItemFail.setVisibility(View.GONE);
                break;
            case Constants.CHAT_ITEM_SEND_ERROR:
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.VISIBLE);
                break;
            case Constants.CHAT_ITEM_SEND_SUCCESS:
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.GONE);
                break;
        }
    }
}
