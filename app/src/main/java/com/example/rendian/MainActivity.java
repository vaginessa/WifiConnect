package com.example.rendian;

import android.net.wifi.WifiManager;
import android.os.Bundle;  
import android.app.Activity;  
import android.content.BroadcastReceiver;  
import android.content.Context;  
import android.content.Intent;  
import android.content.IntentFilter;  
import android.util.Log;  
import android.view.Menu;  
import android.view.View;  
import android.view.View.OnClickListener;
import android.widget.Button;  
  
public class MainActivity extends Activity {  
  
    public static final String TAG = "MainActivity";  
      
    private Button mBtn1, mBtn2,mBtn3;  
    private WifiAdmin mWifiAdmin;
    private Context mContext = this;
    private final String mWifiName = "TP-LINK_5A28";
    private final String mWifiPassword = "youtu505";
    private final String str1="\"";
    private final String str2="\"";
    private final String UserWifiName="km1930";
    private final String UserWifiPassword = "youtu123";

    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        
        final WifiManager systemService = (WifiManager) MainActivity.this.getSystemService(Context.WIFI_SERVICE);
        setContentView(R.layout.activity_main);  
        mBtn1 = (Button)findViewById(R.id.button1);  
        mBtn2 = (Button)findViewById(R.id.button2);  
        mBtn3 = (Button)findViewById(R.id.button3);  
       mBtn1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                mWifiAdmin = new WifiAdmin(mContext) {

                    @Override
                    public void myUnregisterReceiver(BroadcastReceiver receiver) {
                        MainActivity.this.unregisterReceiver(receiver);
                    }

                    @Override
                    public Intent myRegisterReceiver(BroadcastReceiver receiver,
                            IntentFilter filter) {
                        MainActivity.this.registerReceiver(receiver, filter);
                        return null;
                    }

                    @Override
                    public void onNotifyWifiConnected() {
                        Log.d(TAG, "have connected success!");
                    }
                    @Override
                    public void onNotifyWifiConnectFailed() {
                        Log.d(TAG, "have connected failed!");
                    }
                };
                mWifiAdmin.openWifi();
                mWifiAdmin.addNetwork(mWifiAdmin.createWifiInfo(mWifiName, mWifiPassword, WifiAdmin.TYPE_WPA));

            }
        });


       mBtn2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiApAdmin wifiAp = new WifiApAdmin(mContext);
                wifiAp.startWifiAp(str1+UserWifiName+str2, UserWifiPassword);
            }
        });

        mBtn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				systemService.setWifiEnabled(true);
				
			}
		});
    }
}  