package com.example.aieffectdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.aieffectdemo.adapter.PendantAdapter;
import com.example.aieffectdemo.data.AiEffectParametersData;
import com.example.aieffectdemo.databinding.ActivityHomeBinding;
import com.example.aieffectdemo.util.AiEffectConstant;
import com.example.aieffectdemo.util.AiEffectManager;
import com.example.aieffectdemo.util.AssetsFileUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import im.zego.effects.ZegoEffects;
import im.zego.effects.entity.ZegoEffectsBigEyesParam;
import im.zego.effects.entity.ZegoEffectsBlusherParam;
import im.zego.effects.entity.ZegoEffectsCheekboneSlimmingParam;
import im.zego.effects.entity.ZegoEffectsColoredcontactsParam;
import im.zego.effects.entity.ZegoEffectsDarkCirclesRemovingParam;
import im.zego.effects.entity.ZegoEffectsEyelashesParam;
import im.zego.effects.entity.ZegoEffectsEyelinerParam;
import im.zego.effects.entity.ZegoEffectsEyesBrighteningParam;
import im.zego.effects.entity.ZegoEffectsEyeshadowParam;
import im.zego.effects.entity.ZegoEffectsFaceLiftingParam;
import im.zego.effects.entity.ZegoEffectsFaceShorteningParam;
import im.zego.effects.entity.ZegoEffectsFilterParam;
import im.zego.effects.entity.ZegoEffectsForeheadShorteningParam;
import im.zego.effects.entity.ZegoEffectsLipstickParam;
import im.zego.effects.entity.ZegoEffectsLongChinParam;
import im.zego.effects.entity.ZegoEffectsMakeupParam;
import im.zego.effects.entity.ZegoEffectsMandibleSlimmingParam;
import im.zego.effects.entity.ZegoEffectsNoseLengtheningParam;
import im.zego.effects.entity.ZegoEffectsNoseNarrowingParam;
import im.zego.effects.entity.ZegoEffectsRosyParam;
import im.zego.effects.entity.ZegoEffectsSharpenParam;
import im.zego.effects.entity.ZegoEffectsSmallMouthParam;
import im.zego.effects.entity.ZegoEffectsSmoothParam;
import im.zego.effects.entity.ZegoEffectsTeethWhiteningParam;
import im.zego.effects.entity.ZegoEffectsVideoFrameParam;
import im.zego.effects.entity.ZegoEffectsWhitenParam;
import im.zego.effects.entity.ZegoEffectsWrinklesRemovingParam;
import im.zego.effects.enums.ZegoEffectsVideoFrameFormat;
import im.zego.zegoexpress.ZegoExpressEngine;
import im.zego.zegoexpress.callback.IZegoCustomVideoProcessHandler;
import im.zego.zegoexpress.callback.IZegoEventHandler;
import im.zego.zegoexpress.constants.ZegoPublishChannel;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoUpdateType;
import im.zego.zegoexpress.constants.ZegoVideoBufferType;
import im.zego.zegoexpress.entity.ZegoCanvas;
import im.zego.zegoexpress.entity.ZegoCustomVideoProcessConfig;
import im.zego.zegoexpress.entity.ZegoEngineProfile;
import im.zego.zegoexpress.entity.ZegoRoomConfig;
import im.zego.zegoexpress.entity.ZegoStream;
import im.zego.zegoexpress.entity.ZegoUser;

public class HomeActivity extends AppCompatActivity implements PendantAdapter.OnItemListener {

    ActivityHomeBinding binding;

    private Long appId;
    private String appSign;
    private String userId;
    private String userName;
    private String roomId = "1000000";
    private String licenseData;
    private AiEffectParametersData.BeautyData beautyData;
    private AiEffectParametersData.SmoothData smoothData;
    private AiEffectParametersData.MakeupsData makeupsData;
    private ZegoEngineProfile profile;

    private PendantAdapter adapter;

    private boolean isOpen = false;
    private String bgImagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.fabSticker.getDrawable().setTint(ContextCompat.getColor(this, R.color.white));
//        binding.fabBg.getDrawable().setTint(ContextCompat.getColor(this, R.color.white));

        getParameters();
//        initFab();
//        setPendant();
        binding.ivEnd.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
        destroyEngine();
    }


//    private void initFab() {
//        binding.fabSticker.setOnClickListener(v -> {
//            binding.fabSticker.setVisibility(View.GONE);
//            binding.fabBg.setVisibility(View.GONE);
//            binding.rvSticker.setVisibility(View.VISIBLE);
//            setPendant();
//        });
//
//        binding.fabBg.setOnClickListener(v -> {
//            binding.fabBg.setVisibility(View.GONE);
//            binding.fabSticker.setVisibility(View.GONE);
//            binding.rvSticker.setVisibility(View.VISIBLE);
//            setBg();
//        });
//
//        binding.clEffect.setOnClickListener(v -> {
//            closePendant();
//        });
//    }

//    private void setPendant() {
//
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("Animal");
//        arrayList.add("Cat");
//        arrayList.add("ClawMachine");
//        arrayList.add("Clown");
//        arrayList.add("CoolGirl");
//        arrayList.add("Deer");
//        arrayList.add("Dive");
//        arrayList.add("SailorMoon");
//        arrayList.add("Watermelon");
//        arrayList.add("移除掛件");
//
//        adapter = new PendantAdapter();
//        adapter.setOnItemListener(this);
//        binding.rvSticker.setLayoutManager(new LinearLayoutManager(this));
//        binding.rvSticker.setHasFixedSize(true);
//        binding.rvSticker.setAdapter(adapter);
//        adapter.setData(arrayList);
//    }

//    private void setBg() {
//
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("背景1");
//        arrayList.add("背景2");
//        arrayList.add("背景3");
//        arrayList.add("移除背景效果");
//
//
//        adapter = new PendantAdapter();
//        adapter.setOnItemListener(this);
//        binding.rvSticker.setLayoutManager(new LinearLayoutManager(this));
//        binding.rvSticker.setHasFixedSize(true);
//        binding.rvSticker.setAdapter(adapter);
//        adapter.setData(arrayList);
//    }
//
//    private void closePendant() {
//        binding.fabSticker.setVisibility(View.VISIBLE);
//        binding.fabBg.setVisibility(View.VISIBLE);
//        binding.rvSticker.setVisibility(View.GONE);
//        isOpen = !isOpen;
//    }


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

                AiEffectManager.getInstance().initAiEffect(this, licenseData);
                AiEffectManager.getInstance().setBeautyData(beautyData);
                AiEffectManager.getInstance().setSmoothData(smoothData);
                AiEffectManager.getInstance().setMakeupsData(makeupsData);

                createEngine();
                initVideoProcess();
                startListenerEvent();
                loginRoom();
            }

        }

    }

    private void createEngine() {
        profile = new ZegoEngineProfile();
        profile.appID = appId;
        profile.appSign = appSign;
        profile.scenario = ZegoScenario.STANDARD_VIDEO_CALL;
        profile.application = this.getApplication();
        ZegoExpressEngine.createEngine(profile, null);
    }

    private void destroyEngine() {
        ZegoExpressEngine.destroyEngine(null);
    }

    private void initVideoProcess() {
        ZegoCustomVideoProcessConfig config = new ZegoCustomVideoProcessConfig();
        config.bufferType = ZegoVideoBufferType.GL_TEXTURE_2D;
        ZegoExpressEngine.getEngine().enableCustomVideoProcessing(true, config, ZegoPublishChannel.MAIN);
        ZegoExpressEngine.getEngine().setCustomVideoProcessHandler(new IZegoCustomVideoProcessHandler() {

            @Override
            public void onStart(ZegoPublishChannel channel) {
                super.onStart(channel);
                AiEffectManager.getInstance().getEffects().initEnv(1280, 720);
            }

            @Override
            public void onStop(ZegoPublishChannel channel) {
                super.onStop(channel);
            }

            @Override
            public void onCapturedUnprocessedTextureData(int textureID, int width, int height, long referenceTimeMillisecond, ZegoPublishChannel channel) {
                ZegoEffectsVideoFrameParam param = new ZegoEffectsVideoFrameParam();
                param.format = ZegoEffectsVideoFrameFormat.RGBA32;
                param.width = width;
                param.height = height;

                int processedTextureID = AiEffectManager.getInstance().getEffects().processTexture(textureID, param);
                ZegoExpressEngine.getEngine().sendCustomVideoProcessedTextureData(
                        processedTextureID,
                        width,
                        height,
                        referenceTimeMillisecond);
            }
        });
    }

    private void stopListenEvent() {
        ZegoExpressEngine.getEngine().setEventHandler(null);
    }

    private void startListenerEvent() {
        ZegoExpressEngine.getEngine().setEventHandler(new IZegoEventHandler() {
            @Override
            public void onRoomStreamUpdate(String roomID, ZegoUpdateType updateType, ArrayList<ZegoStream> streamList, JSONObject extendedData) {
                super.onRoomStreamUpdate(roomID, updateType, streamList, extendedData);
                if (updateType == ZegoUpdateType.ADD) {
                    startPlayStream(streamList.get(0).streamID);
                } else {
                    stopPlayStream(streamList.get(0).streamID);
                }
            }
        });
    }

    private void loginRoom() {
        ZegoUser user = new ZegoUser(userId, userName);
        ZegoRoomConfig roomConfig = new ZegoRoomConfig();
        roomConfig.isUserStatusNotify = true;
        ZegoExpressEngine.getEngine().loginRoom(roomId, user, roomConfig, (int error, JSONObject extendData) -> {
            if (error == 0) {
                startPreview();
                startPublishStream();
            }
        });
    }

    //顯示畫面預覽(自己)
    private void startPreview() {
        ZegoCanvas canvas = new ZegoCanvas(binding.ttvLocal);
        ZegoExpressEngine.getEngine().startPreview(canvas);
    }

    //停止畫面預覽
    private void stopPreview() {
        ZegoExpressEngine.getEngine().stopPreview();
    }

    //推流
    private void startPublishStream() {
        ZegoCanvas canvas = new ZegoCanvas(binding.ttvLocal);
        ZegoExpressEngine.getEngine().startPreview(canvas);
        String streamId = roomId + "_" + userId;
        ZegoExpressEngine.getEngine().startPublishingStream(streamId);
    }

    //停止推流
    private void stopPublishStream() {
        ZegoExpressEngine.getEngine().stopPublishingStream();
    }

    //播放串流
    private void startPlayStream(String streamId) {
        ZegoCanvas canvas = new ZegoCanvas(binding.ttvRemote);
        ZegoExpressEngine.getEngine().startPlayingStream(streamId, canvas);
    }

    //停止播放串流
    private void stopPlayStream(String streamId) {
        ZegoExpressEngine.getEngine().stopPlayingStream(streamId);
    }



    @Override
    public void onPendant(int position, String itemName) {
        String path = this.getCacheDir().getPath();
        switch (itemName) {
            case "Animal": {
                AiEffectManager.getInstance().setPendantEffect(path + File.separator + AiEffectConstant.Pendant.pendantAnimal);
                break;
            }
            case "Cat": {
                AiEffectManager.getInstance().setPendantEffect(path + File.separator + AiEffectConstant.Pendant.pendantCat);
                break;
            }
            case "ClawMachine": {
                AiEffectManager.getInstance().setPendantEffect(path + File.separator + AiEffectConstant.Pendant.pendantClawMachine);
                break;
            }
            case "Clown": {
                AiEffectManager.getInstance().setPendantEffect(path + File.separator + AiEffectConstant.Pendant.pendantClown);
                break;
            }
            case "CoolGirl": {
                AiEffectManager.getInstance().setPendantEffect(path + File.separator + AiEffectConstant.Pendant.pendantCoolGirl);
                break;
            }
            case "Deer": {
                AiEffectManager.getInstance().setPendantEffect(path + File.separator + AiEffectConstant.Pendant.pendantDeer);
                break;
            }
            case "Dive": {
                AiEffectManager.getInstance().setPendantEffect(path + File.separator + AiEffectConstant.Pendant.pendantDive);
                break;
            }
            case "SailorMoon": {
                AiEffectManager.getInstance().setPendantEffect(path + File.separator + AiEffectConstant.Pendant.pendantSailorMoon);
                break;
            }
            case "Watermelon": {
                AiEffectManager.getInstance().setPendantEffect(path + File.separator + AiEffectConstant.Pendant.pendantWatermelon);
                break;
            }
            case "移除掛件": {
                AiEffectManager.getInstance().closePendantEffect();
                break;
            }
            case "背景1": {
                AiEffectManager.getInstance().setPortraitSegmentation(path + File.separator + AiEffectConstant.BgImage.bgImage_1);
                break;
            }
            case "背景2": {
                AiEffectManager.getInstance().setPortraitSegmentation(path + File.separator + AiEffectConstant.BgImage.bgImage_2);
                break;
            }
            case "背景3": {
                AiEffectManager.getInstance().setPortraitSegmentation(path + File.separator + AiEffectConstant.BgImage.bgImage_3);
                break;
            }
            case "移除背景效果": {
                AiEffectManager.getInstance().closeSetPortraitSegmentation();
            }
            default:
                break;
        }
//        closePendant();
    }
}
