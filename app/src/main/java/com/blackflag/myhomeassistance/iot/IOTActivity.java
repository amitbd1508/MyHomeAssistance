package com.blackflag.myhomeassistance.iot;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.blackflag.myhomeassistance.R;

import java.util.ArrayList;
import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

public class IOTActivity extends AppCompatActivity {


    private void askForPermission(String permission, Integer requestCode) {
        try{
            if (ContextCompat.checkSelfPermission(IOTActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(IOTActivity.this, permission)) {

                    //This is called if user has denied the permission before
                    //In this case I am just asking the permission again
                    ActivityCompat.requestPermissions(IOTActivity.this, new String[]{permission}, requestCode);

                } else {

                    ActivityCompat.requestPermissions(IOTActivity.this, new String[]{permission}, requestCode);
                }
            } else {
                // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_BT__REQUEST) {
            if (resultCode == RESULT_OK) {
                mSmoothBluetooth.tryConnection();
            }
        }
    }
    public static SmoothBluetooth mSmoothBluetooth;
    public static final int ENABLE_BT__REQUEST = 1;
    private SmoothBluetooth.Listener mListener=new SmoothBluetooth.Listener() {
        @Override
        public void onBluetoothNotSupported() {
            Toast.makeText(IOTActivity.this, "Bluetooth not found", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBluetoothNotEnabled() {

            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, ENABLE_BT__REQUEST);
        }

        @Override
        public void onConnecting(Device device) {


        }

        @Override
        public void onConnected(Device device) {

            Toast.makeText(IOTActivity.this, "Conected to  :  "+device.getName()+"\n"+device.getAddress(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onConnectionFailed(Device device) {

            Toast.makeText(IOTActivity.this, "Device Disconnected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDiscoveryStarted() {

            Toast.makeText(getApplicationContext(), "Searching", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDiscoveryFinished() {

        }

        @Override
        public void onNoDevicesFound() {

            Toast.makeText(getApplicationContext(), "No device found", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDevicesFound(final List<Device> deviceList, final SmoothBluetooth.ConnectionCallback connectionCallback) {

            String names[] ={"A","B","C","D"};
            List<String> list=new ArrayList<String>();
            for (Device d:deviceList)
                list.add(d.getName()+"\n"+d.getAddress()+"\n Paired : "+d.isPaired());
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(IOTActivity.this);
            final AlertDialog OptionDialog = alertDialog.create();
            LayoutInflater inflater = getLayoutInflater();
            View convertView = (View) inflater.inflate(R.layout.custom, null);
            alertDialog.setView(convertView);
            alertDialog.setTitle("Device List");
            ListView lv = (ListView) convertView.findViewById(R.id.listView1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,list);
            lv.setAdapter(adapter);
            alertDialog.show();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(IOTActivity.this, deviceList.get(i).getName(), Toast.LENGTH_SHORT).show();
                    connectionCallback.connectTo(deviceList.get(i));
                    OptionDialog.dismiss();

                }
            });
        }

        @Override
        public void onDataReceived(int data) {



        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iot);
        mSmoothBluetooth = new SmoothBluetooth(this, SmoothBluetooth.ConnectionTo.OTHER_DEVICE, SmoothBluetooth.Connection.SECURE,mListener);

        askForPermission("android.permission.BLUETOOTH",101);
        askForPermission("android.permission.BLUETOOTH_ADMIN",102);


        findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSmoothBluetooth.tryConnection();
            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSmoothBluetooth.stop();
    }
}
