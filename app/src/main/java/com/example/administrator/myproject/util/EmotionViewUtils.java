package com.example.administrator.myproject.util;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ashokvarma.bottomnavigation.utils.*;
import com.example.administrator.myproject.MyApplication;
import com.example.administrator.myproject.adapter.EmotionGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */

public class EmotionViewUtils {
    private static Context mContext;

    private static int screenWidth;

    private static int gvHeight;

    public static List<GridView> getEmotionGridView(Context context) {
        mContext = context;
        // 获取屏幕宽度
        screenWidth = MyApplication.screenWidth;
        // item的间距
        int spacing = com.ashokvarma.bottomnavigation.utils.Utils.dp2px(context, 12);
        // 动态计算item的宽度和高度
        int itemWidth = (screenWidth - spacing * 8) / 7;
        //动态计算gridview的总高度
        gvHeight = itemWidth * 3 + spacing * 6;

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
        return emotionViews;
    }


    /**
     * 创建显示表情的GridView
     */
    private static GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
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

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        EmotionViewUtils.screenWidth = screenWidth;
    }

    public static int getGvHeight() {
        return gvHeight;
    }

    public static void setGvHeight(int gvHeight) {
        EmotionViewUtils.gvHeight = gvHeight;
    }
}
