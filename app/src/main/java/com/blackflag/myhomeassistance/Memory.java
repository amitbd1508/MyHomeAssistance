package com.blackflag.myhomeassistance;


import java.util.ArrayList;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.blackflag.myhomeassistance.listener.IDataReciver;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by BlackFlag on 6/27/2016.
 */
public class Memory  {


    public static String ip1="0";
    public static String ip2="102";
    static IDataReciver iDataReciver;



    public static void init(IDataReciver iData)
    {
        iDataReciver=iData;
    }
    //methos

    public  static  boolean isConnected=false;
    public static WebSocketClient mWebSocketClient;
    public static void connectWebsoket(String ip1,String ip2) throws Exception
    {


        Log.d("%%%%%%%%%%","connectedWebsoket");
        URI uri;
        try {

            uri = new URI("ws://192.168."+ip1+"."+ip2+""+":7000/BaseConnection");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient=new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {

                Log.d("%%%%","open");
                isConnected=true;
            }

            @Override
            public void onMessage(String message) {
                iDataReciver.reciveData(message);

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

                isConnected=false;


            }
            @Override
            public void onError(Exception ex) {

                Log.d("%%%%",ex.getMessage());
                isConnected=false;

            }
        };
    }


}
