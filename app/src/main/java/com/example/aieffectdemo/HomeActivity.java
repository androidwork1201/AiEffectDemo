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
    private String roomId;
    private String licenseData;
    private AiEffectParametersData.BeautyData beautyData;
    private AiEffectParametersData.SmoothData smoothData;
    private AiEffectParametersData.MakeupsData makeupsData;

    private ZegoEffects effects;
    private ZegoEngineProfile profile;

    private PendantAdapter adapter;

    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fabSticker.getDrawable().setTint(ContextCompat.getColor(this, R.color.white));

        getParameters();
        initFab();
        initPendant();
        binding.ivEnd.setOnClickListener(v -> finish());
    }

    @Override
    protected void onStop() {
        super.onStop();
        destroyEngine();
    }

    private void initFab() {
        binding.fabSticker.setOnClickListener(v -> {
            binding.fabSticker.setVisibility(View.GONE);
            binding.rvSticker.setVisibility(View.VISIBLE);
        });

        binding.clEffect.setOnClickListener(v -> {
            closePendant();
        });
    }

    private void initPendant() {

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Animal");
        arrayList.add("Cat");
        arrayList.add("ClawMachine");
        arrayList.add("Clown");
        arrayList.add("CoolGirl");
        arrayList.add("Deer");
        arrayList.add("Dive");
        arrayList.add("SailorMoon");
        arrayList.add("Watermelon");
        arrayList.add("移除掛件");

        adapter = new PendantAdapter();
        adapter.setOnItemListener(this);
        binding.rvSticker.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSticker.setHasFixedSize(true);
        binding.rvSticker.setAdapter(adapter);
        adapter.setData(arrayList);
    }

    private void closePendant() {
        binding.fabSticker.setVisibility(View.VISIBLE);
        binding.rvSticker.setVisibility(View.GONE);
        isOpen = !isOpen;
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

                initAiEffect();
                createEngine();
                initVideoProcess();
                startListenerEvent();
                loginRoom();
            }

        }

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

        String filterDreamyCozily = "AdvancedResources/filterDreamyCozily.bundle";
        String filterDreamySunset = "AdvancedResources/filterDreamySunset.bundle";
        String filterDreamySweet = "AdvancedResources/filterDreamySweet.bundle";
        String filterGrayFilmlike = "AdvancedResources/filterGrayFilmlike.bundle";
        String filterGrayMonet = "AdvancedResources/filterGrayMonet.bundle";
        String filterGrayNight = "AdvancedResources/filterGrayNight.bundle";
        String filterNaturalAutumn = "AdvancedResources/filterNaturalAutumn.bundle";
        String filterNaturalBrighten = "AdvancedResources/filterNaturalBrighten.bundle";
        String filterNaturalCreamy = "AdvancedResources/filterNaturalCreamy.bundle";
        String filterNaturalFresh = "AdvancedResources/filterNaturalFresh.bundle";

        AssetsFileUtil.copyFileFromAssets(this, filterDreamyCozily, path + File.separator + filterDreamyCozily);
        AssetsFileUtil.copyFileFromAssets(this, filterDreamySunset, path + File.separator + filterDreamySunset);
        AssetsFileUtil.copyFileFromAssets(this, filterDreamySweet, path + File.separator + filterDreamySweet);
        AssetsFileUtil.copyFileFromAssets(this, filterGrayFilmlike, path + File.separator + filterGrayFilmlike);
        AssetsFileUtil.copyFileFromAssets(this, filterGrayMonet, path + File.separator + filterGrayMonet);
        AssetsFileUtil.copyFileFromAssets(this, filterGrayNight, path + File.separator + filterGrayNight);
        AssetsFileUtil.copyFileFromAssets(this, filterNaturalAutumn, path + File.separator + filterNaturalAutumn);
        AssetsFileUtil.copyFileFromAssets(this, filterNaturalBrighten, path + File.separator + filterNaturalBrighten);
        AssetsFileUtil.copyFileFromAssets(this, filterNaturalCreamy, path + File.separator + filterNaturalCreamy);
        AssetsFileUtil.copyFileFromAssets(this, filterNaturalFresh, path + File.separator + filterNaturalFresh);

        String animalPendant = "AdvancedResources/stickerAnimal.bundle";
        String catPendant = "AdvancedResources/stickerCat.bundle";
        String clawMachinePendant = "AdvancedResources/stickerClawMachine.bundle";
        String clownPendant = "AdvancedResources/stickerClown.bundle";
        String coolGirlPendant = "AdvancedResources/stickerCoolGirl.bundle";
        String deerPendant = "AdvancedResources/stickerDeer.bundle";
        String divePendant = "AdvancedResources/stickerDive.bundle";
        String sailorMoonPendant = "AdvancedResources/stickerSailorMoon.bundle";
        String watermelonPendant = "AdvancedResources/stickerWatermelon.bundle";

        AssetsFileUtil.copyFileFromAssets(this, animalPendant, path + File.separator + animalPendant);
        AssetsFileUtil.copyFileFromAssets(this, catPendant, path + File.separator + catPendant);
        AssetsFileUtil.copyFileFromAssets(this, clawMachinePendant, path + File.separator + clawMachinePendant);
        AssetsFileUtil.copyFileFromAssets(this, clownPendant, path + File.separator + clownPendant);
        AssetsFileUtil.copyFileFromAssets(this, coolGirlPendant, path + File.separator + coolGirlPendant);
        AssetsFileUtil.copyFileFromAssets(this, deerPendant, path + File.separator + deerPendant);
        AssetsFileUtil.copyFileFromAssets(this, divePendant, path + File.separator + divePendant);
        AssetsFileUtil.copyFileFromAssets(this, sailorMoonPendant, path + File.separator + sailorMoonPendant);
        AssetsFileUtil.copyFileFromAssets(this, watermelonPendant, path + File.separator + watermelonPendant);


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

        //美顏
        faceWhiten(beautyData.getWhiten());
        smoothFace(beautyData.getSmooth());
        rosyEffect(beautyData.getRosy());
        sharpenEffect(beautyData.getSharpen());
        wrinklesEffect(beautyData.getWrinkles());
        darkCirclesEffect(beautyData.getDarkCircles());

        //美型
        bigEyeEffect(smoothData.getBigEyes());
        faceLiftEffect(smoothData.getFaceLifting());
        smailMouthEffect(smoothData.getSmallMouth());
        eyesBrighteningEffect(smoothData.getEyesBrightening());
        noseNarrowingEffect(smoothData.getNoseNarrowing());
        teethWhiteningEffect(smoothData.getTeethWhitening());
        longChinEffect(smoothData.getLongChin());
        foreheadShorteningEffect(smoothData.getForeheadShortening());
        mandibleSlimmingEffect(smoothData.getMandibleSlimming());
        cheekboneSlimmingEffect(smoothData.getCheekboneSlimming());
        faceShorteningEffect(smoothData.getFaceShortening());
        noseLengtheningEffect(smoothData.getNoseLengthening());

        //美妝
        eyeLineEffect(makeupsData.getEyeliner(), makeupsData.getEyelinerRes());
        eyeShadowEffect(makeupsData.getEyeShadow(), makeupsData.getEyeShadowRes());
        eyeLashesEffect(makeupsData.getEyelashes(), makeupsData.getEyelashesRes());
        blusherEffect(makeupsData.getBlush(), makeupsData.getBlushRes());
        lipStickEffect(makeupsData.getLipstick(), makeupsData.getLipstickRes());
        coloredContactsEffect(makeupsData.getLenses(), makeupsData.getLensesRes());
        makeupEffect(makeupsData.getStyleMakeup(), makeupsData.getStyleMakeupRes());

        //濾鏡
        filterEffect(makeupsData.getFilter(), makeupsData.getFilterRes());

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


    //----------------------------------------------美顏-------------------------------------------------
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

    //----------------------------------------------美型-------------------------------------------------
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

    //----------------------------------------------美妝-------------------------------------------------
    //眼影
    private void eyeLineEffect(int progress, String fileName) {
        effects.setEyeliner(fileName);
        ZegoEffectsEyelinerParam param = new ZegoEffectsEyelinerParam();
        param.intensity = progress;
        effects.setEyelinerParam(param);
    }

    //眼影
    private void eyeShadowEffect(int progress, String fileName) {
        effects.setEyeshadow(fileName);
        ZegoEffectsEyeshadowParam param = new ZegoEffectsEyeshadowParam();
        param.intensity = progress;
        effects.setEyeshadowParam(param);
    }

    //眼睫毛
    private void eyeLashesEffect(int progress, String fileName) {
        effects.setEyelashes(fileName);
        ZegoEffectsEyelashesParam param = new ZegoEffectsEyelashesParam();
        param.intensity = progress;
        effects.setEyelashesParam(param);
    }

    //腮紅
    private void blusherEffect(int progress, String fileName) {
        effects.setBlusher(fileName);
        ZegoEffectsBlusherParam param = new ZegoEffectsBlusherParam();
        param.intensity = progress;
        effects.setBlusherParam(param);
    }

    //口紅
    private void lipStickEffect(int progress, String fileName) {
        effects.setLipstick(fileName);
        ZegoEffectsLipstickParam param = new ZegoEffectsLipstickParam();
        param.intensity = progress;
        effects.setLipstickParam(param);
    }

    //美瞳
    private void coloredContactsEffect(int progress, String fileName) {
        effects.setColoredcontacts(fileName);
        ZegoEffectsColoredcontactsParam param = new ZegoEffectsColoredcontactsParam();
        param.intensity = progress;
        effects.setColoredcontactsParam(param);
    }

    //風格妝
    private void makeupEffect(int progress, String fileName) {
        effects.setMakeup(fileName);
        ZegoEffectsMakeupParam param = new ZegoEffectsMakeupParam();
        param.intensity = progress;
        effects.setMakeupParam(param);
    }

    //濾鏡
    private void filterEffect(int progress, String fileName) {
        effects.setFilter(fileName);
        ZegoEffectsFilterParam param = new ZegoEffectsFilterParam();
        param.intensity = progress;
        effects.setFilterParam(param);
    }


    @Override
    public void onPendant(int position, String itemName) {
        String path = this.getCacheDir().getPath();
        String animalPendant = "AdvancedResources/stickerAnimal.bundle";
        String catPendant = "AdvancedResources/stickerCat.bundle";
        String clawMachinePendant = "AdvancedResources/stickerClawMachine.bundle";
        String clownPendant = "AdvancedResources/stickerClown.bundle";
        String coolGirlPendant = "AdvancedResources/stickerCoolGirl.bundle";
        String deerPendant = "AdvancedResources/stickerDeer.bundle";
        String divePendant = "AdvancedResources/stickerDive.bundle";
        String sailorMoonPendant = "AdvancedResources/stickerSailorMoon.bundle";
        String watermelonPendant = "AdvancedResources/stickerWatermelon.bundle";


        switch (itemName) {
            case "Animal": {
                effects.setPendant(path + File.separator + animalPendant);
                break;
            }
            case "Cat": {
                effects.setPendant(path + File.separator + catPendant);
                break;
            }
            case "ClawMachine": {
                effects.setPendant(path + File.separator + clawMachinePendant);
                break;
            }
            case "Clown": {
                effects.setPendant(path + File.separator + clownPendant);
                break;
            }
            case "CoolGirl": {
                effects.setPendant(path + File.separator + coolGirlPendant);
                break;
            }
            case "Deer": {
                effects.setPendant(path + File.separator + deerPendant);
                break;
            }
            case "Dive": {
                effects.setPendant(path + File.separator + divePendant);
                break;
            }
            case "SailorMoon": {
                effects.setPendant(path + File.separator + sailorMoonPendant);
                break;
            }
            case "Watermelon": {
                effects.setPendant(path + File.separator + watermelonPendant);
                break;
            }
            case "移除掛件": {
                effects.setPendant(null);
                break;
            }
            default:
                break;
        }
        closePendant();
    }
}
