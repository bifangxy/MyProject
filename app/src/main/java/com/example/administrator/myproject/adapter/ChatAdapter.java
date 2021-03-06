package com.example.administrator.myproject.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.myproject.adapter.holder.ChatAcceptViewHolder;
import com.example.administrator.myproject.adapter.holder.ChatSendViewHolder;
import com.example.administrator.myproject.data.MessageInfo;
import com.example.administrator.myproject.util.Constants;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;


/**
 * 作者：Rance on 2016/11/29 10:46
 * 邮箱：rance935@163.com
 */
public class ChatAdapter extends RecyclerArrayAdapter<MessageInfo> {

    private onItemClickListener onItemClickListener;
    public Handler handler;
    private Context mContext;

    public ChatAdapter(Context context) {
        super(context);
        mContext = context;
        handler = new Handler();
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case Constants.CHAT_ITEM_TYPE_LEFT:
                viewHolder = new ChatAcceptViewHolder(parent, onItemClickListener, handler, mContext);
                break;
            case Constants.CHAT_ITEM_TYPE_RIGHT:
                viewHolder = new ChatSendViewHolder(parent, onItemClickListener, handler, mContext);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getViewType(int position) {
        return getAllData().get(position).getType();
    }

    public void addItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onHeaderClick(int position);

        void onImageClick(View view, int position);

        void onVoiceClick(ImageView imageView, int position);

    }
}
