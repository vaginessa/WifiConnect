package com.example.rendian;
import java.lang.reflect.InvocationTargetException;  
import java.lang.reflect.Method;  
import android.content.Context;  
import android.net.wifi.WifiConfiguration;  
import android.net.wifi.WifiManager;  
import android.util.Log;  
/** 
 * 创建热点 
 * 
 */  
public class WifiApAdmin {  
    public static final String TAG = WifiApAdmin.class.getSimpleName();
    public static void closeWifiAp(Context context) {  
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);   
        closeWifiAp(wifiManager);  
    }  
    private WifiManager mWifiManager = null; 
    private Context mContext = null;  
    public WifiApAdmin(Context context) {  
        mContext = context;      
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);        
        closeWifiAp(mWifiManager);  
    }  
    private String mSSID = "";  
    private String mPasswd = "";  
    public void startWifiAp(String ssid, String passwd) {  
        mSSID = ssid;  
        mPasswd = passwd;  
        if (mWifiManager.isWifiEnabled()) {  
            mWifiManager.setWifiEnabled(false);  
        }    
        stratWifiAp();   
        MyTimerCheck timerCheck = new MyTimerCheck() {       
            @Override  
            public void doTimerCheckWork() {           
                if (isWifiApEnabled(mWifiManager)) {  
                    Log.v(TAG, "Wifi enabled success!");  
                    this.exit();  
                } else {  
                    Log.v(TAG, "Wifi enabled failed!");  
                }  
            }  
  
            @Override  
            public void doTimeOutWork() {  
                this.exit();  
            }  
        };  
        timerCheck.start(15, 1000);  
          
    } 
    
    public void stratWifiAp() {  
        Method method1 = null;  
        try {  
            method1 = mWifiManager.getClass().getMethod("setWifiApEnabled",  
                    WifiConfiguration.class, boolean.class);  
            WifiConfiguration netConfig = new WifiConfiguration();  
            netConfig.SSID = mSSID;  
            netConfig.preSharedKey = mPasswd;  
            netConfig.allowedAuthAlgorithms  //allowedAuthAlgorithms为这个配置支持的身份验证协议。
                    .set(WifiConfiguration.AuthAlgorithm.OPEN); //WifiConfiguration.AuthAlgorithm 公认的IEEE 802.11认证算法
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);  
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);  //WifiConfiguration.Protocol 公认的安全协议。
            netConfig.allowedKeyManagement  
                    .set(WifiConfiguration.KeyMgmt.WPA_PSK);  //WifiConfiguration.KeyMgmt 公认的密钥管理方案。
            netConfig.allowedPairwiseCiphers  
                    .set(WifiConfiguration.PairwiseCipher.CCMP); //WifiConfiguration.PairwiseCipher 公认的成对WPA密码。 
            netConfig.allowedPairwiseCiphers  
                    .set(WifiConfiguration.PairwiseCipher.TKIP);  
            netConfig.allowedGroupCiphers  
                    .set(WifiConfiguration.GroupCipher.CCMP);  //WifiConfiguration.GroupCipher确认密码。
            netConfig.allowedGroupCiphers  
                    .set(WifiConfiguration.GroupCipher.TKIP);  
            method1.invoke(mWifiManager, netConfig, true);  //invoke()返回的结果动态地调用这个方法。

        } catch (IllegalArgumentException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        } catch (InvocationTargetException e) {  
            e.printStackTrace();  
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (NoSuchMethodException e) {  
            e.printStackTrace();  
        }  
    }  
  
    private static void closeWifiAp(WifiManager wifiManager) {  
        if (isWifiApEnabled(wifiManager)) {  
            try {  
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");  
                method.setAccessible(true);  
                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);  
                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);  
                method2.invoke(wifiManager, config, false);  
            } catch (NoSuchMethodException e) {  
                e.printStackTrace();  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            } catch (InvocationTargetException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    private static boolean isWifiApEnabled(WifiManager wifiManager) {  
        try {  
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");  
            method.setAccessible(true);  
            return (Boolean) method.invoke(wifiManager);  
        } catch (NoSuchMethodException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return false;  
    }  
}  