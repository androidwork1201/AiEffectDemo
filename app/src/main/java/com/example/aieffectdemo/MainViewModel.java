package com.example.aieffectdemo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aieffectdemo.data.ApiClient;
import com.example.aieffectdemo.data.ApiInterface;
import com.example.aieffectdemo.data.ResAiEffectData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> licenseData = new MutableLiveData<>();


    public void getZegoEffectData(String action,
                                  int appId,
                                  String authInfo) {
        ApiInterface apiInterface = ApiClient.getApiData().create(ApiInterface.class);
        Call<ResAiEffectData> call = apiInterface.getEffectData(action, appId, authInfo);

        call.enqueue(new Callback<ResAiEffectData>() {
            @Override
            public void onResponse(Call<ResAiEffectData> call, Response<ResAiEffectData> response) {
                try {
                    licenseData.postValue(response.body().getData().getLicense());
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResAiEffectData> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<String> getLicenseData() {
        return licenseData;
    }
}
