/*
 * Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.
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
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.wireless.IDualWifiService;
import com.huawei.hms.wireless.INetworkCallback;
import com.huawei.hms.wireless.WirelessClient;
import com.huawei.hms.wireless.WirelessResult;
import com.huawei.hms.wireless.wifi.DualWifiClient;

/**
 * Dual wifi test demo
 *
 * @since 2021-03-10
 */
public class DualWifiActivity extends AppCompatActivity {
    private static final String TAG = "DualWifiActivity";
    private static final String PACKAGE_NAME = "com.huawei.hms.wirlesstestdemo";
    private static final String RSSI_CHANGED_ACTION = "huawei.net.slave_wifi.RSSI_CHANGED";
    private static final String WIFI_STATE_CHANGED_ACTION = "huawei.net.slave_wifi.WIFI_STATE_CHANGED";

    private IDualWifiService mDualWifiService = null;
    private Button mBindServiceButton;
    private Button mEnableDualWifiButton;
    private Button mDisableDualWifiButton;
    private Button mGetWifiConnectionInfoButton;
    private Button mGetLinkPropertiesButton;
    private Button mGetNetworkInfoButton;
    private Button mUnbindServiceButton;
    private BroadcastReceiver receiver = null;
    private TextView showDualWifiText;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDualWifiService = IDualWifiService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDualWifiService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dual_wifi);
        this.setTitle("Dual Wifi");
        initWidget();
        startListener();
        bindDualWifiService();

        enableDualWifi();

        getWifiConnectionInfoButton();

        getLinkproperties();

        getNetworkInfo();

        disableDualWifi();

        unBindDualWifiService();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void initWidget() {
        mBindServiceButton = findViewById(R.id.bindService);
        mEnableDualWifiButton = findViewById(R.id.enableDualWifi);
        mDisableDualWifiButton = findViewById(R.id.disableDualWifi);
        mGetWifiConnectionInfoButton = findViewById(R.id.getWifiConnectionInfo);
        mGetLinkPropertiesButton = findViewById(R.id.getLinkproperties);
        mGetNetworkInfoButton = findViewById(R.id.getNetworkInfo);
        mUnbindServiceButton = findViewById(R.id.unBindService);
        showDualWifiText = findViewById(R.id.wifiinfo);
    }

    private void enableDualWifi() {
        mEnableDualWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDualWifiService != null) {
                    try {
                        showDualWifiText.setText("");
                        mDualWifiService.enableDualWifi(PACKAGE_NAME, callBack);
                    } catch (RemoteException e) {
                        Log.e(TAG, "no unregisterDualWifiCallback api when enable dual wifi");
                    }
                }
            }
        });
    }

    private void getWifiConnectionInfoButton() {
        mGetWifiConnectionInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDualWifiService != null) {
                    try {
                        String connectionInfo = mDualWifiService.getSlaveWifiConnectionInfo();
                        showDualWifiText.setText(connectionInfo);
                    } catch (RemoteException e) {
                        Log.e(TAG, "no unregisterDualWifiCallback api when get wifiConnectionInfo");
                    }
                }
            }
        });
    }

    private void getLinkproperties() {
        mGetLinkPropertiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDualWifiService != null) {
                    try {
                        LinkProperties mLinkProperties = mDualWifiService.getLinkPropertiesForSlaveWifi();
                        if (mLinkProperties != null) {
                            showDualWifiText.setText(mLinkProperties.toString());
                        }
                    } catch (RemoteException e) {
                        Log.e(TAG, "no unregisterDualWifiCallback api when get getLinkProperties for slave wifi");
                    }
                }
            }
        });
    }

    private void getNetworkInfo() {
        mGetNetworkInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDualWifiService != null) {
                    try {
                        NetworkInfo mNetworkInfo = mDualWifiService.getNetworkInfoForSlaveWifi();
                        if (mNetworkInfo != null) {
                            showDualWifiText.setText(mNetworkInfo.toString());
                        }
                    } catch (RemoteException e) {
                        Log.e(TAG, "no unregisterDualWifiCallback api when get getNetworkInfo for slave wifi");
                    }
                }
            }
        });
    }

    private void disableDualWifi() {
        mDisableDualWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDualWifiService != null) {
                    try {
                        mDualWifiService.disableDualWifi(PACKAGE_NAME, callBack);
                        showDualWifiText.setText("");
                    } catch (RemoteException e) {
                        Log.e(TAG, "no unregisterDualWifiCallback api when disable dual wifi");
                    }
                }
            }
        });
    }

    private void bindDualWifiService() {
        mBindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DualWifiClient dualWifiClient = WirelessClient.getDualWifiClient(DualWifiActivity.this);
                dualWifiClient.getDualWifiServiceIntent()
                        .addOnSuccessListener(new OnSuccessListener<WirelessResult>() {
                            @Override
                            public void onSuccess(WirelessResult wirelessResult) {
                                Intent intent = wirelessResult.getIntent();
                                if (intent == null) {
                                    Log.i(TAG, "onSuccess: intent is null");
                                    return;
                                }
                                boolean isBind = DualWifiActivity.this.bindService(intent, mServiceConnection,
                                        Context.BIND_AUTO_CREATE);
                                Log.d(TAG, "isBind: " + isBind);
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

    private void unBindDualWifiService() {
        mUnbindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDualWifiService != null) {
                    DualWifiActivity.this.unbindService(mServiceConnection);
                    mDualWifiService = null;
                }
            }
        });
    }

    private void startListener() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (RSSI_CHANGED_ACTION.equals(intent.getAction())) {
                    Log.d(TAG, "onReceive: get RSSI_CHANGED_ACTION");
                } else if (WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                    Log.d(TAG, "onReceive: get WIFI_STATE_CHANGED_ACTION");
                }
            }
        };

        final IntentFilter filter = new IntentFilter();
        filter.addAction(RSSI_CHANGED_ACTION);
        filter.addAction(WIFI_STATE_CHANGED_ACTION);
        registerReceiver(receiver, filter);
    }

    private INetworkCallback callBack = new INetworkCallback.Stub() {
        @Override
        public void onAvailable(Network network) {
            if (network == null) {
                return;
            }
        }

        @Override
        public void onUnavailable() {
            Log.d(TAG, "onUnavailable: DualWifiActivity receive");
        }

        @Override
        public void onLost(Network network) {
            if (network == null) {
                return;
            }
            Log.d(TAG, "onLost: DualWifiActivity receive");
        }
    };
}