package com.example.aieffectdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.aieffectdemo.adapter.BottomNavAdapter;
import com.example.aieffectdemo.adapter.SubBottomNavAdapter;
import com.example.aieffectdemo.data.AiEffectParametersData;
import com.example.aieffectdemo.databinding.ActivityMainBinding;
import com.example.aieffectdemo.util.AiEffectConstant;
import com.example.aieffectdemo.util.AiEffectManager;
import com.example.aieffectdemo.util.AssetsFileUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

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

public class MainActivity extends AppCompatActivity implements BottomNavAdapter.OnItemListener, SubBottomNavAdapter.OnItemListener {

    public static final Long APP_ID = 172388699L;
    public static final String APP_SIGN = "88688e6673bb3362bfb16f5d6d3833337c2d638fb6fba5d84c0b1ba201e9784f";
    ActivityMainBinding binding;
    BottomNavAdapter adapter;
    SubBottomNavAdapter subAdapter;
    private MainViewModel viewModel;
    private String userId;
    private String userName;
    private String roomId;
    private String licenseData;

    private String eyeLineResPath;
    private String eyeShadowResPath;
    private String eyeLashesResPath;
    private String blusherResPath;
    private String lipStickResPath;
    private String coloredContactsResPath;
    private String makeupResPath;
    private String filterResPath;
    private boolean isOpen = false;
    private boolean isBackAgain = false;

    private ZegoEngineProfile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(binding.getRoot());

        userId = generateUserID();
        userName = "user_" + userId;
        roomId = "3526";

        binding.fabEffect.getDrawable().setTint(ContextCompat.getColor(this, R.color.white));
        binding.fabStart.getDrawable().setTint(ContextCompat.getColor(this, R.color.white));

        checkPermission();
        textHintAnim();
        initFab();
        initBottomNav();
        initSubBottomNav();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isBackAgain) finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPublishStream();
        destroyEngine();
    }

    private void initFab() {
        binding.fabEffect.setOnClickListener(v -> {
            binding.llEffect.setVisibility(View.VISIBLE);
            binding.fabEffect.setVisibility(View.GONE);
            binding.fabStart.setVisibility(View.GONE);
        });

        binding.clEffect.setOnClickListener(v -> closeAiEffect());

        binding.fabStart.setOnClickListener(v -> {
            setParametersToNextPage();

        });
    }

    private void closeAiEffect() {
        binding.llEffect.setVisibility(View.GONE);
        binding.fabEffect.setVisibility(View.VISIBLE);
        binding.fabStart.setVisibility(View.VISIBLE);
        binding.rvSubNav.setVisibility(View.GONE);
        binding.clAttributesAdjust.setVisibility(View.GONE);
        binding.clMakeupsAdjust.setVisibility(View.GONE);
        isOpen = !isOpen;
    }

    private void initBottomNav() {

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("美顏");
        arrayList.add("美型");
        arrayList.add("美妝");
        arrayList.add("濾鏡");

        adapter = new BottomNavAdapter();
        adapter.setOnItemListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvNav.setLayoutManager(manager);
        binding.rvNav.setHasFixedSize(true);
        binding.rvNav.setAdapter(adapter);
        adapter.setData(arrayList);
    }

    private void initSubBottomNav() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("美白");
        arrayList.add("磨皮");
        arrayList.add("紅潤");
        arrayList.add("銳化");
        arrayList.add("去除法令紋");
        arrayList.add("去除黑眼圈");

        subAdapter = new SubBottomNavAdapter();
        subAdapter.setOnItemListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvSubNav.setLayoutManager(manager);
        binding.rvSubNav.setHasFixedSize(true);
        binding.rvSubNav.setAdapter(subAdapter);
        subAdapter.setData(arrayList);
    }

    private void initGetEffectLicenseData() {
        String encryptInfo = ZegoEffects.getAuthInfo(APP_SIGN, this);

        new Handler().post(() -> {
            viewModel.getZegoEffectData("DescribeEffectsLicense", APP_ID.intValue(), encryptInfo);
        });

        viewModel.getLicenseData().observe(this, license -> {
            licenseData = license;

            if (licenseData != null) {
                AiEffectManager.getInstance().initAiEffect(this, licenseData);
                createEngine();
                initVideoProcess();
                startListenerEvent();
                loginRoom();
            }
        });
    }

    @Override
    public void onItem(int position) {
        binding.rvSubNav.setVisibility(isOpen && binding.rvSubNav.getVisibility() == View.VISIBLE
                ? View.GONE
                : View.VISIBLE);
        isOpen = !isOpen;

        switch (position) {
            case 0: {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("美白");
                arrayList.add("磨皮");
                arrayList.add("紅潤");
                arrayList.add("銳化");
                arrayList.add("去除法令紋");
                arrayList.add("去除黑眼圈");

                subAdapter.setData(arrayList);
                binding.clAttributesAdjust.setVisibility(View.GONE);
                binding.clMakeupsAdjust.setVisibility(View.GONE);

                break;
            }
            case 1: {

                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("大眼");
                arrayList.add("瘦臉");
                arrayList.add("小嘴");
                arrayList.add("亮眼");
                arrayList.add("瘦鼻");
                arrayList.add("白牙");
                arrayList.add("長下巴");
                arrayList.add("缩小额頭高度");
                arrayList.add("瘦下額骨");
                arrayList.add("瘦颧骨");
                arrayList.add("長鼻");

                subAdapter.setData(arrayList);
                binding.clAttributesAdjust.setVisibility(View.GONE);
                binding.clMakeupsAdjust.setVisibility(View.GONE);

                break;
            }
            case 2: {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("眼線");
                arrayList.add("眼影");
                arrayList.add("眼睫毛");
                arrayList.add("腮紅");
                arrayList.add("口紅");
                arrayList.add("美瞳");
                arrayList.add("風格妝");

                subAdapter.setData(arrayList);
                binding.clAttributesAdjust.setVisibility(View.GONE);
                binding.clMakeupsAdjust.setVisibility(View.GONE);
                break;
            }
            case 3: {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("自然");
                arrayList.add("灰調");
                arrayList.add("夢境");

                subAdapter.setData(arrayList);
                binding.clAttributesAdjust.setVisibility(View.GONE);
                binding.clMakeupsAdjust.setVisibility(View.GONE);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onSubItem(String itemName) {
        switch (itemName) {
            case "美白": {
                adjustmentFaceWhiten();
                AiEffectManager.getInstance().faceWhiten(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "磨皮": {
                adjustmentSmoothFace();
                AiEffectManager.getInstance().smoothFace(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "紅潤": {
                adjustmentRosy();
                AiEffectManager.getInstance().rosyEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "銳化": {
                adjustmentSharpen();
                AiEffectManager.getInstance().sharpenEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "去除法令紋": {
                adjustmentWrinkles();
                AiEffectManager.getInstance().wrinklesEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "去除黑眼圈": {
                adjustmentDarkCircles();
                AiEffectManager.getInstance().darkCirclesEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }

            case "大眼": {
                adjustmentBigEye();
                AiEffectManager.getInstance().bigEyeEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "瘦臉": {
                adjustmentFaceLift();
                AiEffectManager.getInstance().faceLiftEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "小嘴": {
                adjustmentSmailMouth();
                AiEffectManager.getInstance().smailMouthEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "亮眼": {
                adjustmentEyesBrightening();
                AiEffectManager.getInstance().eyesBrighteningEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "瘦鼻": {
                adjustmentNoseNarrowing();
                AiEffectManager.getInstance().noseNarrowingEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "白牙": {
                adjustmentTeethWhitening();
                AiEffectManager.getInstance().teethWhiteningEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "長下巴": {
                adjustmentLongChin();
                AiEffectManager.getInstance().longChinEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "缩小额頭高度": {
                adjustmentForeheadShortening();
                AiEffectManager.getInstance().foreheadShorteningEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "瘦下額骨": {
                adjustMandibleSlimming();
                AiEffectManager.getInstance().mandibleSlimmingEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "瘦颧骨": {
                adjustmentCheekboneSlimming();
                AiEffectManager.getInstance().cheekboneSlimmingEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "小臉": {
                adjustmentFaceShortening();
                AiEffectManager.getInstance().faceShorteningEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "長鼻": {
                adjustmentNoseLengthening();
                AiEffectManager.getInstance().noseLengtheningEffect(0);
                binding.sbAttributesAdjust.setProgress(0);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "眼線": {
                eyeLineType();
                adjustmentEyeLine();
                AiEffectManager.getInstance().eyeLineEffect(0, eyeLineResPath);
                binding.clMakeupsAdjust.setVisibility(View.VISIBLE);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                binding.rbFirstType.setText("自然");
                binding.rbSecondType.setText("野貓");
                binding.rbThirdType.setText("俏皮");
                binding.rbFourthType.setText("心機");
                binding.rbFifthType.setText("氣質");
                break;
            }
            case "眼影": {
                eyeShadowType();
                adjustmentEyeShadow();
                AiEffectManager.getInstance().eyeShadowEffect(0, eyeShadowResPath);
                binding.clMakeupsAdjust.setVisibility(View.VISIBLE);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                binding.rbFirstType.setText("粉霧海");
                binding.rbSecondType.setText("微光蜜");
                binding.rbThirdType.setText("暖茶棕");
                binding.rbFourthType.setText("元氣橙");
                binding.rbFifthType.setText("摩卡棕");
                break;
            }
            case "眼睫毛": {
                eyeLashesType();
                adjustmentEyeLashes();
                AiEffectManager.getInstance().eyeLashesEffect(0, eyeLashesResPath);
                binding.clMakeupsAdjust.setVisibility(View.VISIBLE);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                binding.rbFirstType.setText("自然");
                binding.rbSecondType.setText("溫柔");
                binding.rbThirdType.setText("捲翹");
                binding.rbFourthType.setText("纖長");
                binding.rbFifthType.setText("濃密");
                break;
            }
            case "腮紅": {
                blusherType();
                adjustmentBlusher();
                AiEffectManager.getInstance().blusherEffect(0, blusherResPath);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                binding.clMakeupsAdjust.setVisibility(View.VISIBLE);
                binding.rbFirstType.setText("微醺");
                binding.rbSecondType.setText("蜜桃");
                binding.rbThirdType.setText("奶橘");
                binding.rbFourthType.setText("杏粉");
                binding.rbFifthType.setText("甜橙");
                break;
            }
            case "口紅": {
                lipStickType();
                adjustmentLipStick();
                AiEffectManager.getInstance().lipStickEffect(0, lipStickResPath);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                binding.clMakeupsAdjust.setVisibility(View.VISIBLE);
                binding.rbFirstType.setText("豆沙粉");
                binding.rbSecondType.setText("甜橘色");
                binding.rbThirdType.setText("鐵鏽紅");
                binding.rbFourthType.setText("珊瑚色");
                binding.rbFifthType.setText("絲絨紅");
                break;
            }
            case "美瞳": {
                coloredContactsType();
                adjustmentColoredContacts();
                AiEffectManager.getInstance().coloredContactsEffect(0, coloredContactsResPath);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                binding.clMakeupsAdjust.setVisibility(View.VISIBLE);
                binding.rbFirstType.setText("水光黑");
                binding.rbSecondType.setText("星空藍");
                binding.rbThirdType.setText("仙棕綠");
                binding.rbFourthType.setText("異瞳棕");
                binding.rbFifthType.setText("可可棕");
                break;
            }
            case "風格妝": {
                binding.tvEffectHint.setVisibility(View.VISIBLE);
                binding.tvEffectHint.setAlpha(1.0f);
                binding.tvEffectHint.setText("風格妝無法與\n眼線、眼影、眼睫毛、腮红、口红、美瞳一起套用");
                binding.tvEffectHint.bringToFront();
                textHintAnim();

                makeupType();
                adjustmentMakeup();
                AiEffectManager.getInstance().makeupEffect(0, makeupResPath);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                binding.clMakeupsAdjust.setVisibility(View.VISIBLE);
                binding.rbFirstType.setText("眼瞼下至妝");
                binding.rbSecondType.setText("銀河眼妝");
                binding.rbThirdType.setText("奶凶");
                binding.rbFourthType.setText("純欲");
                binding.rbFifthType.setText("神顏");
                break;
            }
            case "自然": {
                binding.sbAttributesAdjust.setProgress(100);
                filterNaturalType();
                adjustmentFilter();
                AiEffectManager.getInstance().filterEffect(100, filterResPath);

                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                binding.clMakeupsAdjust.setVisibility(View.VISIBLE);
                binding.rbFirstType.setText("奶油");
                binding.rbSecondType.setText("青春");
                binding.rbThirdType.setText("清新");
                binding.rbFourthType.setText("秋天");
                binding.rbFifthType.setText("-");
                break;
            }
            case "灰調": {
                binding.sbAttributesAdjust.setProgress(100);
                filterGrayType();
                adjustmentFilter();
                AiEffectManager.getInstance().filterEffect(100, filterResPath);

                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                binding.clMakeupsAdjust.setVisibility(View.VISIBLE);
                binding.rbFirstType.setText("莫奈");
                binding.rbSecondType.setText("暗夜");
                binding.rbThirdType.setText("膠片");
                binding.rbFourthType.setText("-");
                binding.rbFifthType.setText("-");
                break;
            }
            case "夢境": {
                binding.sbAttributesAdjust.setProgress(100);
                filterDreamyType();
                adjustmentFilter();
                AiEffectManager.getInstance().filterEffect(100, filterResPath);

                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                binding.clMakeupsAdjust.setVisibility(View.VISIBLE);
                binding.rbFirstType.setText("落日");
                binding.rbSecondType.setText("琉璃");
                binding.rbThirdType.setText("星雲");
                binding.rbFourthType.setText("-");
                binding.rbFifthType.setText("-");
                break;
            }
            default:
                break;

        }
    }

    private void textHintAnim() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(binding.tvEffectHint, View.ALPHA, 1.0f, 0.0f);
        objectAnimator.setDuration(3000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.tvEffectHint.setVisibility(View.GONE);
            }
        });
        objectAnimator.start();
    }

    /**
     * 隨機產生UserID
     */
    private static String generateUserID() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        while (builder.length() < 5) {
            int number = random.nextInt(10);
            if (builder.length() == 0 && number == 0) continue;
            builder.append(number);
        }
        return builder.toString();
    }

    private static String generateRoomID() {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        while (builder.length() < 3) {
            int number = random.nextInt(10);
            if (builder.length() == 0 && number == 0) continue;
            builder.append(number);
        }
        return builder.toString();
    }

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.RECORD_AUDIO,
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.BLUETOOTH,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS}, 100);
        } else {
            initGetEffectLicenseData();
        }
    }

    private void setParametersToNextPage() {
        Bundle bundle = new Bundle();
        bundle.putLong("appId", APP_ID);
        bundle.putString("appSign", APP_SIGN);
        bundle.putString("userId", userId);
        bundle.putString("userName", userName);
        bundle.putString("roomId", roomId);
        bundle.putString("licenseData", licenseData);
        bundle.putString("beautyData", new Gson().toJson(AiEffectManager.getInstance().getBeautyData()));
        bundle.putString("smoothData", new Gson().toJson(AiEffectManager.getInstance().getSmoothData()));
        bundle.putString("makeupsData", new Gson().toJson(AiEffectManager.getInstance().getMakeupsData()));

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("bundle", bundle);

        new Handler().postDelayed((Runnable) () -> {
            startActivity(intent);
            isBackAgain = true;
        }, 500);

        destroyEngine();
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

    private void createEngine() {
        profile = new ZegoEngineProfile();
        profile.appID = APP_ID.intValue();
        profile.appSign = APP_SIGN;
        profile.scenario = ZegoScenario.STANDARD_VIDEO_CALL;
        profile.application = this.getApplication();
        ZegoExpressEngine.createEngine(profile, null);
    }

    private void destroyEngine() {
        ZegoExpressEngine.destroyEngine(null);
    }

    private void loginRoom() {
        ZegoUser user = new ZegoUser(userId, userName);
        ZegoRoomConfig roomConfig = new ZegoRoomConfig();
        roomConfig.isUserStatusNotify = true;
        ZegoExpressEngine.getEngine().loginRoom(roomId, user, roomConfig, (int error, JSONObject extendData) -> {

            if (error == 0) {
                startPublishStream();
            }
        });
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
                param.format = ZegoEffectsVideoFrameFormat.BGRA32;
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
        ZegoCanvas canvas = new ZegoCanvas(binding.ttvLocal);
        ZegoExpressEngine.getEngine().startPlayingStream(streamId, canvas);
    }

    //停止播放串流
    private void stopPlayStream(String streamId) {
        ZegoExpressEngine.getEngine().stopPlayingStream(streamId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGetEffectLicenseData();
            } else {
                Toast.makeText(this, "not get Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }



    //----------------------------------------------美顏-------------------------------------------------
    private void adjustmentFaceWhiten() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().faceWhiten(progress);
                AiEffectManager.getInstance().getBeautyData().setWhiten(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentSmoothFace() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().smoothFace(progress);
                AiEffectManager.getInstance().getBeautyData().setSmooth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentRosy() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().rosyEffect(progress);
                AiEffectManager.getInstance().getBeautyData().setRosy(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentSharpen() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().sharpenEffect(progress);
                AiEffectManager.getInstance().getBeautyData().setSharpen(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentWrinkles() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().wrinklesEffect(progress);
                AiEffectManager.getInstance().getBeautyData().setWrinkles(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentDarkCircles() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().darkCirclesEffect(progress);
                AiEffectManager.getInstance().getBeautyData().setDarkCircles(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //----------------------------------------------美型-------------------------------------------------
    private void adjustmentBigEye() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().bigEyeEffect(progress);
                AiEffectManager.getInstance().getSmoothData().setBigEyes(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentFaceLift() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().faceLiftEffect(progress);
                AiEffectManager.getInstance().getSmoothData().setFaceLifting(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentSmailMouth() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().smailMouthEffect(progress);
                AiEffectManager.getInstance().getSmoothData().setSmallMouth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentEyesBrightening() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().eyesBrighteningEffect(progress);
                AiEffectManager.getInstance().getSmoothData().setEyesBrightening(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentNoseNarrowing() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().noseNarrowingEffect(progress);
                AiEffectManager.getInstance().getSmoothData().setNoseNarrowing(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentTeethWhitening() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().teethWhiteningEffect(progress);
                AiEffectManager.getInstance().getSmoothData().setTeethWhitening(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentLongChin() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().longChinEffect(progress);
                AiEffectManager.getInstance().getSmoothData().setLongChin(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentForeheadShortening() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().foreheadShorteningEffect(progress);
                AiEffectManager.getInstance().getSmoothData().setForeheadShortening(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustMandibleSlimming() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().mandibleSlimmingEffect(progress);
                AiEffectManager.getInstance().getSmoothData().setMandibleSlimming(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentCheekboneSlimming() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().cheekboneSlimmingEffect(progress);
                AiEffectManager.getInstance().getSmoothData().setCheekboneSlimming(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentFaceShortening() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().faceShorteningEffect(progress);
                AiEffectManager.getInstance().getSmoothData().setFaceShortening(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void adjustmentNoseLengthening() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().noseLengtheningEffect(progress);
                AiEffectManager.getInstance().getSmoothData().setNoseLengthening(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //----------------------------------------------美妝-------------------------------------------------

    private void adjustmentEyeLine() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().eyeLineEffect(progress, eyeLineResPath);
                AiEffectManager.getInstance().getMakeupsData().setEyeliner(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void eyeLineType() {
        String path = this.getCacheDir().getPath();

        binding.rgMakeup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == binding.rbFirstType.getId()) {
                eyeLineResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelinerNatural.bundle";
                AiEffectManager.getInstance().eyeLineEffect(binding.sbAttributesAdjust.getProgress(), eyeLineResPath);

            } else if (checkedId == binding.rbSecondType.getId()) {
                eyeLineResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelinerCatEye.bundle";
                AiEffectManager.getInstance().eyeLineEffect(binding.sbAttributesAdjust.getProgress(), eyeLineResPath);

            } else if (checkedId == binding.rbThirdType.getId()) {
                eyeLineResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelinerNaughty.bundle";
                AiEffectManager.getInstance().eyeLineEffect(binding.sbAttributesAdjust.getProgress(), eyeLineResPath);

            } else if (checkedId == binding.rbFourthType.getId()) {
                eyeLineResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelinerInnocent.bundle";
                AiEffectManager.getInstance().eyeLineEffect(binding.sbAttributesAdjust.getProgress(), eyeLineResPath);

            } else if (checkedId == binding.rbFifthType.getId()) {
                eyeLineResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelinerDignified.bundle";
                AiEffectManager.getInstance().eyeLineEffect(binding.sbAttributesAdjust.getProgress(), eyeLineResPath);

            }
            AiEffectManager.getInstance().getMakeupsData().setEyelinerRes(eyeLineResPath);
        });
    }

    private void adjustmentEyeShadow() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().eyeShadowEffect(progress, eyeShadowResPath);
                AiEffectManager.getInstance().getMakeupsData().setEyeShadow(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void eyeShadowType() {
        String path = this.getCacheDir().getPath();

        binding.rgMakeup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == binding.rbFirstType.getId()) {
                eyeShadowResPath = path + File.separator + "AdvancedResources/beautyMakeupEyeshadowPinkMist.bundle";
                AiEffectManager.getInstance().eyeShadowEffect(binding.sbAttributesAdjust.getProgress(), eyeShadowResPath);

            } else if (checkedId == binding.rbSecondType.getId()) {
                eyeShadowResPath = path + File.separator + "AdvancedResources/beautyMakeupEyeshadowShimmerPink.bundle";
                AiEffectManager.getInstance().eyeShadowEffect(binding.sbAttributesAdjust.getProgress(), eyeShadowResPath);

            } else if (checkedId == binding.rbThirdType.getId()) {
                eyeShadowResPath = path + File.separator + "AdvancedResources/beautyMakeupEyeshadowTeaBrown.bundle";
                AiEffectManager.getInstance().eyeShadowEffect(binding.sbAttributesAdjust.getProgress(), eyeShadowResPath);

            } else if (checkedId == binding.rbFourthType.getId()) {
                eyeShadowResPath = path + File.separator + "AdvancedResources/beautyMakeupEyeshadowBrightOrange.bundle";
                AiEffectManager.getInstance().eyeShadowEffect(binding.sbAttributesAdjust.getProgress(), eyeShadowResPath);

            } else if (checkedId == binding.rbFifthType.getId()) {
                eyeShadowResPath = path + File.separator + "AdvancedResources/beautyMakeupEyeshadowMochaBrown.bundle";
                AiEffectManager.getInstance().eyeShadowEffect(binding.sbAttributesAdjust.getProgress(), eyeShadowResPath);

            }
            AiEffectManager.getInstance().getMakeupsData().setEyeShadowRes(eyeShadowResPath);
        });
    }

    private void adjustmentEyeLashes() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().eyeLashesEffect(progress, eyeLashesResPath);
                AiEffectManager.getInstance().getMakeupsData().setEyelashes(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void eyeLashesType() {
        String path = this.getCacheDir().getPath();

        binding.rgMakeup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == binding.rbFirstType.getId()) {
                eyeLashesResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelashesNatural.bundle";
                AiEffectManager.getInstance().eyeLashesEffect(binding.sbAttributesAdjust.getProgress(), eyeLashesResPath);

            } else if (checkedId == binding.rbSecondType.getId()) {
                eyeLashesResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelashesTender.bundle";
                AiEffectManager.getInstance().eyeLashesEffect(binding.sbAttributesAdjust.getProgress(), eyeLashesResPath);

            } else if (checkedId == binding.rbThirdType.getId()) {
                eyeLashesResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelashesCurl.bundle";
                AiEffectManager.getInstance().eyeLashesEffect(binding.sbAttributesAdjust.getProgress(), eyeLashesResPath);

            } else if (checkedId == binding.rbFourthType.getId()) {
                eyeLashesResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelashesEverlong.bundle";
                AiEffectManager.getInstance().eyeLashesEffect(binding.sbAttributesAdjust.getProgress(), eyeLashesResPath);

            } else if (checkedId == binding.rbFifthType.getId()) {
                eyeLashesResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelashesThick.bundle";
                AiEffectManager.getInstance().eyeLashesEffect(binding.sbAttributesAdjust.getProgress(), eyeLashesResPath);

            }
            AiEffectManager.getInstance().getMakeupsData().setEyelashesRes(eyeLashesResPath);
        });
    }

    private void adjustmentBlusher() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().blusherEffect(progress, blusherResPath);
                AiEffectManager.getInstance().getMakeupsData().setBlush(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void blusherType() {
        String path = this.getCacheDir().getPath();

        binding.rgMakeup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == binding.rbFirstType.getId()) {
                blusherResPath = path + File.separator + "AdvancedResources/beautyMakeupBlusherSlightlyDrunk.bundle";
                AiEffectManager.getInstance().blusherEffect(binding.sbAttributesAdjust.getProgress(), blusherResPath);

            } else if (checkedId == binding.rbSecondType.getId()) {
                blusherResPath = path + File.separator + "AdvancedResources/beautyMakeupBlusherPeach.bundle";
                AiEffectManager.getInstance().blusherEffect(binding.sbAttributesAdjust.getProgress(), blusherResPath);

            } else if (checkedId == binding.rbThirdType.getId()) {
                blusherResPath = path + File.separator + "AdvancedResources/beautyMakeupBlusherMilkyOrange.bundle";
                AiEffectManager.getInstance().blusherEffect(binding.sbAttributesAdjust.getProgress(), blusherResPath);

            } else if (checkedId == binding.rbFourthType.getId()) {
                blusherResPath = path + File.separator + "AdvancedResources/beautyMakeupBlusherAprocitPink.bundle";
                AiEffectManager.getInstance().blusherEffect(binding.sbAttributesAdjust.getProgress(), blusherResPath);

            } else if (checkedId == binding.rbFifthType.getId()) {
                blusherResPath = path + File.separator + "AdvancedResources/beautyMakeupBlusherSweetOrange.bundle";
                AiEffectManager.getInstance().blusherEffect(binding.sbAttributesAdjust.getProgress(), blusherResPath);

            }
            AiEffectManager.getInstance().getMakeupsData().setBlushRes(blusherResPath);
        });
    }

    private void adjustmentLipStick() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().lipStickEffect(progress, lipStickResPath);
                AiEffectManager.getInstance().getMakeupsData().setLipstick(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void lipStickType() {
        String path = this.getCacheDir().getPath();
        binding.rgMakeup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == binding.rbFirstType.getId()) {
                lipStickResPath = path + File.separator + "AdvancedResources/beautyMakeupLipstickCameoPink.bundle";
                AiEffectManager.getInstance().lipStickEffect(binding.sbAttributesAdjust.getProgress(), lipStickResPath);

            } else if (checkedId == binding.rbSecondType.getId()) {
                lipStickResPath = path + File.separator + "AdvancedResources/beautyMakeupLipstickSweetOrange.bundle";
                AiEffectManager.getInstance().lipStickEffect(binding.sbAttributesAdjust.getProgress(), lipStickResPath);

            } else if (checkedId == binding.rbThirdType.getId()) {
                lipStickResPath = path + File.separator + "AdvancedResources/beautyMakeupLipstickRustRed.bundle";
                AiEffectManager.getInstance().lipStickEffect(binding.sbAttributesAdjust.getProgress(), lipStickResPath);

            } else if (checkedId == binding.rbFourthType.getId()) {
                lipStickResPath = path + File.separator + "AdvancedResources/beautyMakeupLipstickCoral.bundle";
                AiEffectManager.getInstance().lipStickEffect(binding.sbAttributesAdjust.getProgress(), lipStickResPath);

            } else if (checkedId == binding.rbFifthType.getId()) {
                lipStickResPath = path + File.separator + "AdvancedResources/beautyMakeupLipstickRedVelvet.bundle";
                AiEffectManager.getInstance().lipStickEffect(binding.sbAttributesAdjust.getProgress(), lipStickResPath);

            }
            AiEffectManager.getInstance().getMakeupsData().setLipstickRes(lipStickResPath);
        });
    }

    private void adjustmentColoredContacts() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().coloredContactsEffect(progress, coloredContactsResPath);
                AiEffectManager.getInstance().getMakeupsData().setLenses(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void coloredContactsType() {
        String path = this.getCacheDir().getPath();
        binding.rgMakeup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == binding.rbFirstType.getId()) {
                coloredContactsResPath = path + File.separator + "AdvancedResources/beautyMakeupColoredContactsDarknightBlack.bundle";
                AiEffectManager.getInstance().coloredContactsEffect(binding.sbAttributesAdjust.getProgress(), coloredContactsResPath);

            } else if (checkedId == binding.rbSecondType.getId()) {
                coloredContactsResPath = path + File.separator + "AdvancedResources/beautyMakeupColoredContactsStarryBlue.bundle";
                AiEffectManager.getInstance().coloredContactsEffect(binding.sbAttributesAdjust.getProgress(), coloredContactsResPath);

            } else if (checkedId == binding.rbThirdType.getId()) {
                coloredContactsResPath = path + File.separator + "AdvancedResources/beautyMakeupColoredContactsBrownGreen.bundle";
                AiEffectManager.getInstance().coloredContactsEffect(binding.sbAttributesAdjust.getProgress(), coloredContactsResPath);

            } else if (checkedId == binding.rbFourthType.getId()) {
                coloredContactsResPath = path + File.separator + "AdvancedResources/beautyMakeupColoredContactsLightsBrown.bundle";
                AiEffectManager.getInstance().coloredContactsEffect(binding.sbAttributesAdjust.getProgress(), coloredContactsResPath);

            } else if (checkedId == binding.rbFifthType.getId()) {
                coloredContactsResPath = path + File.separator + "AdvancedResources/beautyMakeupColoredContactsChocolateBrown.bundle";
                AiEffectManager.getInstance().coloredContactsEffect(binding.sbAttributesAdjust.getProgress(), coloredContactsResPath);

            }
            AiEffectManager.getInstance().getMakeupsData().setLensesRes(coloredContactsResPath);
        });
    }

    private void adjustmentMakeup() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().makeupEffect(progress, makeupResPath);
                AiEffectManager.getInstance().getMakeupsData().setStyleMakeup(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void makeupType() {
        String path = this.getCacheDir().getPath();
        binding.rgMakeup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == binding.rbFirstType.getId()) {
                makeupResPath = path + File.separator + "AdvancedResources/beautyStyleMakeupInnocentEyes.bundle";
                AiEffectManager.getInstance().makeupEffect(binding.sbAttributesAdjust.getProgress(), makeupResPath);

            } else if (checkedId == binding.rbSecondType.getId()) {
                makeupResPath = path + File.separator + "AdvancedResources/beautyStyleMakeupMilkyEyes.bundle";
                AiEffectManager.getInstance().makeupEffect(binding.sbAttributesAdjust.getProgress(), makeupResPath);

            } else if (checkedId == binding.rbThirdType.getId()) {
                makeupResPath = path + File.separator + "AdvancedResources/beautyStyleMakeupCutieCool.bundle";
                AiEffectManager.getInstance().makeupEffect(binding.sbAttributesAdjust.getProgress(), makeupResPath);

            } else if (checkedId == binding.rbFourthType.getId()) {
                makeupResPath = path + File.separator + "AdvancedResources/beautyStyleMakeupPureSexy.bundle";
                AiEffectManager.getInstance().makeupEffect(binding.sbAttributesAdjust.getProgress(), makeupResPath);

            } else if (checkedId == binding.rbFifthType.getId()) {
                makeupResPath = path + File.separator + "AdvancedResources/beautyStyleMakeupFlawless.bundle";
                AiEffectManager.getInstance().makeupEffect(binding.sbAttributesAdjust.getProgress(), makeupResPath);

            }
            AiEffectManager.getInstance().getMakeupsData().setStyleMakeupRes(makeupResPath);
        });
    }

    private void adjustmentFilter() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AiEffectManager.getInstance().filterEffect(progress, filterResPath);
                AiEffectManager.getInstance().getMakeupsData().setFilter(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void filterNaturalType() {
        String path = this.getCacheDir().getPath();
        binding.rgMakeup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == binding.rbFirstType.getId()) {
                filterResPath = path + File.separator + "AdvancedResources/filterNaturalCreamy.bundle";
                AiEffectManager.getInstance().filterEffect(binding.sbAttributesAdjust.getProgress(), filterResPath);

            } else if (checkedId == binding.rbSecondType.getId()) {
                filterResPath = path + File.separator + "AdvancedResources/filterNaturalBrighten.bundle";
                AiEffectManager.getInstance().filterEffect(binding.sbAttributesAdjust.getProgress(), filterResPath);

            } else if (checkedId == binding.rbThirdType.getId()) {
                filterResPath = path + File.separator + "AdvancedResources/filterNaturalFresh.bundle";
                AiEffectManager.getInstance().filterEffect(binding.sbAttributesAdjust.getProgress(), filterResPath);

            } else if (checkedId == binding.rbFourthType.getId()) {
                filterResPath = path + File.separator + "AdvancedResources/filterNaturalAutumn.bundle";
                AiEffectManager.getInstance().filterEffect(binding.sbAttributesAdjust.getProgress(), filterResPath);

            }
            AiEffectManager.getInstance().getMakeupsData().setFilterRes(filterResPath);
        });
    }

    private void filterGrayType() {
        String path = this.getCacheDir().getPath();
        binding.rgMakeup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == binding.rbFirstType.getId()) {
                filterResPath = path + File.separator + "AdvancedResources/filterGrayMonet.bundle";
                AiEffectManager.getInstance().filterEffect(binding.sbAttributesAdjust.getProgress(), filterResPath);

            } else if (checkedId == binding.rbSecondType.getId()) {
                filterResPath = path + File.separator + "AdvancedResources/filterGrayNight.bundle";
                AiEffectManager.getInstance().filterEffect(binding.sbAttributesAdjust.getProgress(), filterResPath);

            } else if (checkedId == binding.rbThirdType.getId()) {
                filterResPath = path + File.separator + "AdvancedResources/filterGrayFilmlike.bundle";
                AiEffectManager.getInstance().filterEffect(binding.sbAttributesAdjust.getProgress(), filterResPath);

            }
            AiEffectManager.getInstance().getMakeupsData().setFilterRes(filterResPath);
        });
    }

    private void filterDreamyType() {
        String path = this.getCacheDir().getPath();
        binding.rgMakeup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == binding.rbFirstType.getId()) {
                filterResPath = path + File.separator + "AdvancedResources/filterDreamySunset.bundle";
                AiEffectManager.getInstance().filterEffect(binding.sbAttributesAdjust.getProgress(), filterResPath);

            } else if (checkedId == binding.rbSecondType.getId()) {
                filterResPath = path + File.separator + "AdvancedResources/filterDreamyCozily.bundle";
                AiEffectManager.getInstance().filterEffect(binding.sbAttributesAdjust.getProgress(), filterResPath);

            } else if (checkedId == binding.rbThirdType.getId()) {
                filterResPath = path + File.separator + "AdvancedResources/filterDreamySweet.bundle";
                AiEffectManager.getInstance().filterEffect(binding.sbAttributesAdjust.getProgress(), filterResPath);

            }
            AiEffectManager.getInstance().getMakeupsData().setFilterRes(filterResPath);
        });
    }
}