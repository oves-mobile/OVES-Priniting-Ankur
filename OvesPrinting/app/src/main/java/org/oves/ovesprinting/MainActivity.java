package org.oves.ovesprinting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lvrenyang.io.BTPrinting;
import com.lvrenyang.io.Pos;

import org.oves.ovesprinting.task.TaskCmd;
import org.oves.ovesprinting.utils.AppConst;
import org.oves.ovesprinting.utils.PrinterApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.printlibrary.utils.ArrayUtils.convert2HexArray;
import static com.example.printlibrary.utils.StringUtils.byte2hex;
import static com.example.printlibrary.utils.StringUtils.hexStringToBytes;
import static com.example.printlibrary.utils.StringUtils.randomHexString;
import static org.oves.ovesprinting.cpp.NativeUtil.EncodeBuffer;
import static org.oves.ovesprinting.cpp.NativeUtil.KeyInit;
import static org.oves.ovesprinting.cpp.NativeUtil.encodeBuffer;
import static org.oves.ovesprinting.cpp.NativeUtil.keyInit;

public class MainActivity extends AppCompatActivity {

    private TextView tvStatus;
    private Button btnBluetooth;
    private Button btnPrint;
    private boolean isPrinterConnected;

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1199;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.tvStatus);
        btnBluetooth = findViewById(R.id.btnBluetooth);
        btnPrint = findViewById(R.id.btnPrint);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBluetoothClick();
            }
        });
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPrintClick();
            }
        });
        onCreateActivity();
    }


    private void onCreateActivity() {
        resetUI();
        validateBleSupportUI();
    }

    private void validateBleSupportUI() {
        if (isBLESupported()) {
            initBTAdapter();
            initBroadcast();
        } else {
            printStatus("Device does not support the Bluetooth Function");
            printButtonText("NOT SUPPORTED");
            btnBluetooth.setEnabled(false);
        }
    }

    private BluetoothAdapter mBluetoothAdapter;

    private void initBTAdapter() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
    }

    private BroadcastReceiver broadcastReceiver = null;
    private boolean isDeviceFound;


    private void initBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String action = intent.getAction();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        printLog("ACTION_FOUND");
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if (device != null) {
                            String address = device.getAddress();
                            String name = device.getName();
                            if (name == null) {
                                name = "Unnamed";
                            } else if (name.equals(address)) {
                                name = "BlueTooth";
                            }
                            printLog("Address: " + device.getAddress() + " Name: " + name + " TYPE: " + device.getType());
                            if (name.contentEquals("iKANOO i808")) {
                                connectToDevice(device);
                            }
                            if (name.contentEquals("MBT-II")) {
                                connectToDevice(device);
                            }
                            if (name.contentEquals("MPT-II")) {
                                connectToDevice(device);
                            }
                        }
                    } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                        Log.e("ANKUR", "ACTION_DISCOVERY_STARTED");
                        printButtonText("Scanning");
                        printStatus("Scanning Bluetooth Devices...");
                    } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                        Log.e("ANKUR", "ACTION_DISCOVERY_FINISHED");
                        if (!isDeviceFound) {
                            printButtonText("Connect");
                            printStatus("No printer connected");
                        }
                    }
                }
            }

        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void connectToDevice(BluetoothDevice bluetoothDevice) {
        mBluetoothAdapter.cancelDiscovery();
        isDeviceFound = true;
        printButtonText("Found");
        printStatus("Found Bluetooth Device...");
        startConnectingToBTPrinter(bluetoothDevice.getAddress());
    }

    private Pos m_pos = new Pos();
    private BTPrinting m_bt = new BTPrinting();

    private void startConnectingToBTPrinter(String btAddress) {
        Log.e("ANKUR", "startConnectingToBTPrinter");
        m_pos.Set(m_bt);
        boolean result = m_bt.Open(btAddress, this);
        if (result && m_bt.IsOpened()) {
            printStatus("Printer Device Connected");
            printButtonText(getString(R.string.str_disconnect));
            isPrinterConnected = true;
            btnPrint.setVisibility(View.VISIBLE);
        }
    }

    private void unRegisterBroadcast() {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }


    private void btnBluetoothClick() {
        if (btnBluetooth.getText().toString().equalsIgnoreCase(getString(R.string.str_disconnect))) {
            reConnectBT();
        } else {
            connectBT();
        }
    }

    private void reConnectBT() {
        if (mBluetoothAdapter != null) {
            int currentState = mBluetoothAdapter.getState();
            Log.e("ANKUR", "State: " + currentState);
            if (currentState == BluetoothAdapter.STATE_CONNECTED) {
                Toast.makeText(this, "Device Connected Already", Toast.LENGTH_SHORT).show();
            } else {
                connectBT();
            }
        } else {
            connectBT();
        }
    }

    private void btnPrintClick() {
        if (isPrinterConnected) {
            //  m_pos.POS_S_TextOut("Hello Oves",2,0,0,0,1);
            PrintTextDemo();
        }
    }


    private final int REQUEST_ENABLE_BT = 1;

    private void connectBT() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mBluetoothAdapter.isEnabled()) {
                if (mBluetoothAdapter.enable()) {
                    String boundedPrinterAddress = isPrinterBounded();
                    if (boundedPrinterAddress != null) {
                        startConnectingToBTPrinter(boundedPrinterAddress);
                    } else {
                        mBluetoothAdapter.startDiscovery();
                    }
                } else {
                    printStatus("DEVICE CAN NOT SUPPORT BLUETOOTH");
                    printButtonText("NO BLUETOOTH");
                }
            } else {
                printLog("Bluetooth have no permission");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private String isPrinterBounded() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return null;
        }
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                String name = device.getName();
                if (name.contentEquals("iKANOO i808")) {
                    return device.getAddress();
                }
                if (name.contentEquals("MBT-II")) {
                    return device.getAddress();
                }
                if (name.contentEquals("MPT-II")) {
                    return device.getAddress();
                }
            }
        }
        return null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                connectBT();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            connectBT();
        }
    }


    private void printLog(String message) {
        Log.e("ANKUR", message);
    }

    private boolean isBluetoothEnabled() {
        return mBluetoothAdapter == null || mBluetoothAdapter.isEnabled();
    }

    private void printStatus(String message) {
        tvStatus.setText(message);
    }

    private void printButtonText(String message) {
        btnBluetooth.setText(message);
    }

    private void resetUI() {
        printStatus("No Printer Connected");
        printButtonText("Connect");
        isPrinterConnected = false;
        btnPrint.setVisibility(View.GONE);
    }

    private boolean isBLESupported() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
    }


    // print text demo
    private void PrintTextDemo() {
        try {
            // 初始化打印机，打印机恢复默认值
            // init printer, reset printer
            sendBuffer(hexStringToBytes(AppConst.PRNCMD_TEST_INIT));
            // 打印文本
            // print text
            sendBuffer(hexStringToBytes(AppConst.PRNCMD_TEST_LEFT));        // print aligne left
            sendBuffer(PrinterApi.getPrintStringGBKBytes("123456789\n"));

            sendBuffer(hexStringToBytes(AppConst.PRNCMD_TEST_CENTER));        // print aligne center
            sendBuffer(PrinterApi.getPrintStringGBKBytes("ABCDEFGHIJKL\n"));

            sendBuffer(hexStringToBytes(AppConst.PRNCMD_TEST_RIGHT));        // print aligne right
            sendBuffer(PrinterApi.getPrintStringGBKBytes("abcdefghijkl\n"));

            // 初始化打印机，打印机恢复默认值
            // init printer, reset printer
            sendBuffer(hexStringToBytes(AppConst.PRNCMD_TEST_INIT));
        } catch (Exception e) {
            printButtonText("" + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean sendBuffer(byte[] data) {
        return m_pos.POS_SendBuffer(data);
    }

    // 获取监测数据
    private byte[] getPrintCheckBytes(String mRandStr) {
        String str = "";
        byte[] dat = hexStringToBytes(mRandStr);        // 1. Generate random numbers
        if (true) {// Strong encryption.
            KeyInit(TaskCmd.mEncryptPsw);        // Set encryption password
            EncodeBuffer(dat);                        // 2. Encrypted data
            str = AppConst.PRNCMD_TEST_ENCRYPT + byte2hex(dat);    // Add to 1e5618 + d1...dn
        } else { // Encryption 2
            keyInit(TaskCmd.mEncryptKey, TaskCmd.mEncryptKey2);
            encodeBuffer(dat);  // encrypt
            str = AppConst.PRNCMD_TEST_QUERY + byte2hex(dat);
        }
        byte[] buffer = convert2HexArray(str);
        return buffer;
    }

    private boolean getPrinterStatus(byte[] dat, String mRandStr) {
        int retry = 0;
        boolean isOk = false;
        byte[] readBuffer = new byte[128];

        // 是否过滤指定的打印机
        if (false) {
            do {
                isOk = false;
                sendRead(readBuffer, readBuffer.length, 100 + retry * 100);        // read dummy data
                boolean result = sendWrite(dat, dat.length);            //发送随机数
                if (result) {
                    isOk = waitPrinterAck(readBuffer, readBuffer.length, mRandStr, 2000);//读取返回数据
                }
                retry++;
            } while (!isOk && retry < 3);    // 重试2次
        } else {        // 不过滤指定的打印机
            isOk = true;
        }
        return isOk;
    }

    // 写数据
    private boolean sendWrite(byte[] buffer, int count) {
        if (m_pos.POS_SendBuffer(buffer, 0, count) == count) {
            return true;
        } else {
            return false;
        }
    }

    // 读数据
    private int sendRead(byte[] buffer, int count, int timeout) {
        int read = m_pos.POS_ReadBuffer(buffer, 0, count, timeout);
        if (read != -1 && read != 0 && read != count) {
            return read;
        } else {
            return -2;
        }
    }

    private boolean waitPrinterAck(byte[] buffer, int max_size, String valueNum, int timeout) {
        int time = 0;
        int pos = 0;
        int count = 8;
        do {
            int read = m_pos.POS_ReadBuffer(buffer, pos, 1, 100);
            if (read > 0) {
                time = 0;   // 重新计时
                pos += read;
                if (pos >= count) {
                    boolean equals = false;
                    byte[] orig = hexStringToBytes(valueNum);
                    if (orig.length >= count) {
                        equals = true;
                        // 第一个字节的数据为版本号
                        for (int i = 1; i < count; i++) {   // first type is command type
                            if (orig[i] != buffer[i]) {
                                equals = false;
                                break;
                            }
                        }
                    }
                    return equals;
                }
            }
            time += 10;
        } while (time < timeout && pos < count);
        return false;
    }
}