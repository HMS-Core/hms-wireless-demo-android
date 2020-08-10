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
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.wireless.IQoeCallBack;
import com.huawei.hms.wireless.IQoeService;
import com.huawei.hms.wireless.NetworkQoeClient;
import com.huawei.hms.wireless.WirelessClient;
import com.huawei.hms.wireless.WirelessResult;

/**
 * Network qoe test demo
 *
 * @since 2020-07-9
 */
public class NetworkQoeActivity extends AppCompatActivity {
    private static final String TAG = "networkQoe";
    private static final int NETWORK_QOE_INFO_TYPE = 0;

    private Button getQoeButton;
    private Button registerButton;
    private Button unRegisterButton;
    private Button showQoeDetailButton;
    private Button cancelQoeButton;
    private EditText getQoeStateText;
    private EditText registerStateText;
    private EditText unregisterStateText;
    private EditText showQoeDetailsText;
    private EditText callQoeDetails;
    private int[] channelIndex = new int[4];
    private int[] uLRtt = new int[4];
    private int[] dLRtt = new int[4];
    private int[] uLBandwidth = new int[4];
    private int[] dLBandwidth = new int[4];
    private int[] uLRate = new int[4];
    private int[] dLRate = new int[4];
    private int[] netQoeLevel = new int[4];
    private int[] uLPkgLossRate = new int[4];
    private IQoeService qoeService = null;
    private ServiceConnection srcConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            qoeService = IQoeService.Stub.asInterface(service);
            getQoeStateText.setText("Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            qoeService = null;
            Log.i(TAG, "onServiceDisConnected.");
            getQoeStateText.setText("Disconnected");
        }
    };

    private IQoeCallBack callBack = new IQoeCallBack.Stub() {
        @Override
        public void callBack(int type, Bundle qoeInfo) throws RemoteException {
            if (qoeInfo == null || type != NETWORK_QOE_INFO_TYPE) {
                Log.e(TAG, "callback failed.type:" + type);
                return;
            }

            int channelNum = 0;
            if (qoeInfo.containsKey("channelNum")) {
                channelNum = qoeInfo.getInt("channelNum");
            }

            String channelQoe = String.valueOf(channelNum);
            for (int i = 0; i < channelNum; i++) {
                uLRtt[i] = qoeInfo.getInt("uLRtt" + i);
                dLRtt[i] = qoeInfo.getInt("dLRtt" + i);
                uLBandwidth[i] = qoeInfo.getInt("uLBandwidth" + i);
                dLBandwidth[i] = qoeInfo.getInt("dLBandwidth" + i);
                uLRate[i] = qoeInfo.getInt("uLRate" + i);
                dLRate[i] = qoeInfo.getInt("dLRate" + i);
                netQoeLevel[i] = qoeInfo.getInt("netQoeLevel" + i);
                uLPkgLossRate[i] = qoeInfo.getInt("uLPkgLossRate" + i);
                channelIndex[i] = qoeInfo.getInt("channelIndex" + i);
                channelQoe += "," + channelIndex[i] + "," + uLRtt[i] + "," + dLRtt[i] + "," + uLBandwidth[i] + ","
                    + dLBandwidth[i] + "," + uLRate[i] + "," + dLRate[i] + "," + netQoeLevel[i] + ","
                    + uLPkgLossRate[i];
            }

            Log.i(TAG, channelQoe);
            callQoeDetails.setText(channelQoe);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_qoe);
        this.setTitle("Network qoe");
        initWidget();

        // Bind QoeService
        bindQoeService();

        // Register network qoe callback
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                int ret = 0;
                if (qoeService != null) {
                    try {
                        ret = qoeService.registerNetQoeCallBack("com.huawei.hms.wirelessdemo", callBack);
                        registerStateText.setText(Integer.toString(ret));
                    } catch (RemoteException ex) {
                        Log.e(TAG, "no registerNetQoeCallback api");
                    }
                }
            }
        });

        // Unregister network qoe callback
        unRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                int ret = 0;
                if (qoeService != null) {
                    try {
                        ret = qoeService.unRegisterNetQoeCallBack("com.huawei.hms.wirelessdemo", callBack);
                        unregisterStateText.setText(Integer.toString(ret));
                    } catch (RemoteException ex) {
                        Log.e(TAG, "no unregisterNetQoeCallback api");
                    }
                }
            }
        });

        // Query real time qoe information
        showRealTimeQoeInfo();

        // Unbind QoeService
        cancelQoeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                if (qoeService != null) {
                    NetworkQoeActivity.this.unbindService(srcConn);
                    qoeService = null;
                    getQoeStateText.setText("Disconnected");
                }
            }
        });
    }

    private void initWidget() {
        getQoeButton = findViewById(R.id.ConnectQoe);
        registerButton = findViewById(R.id.registerQoe);
        unRegisterButton = findViewById(R.id.unRegisterQoe);
        showQoeDetailButton = findViewById(R.id.getQoeData);
        cancelQoeButton = findViewById(R.id.cancleService);
        getQoeStateText = findViewById(R.id.ConnectQoeState);
        registerStateText = findViewById(R.id.registerState);
        unregisterStateText = findViewById(R.id.unregisterState);
        showQoeDetailsText = findViewById(R.id.getQoeDataContext);
        callQoeDetails = findViewById(R.id.callQoeDetails);
    }

    private void bindQoeService() {
        getQoeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkQoeClient networkQoeClient = WirelessClient.getNetworkQoeClient(NetworkQoeActivity.this);
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
                                NetworkQoeActivity.this.bindService(intent, srcConn, Context.BIND_AUTO_CREATE);
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
            }
        });
    }

    private void showRealTimeQoeInfo() {
        showQoeDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                if (qoeService != null) {
                    try {
                        Bundle qoeInfo = qoeService.queryRealTimeQoe("com.huawei.hms.wirelessdemo");
                        if (qoeInfo == null) {
                            Log.e(TAG, "queryRealTimeQoe is empty.");
                            return;
                        }

                        int channelNum = 0;
                        if (qoeInfo.containsKey("channelNum")) {
                            channelNum = qoeInfo.getInt("channelNum");
                        }

                        String channelQoe = String.valueOf(channelNum);
                        for (int i = 0; i < channelNum; i++) {
                            uLRtt[i] = qoeInfo.getInt("uLRtt" + i);
                            dLRtt[i] = qoeInfo.getInt("dLRtt" + i);
                            uLBandwidth[i] = qoeInfo.getInt("uLBandwidth" + i);
                            dLBandwidth[i] = qoeInfo.getInt("dLBandwidth" + i);
                            uLRate[i] = qoeInfo.getInt("uLRate" + i);
                            dLRate[i] = qoeInfo.getInt("dLRate" + i);
                            netQoeLevel[i] = qoeInfo.getInt("netQoeLevel" + i);
                            uLPkgLossRate[i] = qoeInfo.getInt("uLPkgLossRate" + i);
                            channelIndex[i] = qoeInfo.getInt("channelIndex" + i);
                            channelQoe += "," + channelIndex[i] + "," + uLRtt[i] + "," + dLRtt[i] + ","
                                + uLBandwidth[i] + "," + dLBandwidth[i] + "," + uLRate[i] + ","
                                + dLRate[i] + "," + netQoeLevel[i] + "," + uLPkgLossRate[i];
                        }

                        Log.i(TAG, channelQoe);
                        showQoeDetailsText.setText(channelQoe);
                    } catch (RemoteException exception) {
                        Log.e(TAG, "no unregisterNetQoeCallback api");
                    }
                }
            }
        });
    }
}