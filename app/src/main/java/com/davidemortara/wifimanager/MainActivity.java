package com.davidemortara.wifimanager;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.status) TextView statusLabel;
    @BindView(R.id.ssid) EditText ssidText;
    @BindView(R.id.passphrase) EditText passphraseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.save)
    public void save(){
        saveWifi(ssidText.getText().toString(), passphraseText.getText().toString());
    }

    @OnClick(R.id.search)
    public void search(){
        if(isWifiPresent(ssidText.getText().toString()))
            statusLabel.setText("present");
        else
            statusLabel.setText("Not found");
    }

    private void saveWifi(String ssid, String passphrase){
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";   // Please note the quotes. String should contain ssid in quotes
        conf.preSharedKey = "\""+ passphrase +"\"";

        WifiManager wifiManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration configuration : list ) {
            if(configuration.SSID != null && configuration.SSID.equals("\"" + ssid + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(configuration.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }
    }

    private boolean isWifiPresent(String ssid){

        WifiManager wifiManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                return true;
            }
        }

        return false;
    }
}

