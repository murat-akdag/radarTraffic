package com.rynnr.radar.library;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Set;


public class BluetoothSerial {

    private static final String TAG = "BluetoothSerial";

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;

    protected static final int MESSAGE_STATE_CHANGE = 1;
    protected static final int MESSAGE_READ = 2;
    protected static final int MESSAGE_WRITE = 3;
    protected static final int MESSAGE_DEVICE_INFO = 4;

    protected static final String KEY_DEVICE_NAME = "DEVICE_NAME";
    protected static final String KEY_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private static final byte[] CRLF = { 0x0D, 0x0A }; // \r\n

    private BluetoothAdapter mAdapter;
    private Set<BluetoothDevice> mPairedDevices;

    private BluetoothSerialListener mListener;
    private SPPService mService;

    private String mConnectedDeviceName, mConnectedDeviceAddress;

    private boolean isRaw;

    public BluetoothSerial(Context context, BluetoothSerialListener listener) {
        mAdapter = getAdapter(context);
        mListener = listener;
        isRaw = mListener instanceof BluetoothSerialRawListener;
    }

    public static BluetoothAdapter getAdapter(Context context) {
        BluetoothAdapter bluetoothAdapter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager != null)
                bluetoothAdapter = bluetoothManager.getAdapter();
        } else {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return bluetoothAdapter;
    }


    public void setup() {
        if (checkBluetooth()) {
            mPairedDevices = mAdapter.getBondedDevices();
            mService = new SPPService(mHandler);
        }
    }


    public boolean isBluetoothEnabled() {
        return mAdapter.isEnabled();
    }

    public boolean checkBluetooth() {
        if (mAdapter == null) {
            mListener.onBluetoothNotSupported();
            return false;
        } else {
            if (!mAdapter.isEnabled()) {
                mListener.onBluetoothDisabled();
                return false;
            } else {
                return true;
            }
        }
    }

    public void start() {
        if (mService != null && mService.getState() == STATE_DISCONNECTED) {
            mService.start();
        }
    }


    public void connect(String address) {
        BluetoothDevice device = null;
        try {
            device = mAdapter.getRemoteDevice(address);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Device not found!");
        }
        if (device != null)
            connect(device);
    }

    public void connect(BluetoothDevice device) {
        if (mService != null) {
            mService.connect(device);
        }
    }

    public void write(byte[] data) {
        if (mService.getState() == STATE_CONNECTED) {
            mService.write(data);
        }
    }


    public void write(String data, boolean crlf) {
        write(data.getBytes());
        if (crlf)
            write(CRLF);
    }


    public void write(String data) {
        write(data.getBytes());
    }


    public void writeln(String data) {
        write(data.getBytes());
        write(CRLF);
    }


    public void stop() {
        if (mService != null) {
            mService.stop();
        }
    }


    public int getState() {
        return mService.getState();
    }


    public boolean isConnected() {
        return (mService.getState() == STATE_CONNECTED);
    }


    public String getConnectedDeviceName() {
        return mConnectedDeviceName;
    }

 
    public String getConnectedDeviceAddress() {
        return mConnectedDeviceAddress;
    }

   
    public Set<BluetoothDevice> getPairedDevices() {
        return mPairedDevices;
    }


    public String[] getPairedDevicesName() {
        if (mPairedDevices != null) {
            String[] name = new String[mPairedDevices.size()];
            int i = 0;
            for (BluetoothDevice d : mPairedDevices) {
                name[i] = d.getName();
                i++;
            }
            return name;
        }
        return null;
    }


    public String[] getPairedDevicesAddress() {
        if (mPairedDevices != null) {
            String[] address = new String[mPairedDevices.size()];
            int i = 0;
            for (BluetoothDevice d : mPairedDevices) {
                address[i] = d.getAddress();
                i++;
            }
            return address;
        }
        return null;
    }


    public String getLocalAdapterName() {
        return mAdapter.getName();
    }


    public String getLocalAdapterAddress() {
        return mAdapter.getAddress();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case STATE_CONNECTED:
                            mListener.onBluetoothDeviceConnected(mConnectedDeviceName, mConnectedDeviceAddress);
                            break;
                        case STATE_CONNECTING:
                            mListener.onConnectingBluetoothDevice();
                            break;
                        case STATE_DISCONNECTED:
                            mListener.onBluetoothDeviceDisconnected();
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] bufferWrite = (byte[]) msg.obj;
                    String messageWrite = new String(bufferWrite);
                    mListener.onBluetoothSerialWrite(messageWrite);
                    if (isRaw) {
                        ((BluetoothSerialRawListener) mListener).onBluetoothSerialWriteRaw(bufferWrite);
                    }
                    break;
                case MESSAGE_READ:
                    byte[] bufferRead = (byte[]) msg.obj;
                    String messageRead = new String(bufferRead);
                    mListener.onBluetoothSerialRead(messageRead);
                    if (isRaw) {
                        ((BluetoothSerialRawListener) mListener).onBluetoothSerialReadRaw(bufferRead);
                    }
                    break;
                case MESSAGE_DEVICE_INFO:
                    mConnectedDeviceName = msg.getData().getString(KEY_DEVICE_NAME);
                    mConnectedDeviceAddress = msg.getData().getString(KEY_DEVICE_ADDRESS);
                    break;
            }
        }
    };

}
