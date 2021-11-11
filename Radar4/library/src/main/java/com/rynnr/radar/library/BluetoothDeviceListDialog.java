package com.rynnr.radar.library;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Set;


public class BluetoothDeviceListDialog {

    public interface OnDeviceSelectedListener {

        void onBluetoothDeviceSelected(BluetoothDevice device);

    }

    private Context mContext;
    private OnDeviceSelectedListener mListener;
    private Set<BluetoothDevice> mDevices;
    private String[] mNames, mAddresses;
    private String mTitle;
    private boolean mShowAddress = true;
    private boolean mUseDarkTheme;

    public BluetoothDeviceListDialog(Context context) {
        mContext = context;
    }

    public void setOnDeviceSelectedListener(OnDeviceSelectedListener listener) {
        mListener = listener;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setTitle(int resId) {
        mTitle = mContext.getString(resId);
    }

    public void setDevices(Set<BluetoothDevice> devices) {
        mDevices = devices;

        if (devices != null) {
            mNames = new String[devices.size()];
            mAddresses = new String[devices.size()];
            int i = 0;
            for (BluetoothDevice d : devices) {
                mNames[i] = d.getName();
                mAddresses[i] = d.getAddress();
                i++;
            }
        }
    }


    public void showAddress(boolean showAddress) {
        mShowAddress = showAddress;
    }


    @Deprecated
    public void useDarkTheme(boolean useDarkTheme) {
        mUseDarkTheme = useDarkTheme;
    }

    public void show() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(mTitle)
                .setAdapter(new BluetoothDeviceListItemAdapter(mContext, mNames, mAddresses, mShowAddress), null)
                .create();

        final ListView listView = dialog.getListView();
        if (listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mListener.onBluetoothDeviceSelected(BluetoothSerial.getAdapter(mContext).getRemoteDevice(mAddresses[position]));
                    dialog.cancel();
                }
            });
        }

        dialog.show();
    }

}
