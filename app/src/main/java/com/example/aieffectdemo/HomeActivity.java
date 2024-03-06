package com.example.aieffectdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.aieffectdemo.data.AiEffectParametersData;
import com.example.aieffectdemo.databinding.ActivityHomeBinding;
import com.google.gson.Gson;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    private Long appId;
    private String appSign;
    private String userId;
    private String userName;
    private String roomId;
    private String licenseData;
    private AiEffectParametersData.BeautyData beautyData;
    private AiEffectParametersData.SmoothData smoothData;
    private AiEffectParametersData.MakeupsData makeupsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getParameters();
    }

    private void getParameters() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            if (bundle != null) {
                appId = bundle.getLong("appId");
                appSign = bundle.getString("appSign");
                userId = bundle.getString("userId");
                userName = bundle.getString("userName");
                roomId = bundle.getString("roomId");
                licenseData = bundle.getString("licenseData");
                beautyData = new Gson().fromJson(bundle.getString("beautyData"), AiEffectParametersData.BeautyData.class);
                smoothData = new Gson().fromJson(bundle.getString("smoothData"), AiEffectParametersData.SmoothData.class);
                makeupsData = new Gson().fromJson(bundle.getString("makeupsData"), AiEffectParametersData.MakeupsData.class);

            }

        }

    }
}