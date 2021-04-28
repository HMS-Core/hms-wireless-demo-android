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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Wireless kit test demo
 *
 * @since 2020-07-09
 */
public class MainActivity extends AppCompatActivity {
    private Button reportAppQuality;
    private Button networkPrediction;
    private Button networkQoe;
    private Button mWifiEnhance;
    private Button mDualWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reportAppQuality = findViewById(R.id.ReportAppQuality);
        networkPrediction = findViewById(R.id.NetworkPrediction);
        networkQoe = findViewById(R.id.NetworkQoe);
        mWifiEnhance = findViewById(R.id.WifiEnhance);
        mDualWifi = findViewById(R.id.DualWifi);

        // Click ReportAppQuality button to start ReportAppQualityActivity
        reportAppQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReportAppQualityActivity.class);
                startActivity(intent);
            }
        });

        // Click NetworkPrediction button to start NetworkPredictActivity
        networkPrediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NetworkPredictActivity.class);
                startActivity(intent);
            }
        });

        // Click NetworkQoe button to start NetworkQoeActivity
        networkQoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NetworkQoeActivity.class);
                startActivity(intent);
            }
        });

        // Click WifiEnhance button to start WifiEnhanceActivity
        mWifiEnhance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WifiEnhanceActivity.class);
                startActivity(intent);
            }
        });

        // Click DualWifi button to start DualWifiActivity
        mDualWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DualWifiActivity.class);
                startActivity(intent);
            }
        });
    }
}