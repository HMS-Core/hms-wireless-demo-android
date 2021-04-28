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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.wireless.IWifiEnhanceService;
import com.huawei.hms.wireless.WirelessClient;
import com.huawei.hms.wireless.WirelessResult;
import com.huawei.hms.wireless.wifi.WifiEnhanceClient;

/**
 * wifi enhance activity
 *
 * @since 2020-08-17
 */
public class WifiEnhanceActivity extends AppCompatActivity {
    private static final String TAG = "WifiEnhanceActivity";
    private static final int TCP_PROTOCOL = 6;
    private static final int UDP_PROTOCOL = 17;
    private static final String PACKAGE_NAME = "com.huawei.hms.wirlesstestdemo";

    private IWifiEnhanceService mWifiEnhanceService = null;
    private Button mBindServiceButton;
    private Button mEnableTcpButton;
    private Button mEnableUdpButton;
    private Button mDisableAllButton;
    private Button mUnbindServiceButton;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mWifiEnhanceService = IWifiEnhanceService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWifiEnhanceService = null;
            Log.i(TAG, "onServiceDisConnected.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_enhance);
        this.setTitle("High Priority");
        initWidget();
        bindWifiEnhanceService();

        // enable tcp high priority
        enableTcp();

        // enable udp high priority
        enableUdp();

        // disable High Priority
        disableAll();
        mUnbindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWifiEnhanceService != null) {
                    WifiEnhanceActivity.this.unbindService(mServiceConnection);
                    mWifiEnhanceService = null;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWifiEnhanceService != null) {
            WifiEnhanceActivity.this.unbindService(mServiceConnection);
            mWifiEnhanceService = null;
        }
    }

    private void initWidget() {
        mBindServiceButton = findViewById(R.id.bindService);
        mEnableTcpButton = findViewById(R.id.enableTcp);
        mEnableUdpButton = findViewById(R.id.enableUdp);
        mDisableAllButton = findViewById(R.id.disableAll);
        mUnbindServiceButton = findViewById(R.id.unBindService);
    }

    private void bindWifiEnhanceService() {
        mBindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WifiEnhanceClient wifiEnhanceClient = WirelessClient.getWifiEnhanceClient(WifiEnhanceActivity.this);
                wifiEnhanceClient.getWifiEnhanceServiceIntent()
                        .addOnSuccessListener(new OnSuccessListener<WirelessResult>() {
                            @Override
                            public void onSuccess(WirelessResult wirelessResult) {
                                Intent intent = wirelessResult.getIntent();
                                if (intent == null) {
                                    Log.i(TAG, "intent is null.");
                                    return;
                                }
                                WifiEnhanceActivity.this.bindService(intent, mServiceConnection,
                                        Context.BIND_AUTO_CREATE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception exception) {
                                if (exception instanceof ApiException) {
                                    ApiException ex = (ApiException) exception;
                                    int errCode = ex.getStatusCode();
                                    Log.e(TAG, "Get intent failed:" + errCode);
                                }
                            }
                        });
            }
        });
    }

    private void enableTcp() {
        mEnableTcpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWifiEnhanceService != null) {
                    try {
                        Log.i(TAG, "enable WifiEnhanceService");
                        mWifiEnhanceService.setHighPriority(PACKAGE_NAME, TCP_PROTOCOL, 1); // 1: enable mode
                    } catch (RemoteException exception) {
                        Log.e(TAG, "no unregisterWifiEnhanceCallback api when enable tcp");
                    }
                }
            }
        });
    }

    private void enableUdp() {
        mEnableUdpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWifiEnhanceService != null) {
                    try {
                        mWifiEnhanceService.setHighPriority(PACKAGE_NAME, UDP_PROTOCOL, 1); // 1: enable mode
                    } catch (RemoteException e) {
                        Log.e(TAG, "no unregisterWifiEnhanceCallback api when enable udp");
                    }
                }
            }
        });
    }

    private void disableAll() {
        mDisableAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWifiEnhanceService != null) {
                    try {
                        Log.i(TAG, "disableHighPriority");
                        mWifiEnhanceService.setHighPriority(PACKAGE_NAME, UDP_PROTOCOL, 0); // 0: disable mode
                    } catch (RemoteException exception) {
                        Log.e(TAG, "no unregisterWifiEnhanceCallback api when disable all");
                    }
                }
            }
        });
    }
}
