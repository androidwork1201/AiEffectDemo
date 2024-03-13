package com.example.aieffectdemo.util;

import android.content.Context;

import com.example.aieffectdemo.data.AiEffectParametersData;

import java.io.File;
import java.util.ArrayList;

import im.zego.effects.ZegoEffects;
import im.zego.effects.entity.ZegoEffectsBigEyesParam;
import im.zego.effects.entity.ZegoEffectsBlurParam;
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
import im.zego.effects.entity.ZegoEffectsWhitenParam;
import im.zego.effects.entity.ZegoEffectsWrinklesRemovingParam;
import im.zego.effects.enums.ZegoEffectsScaleMode;

public class AiEffectManager {
    private static AiEffectManager sInstance;
    private ZegoEffects effects;
    private AiEffectParametersData.BeautyData beautyData;
    private AiEffectParametersData.SmoothData smoothData;
    private AiEffectParametersData.MakeupsData makeupsData;

    private AiEffectManager() {

    }

    public static AiEffectManager getInstance() {
        if (sInstance == null) {
            synchronized (AiEffectManager.class) {
                if (sInstance == null) {
                    sInstance = new AiEffectManager();
                }
            }
        }
        return sInstance;
    }

    public void initAiEffect(Context context, String licenseData) {
        beautyData = new AiEffectParametersData.BeautyData();
        smoothData = new AiEffectParametersData.SmoothData();
        makeupsData = new AiEffectParametersData.MakeupsData();

        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.WholeModule.faceDetection, getFilePath(context, AiEffectConstant.WholeModule.faceDetection));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.WholeModule.pendantDetection, getFilePath(context, AiEffectConstant.WholeModule.pendantDetection));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.WholeModule.rosyDetection, getFilePath(context, AiEffectConstant.WholeModule.rosyDetection));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.WholeModule.teethWhiteningDetection, getFilePath(context, AiEffectConstant.WholeModule.teethWhiteningDetection));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.WholeModule.commonDetection, getFilePath(context, AiEffectConstant.WholeModule.commonDetection));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.WholeModule.stickerBaseDetection, getFilePath(context, AiEffectConstant.WholeModule.stickerBaseDetection));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.WholeModule.faceModel, getFilePath(context, AiEffectConstant.WholeModule.faceModel));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.WholeModule.segmentationModel, getFilePath(context, AiEffectConstant.WholeModule.segmentationModel));

        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeLine.natural, getFilePath(context, AiEffectConstant.EyeLine.natural));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeLine.catEye, getFilePath(context, AiEffectConstant.EyeLine.catEye));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeLine.naughty, getFilePath(context, AiEffectConstant.EyeLine.naughty));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeLine.innocent, getFilePath(context, AiEffectConstant.EyeLine.innocent));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeLine.dignified, getFilePath(context, AiEffectConstant.EyeLine.dignified));

        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeShadow.pinkMist, getFilePath(context, AiEffectConstant.EyeShadow.pinkMist));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeShadow.shimmerPink, getFilePath(context, AiEffectConstant.EyeShadow.shimmerPink));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeShadow.teaBrown, getFilePath(context, AiEffectConstant.EyeShadow.teaBrown));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeShadow.brightOrange, getFilePath(context, AiEffectConstant.EyeShadow.brightOrange));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeShadow.mochaBrown, getFilePath(context, AiEffectConstant.EyeShadow.mochaBrown));

        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeLashes.natural, getFilePath(context, AiEffectConstant.EyeLashes.natural));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeLashes.tender, getFilePath(context, AiEffectConstant.EyeLashes.tender));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeLashes.curl, getFilePath(context, AiEffectConstant.EyeLashes.curl));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeLashes.everLong, getFilePath(context, AiEffectConstant.EyeLashes.everLong));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.EyeLashes.thick, getFilePath(context, AiEffectConstant.EyeLashes.thick));

        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Blusher.slightlyDrunk, getFilePath(context, AiEffectConstant.Blusher.slightlyDrunk));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Blusher.peach, getFilePath(context, AiEffectConstant.Blusher.peach));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Blusher.milkyOrange, getFilePath(context, AiEffectConstant.Blusher.milkyOrange));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Blusher.aprocitPink, getFilePath(context, AiEffectConstant.Blusher.aprocitPink));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Blusher.sweetOrange, getFilePath(context, AiEffectConstant.Blusher.sweetOrange));

        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Lipstick.cameoPink, getFilePath(context, AiEffectConstant.Lipstick.cameoPink));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Lipstick.sweetOrange, getFilePath(context, AiEffectConstant.Lipstick.sweetOrange));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Lipstick.rustRed, getFilePath(context, AiEffectConstant.Lipstick.rustRed));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Lipstick.coral, getFilePath(context, AiEffectConstant.Lipstick.coral));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Lipstick.redVelvet, getFilePath(context, AiEffectConstant.Lipstick.redVelvet));

        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.ColoredContacts.darkNightBlack, getFilePath(context, AiEffectConstant.ColoredContacts.darkNightBlack));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.ColoredContacts.starryBlue, getFilePath(context, AiEffectConstant.ColoredContacts.starryBlue));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.ColoredContacts.brownGreen, getFilePath(context, AiEffectConstant.ColoredContacts.brownGreen));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.ColoredContacts.lightsBrown, getFilePath(context, AiEffectConstant.ColoredContacts.lightsBrown));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.ColoredContacts.chocolateBrown, getFilePath(context, AiEffectConstant.ColoredContacts.chocolateBrown));

        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.StyleMakeup.innocentEyes, getFilePath(context, AiEffectConstant.StyleMakeup.innocentEyes));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.StyleMakeup.milkyEyes, getFilePath(context, AiEffectConstant.StyleMakeup.milkyEyes));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.StyleMakeup.cutieCool, getFilePath(context, AiEffectConstant.StyleMakeup.cutieCool));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.StyleMakeup.pureSexy, getFilePath(context, AiEffectConstant.StyleMakeup.pureSexy));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.StyleMakeup.flawless, getFilePath(context, AiEffectConstant.StyleMakeup.flawless));

        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Filter.dreamyCozily, getFilePath(context, AiEffectConstant.Filter.dreamyCozily));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Filter.dreamySunset, getFilePath(context, AiEffectConstant.Filter.dreamySunset));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Filter.dreamySweet, getFilePath(context, AiEffectConstant.Filter.dreamySweet));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Filter.grayFilmlike, getFilePath(context, AiEffectConstant.Filter.grayFilmlike));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Filter.grayMonet, getFilePath(context, AiEffectConstant.Filter.grayMonet));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Filter.grayNight, getFilePath(context, AiEffectConstant.Filter.grayNight));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Filter.naturalAutumn, getFilePath(context, AiEffectConstant.Filter.naturalAutumn));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Filter.naturalBrighten, getFilePath(context, AiEffectConstant.Filter.naturalBrighten));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Filter.naturalCreamy, getFilePath(context, AiEffectConstant.Filter.naturalCreamy));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Filter.naturalFresh, getFilePath(context, AiEffectConstant.Filter.naturalFresh));

        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Pendant.pendantAnimal, getFilePath(context, AiEffectConstant.Pendant.pendantAnimal));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Pendant.pendantCat, getFilePath(context, AiEffectConstant.Pendant.pendantCat));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Pendant.pendantClawMachine, getFilePath(context, AiEffectConstant.Pendant.pendantClawMachine));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Pendant.pendantClown, getFilePath(context, AiEffectConstant.Pendant.pendantClown));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Pendant.pendantCoolGirl, getFilePath(context, AiEffectConstant.Pendant.pendantCoolGirl));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Pendant.pendantDeer, getFilePath(context, AiEffectConstant.Pendant.pendantDeer));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Pendant.pendantDive, getFilePath(context, AiEffectConstant.Pendant.pendantDive));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Pendant.pendantSailorMoon, getFilePath(context, AiEffectConstant.Pendant.pendantSailorMoon));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.Pendant.pendantWatermelon, getFilePath(context, AiEffectConstant.Pendant.pendantWatermelon));

        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.BgImage.bgImage_1, getFilePath(context, AiEffectConstant.BgImage.bgImage_1));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.BgImage.bgImage_2, getFilePath(context, AiEffectConstant.BgImage.bgImage_2));
        AssetsFileUtil.copyFileFromAssets(context, AiEffectConstant.BgImage.bgImage_3, getFilePath(context, AiEffectConstant.BgImage.bgImage_3));


        ArrayList<String> effectList = new ArrayList<>();
        effectList.add(getFilePath(context, AiEffectConstant.WholeModule.faceDetection));
        effectList.add(getFilePath(context, AiEffectConstant.WholeModule.pendantDetection));
        effectList.add(getFilePath(context, AiEffectConstant.WholeModule.rosyDetection));
        effectList.add(getFilePath(context, AiEffectConstant.WholeModule.teethWhiteningDetection));
        effectList.add(getFilePath(context, AiEffectConstant.WholeModule.commonDetection));
        effectList.add(getFilePath(context, AiEffectConstant.WholeModule.stickerBaseDetection));
        effectList.add(getFilePath(context, AiEffectConstant.WholeModule.faceModel));
        effectList.add(getFilePath(context, AiEffectConstant.WholeModule.segmentationModel));

        ZegoEffects.setResources(effectList);

        effects = ZegoEffects.create(licenseData, context);
        effects.enableFaceDetection(true);

        makeupsData.setEyelinerRes(getFilePath(context, AiEffectConstant.EyeLine.natural));
        makeupsData.setEyeShadowRes(getFilePath(context, AiEffectConstant.EyeShadow.pinkMist));
        makeupsData.setEyelashesRes(getFilePath(context, AiEffectConstant.EyeLashes.natural));
        makeupsData.setBlushRes(getFilePath(context, AiEffectConstant.Blusher.slightlyDrunk));
        makeupsData.setLipstickRes(getFilePath(context, AiEffectConstant.Lipstick.cameoPink));
        makeupsData.setLensesRes(getFilePath(context, AiEffectConstant.ColoredContacts.darkNightBlack));
        makeupsData.setStyleMakeupRes(getFilePath(context, AiEffectConstant.StyleMakeup.innocentEyes));
        makeupsData.setFilterRes(getFilePath(context, AiEffectConstant.Filter.dreamyCozily));
    }

    private String getFilePath(Context context, String subPath) {
        String path = context.getCacheDir().getPath();
        return path + File.separator + subPath;
    }

    public AiEffectParametersData.BeautyData getBeautyData(){
        return beautyData;
    }

    public AiEffectParametersData.SmoothData getSmoothData(){
        return smoothData;
    }

    public AiEffectParametersData.MakeupsData getMakeupsData() {
        return makeupsData;
    }

    public void setBeautyData(AiEffectParametersData.BeautyData beautyData) {
        this.beautyData = beautyData;
    }

    public void setSmoothData(AiEffectParametersData.SmoothData smoothData) {
        this.smoothData = smoothData;
    }

    public void setMakeupsData(AiEffectParametersData.MakeupsData makeupsData) {
        this.makeupsData = makeupsData;
    }

    public ZegoEffects getEffects() {
        return effects;
    }

    //美白
    public void faceWhiten(int progress) {
        effects.enableWhiten(true);
        ZegoEffectsWhitenParam param = new ZegoEffectsWhitenParam();
        param.intensity = progress;
        effects.setWhitenParam(param);
    }

    //平滑肌膚
    public void smoothFace(int progress) {
        effects.enableSmooth(true);
        ZegoEffectsSmoothParam param = new ZegoEffectsSmoothParam();
        param.intensity = progress;
        effects.setSmoothParam(param);
    }

    //腮紅
    public void rosyEffect(int progress) {
        effects.enableRosy(true);
        ZegoEffectsRosyParam param = new ZegoEffectsRosyParam();
        param.intensity = progress;
        effects.setRosyParam(param);
    }

    //影像銳化
    public void sharpenEffect(int progress) {
        effects.enableSharpen(true);
        ZegoEffectsSharpenParam param = new ZegoEffectsSharpenParam();
        param.intensity = progress;
        effects.setSharpenParam(param);
    }

    //去除法令纹
    public void wrinklesEffect(int progress) {
        effects.enableWrinklesRemoving(true);
        ZegoEffectsWrinklesRemovingParam param = new ZegoEffectsWrinklesRemovingParam();
        param.intensity = progress;
        effects.setWrinklesRemovingParam(param);
    }

    //去除黑眼圈
    public void darkCirclesEffect(int progress) {
        effects.enableDarkCirclesRemoving(true);
        ZegoEffectsDarkCirclesRemovingParam param = new ZegoEffectsDarkCirclesRemovingParam();
        param.intensity = progress;
        effects.setDarkCirclesRemovingParam(param);
    }

    //大眼
    public void bigEyeEffect(int progress) {
        effects.enableBigEyes(true);
        ZegoEffectsBigEyesParam param = new ZegoEffectsBigEyesParam();
        param.intensity = progress;
        effects.setBigEyesParam(param);
    }

    //瘦臉
    public void faceLiftEffect(int progress) {
        effects.enableFaceLifting(true);
        ZegoEffectsFaceLiftingParam param = new ZegoEffectsFaceLiftingParam();
        param.intensity = progress;
        effects.setFaceLiftingParam(param);
    }

    //小嘴
    public void smailMouthEffect(int progress) {
        effects.enableSmallMouth(true);
        ZegoEffectsSmallMouthParam param = new ZegoEffectsSmallMouthParam();
        param.intensity = progress;
        effects.setSmallMouthParam(param);
    }

    //亮眼
    public void eyesBrighteningEffect(int progress) {
        effects.enableEyesBrightening(true);
        ZegoEffectsEyesBrighteningParam param = new ZegoEffectsEyesBrighteningParam();
        param.intensity = progress;
        effects.setEyesBrighteningParam(param);
    }

    //瘦鼻
    public void noseNarrowingEffect(int progress) {
        effects.enableNoseNarrowing(true);
        ZegoEffectsNoseNarrowingParam param = new ZegoEffectsNoseNarrowingParam();
        param.intensity = progress;
        effects.setNoseNarrowingParam(param);
    }

    //白牙
    public void teethWhiteningEffect(int progress) {
        effects.enableTeethWhitening(true);
        ZegoEffectsTeethWhiteningParam param = new ZegoEffectsTeethWhiteningParam();
        param.intensity = progress;
        effects.setTeethWhiteningParam(param);
    }

    //長下巴
    public void longChinEffect(int progress) {
        effects.enableLongChin(true);
        ZegoEffectsLongChinParam param = new ZegoEffectsLongChinParam();
        param.intensity = progress;
        effects.setLongChinParam(param);
    }

    //縮小額頭高度
    public void foreheadShorteningEffect(int progress) {
        effects.enableForeheadShortening(true);
        ZegoEffectsForeheadShorteningParam param = new ZegoEffectsForeheadShorteningParam();
        param.intensity = progress;
        effects.setForeheadShorteningParam(param);
    }

    //瘦下額骨
    public void mandibleSlimmingEffect(int progress) {
        effects.enableMandibleSlimming(true);
        ZegoEffectsMandibleSlimmingParam param = new ZegoEffectsMandibleSlimmingParam();
        param.intensity = progress;
        effects.setMandibleSlimmingParam(param);
    }

    //瘦顱骨
    public void cheekboneSlimmingEffect(int progress) {
        effects.enableCheekboneSlimming(true);
        ZegoEffectsCheekboneSlimmingParam param = new ZegoEffectsCheekboneSlimmingParam();
        param.intensity = progress;
        effects.setCheekboneSlimmingParam(param);
    }

    //小臉
    public void faceShorteningEffect(int progress) {
        effects.enableFaceShortening(true);
        ZegoEffectsFaceShorteningParam param = new ZegoEffectsFaceShorteningParam();
        param.intensity = progress;
        effects.setFaceShorteningParam(param);
    }

    //长鼻
    public void noseLengtheningEffect(int progress) {
        effects.enableNoseLengthening(true);
        ZegoEffectsNoseLengtheningParam param = new ZegoEffectsNoseLengtheningParam();
        param.intensity = progress;
        effects.setNoseLengtheningParam(param);
    }

    public void eyeLineEffect(int progress, String filePath) {
        effects.setEyeliner(filePath);
        ZegoEffectsEyelinerParam param = new ZegoEffectsEyelinerParam();
        param.intensity = progress;
        effects.setEyelinerParam(param);
    }

    public void eyeShadowEffect(int progress, String fileName) {
        effects.setEyeshadow(fileName);
        ZegoEffectsEyeshadowParam param = new ZegoEffectsEyeshadowParam();
        param.intensity = progress;
        effects.setEyeshadowParam(param);
    }

    public void eyeLashesEffect(int progress, String fileName) {
        effects.setEyelashes(fileName);
        ZegoEffectsEyelashesParam param = new ZegoEffectsEyelashesParam();
        param.intensity = progress;
        effects.setEyelashesParam(param);
    }

    public void blusherEffect(int progress, String fileName) {
        effects.setBlusher(fileName);
        ZegoEffectsBlusherParam param = new ZegoEffectsBlusherParam();
        param.intensity = progress;
        effects.setBlusherParam(param);
    }

    public void lipStickEffect(int progress, String fileName) {
        effects.setLipstick(fileName);
        ZegoEffectsLipstickParam param = new ZegoEffectsLipstickParam();
        param.intensity = progress;
        effects.setLipstickParam(param);
    }

    public void coloredContactsEffect(int progress, String fileName) {
        effects.setColoredcontacts(fileName);
        ZegoEffectsColoredcontactsParam param = new ZegoEffectsColoredcontactsParam();
        param.intensity = progress;
        effects.setColoredcontactsParam(param);
    }

    public void makeupEffect(int progress, String fileName) {
        effects.setMakeup(fileName);
        ZegoEffectsMakeupParam param = new ZegoEffectsMakeupParam();
        param.intensity = progress;
        effects.setMakeupParam(param);
    }

    public void filterEffect(int progress, String fileName) {
        effects.setFilter(fileName);
        ZegoEffectsFilterParam param = new ZegoEffectsFilterParam();
        param.intensity = progress;
        effects.setFilterParam(param);
    }


    public void setPendantEffect(String fileName) {
        effects.setPendant(fileName);
    }

    public void closePendantEffect() {
        effects.setPendant(null);
    }


    public void setPortraitSegmentation(String imagePath) {
        effects.setPortraitSegmentationBackgroundPath(imagePath, ZegoEffectsScaleMode.ASPECT_FILL);
        effects.enablePortraitSegmentation(true);

    }
    public void closeSetPortraitSegmentation() {
        effects.setPortraitSegmentationBackgroundPath(null, ZegoEffectsScaleMode.ASPECT_FILL);
        effects.enablePortraitSegmentation(false);
    }
}
