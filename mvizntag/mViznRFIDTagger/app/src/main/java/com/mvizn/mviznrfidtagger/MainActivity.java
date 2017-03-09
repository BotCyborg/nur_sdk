package com.mvizn.mviznrfidtagger;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.nordicid.nurapi.*;

public class MainActivity extends AppCompatActivity implements NurApiListener {

    private NurApi mApi;
    private NurApiAutoConnectTransport mAutoConnectTransport = null;
    private boolean mListening = false;
    private Button mControlBtn;
    private TextView mStatus;
    private TextView mInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mControlBtn = (Button)findViewById(R.id.controlBtn);
        mStatus = (TextView)findViewById(R.id.statusText);
        mInformation = (TextView)findViewById(R.id.infoText);
        mStatus.setText("Idle.");
        mInformation.setText("Reader information: N/ A");

        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListening = !mListening;
                if (mListening) {
                    startConnectionListening();
                    mStatus.setText("Waiting for USB connection...");
                    mControlBtn.setText("Stop listening");
                }
                else {
                    stopConnectionListening();
                    mStatus.setText("Idle");
                    mControlBtn.setText("Start listening");
                }
            }
        });
        prepareAPI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAutoConnectTransport != null)
            mAutoConnectTransport.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mAutoConnectTransport != null)
            mAutoConnectTransport.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mAutoConnectTransport != null)
            mAutoConnectTransport.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mAutoConnectTransport != null)
            mAutoConnectTransport.onDestroy();
        if (mApi != null)
            mApi.dispose();
    }

    private void prepareAPI() {
        mApi = new NurApi();
        // Needed for UI access.
        mApi.setUiThreadRunner(new NurApiUiThreadRunner() {
            @Override
            public void runOnUiThread(Runnable runnable) {
                MainActivity.this.runOnUiThread(runnable);
            }
        });
        // “this” implements the listener:
        mApi.setListener(this);
    }

    private void startConnectionListening() {
        if (mAutoConnectTransport == null) {
            mAutoConnectTransport = new NurApiUsbAutoConnect(this, mApi);
            mAutoConnectTransport.setAddress("USB");
        }
    }

    private void stopConnectionListening() {
        if (mAutoConnectTransport != null)
        {
            mAutoConnectTransport.dispose();
            mAutoConnectTransport = null;
        }
    }

    @Override
    public void logEvent(int i, String s) {

    }

    @Override
    public void connectedEvent() {
        mStatus.setText("Connected.");
        tryReaderInformation();
    }

    @Override
    public void disconnectedEvent() {
        if (mListening)
            mStatus.setText("Waiting for USB connection...");
        else
            mStatus.setText("Idle.");
        mInformation.setText("Reader information: N/ A");
    }

    @Override
    public void bootEvent(String s) {

    }

    @Override
    public void inventoryStreamEvent(NurEventInventory nurEventInventory) {

    }

    @Override
    public void IOChangeEvent(NurEventIOChange nurEventIOChange) {

    }

    @Override
    public void traceTagEvent(NurEventTraceTag nurEventTraceTag) {

    }

    @Override
    public void triggeredReadEvent(NurEventTriggeredRead nurEventTriggeredRead) {

    }

    @Override
    public void frequencyHopEvent(NurEventFrequencyHop nurEventFrequencyHop) {

    }

    @Override
    public void debugMessageEvent(String s) {

    }

    @Override
    public void inventoryExtendedStreamEvent(NurEventInventory nurEventInventory) {

    }

    @Override
    public void programmingProgressEvent(NurEventProgrammingProgress nurEventProgrammingProgress) {

    }

    @Override
    public void deviceSearchEvent(NurEventDeviceInfo nurEventDeviceInfo) {

    }

    @Override
    public void clientConnectedEvent(NurEventClientInfo nurEventClientInfo) {

    }

    @Override
    public void clientDisconnectedEvent(NurEventClientInfo nurEventClientInfo) {

    }

    @Override
    public void nxpEasAlarmEvent(NurEventNxpAlarm nurEventNxpAlarm) {

    }

    @Override
    public void epcEnumEvent(NurEventEpcEnum nurEventEpcEnum) {

    }

    @Override
    public void autotuneEvent(NurEventAutotune nurEventAutotune) {

    }

    private void tryReaderInformation() {
        try {
            NurRespReaderInfo ri;
            String strInfo = "";
            ri = mApi.getReaderInfo();
            strInfo = "Reader: " + ri.name + "\n" + "FW: " + ri.swVersion;
            mInformation.setText(strInfo);
        }
        catch (Exception e)
        {
            mInformation.setText("Reader information error: " +
                    e.getMessage());
        }
    }
}
