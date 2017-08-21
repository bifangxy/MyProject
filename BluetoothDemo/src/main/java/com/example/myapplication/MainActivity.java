package com.example.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_BLUETOOTH_ON = 1313;

    private static final String UUID_STR = "00001101-0000-1000-8000-00805F9B34FB";

    private Context mContext;

    private Spinner spinner_device;


    private Button bt_start;


    private Button bt_exit;

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

    private boolean isDiscovering;

    private List<String> dataList;

    private ArrayAdapter<String> adapter;

    private List<BluetoothDevice> deviceList;

    private BluetoothDevice bluetoothDevice;

    private BluetoothSocket bluetoothSocket;

    private ArrayList<Entry> s1_list;

    private ArrayList<Entry> s2_list;

    private ArrayList<Entry> s3_list;

    private ArrayList<Entry> a1_list;

    private ArrayList<Entry> a2_list;

    //    private byte[] test_data = {0x2F, 0xFF, 0x37, 0xEF, 0x40, 0x01, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xEF, 0x40, 0x01, 0x8F, 0xA0, 0x9F, 0xA0, 0xAF, 0xA0, 0xBF, 0xA0, 0xC0, 0x00, 0xB1, 0xCC, 0x33, 0xC3, 0x3C, 0xAA, 0x00, 0x28, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xEE, 0x40, 0x01, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xED, 0x40, 0x01, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xEF, 0x40, 0x01, 0x8F, 0xA0, 0x9F, 0xA0, 0xAF, 0xA0, 0xBF, 0xA0, 0xC0, 0x00, 0xB2, 0xCC, 0x33, 0xC3, 0x3C, 0xAA, 0x00, 0x28, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xEE, 0x40, 0x01, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xED, 0x40, 0x01, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37, 0xEE, 0x40, 0x01, 0x8F, 0xA0, 0x9F, 0xA0, 0xAF, 0xA0, 0xBF, 0xA0, 0xC0, 0x00, 0xB3, 0xCC, 0x33, 0xC3, 0x3C, 0xAA, 0x00, 0x28, 0x0F, 0xFF, 0x1F, 0xFF, 0x2F, 0xFF, 0x37EE, 0x4001, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EF4001, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EF4001, 0x8FA0, 0x9FA0, 0xAFA0, 0xBFA0, 0xC0,0x00, 0xB0,0xCC, 0x33,0xC3, 0x3C,0xAA, 0x0028, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EF4001, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EE, 0x4001, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EE, 0x4001, 0x8FA0, 0x9FA0, 0xAFA0, 0xBFA0, 0xC000, 0xB1CC, 0x33C3, 0x3CAA, 0x0028, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EF4001, 0x0FFF, 0x1FFF, 0x2FFF, 0x37EF4001, 0x0FFF, 0x1FFF, 0x2FFF, 0x37F0, 0x4001, 0x8FA0, 0x9FA0, 0xAFA0, 0xBFA0, 0xC000, 0xAECC, 0x33C3, 0x3CAA, 0x0028, 0x0FFF, 0x1FFF, 0x2FFF, 0x37F0, 0x4001, 0x0FFF, 0x1FFF};
    private byte[] test_data = {
            0x3C, (byte) 0xAA, 0x00, 0x28, 0x0F, (byte) 0xFF, 0x1F, (byte) 0xFF, 0x2F, (byte) 0xFF, 0x37, (byte) 0xEE, 0x40, 0x01, 0x0F, (byte) 0xFF, 0x1F, (byte) 0xFF, 0x2F, (byte) 0xFF, 0x37, (byte) 0xED, 0x40, 0x01, 0x0F, (byte) 0xFF, 0x1F, (byte) 0xFF, 0x2F, (byte) 0xFF, 0x37, (byte) 0xEF, 0x40, 0x01, (byte) 0x8F, (byte) 0xA0, (byte) 0x9F, (byte) 0xA0, (byte) 0xAF, (byte) 0xA0, (byte) 0xBF, (byte) 0xA0, (byte) 0xC0, 0x00, (byte) 0xB2, (byte) 0xCC, 0x33, (byte) 0xC3, 0x3C, (byte) 0xAA, 0x00, 0x28, 0x0F, (byte) 0xFF, 0x1F, (byte) 0xFF, 0x2F, (byte) 0xFF, 0x37, (byte) 0xEE, 0x40, 0x01, 0x0F, (byte) 0xFF, 0x1F, (byte) 0xFF, 0x2F, (byte) 0xFF, 0x37, (byte) 0xED, 0x40, 0x01, 0x0F, (byte) 0xFF, 0x1F, (byte) 0xFF, 0x2F, (byte) 0xFF, 0x37, (byte) 0xEE, 0x40, 0x01, (byte) 0x8F, (byte) 0xA0, (byte) 0x9F, (byte) 0xA0, (byte) 0xAF, (byte) 0xA0, (byte) 0xBF, (byte) 0xA0, (byte) 0xC0, 0x00, (byte) 0xB3, (byte) 0xCC, 0x33, (byte) 0xC3, 0x3C, (byte) 0xAA
    };


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                default:
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        dataList = new ArrayList<>();
        deviceList = new ArrayList<>();
        s1_list = new ArrayList<>();
        s2_list = new ArrayList<>();
        s3_list = new ArrayList<>();
        a1_list = new ArrayList<>();
        a2_list = new ArrayList<>();

        checkBT();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_device.setAdapter(adapter);

        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int s = random.nextInt(100) % (100 + 1);
            Entry entry = new Entry(i, s);
            s1_list.add(entry);
        }
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int s = random.nextInt(100) % (100 + 1);
            Entry entry = new Entry(i, s);
            s2_list.add(entry);
        }
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int s = random.nextInt(100) % (100 + 1);
            Entry entry = new Entry(i, s);
            s3_list.add(entry);
        }
        LineChartManager.initDoubleLineChart(mContext, mChart_one, s1_list, s2_list, s3_list);

    }

    private void initListener() {

        bt_start.setOnClickListener(this);
        bt_exit.setOnClickListener(this);

        spinner_device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Toast.makeText(MainActivity.this, "你点击的是:" + pos, Toast.LENGTH_SHORT).show();
                bluetoothDevice = deviceList.get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start:
                try {
                    bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(UUID_STR));
                    if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                        Method creMethod = BluetoothDevice.class.getMethod("createBond");
                        creMethod.invoke(bluetoothDevice);
                        Toast.makeText(mContext, "未配对", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "已配对", Toast.LENGTH_SHORT).show();
//                        bluetoothSocket.connect();
                        ConnectThread connectThread = new ConnectThread(bluetoothDevice);
                        connectThread.start();
                        Toast.makeText(mContext, "连接成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }


                break;
            case R.id.bt_exit:


                for (int i = 0; i < test_data.length - 10; i++) {
                    if ((test_data[i] & 0xff) == 0xAA) {

                        int high = test_data[i + 1];

                        int low = test_data[i + 2];

                        int leng = 256 * high + low;

                        byte[] values = new byte[leng + 7];

                        System.arraycopy(test_data, i + 1, values, 0, leng + 7);

                        int value = values[0];

                        for (int j = 0; j < values.length - 6; j++) {
                            value = value ^ values[j + 1];
                        }
                        if (values[leng + 2] == value) {
                            if ((values[leng + 3] & 0xff) == 0xCC && (values[leng + 4] & 0xff) == 0x33 && (values[leng + 5] & 0xff) == 0xC3 && (values[leng + 6] & 0xff) == 0x3C) {
                                analysis(values);
                            }
                        }
                    }
                }

                break;
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
                isDiscovering = bt_adapter.startDiscovery();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
                registerReceiver(mReceiver, filter);
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
                }
                break;
                // 点击取消按钮或点击返回键
                case Activity.RESULT_CANCELED:
                    // TODO 用户拒绝打开 Bluetooth, Bluetooth 不会被开启
                    break;
                default:
                    break;
            }
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(LOG_TAG, action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                dataList.add(device.getName());
                deviceList.add(device);
                adapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(mContext, "搜素结束", Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                Toast.makeText(mContext, "连接成功2", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;

        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_STR));
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            bt_adapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }


            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private void manageConnectedSocket(BluetoothSocket mmSocket) {
        ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        connectedThread.start();
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[512];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    for (int i = 0; i < bytes; i++) {
                        if (buffer[i] == 0xAA) {

                            int high = buffer[i + 1];

                            int low = buffer[i + 2];

                            int leng = 256 * high + low;

                            byte[] values = new byte[leng + 7];

                            System.arraycopy(buffer, i + 1, values, 0, leng + 7);

                            int value = values[0];

                            for (int j = 0; j < values.length - 6; j++) {
                                value = value ^ values[j + 1];
                            }
                            if (values[leng + 2] == value) {
                                if (values[leng + 3] == 0xCC && values[leng + 4] == 0x33 && values[leng + 5] == 0xC3 && values[leng + 6] == 0x3C) {
                                    Log.d(LOG_TAG, "-------数据正确-----");
                                    analysis(values);
                                }
                            }
                        } else {
                            i++;
                        }
                    }

                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
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
       /* for (int i = 2; i < values.length - 5; i++) {
            int value = values[i] * 256 + values[i + 1];
            int type = value & 0xf000 >> 12;
            int real_value = value & 0x0fff;
            switch (type) {
                case 0:
                    s1_list.add(real_value);
                    break;
                case 1:
                    s2_list.add(real_value);
                    break;
                case 2:
                    s3_list.add(real_value);
                    break;
                case 3:
                    a1_list.add(real_value);
                    break;
                case 4:
                    a2_list.add(real_value);
                    break;
            }
            i++;
        }*/
    }


}
