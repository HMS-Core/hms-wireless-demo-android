/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.huawei.hms.wirelessdemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.wireless.IQoeService;
import com.huawei.hms.wireless.NetworkQoeClient;
import com.huawei.hms.wireless.WirelessClient;
import com.huawei.hms.wireless.WirelessResult;

/**
 * Network predict test demo
 *
 * @since 2020-07-9
 */
public class NetworkPredictActivity extends AppCompatActivity {
    private static final String TAG = "networkPredict";
    private static final String NETWORK_PREDICTION_ACTION = "com.huawei.hms.action.ACTION_NETWORK_PREDICTION";

    private TextView enteringTimer;
    private TextView leaveingTimer;
    private TextView typeValue;
    private TextView showStateText;
    private NetworkQoeClient networkQoeClient;
    private int reportTime = 0;
    private BroadcastReceiver receiver = null;
    private IQoeService qoeService = null;

    private ServiceConnection mSrcConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            qoeService = IQoeService.Stub.asInterface(service);
            if (qoeService != null) {
                Log.i(TAG, "bind success.");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            qoeService = null;
            Log.i(TAG, "bind failed.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_prediction);
        this.setTitle("Network prediction");
        showStateText = findViewById(R.id.showState);
        enteringTimer = findViewById(R.id.enteringTimer);
        leaveingTimer = findViewById(R.id.leaveingTimer);
        typeValue = findViewById(R.id.typeValue);
        showStateText.setText("Prediction start!");
        startListener();
        networkQoeClient = WirelessClient.getNetworkQoeClient(NetworkPredictActivity.this);
        if (networkQoeClient != null) {
            networkQoeClient.getNetworkQoeServiceIntent()
                .addOnSuccessListener(new OnSuccessListener<WirelessResult>() {
                    @Override
                    public void onSuccess(WirelessResult wirelessResult) {
                        Intent intent = wirelessResult.getIntent();
                        if (intent == null) {
                            Log.i(TAG, "intent is null.");
                            return;
                        }

                        NetworkPredictActivity.this.bindService(intent, mSrcConn, Context.BIND_AUTO_CREATE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception excep) {
                        if (excep instanceof ApiException) {
                            ApiException ex = (ApiException) excep;
                            int errCode = ex.getStatusCode();
                            Log.e(TAG, "Get intent failed:" + errCode);
                        }
                    }
                });
        }
    }

    private void startListener() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                reportTime++;
                int enteringTime = intent.getIntExtra("enteringTime", 0);
                int leavingTime = intent.getIntExtra("leavingTime", 0);
                int type = intent.getIntExtra("type", 0);
                enteringTimer.setText(Integer.toString(enteringTime));
                leaveingTimer.setText(Integer.toString(leavingTime));
                typeValue.setText(Integer.toString(type));
            }
        };

        final IntentFilter filter = new IntentFilter();
        filter.addAction(NETWORK_PREDICTION_ACTION);
        registerReceiver(receiver, filter);
    }
}