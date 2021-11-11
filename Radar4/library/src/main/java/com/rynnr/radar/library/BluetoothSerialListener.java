package com.rynnr.radar.library;


public interface BluetoothSerialListener {


    void onBluetoothNotSupported();


    void onBluetoothDisabled();


    void onBluetoothDeviceDisconnected();

    void onConnectingBluetoothDevice();

    void onBluetoothDeviceConnected(String name, String address);

    void onBluetoothSerialRead(String message);

    void onBluetoothSerialWrite(String message);

}
