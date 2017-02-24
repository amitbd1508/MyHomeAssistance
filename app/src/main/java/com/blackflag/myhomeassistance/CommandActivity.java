package com.blackflag.myhomeassistance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class CommandActivity extends AppCompatActivity {


    EditText ip1,ip2,message;
    Button send,connect;
    WebSocketClient webSocketClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);

        ip1= (EditText) findViewById(R.id.ip1);
        ip2= (EditText) findViewById(R.id.ip12);
        message= (EditText) findViewById(R.id.message);


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
}
