package com.example.administrator.myproject.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2017/10/13.
 */

public class CameraAndPicUntil {

    private Activity mContext;

    private String image_file_path;

    private String image_file_name;

    private int output_X = 320;

    private int output_Y = 320;

    public CameraAndPicUntil(Activity mContext, String image_file_path) {
        this.mContext = mContext;
        this.image_file_path = image_file_path;
    }

    public void selectToPicture(int code, String image_file_name) {
        this.image_file_name = image_file_name;
        File file = new File(image_file_path, image_file_name);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Intent mIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//
        mContext.startActivityForResult(mIntent, code);
    }

    public Uri selectToCamera(int code, String image_file_name) {
        this.image_file_name = image_file_name;
        File file = new File(image_file_path, image_file_name);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = FileProvider.getUriForFile(mContext, "com.example.administrator.myproject.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        mContext.startActivityForResult(intent, code);
        return imageUri;
    }

    public Uri cropRawPhoto(Uri uri, int code) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");
        if (android.os.Build.MANUFACTURER.contains("HUAWEI")) {// 华为特殊处理 不然会显示圆
            intent.putExtra("aspectX", 9999);
            intent.putExtra("aspectY", 9998);
        } else {
            // aspectX , aspectY :宽高的比例
            intent.putExtra("aspectX", 1.5);
            intent.putExtra("aspectY", 1);
        }
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        Uri uri_tempFile = Uri.parse("file:///" + image_file_path + "/" + image_file_name);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_tempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        mContext.startActivityForResult(intent, code);
        return uri_tempFile;
    }

    public File setPicToView(Uri uri, ImageView imageView) {
        // 取得SDCard图片路径做显示
        Bitmap photo = null;
        try {
            photo = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Drawable drawable = new BitmapDrawable(null, photo);
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
        File file = null;
        try {
            file = new File(new URI(uri.toString()));
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }


    public int getOutput_X() {
        return output_X;
    }

    public void setOutput_X(int output_X) {
        this.output_X = output_X;
    }

    public int getOutput_Y() {
        return output_Y;
    }

    public void setOutput_Y(int output_Y) {
        this.output_Y = output_Y;
    }

    public String getImage_file_path() {
        return image_file_path;
    }

    public void setImage_file_path(String image_file_path) {
        this.image_file_path = image_file_path;
    }

    public String getImage_file_name() {
        return image_file_name;
    }

    public void setImage_file_name(String image_file_name) {
        this.image_file_name = image_file_name;
    }
}
