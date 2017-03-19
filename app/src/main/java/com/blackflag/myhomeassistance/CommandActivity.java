package com.blackflag.myhomeassistance;

import android.app.SearchManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackflag.myhomeassistance.iot.IOTActivity;
import com.blackflag.myhomeassistance.listener.IDataReciver;
import com.blackflag.myhomeassistance.voiceRecognization.VoiceRecognitionActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;


public class CommandActivity extends AppCompatActivity implements IDataReciver{


    EditText ip1,ip2,message,msg,time;
    Button send,speak,details,gretings,alarm,set_alarm;
    TextView temp,light,smoke;
    IDataReciver iDataReciver;
    LinearLayout view;



    String getTime()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
        iDataReciver=this;
        ip1= (EditText) findViewById(R.id.ip1);
        ip2= (EditText) findViewById(R.id.ip12);
        msg= (EditText) findViewById(R.id.note_time);
        time= (EditText) findViewById(R.id.note_message);
        message= (EditText) findViewById(R.id.message);

        checkVoiceRecognition();
        mSmoothBluetooth = new SmoothBluetooth(this, SmoothBluetooth.ConnectionTo.OTHER_DEVICE, SmoothBluetooth.Connection.SECURE,mListener);
        temp= (TextView) findViewById(R.id.temp_value);
        light= (TextView) findViewById(R.id.light_value);
        smoke= (TextView) findViewById(R.id.smoke_value);
        view= (LinearLayout) findViewById(R.id.lintop);

        speak= (Button) findViewById(R.id.speak);
        set_alarm= (Button) findViewById(R.id.note_set);


        time.setText(getTime());


        set_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread note_reader=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(getTime().equals(time.getText()))
                        {
                            try
                            {
                                Memory.mWebSocketClient.send("Your have a riminder "+msg.getText());
                            }catch (Exception ex)
                            {
                                Toast.makeText(CommandActivity.this, "Not connected", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(CommandActivity.this, msg.getText(), Toast.LENGTH_SHORT).show();
                        }


                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                note_reader.start();
                Toast.makeText(CommandActivity.this,"Alarm set", Toast.LENGTH_SHORT).show();

            }
        });
        findViewById(R.id.feel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Memory.mWebSocketClient.send(": I feel good");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.battary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Memory.mWebSocketClient.send(": I have 80% charge ");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.sleepy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Memory.mWebSocketClient.send(": I have to sleep");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.device_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Memory.mWebSocketClient.send("H");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.device_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Memory.mWebSocketClient.send("details");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.device_thanks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Memory.mWebSocketClient.send("Welcome "+message.getText());
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.INVISIBLE);
            }
        });
        findViewById(R.id.connect_blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSmoothBluetooth.tryConnection();

            }
        });
        findViewById(R.id.light_on).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    mSmoothBluetooth.send("J");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.light_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    mSmoothBluetooth.send("j");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.device_on).setOnClickListener(new View.OnClickListener() { // blue
            @Override
            public void onClick(View view) {

                try{
                    mSmoothBluetooth.send("N");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.device_off).setOnClickListener(new View.OnClickListener() { //blue
            @Override
            public void onClick(View view) {

                try{

                    mSmoothBluetooth.send("n");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.auto_enable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    Memory.mWebSocketClient.send("A");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.auto_disible).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    Memory.mWebSocketClient.send("X");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }
            }
        });





        Drawer();


        findViewById(R.id.up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Memory.mWebSocketClient.send("F");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }


            }
        });
        findViewById(R.id.down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Memory.mWebSocketClient.send("B");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }


            }
        });
        findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Memory.mWebSocketClient.send("L");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }

            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Memory.mWebSocketClient.send("S");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }

            }
        });
        findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Memory.mWebSocketClient.send("R");
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }


            }
        });
        findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Memory.init(iDataReciver);
                    Memory.connectWebsoket(ip1.getText().toString(),ip2.getText().toString());
                    Memory.mWebSocketClient.connect();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        findViewById(R.id.Send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    String mes=message.getText().toString();
                    Memory.mWebSocketClient.send(mes);
                }
                catch (Exception ex) {
                    Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                }

            }
        });






    }

    @Override
    public void reciveData(String data) {
        Log.d("Message",data);
        String lines[] = data.split("\\r?\\n");
        for (String s:lines)
        {
            if(s.contains("TEMPRATURE"))
                temp.setText(s.split(":")[1]);
            if(s.contains("Smoke"))
                smoke.setText(s.split(":")[1]);
            if(s.contains("Light"))
                light.setText(s.split(":")[1]);

        }

    }
    public void Drawer() {

        try{
            AccountHeader headerResult = new AccountHeaderBuilder()
                    .withActivity(this)

                    .addProfiles(
                            new ProfileDrawerItem()
                                    .withName("Threat Equation")
                                    .withIdentifier(0)
                                    //.withSelectedColor(Color.parseColor("#0027A2"))
                                    .withTextColor(Color.parseColor("#0027A2"))
                                    .withIdentifier(100)
                    )
                    .withCompactStyle(true)



                    .build();
            final Drawer result = new DrawerBuilder()
                    .withActivity(this)
                    .withDrawerWidthDp(250)
                    .withActionBarDrawerToggle(true)
                    .withActionBarDrawerToggleAnimated(true)
                    .withActivity(CommandActivity.this)

                    .withFullscreen(false)
                    .withCloseOnClick(true)
                    .withAccountHeader(headerResult)

                    .addDrawerItems(
                            new PrimaryDrawerItem().withName("Contorl").withIdentifier(1),
                            new PrimaryDrawerItem().withName("Speach").withIdentifier(2),
                            new PrimaryDrawerItem().withName("IOT").withIdentifier(3)

                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            if (drawerItem.equals(100)) {

                            }
                            if (drawerItem.equals(1)) {
                                startActivity(new Intent(getApplicationContext(),CommandActivity.class));
                            }
                            if (drawerItem.equals(2)) {
                                startActivity(new Intent(getApplicationContext(),VoiceRecognitionActivity.class));
                            }
                            if (drawerItem.equals(3)) {
                                startActivity(new Intent(getApplicationContext(),IOTActivity.class));
                            }
                            return true;
                        }
                    }).withDrawerGravity(Gravity.LEFT)
                    .build();
            result.openDrawer();
            result.closeDrawer();
            result.isDrawerOpen();

        }catch (Exception e)
        {

        }
    }


    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

    public static SmoothBluetooth mSmoothBluetooth;
    public static final int ENABLE_BT__REQUEST = 1;
    private SmoothBluetooth.Listener mListener=new SmoothBluetooth.Listener() {
        @Override
        public void onBluetoothNotSupported() {
            Toast.makeText(CommandActivity.this, "Bluetooth not found", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(CommandActivity.this, "Conected to  :  "+device.getName()+"\n"+device.getAddress(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onConnectionFailed(Device device) {

            Toast.makeText(CommandActivity.this, "Device Disconnected", Toast.LENGTH_SHORT).show();
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
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CommandActivity.this);
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
                    Toast.makeText(CommandActivity.this, deviceList.get(i).getName(), Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        super.onDestroy();
        if(mSmoothBluetooth!=null)
            mSmoothBluetooth.stop();
    }

    public void speak(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify the calling package to identify your application
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
                .getPackage().getName());

        // Display an hint to the user about what he should say.
        /*intent.putExtra(RecognizerIntent.EXTRA_PROMPT, metTextHint.getText()
                .toString());*/

        // Given an hint to the recognizer about what the user is going to say
        //There are two form of language model available
        //1.LANGUAGE_MODEL_WEB_SEARCH : For short phrases
        //2.LANGUAGE_MODEL_FREE_FORM  : If not sure about the words or phrases and its domain.
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        // If number of Matches is not selected then return show toast message
        /*if (msTextMatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            Toast.makeText(this, "Please select No. of Matches from spinner",
                    Toast.LENGTH_SHORT).show();
            return;
        }*/

        /*int noOfMatches = Integer.parseInt(msTextMatches.getSelectedItem()
                .toString());*/
        // Specify how many results you want to receive. The results will be
        // sorted where the first result is the one with higher confidence.
        //intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
        //Start the Voice recognizer activity for the result.
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_BT__REQUEST) {
            if (resultCode == RESULT_OK) {
                mSmoothBluetooth.tryConnection();
            }
        }

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

            //If Voice recognition is successful then it returns RESULT_OK
            if(resultCode == RESULT_OK) {

                ArrayList<String> textMatchList = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (!textMatchList.isEmpty()) {
                    // If first Match contains the 'search' word
                    // Then start web search.
                    if (textMatchList.get(0).contains("search")) {

                        String searchQuery = textMatchList.get(0);
                        searchQuery = searchQuery.replace("search","");
                        Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                        search.putExtra(SearchManager.QUERY, searchQuery);
                        startActivity(search);
                    } else {
                        // populate the Matches



                        if (textMatchList.contains("hi"))
                        {
                            try{

                                Memory.mWebSocketClient.send("details");
                            }
                            catch (Exception ex) {
                                Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else if (textMatchList.contains("go"))
                        {

                            try{

                                Memory.mWebSocketClient.send("F");
                            }
                            catch (Exception ex) {
                                Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                            }

                        }

                        else if (textMatchList.contains("back"))
                        {
                            try{

                                Memory.mWebSocketClient.send("B");
                            }
                            catch (Exception ex) {
                                Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                            }

                        }

                        else if (textMatchList.contains("right"))
                        {
                            try{

                                Memory.mWebSocketClient.send("R");
                            }
                            catch (Exception ex) {
                                Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else if (textMatchList.contains("left"))
                        {
                            try{

                                Memory.mWebSocketClient.send("L");
                            }
                            catch (Exception ex) {
                                Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else if (textMatchList.contains("stop"))
                        {
                            try{

                                Memory.mWebSocketClient.send("S");
                            }
                            catch (Exception ex) {
                                Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                            }

                        }
                        if (textMatchList.contains("auto"))
                        {
                            try{
                                Toast.makeText(this, "auto", Toast.LENGTH_SHORT).show();
                                Memory.mWebSocketClient.send("A");
                            }
                            catch (Exception ex) {
                                Toast.makeText(CommandActivity.this, "Connect Soket First", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                }
                //Result code for various error.
            }else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
                showToastMessage("Audio Error");
            }else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
                showToastMessage("Client Error");
            }else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
                showToastMessage("Network Error");
            }else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
                showToastMessage("No Match");
            }else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
                showToastMessage("Server Error");
            }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * Helper method to show the toast message
     **/
    void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void checkVoiceRecognition() {
        // Check if voice recognition is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            speak.setEnabled(false);
            speak.setText("Voice recognizer not present");
            Toast.makeText(this, "Voice recognizer not present",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
