package com.rynnr.radar.library;

public interface BluetoothSerialRawListener extends BluetoothSerialListener {

    void onBluetoothSerialReadRaw(byte[] bytes);

    void onBluetoothSerialWriteRaw(byte[] bytes);

}
