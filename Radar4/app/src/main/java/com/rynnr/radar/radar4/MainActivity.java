package com.rynnr.radar.radar4;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.rynnr.radar.library.BluetoothDeviceListDialog;
import com.rynnr.radar.library.BluetoothSerial;
import com.rynnr.radar.library.BluetoothSerialListener;
 
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BluetoothSerialListener,BluetoothDeviceListDialog.OnDeviceSelectedListener {
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 2;

    TabHost host;
    Button Manualbtn, btnAyarla,btnAyarla2,btnReset,btnOlc,btnfb,btnBos;
    TextView Manualtxt;
    EditText ManualEdittxt, radarMaxHiz, radarMinHiz, radarDerece, radarGecikme, roleMin, roleMax, roleKarar;
    ScrollView ManualSc;
    RadioButton rbKm, rbMi, rbIN, rbOUT, rbBIDIR, rbAsci, rbHex, roleIN, roleOUT, roleBIDIR, roleOpen, roleClose;
    String radarVeri, strDerece, strMinHiz, strMaxHiz, strSure, strRoleMin, strRoleMax, strRoleKarar, strHizBirim, strYon, strVeriTip, strRoleYon, strRoleBos,strBos,strOlc;
    String pDerece,pMinHiz,pMaxHiz,pSure,pRoleMin,pRoleMax,pKarar;
    private BluetoothSerial bluetoothSerial;
    private MenuItem actionConnect, actionDisconnect,temizle;
    private boolean crlf = true;
    private BluetoothAdapter mBluetoothAdapter = null;
    boolean isBagli=false;
    Spinner baudspin;
    Spinner mesajspin;

    ArrayList<String> mesajitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabhost();
        initCustomSpinner();
        Manualbtn = (Button) findViewById(R.id.ManualBtn);
        Manualtxt = (TextView) findViewById(R.id.ManualText);
        ManualEdittxt = (EditText) findViewById(R.id.ManualEditText);
        radarDerece = (EditText) findViewById(R.id.radarDerece);
        radarMaxHiz = (EditText) findViewById(R.id.radarMaxHiz);
        radarMinHiz = (EditText) findViewById(R.id.radarMinHiz);
        radarGecikme = (EditText) findViewById(R.id.radarGecikme);
        roleMin = (EditText) findViewById(R.id.roleMin);
        roleMax = (EditText) findViewById(R.id.roleMax);
        roleKarar = (EditText) findViewById(R.id.roleKarar);
        rbKm = (RadioButton) findViewById(R.id.rbKm);
        rbMi = (RadioButton) findViewById(R.id.rbMi);
        rbIN = (RadioButton) findViewById(R.id.rbIN);
        rbOUT = (RadioButton) findViewById(R.id.rbOUT);
        rbBIDIR = (RadioButton) findViewById(R.id.rbBIDIR);
        rbAsci = (RadioButton) findViewById(R.id.rbASCI);
        rbHex = (RadioButton) findViewById(R.id.rbHEX);
        roleIN = (RadioButton) findViewById(R.id.roleIN);
        roleOUT = (RadioButton) findViewById(R.id.roleOUT);
        roleBIDIR = (RadioButton) findViewById(R.id.roleBIDIR);
        roleOpen = (RadioButton) findViewById(R.id.roleOpen);
        roleClose = (RadioButton) findViewById(R.id.roleClose);


        btnAyarla = (Button) findViewById(R.id.btnAyarla);
        btnAyarla2=(Button)findViewById(R.id.btnAyarla2);
        btnBos=(Button)findViewById(R.id.btnBos);
        btnOlc=(Button)findViewById(R.id.btnOlc);
        btnfb=(Button)findViewById(R.id.btnfb);
        btnReset=(Button)findViewById(R.id.btnReset);



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ManualSc = (ScrollView) findViewById(R.id.ManualSc);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

       host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
           @Override
           public void onTabChanged(String tabId) {
               if(tabId.equals("Tab Three")){
                   temizle.setVisible(true);
               }
               else{
                   temizle.setVisible(false);
               }
               if(tabId.equals("Tab One")){

               }
           }
       });
        rbAsci.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    mesajitem.clear();
                    mesajitem.add("a");
                    mesajitem.add("b");
                    mesajitem.add("v");
                }else {
                    mesajitem.clear();
                    mesajitem.add("g");
                    mesajitem.add("h");
                    mesajitem.add("j");
                }

            }
        });



        btnAyarla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBagli==true) {
                    radarVeri = "";
                    btnAlert();
                }
                else if(isBagli==false){
                    showToast("BAĞLANTI YOK",35);
                }
            }
        });

        btnAyarla2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBagli==true) {
                    radarVeri = "";
                    btnAlert();
                }
                else if(isBagli==false){
                    showToast("BAĞLANTI YOK",35);
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBagli==true) {
                    radarVeri = "";
                    gonder("!");
                }
                else if(isBagli==false){
                    showToast("BAĞLANTI YOK",35);
                }
            }
        });

        btnBos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBagli==true) {
                    radarVeri = "";
                    gonder("Y");
                }
                else if(isBagli==false){
                    showToast("BAĞLANTI YOK",35);
                }
            }
        });

        btnOlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBagli==true) {
                    radarVeri = "";
                    gonder("O");
                }
                else if(isBagli==false){
                    showToast("BAĞLANTI YOK",35);
                }
            }
        });

        Manualbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBagli==true) {
                    radarVeri = "";
                    Manualgonder();
                }
                else if(isBagli==false){
                    showToast("BAĞLANTI YOK",35);
                }
            }
        });
        ManualEdittxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                radarVeri = "";
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String send = ManualEdittxt.getText().toString().trim();
                    if (send.length() > 0) {
                        bluetoothSerial.write(send, crlf);
                        ManualEdittxt.setText("");
                    }
                }
                return false;
            }
        });
        // Create a new instance of BluetoothSerial
        bluetoothSerial = new BluetoothSerial(this, this);
    }

    boolean isDurum = false;

    public void gonder(String veri) {
        isManualSorgusu=false;
        if (veri.equals("?")){
            isDurum = true;

        }

        else isDurum = false;
        String send = veri.trim();
        char chVeri;
        send += "\r\n";
        if (send.length() > 0) {
            for (int i = 0; i < send.length(); i++) {
                try {
                    Thread.sleep(50);
                    chVeri = send.charAt(i);
                    byte[] ch = new byte[1];
                    ch[0] = (byte) chVeri;
                    bluetoothSerial.write(ch);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

        }
    }
    boolean isManualSorgusu=false;
    public void Manualgonder() {
        isManualSorgusu = true;
        String send = ManualEdittxt.getText().toString().trim();

        if(send.equals(""))
            return;
        if (send.equals("?")) {

            isDurum = true;
        }
        else isDurum = false;
        char chVeri;
        send += "\r\n";
        if (send.length() > 0) {
            for (int i = 0; i < send.length(); i++) {
                try {
                    Thread.sleep(50);
                    chVeri = send.charAt(i);
                    byte[] ch = new byte[1];
                    ch[0] = (byte) chVeri;
                    bluetoothSerial.write(ch);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void tabhost() {
        host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Radar Ayar");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Genel Ayar");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Manual");
        host.addTab(spec);
    }
    String item;
    boolean isSelectedItem=false;
    private void initCustomSpinner() {

        baudspin = (Spinner) findViewById(R.id.baudspin);
        mesajspin = (Spinner) findViewById(R.id.baudspn);
        // Spinner Drop down elements
        ArrayList<String> bauditem = new ArrayList<String>();
        mesajitem = new ArrayList<String>();
        bauditem.add("9600");
        bauditem.add("19200");
        bauditem.add("38400");
        bauditem.add("57600");
        bauditem.add("115200");

        mesajitem.add("SS");
        CustomSpinnerAdapter customSpinnerAdapter1 = new CustomSpinnerAdapter(MainActivity.this, bauditem);
        CustomSpinnerAdapter customSpinnerAdapter2 = new CustomSpinnerAdapter(MainActivity.this, mesajitem);
        baudspin.setAdapter(customSpinnerAdapter1);
        mesajspin.setAdapter(customSpinnerAdapter2);

        mesajspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //String item = parent.getItemAtPosition(position).toString();
                isSelectedItem=true;
                //Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        baudspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                //Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
            this.asr = asr;
            activity = context;
        }


        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(MainActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(MainActivity.this);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#ffffff"));
            return txt;
        }

    }

    public void btnAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Yapılan Değişikliği Onaylıyor Musun ?");
        builder.setPositiveButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                hizBirim();
                radarYon();
                veriTip();

                  int intRadarMinHiz=Integer.valueOf(radarMinHiz.getText().toString());
                  int intRadarMaxHiz =Integer.valueOf(radarMaxHiz.getText().toString());
                  int intRoleMin = Integer.valueOf(roleMin.getText().toString());
                  int intRoleMax =Integer.valueOf(roleMax.getText().toString());
                  int intRadarDerece= Integer.valueOf(radarDerece.getText().toString());
                  int intRadarGecikme = Integer.valueOf(radarGecikme.getText().toString());
                  int intRoleKarar = Integer.valueOf(roleKarar.getText().toString());

                if (!radarDerece.getText().toString().equals("")&&!pDerece.equals(Integer.toString(intRadarDerece))){
                    if(intRadarDerece>60){
                        radarDerece.setError("0 ile 60 arasında değer giriniz",null);
                        radarDerece.setText(pDerece);
                    }else
                        gonder("A" + radarDerece.getText().toString());
                }


                if (!radarMinHiz.getText().toString().equals("")&&!pMinHiz.equals(Integer.toString(intRadarMinHiz))){
                    if(intRadarMinHiz>=intRadarMaxHiz){
                        radarMinHiz.setError("Min hız Max hızdan büyük ve eşit olamaz",null);
                        radarMinHiz.setText(pMinHiz);
                    }
                    else if(intRadarMinHiz>255){
                        radarMinHiz.setError("0 ile 255 arasında değer giriniz ",null);
                        radarMinHiz.setText(pMinHiz);
                    }
                    else gonder("m" + radarMinHiz.getText().toString());
                }


                  if (!radarMaxHiz.getText().toString().equals("")&&!pMaxHiz.equals(Integer.toString(intRadarMaxHiz))) {
                    if (intRadarMinHiz >= intRadarMaxHiz){
                        radarMaxHiz.setError("Min hız Max hızdan büyük ve eşit olamaz",null);
                        radarMaxHiz.setText(pMaxHiz);
                    }
                    else if(intRadarMaxHiz>255){
                        radarMaxHiz.setError("0 ile 255 arasında değer giriniz",null);
                        radarMaxHiz.setText(pMaxHiz);
                    }
                    else gonder("M" + radarMaxHiz.getText().toString());
                  }


                if (!radarGecikme.getText().toString().equals("")&&!pSure.equals(Integer.toString(intRadarGecikme))){
                    if(intRadarGecikme<3||intRadarGecikme>250){
                        radarGecikme.setError("3 ile 250 arasında değer giriniz",null);
                        radarGecikme.setText(pSure);
                    }else
                        gonder("R" + radarGecikme.getText().toString());
                }


                roleYon();
                roleBos();

                if (!roleMin.getText().toString().equals("")&&!pRoleMin.equals(Integer.toString(intRoleMin))) {
                    if (intRoleMin >= intRoleMax) {
                        roleMin.setError("Min hız Max hızdan büyük ve eşit olamaz",null);
                        roleMin.setText(pRoleMin);

                    }
                    else if(intRoleMin>254){
                        roleMin.setError("0 ile 254 arasında değer giriniz",null);
                        roleMin.setText(pRoleMin);
                    }

                    else {
                        gonder("l" + roleMin.getText().toString());
                    }
                }

                if (!roleMax.getText().toString().equals("")&&!pRoleMax.equals(Integer.toString(intRoleMax))) {
                    if (intRoleMin >= intRoleMax) {
                        roleMax.setError("Min hız Max hızdan büyük ve eşit olamaz",null);
                        roleMax.setText(pRoleMax);

                    }
                    else if(intRoleMax>254){
                        roleMax.setError("0 ile 254 arasında değer giriniz",null);
                        roleMax.setText(pRoleMax);
                    }

                    else {
                        gonder("L" + roleMax.getText().toString());
                    }
                }

                  if (!roleKarar.getText().toString().equals("")&&!pKarar.equals(Integer.toString(intRoleKarar))){
                      if(intRoleKarar<1||intRoleKarar>255){
                          roleKarar.setError("1 ile 255 arasında değer giriniz",null);
                          roleKarar.setText(pKarar);
                      }else
                          gonder("W" + roleKarar.getText().toString());
                  }

                if(isSelectedItem==true){
                    gonder("S"+baudspin.getSelectedItemId());
                    isSelectedItem=false;
                }


            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//Alert değiştir??
        builder.setCancelable(false);
        builder.setMessage("Çıkış Yapmak İstiyor Musun ?");
        builder.setPositiveButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                mBluetoothAdapter.disable();
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check Bluetooth availability on the device and set up the Bluetooth adapter
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BLUETOOTH);
            // Otherwise, setup the chat session
        } else {
            bluetoothSerial.setup();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open a Bluetooth serial port and get ready to establish a connection
        if (bluetoothSerial.checkBluetooth() && bluetoothSerial.isBluetoothEnabled()) {
            if (!bluetoothSerial.isConnected()) {
                bluetoothSerial.start();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect from the remote device and close the serial port
        bluetoothSerial.stop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_terminal, menu);

        actionConnect = menu.findItem(R.id.action_connect);
        actionDisconnect = menu.findItem(R.id.action_disconnect);
        temizle=menu.findItem(R.id.temizle);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent serverIntent = null;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_connect) {

            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            return true;
        } else if (id == R.id.action_disconnect) {
            bluetoothSerial.stop();
            return true;
        }else if (id == R.id.temizle) {
            Manualtxt.setText("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void invalidateOptionsMenu() {
        if (bluetoothSerial == null)
            return;

        // Show or hide the "Connect" and "Disconnect" buttons on the app bar
        if (bluetoothSerial.isConnected()) {
            if (actionConnect != null)
                actionConnect.setVisible(false);
            if (actionDisconnect != null)
                actionDisconnect.setVisible(true);
        } else {
            if (actionConnect != null)
                actionConnect.setVisible(true);
            if (actionDisconnect != null)
                actionDisconnect.setVisible(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
                // Set up Bluetooth serial port when Bluetooth adapter is turned on
                if (resultCode == Activity.RESULT_OK) {
                    bluetoothSerial.setup();
                } else {
                    finish();
                    mBluetoothAdapter.disable();
                }
                break;

            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK)
                    connectDevice(data, true);

                ////////////

                break;
        }
    }

    private void connectDevice(Intent data, boolean b) {
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        bluetoothSerial.connect(device);

    }

    private void updateBluetoothState() {
        // Get the current Bluetooth state
        final int state;
        if (bluetoothSerial != null)
            state = bluetoothSerial.getState();
        else
            state = BluetoothSerial.STATE_DISCONNECTED;

        // Display the current state on the app bar as the subtitle
        String subtitle;
        switch (state) {
            case BluetoothSerial.STATE_CONNECTING:
                subtitle = getString(R.string.status_connecting);
                break;
            case BluetoothSerial.STATE_CONNECTED:

                subtitle = getString(R.string.status_connected, bluetoothSerial.getConnectedDeviceName());
                isBagli=true;
                radarVeri = "";
                gonder("?");
                break;
            default:
                subtitle = getString(R.string.status_disconnected);
                isBagli=false;
                break;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subtitle);
        }
    }

    private void showDeviceListDialog() {
        // Display dialog for selecting a remote Bluetooth device
        BluetoothDeviceListDialog dialog = new BluetoothDeviceListDialog(this);
        dialog.setOnDeviceSelectedListener(this);
        dialog.setTitle(R.string.paired_devices);
        dialog.setDevices(bluetoothSerial.getPairedDevices());
        dialog.showAddress(true);
        dialog.show();
    }

    /* Implementation of BluetoothSerialListener */

    @Override
    public void onBluetoothNotSupported() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.no_bluetooth)
                .setPositiveButton(R.string.action_quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBluetoothDisabled() {
        //Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        //startActivityForResult(enableBluetooth, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    public void onBluetoothDeviceDisconnected() {
        invalidateOptionsMenu();
        updateBluetoothState();
    }

    @Override
    public void onConnectingBluetoothDevice() {
        updateBluetoothState();
    }

    @Override
    public void onBluetoothDeviceConnected(String name, String address) {
        invalidateOptionsMenu();
        updateBluetoothState();
    }

    @Override
    public void onBluetoothSerialRead(String message) {
        // Print the incoming message on the terminal screen


        if(isManualSorgusu)
            Manualtxt.append(message);
        radarVeri += message;
        ManualSc.post(scrollTerminalToBottom);
        try {
            ayarAl();
        } catch (Exception e) {
        }


    }

    @Override
    public void onBluetoothSerialWrite(String message) {
        // Print the outgoing message on the terminal screen
        //Manualtxt.append(message);
        //ManualSc.post(scrollTerminalToBottom);
    }

    /* Implementation of BluetoothDeviceListDialog.OnDeviceSelectedListener */

    @Override
    public void onBluetoothDeviceSelected(BluetoothDevice device) {
        // Connect to the selected remote Bluetooth device
        bluetoothSerial.connect(device);
    }

    /* End of the implementation of listeners */

    private final Runnable scrollTerminalToBottom = new Runnable() {
        @Override
        public void run() {
            // Scroll the terminal screen to the bottom
            ManualSc.fullScroll(ScrollView.FOCUS_DOWN);
        }
    };

    int intHizBirim = -1;
    int intYon = -1;
    int intVeriTip = -1;
    int intDerece = -1;
    int intMinHiz = -1;
    int intMaxHiz = -1;
    int intSure = -1;
    int intRoleYon = -1;
    int intRoleBos = -1;
    int intRoleMin = -1;
    int intRoleMax = -1;
    int intRoleKarar = -1;
    int intBtnBos=-1;
    int intOlc=-1;


    public void ayarAl() {


        intHizBirim = radarVeri.indexOf("Speed unit                     = ");
        if (intHizBirim != -1) {
            strHizBirim = radarVeri.substring(intHizBirim + 33, intHizBirim + 35);
            if (strHizBirim.equals("km")) {
                rbKm.setChecked(true);
                rbMi.setChecked(false);

            } else if (strHizBirim.equals("mi")) {
                rbMi.setChecked(true);
                rbKm.setChecked(false);
            }
        }

        intYon = radarVeri.lastIndexOf("TX direction     = ");
        if (intYon != -1) {
            strYon = radarVeri.substring(intYon + 19, intYon + 21);
            if (strYon.equals("IN")) {
                rbIN.setChecked(true);
                rbOUT.setChecked(false);
                rbBIDIR.setChecked(false);
            } else if (strYon.equals("OU")) {
                rbIN.setChecked(false);
                rbOUT.setChecked(true);
                rbBIDIR.setChecked(false);
            } else if (strYon.equals("BI")) {
                rbIN.setChecked(false);
                rbOUT.setChecked(false);
                rbBIDIR.setChecked(true);
            }
        }

        intVeriTip = radarVeri.indexOf("TX mode:         = ");
        if (intVeriTip != -1) {
            strVeriTip = radarVeri.substring(intVeriTip + 19, intVeriTip + 22);
            if (strVeriTip.equals("ASC")) {
                rbAsci.setChecked(true);
                rbHex.setChecked(false);
            } else if (strVeriTip.equals("HEX")) {
                rbAsci.setChecked(false);
                rbHex.setChecked(true);
            }
        }

        intDerece = radarVeri.indexOf("Angle [degrees]                = ");
        if (intDerece != -1) {
            strDerece = Integer.toString(Integer.valueOf(radarVeri.substring(intDerece + 33, intDerece + 36)));
            radarDerece.setText(strDerece);
            pDerece=strDerece;


        }

        intMinHiz = radarVeri.indexOf("TX minimum speed = ");
        if (intMinHiz != -1) {
            strMinHiz = Integer.toString(Integer.valueOf(radarVeri.substring(intMinHiz + 19, intMinHiz + 22)));
            radarMinHiz.setText(strMinHiz);
            pMinHiz=strMinHiz;
        }

        intMaxHiz = radarVeri.indexOf("TX maximum speed = ");
        if (intMaxHiz != -1) {
            strMaxHiz = Integer.toString(Integer.valueOf(radarVeri.substring(intMaxHiz + 19, intMaxHiz + 22)));
            radarMaxHiz.setText(strMaxHiz);
            pMaxHiz=strMaxHiz;
        }


        intSure = radarVeri.indexOf("Delay between measures [x10ms] = ");
        if (intSure != -1) {
            strSure = Integer.toString(Integer.valueOf(radarVeri.substring(intSure + 33, intSure + 36)));
            radarGecikme.setText(strSure);
            pSure=strSure;
        }

        //---------------------------------------------------
        intRoleYon = radarVeri.lastIndexOf("Contact direction        = ");
        if (intRoleYon != -1) {
            strRoleYon = radarVeri.substring(intRoleYon + 27, intRoleYon + 29);
            if (strRoleYon.equals("IN")) {
                roleIN.setChecked(true);
                roleOUT.setChecked(false);
                roleBIDIR.setChecked(false);
            } else if (strRoleYon.equals("OU")) {
                roleIN.setChecked(false);
                roleOUT.setChecked(true);
                roleBIDIR.setChecked(false);
            } else if (strRoleYon.equals("BI")) {
                roleIN.setChecked(false);
                roleOUT.setChecked(false);
                roleBIDIR.setChecked(true);
            }
        }

        intRoleBos = radarVeri.indexOf("Contact idle state       = ");
        if (intRoleBos != -1) {
            strRoleBos = radarVeri.substring(intRoleBos + 27, intRoleBos + 30);
            if (strRoleBos.equals("OPE")) {
                roleOpen.setChecked(true);
                roleClose.setChecked(false);
            } else if (strRoleBos.equals("CLO")) {
                roleOpen.setChecked(false);
                roleClose.setChecked(true);
            }
        }

        intRoleMin = radarVeri.indexOf("Contact minimum speed    = ");
        if (intRoleMin != -1) {
            strRoleMin = Integer.toString(Integer.valueOf(radarVeri.substring(intRoleMin + 27, intRoleMin + 30)));
            roleMin.setText(strRoleMin);
            pRoleMin=strRoleMin;
        }

        intRoleMax = radarVeri.indexOf("Contact maximum speed    = ");
        if (intRoleMax != -1) {
            strRoleMax = Integer.toString(Integer.valueOf(radarVeri.substring(intRoleMax + 27, intRoleMax + 30)));
            roleMax.setText(strRoleMax);
            pRoleMax=strRoleMax;
        }

        intRoleKarar = radarVeri.indexOf("Contact duration [x10ms] = ");
        if (intRoleKarar != -1) {
            strRoleKarar = Integer.toString(Integer.valueOf(radarVeri.substring(intRoleKarar + 27, intRoleKarar + 30)));
            roleKarar.setText(strRoleKarar);
            pKarar=strRoleKarar;
        }

        intBtnBos = radarVeri.indexOf("Send idle message              = ");
        if(intBtnBos!=-1){
            strBos=radarVeri.substring(intBtnBos+33,intBtnBos+35);
            if(strBos.equals("OF")){
                btnBos.setText("BOŞ MESAJ KAPALI");
            }
            else if(strBos.equals("ON")){
                btnBos.setText("BOŞ MESAJ AÇIK");
            }
        }

        intOlc = radarVeri.indexOf("TX measures      = ");
        if(intOlc!=-1){
            strOlc=radarVeri.substring(intOlc+19,intOlc+21);
            if(strOlc.equals("OF")){
                btnOlc.setText("ÖLÇME KAPALI");
            }
            else if(strOlc.equals("ON")){
                btnOlc.setText("ÖLÇME AÇIK");
            }
        }


        if (isDurum != true) {
            Toast.makeText(getApplicationContext(), "Ayarlarınız Değişti.", Toast.LENGTH_SHORT).show();
            isDurum = true;
        }
    }

    public void showToast(String mesaj,int txtSize){
        LayoutInflater inflater = getLayoutInflater();
        // Inflate the Layout
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.custom_toast_layout));
        TextView text = (TextView) layout.findViewById(R.id.textToShow);
        // Set the Text to show in TextView
        text.setText(mesaj);
        text.setTextSize(txtSize);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
    public void hizBirim() {
        if (strHizBirim.equals("mi")) {
            if (rbKm.isChecked() == true) {
                gonder("U");
            }
        } else if (strHizBirim.equals("km")) {
            if (rbMi.isChecked() == true) {
                gonder("U");
            }
        }
    }

    public void radarYon() {
        if (strYon.equals("IN")) {
            if (rbOUT.isChecked() == true) {
                gonder("D");
            } else if (rbBIDIR.isChecked() == true) {
                gonder("D");
                gonder("D");
            }
        }

        if (strYon.equals("OU")) {
            if (rbIN.isChecked() == true) {
                gonder("D");
                gonder("D");
            } else if (rbBIDIR.isChecked() == true) {
                gonder("D");
            }
        }

        if (strYon.equals("BI")) {
            if (rbIN.isChecked() == true) {
                gonder("D");
            } else if (rbOUT.isChecked() == true) {
                gonder("D");
                gonder("D");
            }
        }
    }

    public void veriTip() {
        if (strVeriTip.equals("ASC")) {
            if (rbHex.isChecked() == true) {
                gonder("X");
            }
        } else if (strVeriTip.equals("HEX")) {
            if (rbAsci.isChecked() == true) {
                gonder("X");
            }
        }
    }

    public void roleYon(){
        if (strRoleYon.equals("IN")) {
            if (roleOUT.isChecked() == true) {
                gonder("e");
            } else if (roleBIDIR.isChecked() == true) {
                gonder("e");
                gonder("e");
            }
        }

        if (strRoleYon.equals("OU")) {
            if (roleIN.isChecked() == true) {
                gonder("e");
                gonder("e");
            } else if (roleBIDIR.isChecked() == true) {
                gonder("e");
            }
        }

        if (strRoleYon.equals("BI")) {
            if (roleIN.isChecked() == true) {
                gonder("e");
            } else if (roleOUT.isChecked() == true) {
                gonder("e");
                gonder("e");
            }
        }
    }

    public void roleBos() {
        if (strRoleBos.equals("CLO")) {
            if (roleOpen.isChecked() == true) {
                gonder("i");
            }
        } else if (strRoleBos.equals("OPE")) {
            if (roleClose.isChecked() == true) {
                gonder("i");
            }
        }
    }
}
