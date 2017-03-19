package com.blackflag.myhomeassistance;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class CameraActivity extends AppCompatActivity {

    WebView webView;
    ImageView img;
    TextView display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        webView= (WebView) findViewById(R.id.webview);
        img= (ImageView) findViewById(R.id.image);

        display= (TextView) findViewById(R.id.display);

       // webView.loadUrl("192.168.0.109:8081");
        webView.loadUrl("http://192.168.0.109:8081");

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //img.setImageBitmap(screenshot2(webView));
                leaptest();
            }
        });






    }

    private void leaptest() {

        Log.d("srgsd","sdfsd");
        URI uri;
        try {

            uri = new URI("ws://192.168.0.104:6437/v7.json");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        WebSocketClient wbWebSocketClient=new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {

                Log.d("Tag",handshakedata.getHttpStatusMessage());
            }

            @Override
            public void onMessage(String message) {
                Log.d("Tag",message);

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

                Log.d("Tag",reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.d("Tag",ex.getMessage());

            }
        };
        wbWebSocketClient.connect();
    }

    public static Bitmap screenshot2(WebView webView) {
        webView.measure(View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        webView.layout(0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight());
        webView.setDrawingCacheEnabled(true);
        webView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(webView.getMeasuredWidth(),
                webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        int iHeight = bitmap.getHeight();
        canvas.drawBitmap(bitmap, 0, iHeight, paint);
        webView.draw(canvas);
        return bitmap;
    }
}
