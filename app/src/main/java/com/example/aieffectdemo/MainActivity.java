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
    private boolean isOpen = false;
    private ZegoEffects effects;
    private ZegoEngineProfile profile;
    private AiEffectParametersData.BeautyData beautyData;
    private AiEffectParametersData.SmoothData smoothData;
    private AiEffectParametersData.MakeupsData makeupsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(binding.getRoot());
        beautyData = new AiEffectParametersData.BeautyData();
        smoothData = new AiEffectParametersData.SmoothData();
        makeupsData = new AiEffectParametersData.MakeupsData();

        userId = generateUserID();
        userName = "user_" + userId;
        roomId = "100";

        binding.fabEffect.getDrawable().setTint(ContextCompat.getColor(this, R.color.white));
        binding.fabStart.getDrawable().setTint(ContextCompat.getColor(this, R.color.white));

        checkPermission();
        textHintAnim();
        initFab();
        initBottomNav();
        initSubBottomNav();
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
                initAiEffect();
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
                faceWhiten(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "磨皮": {
                adjustmentSmoothFace();
                smoothFace(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "紅潤": {
                adjustmentRosy();
                rosyEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "銳化": {
                adjustmentSharpen();
                sharpenEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "去除法令紋": {
                adjustmentWrinkles();
                wrinklesEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "去除黑眼圈": {
                adjustmentDarkCircles();
                darkCirclesEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }

            case "大眼": {
                adjustmentBigEye();
                bigEyeEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "瘦臉": {
                adjustmentFaceLift();
                faceLiftEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "小嘴": {
                adjustmentSmailMouth();
                smailMouthEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "亮眼": {
                adjustmentEyesBrightening();
                eyesBrighteningEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "瘦鼻": {
                adjustmentNoseNarrowing();
                noseNarrowingEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "白牙": {
                adjustmentTeethWhitening();
                teethWhiteningEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "長下巴": {
                adjustmentLongChin();
                longChinEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "缩小额頭高度": {
                adjustmentForeheadShortening();
                foreheadShorteningEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "瘦下額骨": {
                adjustMandibleSlimming();
                mandibleSlimmingEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "瘦颧骨": {
                adjustmentCheekboneSlimming();
                cheekboneSlimmingEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "小臉": {
                adjustmentFaceShortening();
                faceShorteningEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "長鼻": {
                adjustmentNoseLengthening();
                noseLengtheningEffect(50);
                binding.sbAttributesAdjust.setProgress(50);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                break;
            }
            case "眼線": {
                eyeLineType();
                adjustmentEyeLine();
                eyeLineEffect(0, eyeLineResPath);
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
                eyeShadowEffect(0, eyeShadowResPath);
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
                eyeLashesEffect(0, eyeLashesResPath);
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
                blusherEffect(0, blusherResPath);
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
                lipStickEffect(0, lipStickResPath);
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
                coloredContactsEffect(0, coloredContactsResPath);
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
                makeupEffect(0, makeupResPath);
                binding.clAttributesAdjust.setVisibility(View.VISIBLE);
                binding.clMakeupsAdjust.setVisibility(View.VISIBLE);
                binding.rbFirstType.setText("眼瞼下至妝");
                binding.rbSecondType.setText("銀河眼妝");
                binding.rbThirdType.setText("奶凶");
                binding.rbFourthType.setText("純欲");
                binding.rbFifthType.setText("神顏");
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
        bundle.putString("beautyData", new Gson().toJson(beautyData));
        bundle.putString("smoothData", new Gson().toJson(smoothData));
        bundle.putString("makeupsData", new Gson().toJson(makeupsData));

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

    private void initAiEffect() {
        String path = this.getCacheDir().getPath();
        String faceDetection = "Resource/FaceWhiteningResources.bundle";
        String pendantDetection = "Resource/PendantResources.bundle";
        String rosyDetection = "Resource/RosyResources.bundle";
        String teethWhiteningDetection = "Resource/TeethWhiteningResources";
        String commonDetection = "Resource/CommonResources.bundle";
        String stickerBaseDetection = "Resource/StickerBaseResources.bundle";
        String faceModel = "Model/FaceDetectionModel.model";
        String segmentationModel = "Model/SegmentationModel.model";

        AssetsFileUtil.copyFileFromAssets(this, faceDetection, path + File.separator + faceDetection);
        AssetsFileUtil.copyFileFromAssets(this, pendantDetection, path + File.separator + pendantDetection);
        AssetsFileUtil.copyFileFromAssets(this, rosyDetection, path + File.separator + rosyDetection);
        AssetsFileUtil.copyFileFromAssets(this, teethWhiteningDetection, path + File.separator + teethWhiteningDetection);
        AssetsFileUtil.copyFileFromAssets(this, commonDetection, path + File.separator + commonDetection);
        AssetsFileUtil.copyFileFromAssets(this, stickerBaseDetection, path + File.separator + stickerBaseDetection);
        AssetsFileUtil.copyFileFromAssets(this, faceModel, path + File.separator + faceModel);
        AssetsFileUtil.copyFileFromAssets(this, segmentationModel, path + File.separator + segmentationModel);


        String eyeLineNatural = "AdvancedResources/beautyMakeupEyelinerNatural.bundle";
        String eyeLineCatEye = "AdvancedResources/beautyMakeupEyelinerCatEye.bundle";
        String eyeLineNaughty = "AdvancedResources/beautyMakeupEyelinerNaughty.bundle";
        String eyeLineInnocent = "AdvancedResources/beautyMakeupEyelinerInnocent.bundle";
        String eyeLineDignified = "AdvancedResources/beautyMakeupEyelinerDignified.bundle";

        AssetsFileUtil.copyFileFromAssets(this, eyeLineNatural, path + File.separator + eyeLineNatural);
        AssetsFileUtil.copyFileFromAssets(this, eyeLineCatEye, path + File.separator + eyeLineCatEye);
        AssetsFileUtil.copyFileFromAssets(this, eyeLineNaughty, path + File.separator + eyeLineNaughty);
        AssetsFileUtil.copyFileFromAssets(this, eyeLineInnocent, path + File.separator + eyeLineInnocent);
        AssetsFileUtil.copyFileFromAssets(this, eyeLineDignified, path + File.separator + eyeLineDignified);

        String eyeshadowPinkMist = "AdvancedResources/beautyMakeupEyeshadowPinkMist.bundle";
        String eyeshadowShimmerPink = "AdvancedResources/beautyMakeupEyeshadowShimmerPink.bundle";
        String eyeshadowTeaBrown = "AdvancedResources/beautyMakeupEyeshadowTeaBrown.bundle";
        String eyeshadowBrightOrange = "AdvancedResources/beautyMakeupEyeshadowBrightOrange.bundle";
        String eyeshadowMochaBrown = "AdvancedResources/beautyMakeupEyeshadowMochaBrown.bundle";

        AssetsFileUtil.copyFileFromAssets(this, eyeshadowPinkMist, path + File.separator + eyeshadowPinkMist);
        AssetsFileUtil.copyFileFromAssets(this, eyeshadowShimmerPink, path + File.separator + eyeshadowShimmerPink);
        AssetsFileUtil.copyFileFromAssets(this, eyeshadowTeaBrown, path + File.separator + eyeshadowTeaBrown);
        AssetsFileUtil.copyFileFromAssets(this, eyeshadowBrightOrange, path + File.separator + eyeshadowBrightOrange);
        AssetsFileUtil.copyFileFromAssets(this, eyeshadowMochaBrown, path + File.separator + eyeshadowMochaBrown);


        String eyelashesNatural = "AdvancedResources/beautyMakeupEyelashesNatural.bundle";
        String eyelashesTender = "AdvancedResources/beautyMakeupEyelashesTender.bundle";
        String eyelashesCurl = "AdvancedResources/beautyMakeupEyelashesCurl.bundle";
        String eyelashesEverlong = "AdvancedResources/beautyMakeupEyelashesEverlong.bundle";
        String eyelashesThick = "AdvancedResources/beautyMakeupEyelashesThick.bundle";

        AssetsFileUtil.copyFileFromAssets(this, eyelashesNatural, path + File.separator + eyelashesNatural);
        AssetsFileUtil.copyFileFromAssets(this, eyelashesTender, path + File.separator + eyelashesTender);
        AssetsFileUtil.copyFileFromAssets(this, eyelashesCurl, path + File.separator + eyelashesCurl);
        AssetsFileUtil.copyFileFromAssets(this, eyelashesEverlong, path + File.separator + eyelashesEverlong);
        AssetsFileUtil.copyFileFromAssets(this, eyelashesThick, path + File.separator + eyelashesThick);

        String blusherSlightlyDrunk = "AdvancedResources/beautyMakeupBlusherSlightlyDrunk.bundle";
        String blusherPeach = "AdvancedResources/beautyMakeupBlusherPeach.bundle";
        String blusherMilkyOrange = "AdvancedResources/beautyMakeupBlusherMilkyOrange.bundle";
        String blusherAprocitPink = "AdvancedResources/beautyMakeupBlusherAprocitPink.bundle";
        String blusherSweetOrange = "AdvancedResources/beautyMakeupBlusherSweetOrange.bundle";

        AssetsFileUtil.copyFileFromAssets(this, blusherSlightlyDrunk, path + File.separator + blusherSlightlyDrunk);
        AssetsFileUtil.copyFileFromAssets(this, blusherPeach, path + File.separator + blusherPeach);
        AssetsFileUtil.copyFileFromAssets(this, blusherMilkyOrange, path + File.separator + blusherMilkyOrange);
        AssetsFileUtil.copyFileFromAssets(this, blusherAprocitPink, path + File.separator + blusherAprocitPink);
        AssetsFileUtil.copyFileFromAssets(this, blusherSweetOrange, path + File.separator + blusherSweetOrange);

        String lipstickCameoPink = "AdvancedResources/beautyMakeupLipstickCameoPink.bundle";
        String lipstickSweetOrange = "AdvancedResources/beautyMakeupLipstickSweetOrange.bundle";
        String lipstickRustRed = "AdvancedResources/beautyMakeupLipstickRustRed.bundle";
        String lipstickCoral = "AdvancedResources/beautyMakeupLipstickCoral.bundle";
        String lipstickRedVelvet = "AdvancedResources/beautyMakeupLipstickRedVelvet.bundle";

        AssetsFileUtil.copyFileFromAssets(this, lipstickCameoPink, path + File.separator + lipstickCameoPink);
        AssetsFileUtil.copyFileFromAssets(this, lipstickSweetOrange, path + File.separator + lipstickSweetOrange);
        AssetsFileUtil.copyFileFromAssets(this, lipstickRustRed, path + File.separator + lipstickRustRed);
        AssetsFileUtil.copyFileFromAssets(this, lipstickCoral, path + File.separator + lipstickCoral);
        AssetsFileUtil.copyFileFromAssets(this, lipstickRedVelvet, path + File.separator + lipstickRedVelvet);

        String coloredContactsDarknightBlack = "AdvancedResources/beautyMakeupColoredContactsDarknightBlack.bundle";
        String coloredContactsStarryBlue = "AdvancedResources/beautyMakeupColoredContactsStarryBlue.bundle";
        String coloredContactsBrownGreen = "AdvancedResources/beautyMakeupColoredContactsBrownGreen.bundle";
        String coloredContactsLightsBrown = "AdvancedResources/beautyMakeupColoredContactsLightsBrown.bundle";
        String coloredContactsChocolateBrown = "AdvancedResources/beautyMakeupColoredContactsChocolateBrown.bundle";

        AssetsFileUtil.copyFileFromAssets(this, coloredContactsDarknightBlack, path + File.separator + coloredContactsDarknightBlack);
        AssetsFileUtil.copyFileFromAssets(this, coloredContactsStarryBlue, path + File.separator + coloredContactsStarryBlue);
        AssetsFileUtil.copyFileFromAssets(this, coloredContactsBrownGreen, path + File.separator + coloredContactsBrownGreen);
        AssetsFileUtil.copyFileFromAssets(this, coloredContactsLightsBrown, path + File.separator + coloredContactsLightsBrown);
        AssetsFileUtil.copyFileFromAssets(this, coloredContactsChocolateBrown, path + File.separator + coloredContactsChocolateBrown);

        String styleMakeupInnocentEyes = "AdvancedResources/beautyStyleMakeupInnocentEyes.bundle";
        String styleMakeupMilkyEyes = "AdvancedResources/beautyStyleMakeupMilkyEyes.bundle";
        String styleMakeupCutieCool = "AdvancedResources/beautyStyleMakeupCutieCool.bundle";
        String styleMakeupPureSexy = "AdvancedResources/beautyStyleMakeupPureSexy.bundle";
        String styleMakeupFlawless = "AdvancedResources/beautyStyleMakeupFlawless.bundle";

        AssetsFileUtil.copyFileFromAssets(this, styleMakeupInnocentEyes, path + File.separator + styleMakeupInnocentEyes);
        AssetsFileUtil.copyFileFromAssets(this, styleMakeupMilkyEyes, path + File.separator + styleMakeupMilkyEyes);
        AssetsFileUtil.copyFileFromAssets(this, styleMakeupCutieCool, path + File.separator + styleMakeupCutieCool);
        AssetsFileUtil.copyFileFromAssets(this, styleMakeupPureSexy, path + File.separator + styleMakeupPureSexy);
        AssetsFileUtil.copyFileFromAssets(this, styleMakeupFlawless, path + File.separator + styleMakeupFlawless);


        ArrayList<String> effectList = new ArrayList<>();
        effectList.add(path + File.separator + faceDetection);
        effectList.add(path + File.separator + pendantDetection);
        effectList.add(path + File.separator + rosyDetection);
        effectList.add(path + File.separator + teethWhiteningDetection);
        effectList.add(path + File.separator + commonDetection);
        effectList.add(path + File.separator + stickerBaseDetection);
        effectList.add(path + File.separator + faceModel);
        effectList.add(path + File.separator + segmentationModel);

        ZegoEffects.setResources(effectList);

        effects = ZegoEffects.create(licenseData, this.getApplication());
        effects.enableFaceDetection(true);
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
                effects.initEnv(1280, 720);

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

                int processedTextureID = effects.processTexture(textureID, param);
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


    //美白
    private void faceWhiten(int progress) {
        effects.enableWhiten(true);
        ZegoEffectsWhitenParam param = new ZegoEffectsWhitenParam();
        param.intensity = progress;
        effects.setWhitenParam(param);
    }
    //平滑肌膚
    private void smoothFace(int progress) {
        effects.enableSmooth(true);
        ZegoEffectsSmoothParam param = new ZegoEffectsSmoothParam();
        param.intensity = progress;
        effects.setSmoothParam(param);
    }

    //腮紅
    private void rosyEffect(int progress) {
        effects.enableRosy(true);
        ZegoEffectsRosyParam param = new ZegoEffectsRosyParam();
        param.intensity = progress;
        effects.setRosyParam(param);
    }

    //影像銳化
    private void sharpenEffect(int progress) {
        effects.enableSharpen(true);
        ZegoEffectsSharpenParam param = new ZegoEffectsSharpenParam();
        param.intensity = progress;
        effects.setSharpenParam(param);
    }

    //去除法令纹
    private void wrinklesEffect(int progress) {
        effects.enableWrinklesRemoving(true);
        ZegoEffectsWrinklesRemovingParam param = new ZegoEffectsWrinklesRemovingParam();
        param.intensity = progress;
        effects.setWrinklesRemovingParam(param);
    }

    //去除黑眼圈
    private void darkCirclesEffect(int progress) {
        effects.enableDarkCirclesRemoving(true);
        ZegoEffectsDarkCirclesRemovingParam param = new ZegoEffectsDarkCirclesRemovingParam();
        param.intensity = progress;
        effects.setDarkCirclesRemovingParam(param);
    }

    //大眼
    private void bigEyeEffect(int progress) {
        effects.enableBigEyes(true);
        ZegoEffectsBigEyesParam param = new ZegoEffectsBigEyesParam();
        param.intensity = progress;
        effects.setBigEyesParam(param);
    }

    //瘦臉
    private void faceLiftEffect(int progress) {
        effects.enableFaceLifting(true);
        ZegoEffectsFaceLiftingParam param = new ZegoEffectsFaceLiftingParam();
        param.intensity = progress;
        effects.setFaceLiftingParam(param);
    }

    //小嘴
    private void smailMouthEffect(int progress) {
        effects.enableSmallMouth(true);
        ZegoEffectsSmallMouthParam param = new ZegoEffectsSmallMouthParam();
        param.intensity = progress;
        effects.setSmallMouthParam(param);
    }

    //亮眼
    private void eyesBrighteningEffect(int progress) {
        effects.enableEyesBrightening(true);
        ZegoEffectsEyesBrighteningParam param = new ZegoEffectsEyesBrighteningParam();
        param.intensity = progress;
        effects.setEyesBrighteningParam(param);
    }

    //瘦鼻
    private void noseNarrowingEffect(int progress) {
        effects.enableNoseNarrowing(true);
        ZegoEffectsNoseNarrowingParam param = new ZegoEffectsNoseNarrowingParam();
        param.intensity = progress;
        effects.setNoseNarrowingParam(param);
    }

    //白牙
    private void teethWhiteningEffect(int progress) {
        effects.enableTeethWhitening(true);
        ZegoEffectsTeethWhiteningParam param = new ZegoEffectsTeethWhiteningParam();
        param.intensity = progress;
        effects.setTeethWhiteningParam(param);
    }

    //長下巴
    private void longChinEffect(int progress) {
        effects.enableLongChin(true);
        ZegoEffectsLongChinParam param = new ZegoEffectsLongChinParam();
        param.intensity = progress;
        effects.setLongChinParam(param);
    }

    //縮小額頭高度
    private void foreheadShorteningEffect(int progress) {
        effects.enableForeheadShortening(true);
        ZegoEffectsForeheadShorteningParam param = new ZegoEffectsForeheadShorteningParam();
        param.intensity = progress;
        effects.setForeheadShorteningParam(param);
    }

    //瘦下額骨
    private void mandibleSlimmingEffect(int progress) {
        effects.enableMandibleSlimming(true);
        ZegoEffectsMandibleSlimmingParam param = new ZegoEffectsMandibleSlimmingParam();
        param.intensity = progress;
        effects.setMandibleSlimmingParam(param);
    }

    //瘦顱骨
    private void cheekboneSlimmingEffect(int progress) {
        effects.enableCheekboneSlimming(true);
        ZegoEffectsCheekboneSlimmingParam param = new ZegoEffectsCheekboneSlimmingParam();
        param.intensity = progress;
        effects.setCheekboneSlimmingParam(param);
    }

    //小臉
    private void faceShorteningEffect(int progress) {
        effects.enableFaceShortening(true);
        ZegoEffectsFaceShorteningParam param = new ZegoEffectsFaceShorteningParam();
        param.intensity = progress;
        effects.setFaceShorteningParam(param);
    }

    //长鼻
    private void noseLengtheningEffect(int progress) {
        effects.enableNoseLengthening(true);
        ZegoEffectsNoseLengtheningParam param = new ZegoEffectsNoseLengtheningParam();
        param.intensity = progress;
        effects.setNoseLengtheningParam(param);
    }


    //----------------------------------------------美顏-------------------------------------------------
    private void adjustmentFaceWhiten() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                faceWhiten(progress);
                beautyData.setWhiten(progress);
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
                smoothFace(progress);
                beautyData.setSmooth(progress);
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
                rosyEffect(progress);
                beautyData.setRosy(progress);
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
                sharpenEffect(progress);
                beautyData.setSharpen(progress);
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
                wrinklesEffect(progress);
                beautyData.setWrinkles(progress);
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
                darkCirclesEffect(progress);
                beautyData.setDarkCircles(progress);
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
                bigEyeEffect(progress);
                smoothData.setBigEyes(progress);
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
                faceLiftEffect(progress);
                smoothData.setFaceLifting(progress);
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
                smailMouthEffect(progress);
                smoothData.setSmallMouth(progress);
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
                eyesBrighteningEffect(progress);
                smoothData.setEyesBrightening(progress);
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
                noseNarrowingEffect(progress);
                smoothData.setNoseNarrowing(progress);
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
                teethWhiteningEffect(progress);
                smoothData.setTeethWhitening(progress);
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
                longChinEffect(progress);
                smoothData.setLongChin(progress);
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
                foreheadShorteningEffect(progress);
                smoothData.setForeheadShortening(progress);
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
                mandibleSlimmingEffect(progress);
                smoothData.setMandibleSlimming(progress);
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
                cheekboneSlimmingEffect(progress);
                smoothData.setCheekboneSlimming(progress);
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
                faceShorteningEffect(progress);
                smoothData.setFaceShortening(progress);
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
                noseLengtheningEffect(progress);
                smoothData.setNoseLengthening(progress);
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
                eyeLineEffect(progress, eyeLineResPath);
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

            } else if (checkedId == binding.rbSecondType.getId()) {
                eyeLineResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelinerCatEye.bundle";

            } else if (checkedId == binding.rbThirdType.getId()) {
                eyeLineResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelinerNaughty.bundle";

            } else if (checkedId == binding.rbFourthType.getId()) {
                eyeLineResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelinerInnocent.bundle";

            } else if (checkedId == binding.rbFifthType.getId()) {
                eyeLineResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelinerDignified.bundle";

            }

        });
    }
    private void eyeLineEffect(int progress, String filePath) {
        effects.setEyeliner(filePath);
        ZegoEffectsEyelinerParam param = new ZegoEffectsEyelinerParam();
        param.intensity = progress;
        effects.setEyelinerParam(param);
    }

    private void adjustmentEyeShadow() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                eyeShadowEffect(progress, eyeShadowResPath);
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

            } else if (checkedId == binding.rbSecondType.getId()) {
                eyeShadowResPath = path + File.separator + "AdvancedResources/beautyMakeupEyeshadowShimmerPink.bundle";

            } else if (checkedId == binding.rbThirdType.getId()) {
                eyeShadowResPath = path + File.separator + "AdvancedResources/beautyMakeupEyeshadowTeaBrown.bundle";

            } else if (checkedId == binding.rbFourthType.getId()) {
                eyeShadowResPath = path + File.separator + "AdvancedResources/beautyMakeupEyeshadowBrightOrange.bundle";

            } else if (checkedId == binding.rbFifthType.getId()) {
                eyeShadowResPath = path + File.separator + "AdvancedResources/beautyMakeupEyeshadowMochaBrown.bundle";

            }

        });
    }
    private void eyeShadowEffect(int progress, String fileName) {
        effects.setEyeshadow(fileName);
        ZegoEffectsEyeshadowParam param = new ZegoEffectsEyeshadowParam();
        param.intensity = progress;
        effects.setEyeshadowParam(param);
    }

    private void adjustmentEyeLashes() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                eyeLashesEffect(progress, eyeLashesResPath);
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

            } else if (checkedId == binding.rbSecondType.getId()) {
                eyeLashesResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelashesTender.bundle";

            } else if (checkedId == binding.rbThirdType.getId()) {
                eyeLashesResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelashesCurl.bundle";

            } else if (checkedId == binding.rbFourthType.getId()) {
                eyeLashesResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelashesEverlong.bundle";

            } else if (checkedId == binding.rbFifthType.getId()) {
                eyeLashesResPath = path + File.separator + "AdvancedResources/beautyMakeupEyelashesThick.bundle";

            }

        });
    }
    private void eyeLashesEffect(int progress, String fileName) {
        effects.setEyelashes(fileName);
        ZegoEffectsEyelashesParam param = new ZegoEffectsEyelashesParam();
        param.intensity = progress;
        effects.setEyelashesParam(param);
    }

    private void adjustmentBlusher() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blusherEffect(progress, blusherResPath);
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

            } else if (checkedId == binding.rbSecondType.getId()) {
                blusherResPath = path + File.separator + "AdvancedResources/beautyMakeupBlusherPeach.bundle";

            } else if (checkedId == binding.rbThirdType.getId()) {
                blusherResPath = path + File.separator + "AdvancedResources/beautyMakeupBlusherMilkyOrange.bundle";

            } else if (checkedId == binding.rbFourthType.getId()) {
                blusherResPath = path + File.separator + "AdvancedResources/beautyMakeupBlusherAprocitPink.bundle";

            } else if (checkedId == binding.rbFifthType.getId()) {
                blusherResPath = path + File.separator + "AdvancedResources/beautyMakeupBlusherSweetOrange.bundle";

            }

        });
    }
    private void blusherEffect(int progress, String fileName) {
        effects.setBlusher(fileName);
        ZegoEffectsBlusherParam param = new ZegoEffectsBlusherParam();
        param.intensity = progress;
        effects.setBlusherParam(param);
    }

    private void adjustmentLipStick() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lipStickEffect(progress, lipStickResPath);
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

            } else if (checkedId == binding.rbSecondType.getId()) {
                lipStickResPath = path + File.separator + "AdvancedResources/beautyMakeupLipstickSweetOrange.bundle";

            } else if (checkedId == binding.rbThirdType.getId()) {
                lipStickResPath = path + File.separator + "AdvancedResources/beautyMakeupLipstickRustRed.bundle";

            } else if (checkedId == binding.rbFourthType.getId()) {
                lipStickResPath = path + File.separator + "AdvancedResources/beautyMakeupLipstickCoral.bundle";

            } else if (checkedId == binding.rbFifthType.getId()) {
                lipStickResPath = path + File.separator + "AdvancedResources/beautyMakeupLipstickRedVelvet.bundle";

            }
        });
    }
    private void lipStickEffect(int progress, String fileName) {
        effects.setLipstick(fileName);
        ZegoEffectsLipstickParam param = new ZegoEffectsLipstickParam();
        param.intensity = progress;
        effects.setLipstickParam(param);
    }


    private void adjustmentColoredContacts() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                coloredContactsEffect(progress, coloredContactsResPath);
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

            } else if (checkedId == binding.rbSecondType.getId()) {
                coloredContactsResPath = path + File.separator + "AdvancedResources/beautyMakeupColoredContactsStarryBlue.bundle";

            } else if (checkedId == binding.rbThirdType.getId()) {
                coloredContactsResPath = path + File.separator + "AdvancedResources/beautyMakeupColoredContactsBrownGreen.bundle";

            } else if (checkedId == binding.rbFourthType.getId()) {
                coloredContactsResPath = path + File.separator + "AdvancedResources/beautyMakeupColoredContactsLightsBrown.bundle";

            } else if (checkedId == binding.rbFifthType.getId()) {
                coloredContactsResPath = path + File.separator + "AdvancedResources/beautyMakeupColoredContactsChocolateBrown.bundle";

            }

        });
    }
    private void coloredContactsEffect(int progress, String fileName) {
        effects.setColoredcontacts(fileName);
        ZegoEffectsColoredcontactsParam param = new ZegoEffectsColoredcontactsParam();
        param.intensity = progress;
        effects.setColoredcontactsParam(param);
    }

    private void adjustmentMakeup() {
        binding.sbAttributesAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                makeupEffect(progress, makeupResPath);
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

            } else if (checkedId == binding.rbSecondType.getId()) {
                makeupResPath = path + File.separator + "AdvancedResources/beautyStyleMakeupMilkyEyes.bundle";

            } else if (checkedId == binding.rbThirdType.getId()) {
                makeupResPath = path + File.separator + "AdvancedResources/beautyStyleMakeupCutieCool.bundle";

            } else if (checkedId == binding.rbFourthType.getId()) {
                makeupResPath = path + File.separator + "AdvancedResources/beautyStyleMakeupPureSexy.bundle";

            } else if (checkedId == binding.rbFifthType.getId()) {
                makeupResPath = path + File.separator + "AdvancedResources/beautyStyleMakeupFlawless.bundle";

            }

        });
    }
    private void makeupEffect(int progress, String fileName) {
        effects.setMakeup(fileName);
        ZegoEffectsMakeupParam param = new ZegoEffectsMakeupParam();
        param.intensity = progress;
        effects.setMakeupParam(param);
    }


}