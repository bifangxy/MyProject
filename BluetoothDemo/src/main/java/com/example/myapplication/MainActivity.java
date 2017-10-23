package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_BLUETOOTH_ON = 1313;

    private static final String UUID_STR = "00001101-0000-1000-8000-00805F9B34FB";

    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/BluetoothTest/file/";

    private Context mContext;

    private Spinner spinner_device;


    private Button bt_start;

    private Button bt_exit;

    private Button bt_help;

    private Button bt_save_data;

    private TextView tv_s1_value;
    private TextView tv_s2_value;
    private TextView tv_s3_value;
    private TextView tv_a1_value;
    private TextView tv_a2_value;

    private TextView tv_s1_count;
    private TextView tv_s2_count;
    private TextView tv_s3_count;
    private TextView tv_a1_count;
    private TextView tv_a2_count;

    private ImageView iv_s1_state;
    private ImageView iv_s2_state;
    private ImageView iv_s3_state;
    private ImageView iv_a1_state;
    private ImageView iv_a2_state;


    private LineChart mChart_one;

    private LineChart mChart_two;

    private BluetoothAdapter bt_adapter;

//    private boolean isDiscovering;

    private List<String> dataList;

    private ArrayAdapter<String> adapter;

    private List<BluetoothDevice> deviceList;

    private BluetoothDevice bluetoothDevice;

    private BluetoothSocket bluetoothSocket;

    private List<Float> s1_list;

    private List<Float> s2_list;

    private List<Float> s3_list;

    private List<Float> a1_list;

    private List<Float> a2_list;

    private ArrayList<Entry> s1_list_data;

    private ArrayList<Entry> s2_list_data;

    private ArrayList<Entry> s3_list_data;

    private ArrayList<Entry> a1_list_data;

    private ArrayList<Entry> a2_list_data;

    private float s1_value;

    private float s2_value;

    private float s3_value;

    private float a1_value;

    private float a2_value;

    private int s1_count;

    private int s2_count;

    private int s3_count;

    private int a1_count;

    private int a2_count;

    private boolean isConnect = false;

    private BluetoothSocket mSocket;

    private InputStream inputStream;

    private ConnectThread connectThread;

    private ConnectedThread connectedThread;

    //    private byte[] test_data = {0x2F, 0xFF, 0x37, 0xEF, 0x40, 0x01, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xEF, 0x40, 0x01, 0x8F, 0xA0, 0x9F, 0xA0, 0xAF, 0xA0, 0xBF, 0xA0, 0xC0, 0x00, 0xB1, 0xCC, 0x33, 0xC3, 0x3C, 0xAA, 0x00, 0x28, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xEE, 0x40, 0x01, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xED, 0x40, 0x01, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xEF, 0x40, 0x01, 0x8F, 0xA0, 0x9F, 0xA0, 0xAF, 0xA0, 0xBF, 0xA0, 0xC0, 0x00, 0xB2, 0xCC, 0x33, 0xC3, 0x3C, 0xAA, 0x00, 0x28, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xEE, 0x40, 0x01, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xED, 0x40, 0x01, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xEE, 0x40, 0x01, 0x8F, 0xA0, 0x9F, 0xA0, 0xAF, 0xA0, 0xBF, 0xA0, 0xC0, 0x00, 0xB3, 0xCC, 0x33, 0xC3, 0x3C, 0xAA, 0x00, 0x28, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37EE, 0x4001, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EF4001, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EF4001, 0x8FA0, 0x9FA0, 0xAFA0, 0xBFA0, 0xC0,0x00, 0xB0,0xCC, 0x33,0xC3, 0x3C,0xAA, 0x0028, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EF4001, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EE, 0x4001, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EE, 0x4001, 0x8FA0, 0x9FA0, 0xAFA0, 0xBFA0, 0xC000, 0xB1CC, 0x33C3, 0x3CAA, 0x0028, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EF4001, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EF4001, 0x0FFF, 0x1FFF, 0x2FFF, 0x37F0, 0x4001, 0x8FA0, 0x9FA0, 0xAFA0, 0xBFA0, 0xC000, 0xAECC, 0x33C3, 0x3CAA, 0x0028, 0x0FFF, 0x1FFF, 0x2FFF, 0x37F0, 0x4001, 0x0FFF, 0x1FFF};
    private byte[] test_data = {
            0x3C, (byte) 0xAA, 0x00, 0x28, 0x0F, (byte) 0xFF, 0x1F, (byte) 0xFF, 0x2F, (byte) 0xFF, 0x37, (byte) 0xEE, 0x40, 0x01, 0x0F, (byte) 0xFF, 0x1F, (byte) 0xFF, 0x2F, (byte) 0xFF, 0x37, (byte) 0xED, 0x40, 0x01, 0x0F, (byte) 0xFF, 0x1F, (byte) 0xFF, 0x2F, (byte) 0xFF, 0x37, (byte) 0xEF, 0x40, 0x01, (byte) 0x8F, (byte) 0xA0, (byte) 0x9F, (byte) 0xA0, (byte) 0xAF, (byte) 0xA0, (byte) 0xBF, (byte) 0xA0, (byte) 0xC0, 0x00, (byte) 0xB2, (byte) 0xCC, 0x33, (byte) 0xC3, 0x3C, (byte) 0xAA, 0x00, 0x28, 0x0F, (byte) 0xFF, 0x1F, (byte) 0xFF, 0x2F, (byte) 0xFF, 0x37, (byte) 0xEE, 0x40, 0x01, 0x0F, (byte) 0xFF, 0x1F, (byte) 0xFF, 0x2F, (byte) 0xFF, 0x37, (byte) 0xED, 0x40, 0x01, 0x0F, (byte) 0xFF, 0x1F, (byte) 0xFF, 0x2F, (byte) 0xFF, 0x37, (byte) 0xEE, 0x40, 0x01, (byte) 0x8F, (byte) 0xA0, (byte) 0x9F, (byte) 0xA0, (byte) 0xAF, (byte) 0xA0, (byte) 0xBF, (byte) 0xA0, (byte) 0xC0, 0x00, (byte) 0xB3, (byte) 0xCC, 0x33, (byte) 0xC3, 0x3C, (byte) 0xAA
    };

    private boolean isContinue;

    private SweetAlertDialog mDialog;

    private byte[] catch_bytes = new byte[47];

    private boolean isFinish = true;

    private int index = 0;

    private int save_index;

    private String fileName;

    private String fileContent;

    private boolean isSave = false;

    private boolean is_connected = false;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    s1_list_data.clear();

                    try {
                        for (int i = 0; i < s1_list.size(); i++) {
                            if (s1_list != null && s1_list.get(i) != null) {
                                Entry entry = new Entry(i, s1_list.get(i));
                                s1_list_data.add(entry);
                            }
                        }
                        LineChartManager.initThridLineChart(mContext, mChart_one, s1_list_data, s2_list_data, s3_list_data);
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "---" + e.toString());
                    }

                    tv_s1_value.setText("" + s1_value);
                    break;
                case 1:
                    s2_list_data.clear();

                    try {
                        for (int i = 0; i < s2_list.size(); i++) {
                            if (s2_list != null && s2_list.get(i) != null) {
                                Entry entry = new Entry(i, s2_list.get(i));
                                s2_list_data.add(entry);
                            }
                        }
                        LineChartManager.initThridLineChart(mContext, mChart_one, s1_list_data, s2_list_data, s3_list_data);
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "---" + e.toString());
                    }

                    tv_s2_value.setText("" + s2_value);
                    break;
                case 2:
                    s3_list_data.clear();

                    try {
                        for (int i = 0; i < s3_list.size(); i++) {
                            if (s3_list != null && s3_list.get(i) != null) {
                                Entry entry = new Entry(i, s3_list.get(i));
                                s3_list_data.add(entry);
                            }
                        }
                        LineChartManager.initThridLineChart(mContext, mChart_one, s1_list_data, s2_list_data, s3_list_data);
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "---" + e.toString());
                    }

                    tv_s3_value.setText("" + s3_value);
                    break;
                case 3:
                    a1_list_data.clear();

                    try {
                        for (int i = 0; i < a1_list.size(); i++) {
                            if (a1_list != null && a1_list.get(i) != null) {
                                Entry entry = new Entry(i, a1_list.get(i));
                                a1_list_data.add(entry);
                            }
                        }
                        LineChartManager.initDoubleLineChart(mContext, mChart_two, a1_list_data, a2_list_data);
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "---" + e.toString());
                    }
                    tv_a1_value.setText("" + a1_value);
                    break;
                case 4:
                    a2_list_data.clear();

                    try {
                        for (int i = 0; i < a2_list.size(); i++) {
                            if (a2_list != null && a2_list.get(i) != null) {
                                Entry entry = new Entry(i, a2_list.get(i));
                                a2_list_data.add(entry);
                            }
                        }
                        LineChartManager.initDoubleLineChart(mContext, mChart_two, a1_list_data, a2_list_data);
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "---" + e.toString());
                    }

                    tv_a2_value.setText("" + a2_value);
                    break;
                case 8:
                    tv_s1_count.setText("" + s1_count);
                    if (s1_count < 3800) {
                        iv_s1_state.setImageResource(R.mipmap.circle_wrong);
                    } else {
                        iv_s1_state.setImageResource(R.mipmap.circle_right);
                    }
                    break;
                case 9:
                    tv_s2_count.setText("" + s2_count);
                    if (s2_count < 3800) {
                        iv_s2_state.setImageResource(R.mipmap.circle_wrong);
                    } else {
                        iv_s2_state.setImageResource(R.mipmap.circle_right);
                    }
                    break;
                case 10:
                    tv_s3_count.setText("" + s3_count);
                    if (s3_count < 3800) {
                        iv_s3_state.setImageResource(R.mipmap.circle_wrong);
                    } else {
                        iv_s3_state.setImageResource(R.mipmap.circle_right);
                    }
                    break;
                case 11:
                    tv_a1_count.setText("" + a1_count);
                    if (a1_count < 3800) {
                        iv_a1_state.setImageResource(R.mipmap.circle_wrong);
                    } else {
                        iv_a1_state.setImageResource(R.mipmap.circle_right);
                    }
                    break;
                case 12:
                    tv_a2_count.setText("" + a2_count);
                    if (a2_count < 3800) {
                        iv_a2_state.setImageResource(R.mipmap.circle_wrong);
                    } else {
                        iv_a2_state.setImageResource(R.mipmap.circle_right);
                    }
                    break;
                case 101:
                    Toast.makeText(mContext, "连接成功", Toast.LENGTH_SHORT).show();
                    bt_start.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    bt_start.setClickable(true);
                    is_connected = true;
                    break;
                case 506:
                    bt_adapter.cancelDiscovery();
                    break;
                case 10001:
                    Toast.makeText(mContext, "解析到数据", Toast.LENGTH_SHORT).show();
                    break;
                case 10002:
                    Toast.makeText(mContext, "接收到到数据", Toast.LENGTH_SHORT).show();
                    break;
                case 10003:
                    Toast.makeText(mContext, "正在采集", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initData();
        initListener();
    }

    private void initView() {
        spinner_device = (Spinner) findViewById(R.id.spinner_device);
        bt_start = (Button) findViewById(R.id.bt_start);
        bt_exit = (Button) findViewById(R.id.bt_exit);
        bt_help = (Button) findViewById(R.id.bt_help);
        bt_save_data = (Button) findViewById(R.id.bt_save_data);

        tv_s1_value = (TextView) findViewById(R.id.tv_s1_value);
        tv_s2_value = (TextView) findViewById(R.id.tv_s2_value);
        tv_s3_value = (TextView) findViewById(R.id.tv_s3_value);
        tv_a1_value = (TextView) findViewById(R.id.tv_a1_value);
        tv_a2_value = (TextView) findViewById(R.id.tv_a2_value);

        tv_s1_count = (TextView) findViewById(R.id.tv_s1_count);
        tv_s2_count = (TextView) findViewById(R.id.tv_s2_count);
        tv_s3_count = (TextView) findViewById(R.id.tv_s3_count);
        tv_a1_count = (TextView) findViewById(R.id.tv_a1_count);
        tv_a2_count = (TextView) findViewById(R.id.tv_a2_count);

        iv_s1_state = (ImageView) findViewById(R.id.iv_s1_state);
        iv_s2_state = (ImageView) findViewById(R.id.iv_s2_state);
        iv_s3_state = (ImageView) findViewById(R.id.iv_s3_state);
        iv_a1_state = (ImageView) findViewById(R.id.iv_a1_state);
        iv_a2_state = (ImageView) findViewById(R.id.iv_a2_state);

        mChart_one = (LineChart) findViewById(R.id.line_chart_one);
        mChart_two = (LineChart) findViewById(R.id.line_chart_two);


    }

    private void initData() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        save_index = 0;

        dataList = new ArrayList<>();
        deviceList = new ArrayList<>();
        s1_list = new ArrayList<>();
        s2_list = new ArrayList<>();
        s3_list = new ArrayList<>();
        a1_list = new ArrayList<>();
        a2_list = new ArrayList<>();

        dataList.add("选择设备");

        s1_list_data = new ArrayList<>();
        s2_list_data = new ArrayList<>();
        s3_list_data = new ArrayList<>();
        a1_list_data = new ArrayList<>();
        a2_list_data = new ArrayList<>();
        s1_list_data.add(new Entry(0, 0));
        s2_list_data.add(new Entry(0, 0));
        s3_list_data.add(new Entry(0, 0));
        a1_list_data.add(new Entry(0, 0));
        a2_list_data.add(new Entry(0, 0));

        /*mDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.setTitleText("搜索中");*/

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_device.setAdapter(adapter);

        checkBT();

        mChart_one.setDescription(null);
        mChart_two.setDescription(null);
        ArrayList<Entry> datalist = new ArrayList<>();
        datalist.add(new Entry(0, 20));
        LineChartManager.initThridLineChart(mContext, mChart_one, datalist, datalist, datalist);
        LineChartManager.initDoubleLineChart(mContext, mChart_two, datalist, datalist);

    }

    private void initListener() {
        bt_start.setOnClickListener(this);
        bt_exit.setOnClickListener(this);
        bt_help.setOnClickListener(this);
        bt_start.setClickable(false);
        bt_save_data.setOnClickListener(this);


        spinner_device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if (pos != 0) {
                    try {
                        if (connectedThread != null) {
                            connectedThread.cancel();
                        }
                    } catch (Exception e) {

                    }
                    bt_start.setBackgroundColor(Color.parseColor("#eaeaea"));
                    bt_start.setClickable(false);
                    is_connected = false;
                    bluetoothDevice = deviceList.get(pos - 1);
                    try {
                        bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(UUID_STR));
                        if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                            Method creMethod = BluetoothDevice.class.getMethod("createBond");
                            creMethod.invoke(bluetoothDevice);
                            Toast.makeText(mContext, "配对中...", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "连接中...", Toast.LENGTH_SHORT).show();
                            connectThread = new ConnectThread(bluetoothDevice);
                            connectThread.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
                Toast.makeText(mContext, "无法获取相应权限", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start:
                if (!isConnect) {
                    isFinish = true;
                    s1_list.clear();
                    s2_list.clear();
                    s3_list.clear();
                    a1_list.clear();
                    a2_list.clear();
                    bt_start.setText("停止采集");
                    isContinue = true;
                    isConnect = true;
                } else {
                    isContinue = false;
                    isConnect = false;

                    s1_list_data.clear();
                    s2_list_data.clear();
                    s3_list_data.clear();
                    a1_list_data.clear();
                    a2_list_data.clear();

                    s1_list_data.add(new Entry(0, 0));
                    s2_list_data.add(new Entry(0, 0));
                    s3_list_data.add(new Entry(0, 0));
                    a1_list_data.add(new Entry(0, 0));
                    a2_list_data.add(new Entry(0, 0));


                    LineChartManager.initThridLineChart(mContext, mChart_one, s1_list_data, s2_list_data, s3_list_data);
                    LineChartManager.initDoubleLineChart(mContext, mChart_two, a1_list_data, a2_list_data);

                    bt_start.setText("开始采集");
                }

                break;
            case R.id.bt_exit:
                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("确认退出程序？")
                        .setCancelText("取消")
                        .setConfirmText("确认")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                        })
                        .show();
                break;
            case R.id.bt_help:
                new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("使用帮助")
                        .setContentText("1.从设备列表中点选需要连接的目标设备，等待蓝牙连接；\n" +
                                "2.当显示屏显示\"连接成功\"以后，点击\"开始采集\"按钮；\n" +
                                "3.当数据采集完成后，点击\"停止采集\"按钮即可；\n" +
                                "4.当需要进行数据保存时，先点击“保存数据”，此时在弹出的窗口中输入文件名称（即设备的SN），输入完成后点击“OK”；\n" +
                                "5.保存的数据文件在BluetoothTest\\file路径下.")
                        .setConfirmText("确定")
                        .show();
                break;
            case R.id.bt_save_data:
                if (!isSave) {
                    if (is_connected) {
                        verifyStoragePermissions(MainActivity.this);
                        final EditText inputServer = new EditText(mContext);
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("请输入保存文件名").setView(inputServer)
                                .setNegativeButton("Cancel", null);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                fileName = inputServer.getText().toString() + ".txt";
                                isSave = true;
                                bt_save_data.setText("停止保存");
                                fileContent = "Sensor datalog file\t\t\n" +
                                        "Version:\t\n" +
                                        "hostname:\t" + bluetoothDevice.getName() + "\n" +
                                        "Data information:\t" + fileName + "\n" +
                                        "System time:\t" + DateUtils.getYMDHm1(new Date()) + "\n" +
                                        "**FILE HEADER END**\n" +
                                        "S1\t\t\tS2\t\t\tS3\t\t\tA1\t\t\tA2\t\t\tS1_NUM\t\tS2_NUM\t\tS3_NUM\t\tA1_NUM\t\tA2_NUM\n";
                            }
                        });
                        builder.show();
                    } else {
                        Toast.makeText(mContext, "请先连接蓝牙", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    isSave = false;
                    bt_save_data.setText("保存数据");
                    writeTxtToFile(fileContent, FILE_PATH, fileName);
                }
                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (connectedThread != null) {
                connectedThread.cancel();
            }
        } catch (Exception e) {

        }

    }

    public void checkBT() {
        bt_adapter = BluetoothAdapter.getDefaultAdapter();
        if (bt_adapter != null) {
            if (!bt_adapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // 设置蓝牙可见性，最多300秒
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
                startActivityForResult(intent, REQUEST_CODE_BLUETOOTH_ON);
            } else {
//                isDiscovering = bt_adapter.startDiscovery();
//                mDialog.show();

                Set<BluetoothDevice> devices = bt_adapter.getBondedDevices();
                if (devices.size() > 0) {
                    for (Iterator iterator = devices.iterator(); iterator.hasNext(); ) {
                        BluetoothDevice bluetoothDevice = (BluetoothDevice) iterator.next();
                        dataList.add(bluetoothDevice.getName());
                        deviceList.add(bluetoothDevice);
                    }
                }
                adapter.notifyDataSetChanged();

              /*  IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
                filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                registerReceiver(mReceiver, filter);*/
            }
        } else {

        }
    }

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // requestCode 与请求开启 Bluetooth 传入的 requestCode 相对应
        if (requestCode == REQUEST_CODE_BLUETOOTH_ON) {
            switch (resultCode) {
                // 点击确认按钮
                case -1: {
                    // TODO 用户选择开启 Bluetooth，Bluetooth 会被开启
//                    isDiscovering = bt_adapter.startDiscovery();
//                    mHandler.sendEmptyMessageDelayed(506, 10000);
//                    mDialog.show();
                    /*IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                    filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
                    registerReceiver(mReceiver, filter);*/
                    Set<BluetoothDevice> devices = bt_adapter.getBondedDevices();
                    if (devices.size() > 0) {
                        for (Iterator iterator = devices.iterator(); iterator.hasNext(); ) {
                            BluetoothDevice bluetoothDevice = (BluetoothDevice) iterator.next();
                            dataList.add(bluetoothDevice.getName());
                            deviceList.add(bluetoothDevice);
                        }
                    }
                    adapter.notifyDataSetChanged();

                }
                break;
                // 点击取消按钮或点击返回键
                case Activity.RESULT_CANCELED:
                    // TODO 用户拒绝打开 Bluetooth, Bluetooth 不会被开启
                    Toast.makeText(mContext, "蓝牙未打开", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

   /* private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                dataList.add(device.getName());
                deviceList.add(device);
//                adapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                adapter.notifyDataSetChanged();
                Toast.makeText(mContext, "搜素结束", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            } else if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                Toast.makeText(mContext, "连接成功2", Toast.LENGTH_SHORT).show();
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
                if (state == 12) {
                    ConnectThread connectThread = new ConnectThread(bluetoothDevice);
                    connectThread.start();
                    Toast.makeText(mContext, "连接中...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "配对失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };*/


    private class ConnectThread extends Thread {
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_STR));
            } catch (IOException e) {
            }
            mSocket = tmp;

        }

        public void run() {
            try {
                mSocket.connect();
            } catch (IOException connectException) {
                try {
                    mSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }
            mHandler.sendEmptyMessage(101);
            manageConnectedSocket(mSocket);
        }

        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private void manageConnectedSocket(BluetoothSocket mmSocket) {
        connectedThread = new ConnectedThread(mmSocket);
        connectedThread.start();
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            inputStream = tmpIn;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            isContinue = false;
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = inputStream.read(buffer);
                    if (isContinue) {
                        for (int i = 0; i < bytes; i++) {
                            if (i == 0 && !isFinish) {
                                if (bytes >= 47 - index) {
                                    isFinish = true;
                                    System.arraycopy(buffer, i, catch_bytes, index, 47 - index);
                                    i = i + 46 - index;

                                    int high = (catch_bytes[0] & 0xff);
                                    int low = (catch_bytes[1] & 0xff);
                                    int leng = 256 * high + low;
                                    if (leng != 40) {
                                        return;
                                    }
                                    int value = catch_bytes[0];
                                    for (int j = 0; j < catch_bytes.length - 6; j++) {
                                        value = value ^ catch_bytes[j + 1];
                                    }
                                    if (catch_bytes[leng + 2] == value) {
                                        if ((catch_bytes[leng + 3] & 0xff) == 0xCC && (catch_bytes[leng + 4] & 0xff) == 0x33 && (catch_bytes[leng + 5] & 0xff) == 0xC3 && (catch_bytes[leng + 6] & 0xff) == 0x3C) {
                                            analysis(catch_bytes);
                                        }
                                    }

                                } else {
                                    isFinish = false;
                                    System.arraycopy(buffer, i, catch_bytes, index, bytes);
                                    index = index + bytes;
                                    i = bytes - 1;
                                }
                            } else {
                                if ((buffer[i] & 0xff) == 0xAA) {
                                    if (bytes - i >= 48) {
                                        isFinish = true;
                                        int high = (buffer[i + 1] & 0xff);
                                        int low = (buffer[i + 2] & 0xff);
                                        int leng = 256 * high + low;
                                        if (leng != 40) {
                                            return;
                                        }
                                        byte[] values = new byte[leng + 7];
                                        System.arraycopy(buffer, i + 1, values, 0, leng + 7);
                                        int value = values[0];
                                        for (int j = 0; j < values.length - 6; j++) {
                                            value = value ^ values[j + 1];
                                        }
                                        if (values[leng + 2] == value) {
                                            if ((values[leng + 3] & 0xff) == 0xCC && (values[leng + 4] & 0xff) == 0x33 && (values[leng + 5] & 0xff) == 0xC3 && (values[leng + 6] & 0xff) == 0x3C) {
                                                analysis(values);
                                                i = i + 46;
                                            }
                                        }
                                    } else {
                                        isFinish = false;
                                        index = bytes - i - 1;
                                        System.arraycopy(buffer, i + 1, catch_bytes, 0, index);
                                        i = bytes - 1;
                                    }

                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }


        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private void analysis(byte[] values) {
        for (int i = 2; i < values.length - 5; i++) {
            int value = (values[i] & 0xff) * 256 + ((values[i + 1]) & 0xff);
            int type = (value & 0xf000) >> 12;
            int real_value = value & 0x0fff;
            switch (type) {
                case 0:
                    s1_value = ((float) 20 / 4095) * real_value;
                    if (s1_list.size() > 200) {
                        s1_list.remove(200);
                    }
                    s1_list.add(0, s1_value);
                    if (save_index == 0) {
                        if (Math.abs(s1_value) < 10) {
                            fileContent = fileContent + "0" + String.format("%.5f", s1_value) + "\t";
                        } else {
                            fileContent = fileContent + String.format("%.5f", s1_value) + "\t";
                        }
                    }
                    break;
                case 1:
                    s2_value = ((float) 20 / 4095) * real_value;
                    if (s2_list.size() > 200) {
                        s2_list.remove(200);
                    }
                    s2_list.add(0, s2_value);
                    if (save_index == 0) {
                        if (Math.abs(s2_value) < 10) {
                            fileContent = fileContent + "0" + String.format("%.5f", s2_value) + "\t";
                        } else {
                            fileContent = fileContent + String.format("%.5f", s2_value) + "\t";
                        }
                    }
                    break;
                case 2:
                    s3_value = ((float) 20 / 4095) * real_value;
                    if (s3_list.size() > 200) {
                        s3_list.remove(200);
                    }
                    s3_list.add(0, s3_value);
                    if (save_index == 0) {
                        if (Math.abs(s3_value) < 10) {
                            fileContent = fileContent + "0" + String.format("%.5f", s3_value) + "\t";
                        } else {
                            fileContent = fileContent + String.format("%.5f", s3_value) + "\t";
                        }
                    }

                    break;
                case 3:
                    a1_value = ((float) 49 / 2048) * (real_value - 2048);
                    if (a1_list.size() > 200) {
                        a1_list.remove(200);
                    }
                    a1_list.add(0, a1_value);
                    if (save_index == 0) {
                        if (Math.abs(a1_value) < 10) {
                            fileContent = fileContent + "0" + String.format("%.5f", a1_value) + "\t";
                        } else {
                            fileContent = fileContent + String.format("%.5f", a1_value) + "\t";
                        }
                    }
                    break;
                case 4:
                    a2_value = ((float) 49 / 2048) * (real_value - 2048);
                    if (a2_list.size() > 200) {
                        a2_list.remove(200);
                    }
                    a2_list.add(0, a2_value);
                    if (save_index == 0) {
                        if (Math.abs(a2_value) < 10) {
                            fileContent = fileContent + "0" + String.format("%.5f", a2_value) + "\t";
                        } else {
                            fileContent = fileContent + String.format("%.5f", a2_value) + "\t";
                        }
                        save_index = 1;
                    }
                    break;
                case 8:
                    s1_count = real_value;
                    if (Math.abs(s1_count) < 10) {
                        fileContent = fileContent + "0" + String.format("%.5f", (float) s1_count) + "\t";
                    } else {
                        fileContent = fileContent + String.format("%.5f", (float) s1_count) + "\t";
                    }

                    break;
                case 9:
                    s2_count = real_value;
                    if (Math.abs(s2_count) < 10) {
                        fileContent = fileContent + "0" + String.format("%.5f", (float) s2_count) + "\t";
                    } else {
                        fileContent = fileContent + String.format("%.5f", (float) s2_count) + "\t";
                    }

                    break;
                case 10:
                    s3_count = real_value;
                    if (Math.abs(s3_count) < 10) {
                        fileContent = fileContent + "0" + String.format("%.5f", (float) s3_count) + "\t";
                    } else {
                        fileContent = fileContent + String.format("%.5f", (float) s3_count) + "\t";
                    }

                    break;
                case 11:
                    a1_count = real_value;
                    if (Math.abs(a1_count) < 10) {
                        fileContent = fileContent + "0" + String.format("%.5f", (float) a1_count) + "\t";
                    } else {
                        fileContent = fileContent + String.format("%.5f", (float) a1_count) + "\t";
                    }

                    break;
                case 12:
                    a2_count = real_value;
                    save_index = 0;
                    if (Math.abs(a2_count) < 10) {
                        fileContent = fileContent + "0" + String.format("%.5f", (float) a2_count) + "\n";
                    } else {
                        fileContent = fileContent + String.format("%.5f", (float) a2_count) + "\n";
                    }

                    break;
                default:
                    break;
            }
            mHandler.sendEmptyMessage(type);
            i++;
        }
    }


    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d(LOG_TAG, "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error on write File:" + e);
        }
    }

    // 生成文件
    public File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            Log.i(LOG_TAG, e + "");
        }
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

}
