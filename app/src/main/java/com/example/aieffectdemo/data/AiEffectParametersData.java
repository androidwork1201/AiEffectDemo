package com.example.aieffectdemo.data;

import com.example.aieffectdemo.data.ResAiEffectData;

public class AiEffectParametersData {

    private BeautyData beautyData;

    private SmoothData smoothData;

    private MakeupsData makeupsData;


    public static class BeautyData {
        private int whiten = 0;
        private int smooth = 0;
        private int rosy = 0;
        private int sharpen = 0;
        private int wrinkles = 0;
        private int darkCircles = 0;

        public int getWhiten() {
            return whiten;
        }

        public void setWhiten(int whiten) {
            this.whiten = whiten;
        }

        public int getSmooth() {
            return smooth;
        }

        public void setSmooth(int smooth) {
            this.smooth = smooth;
        }

        public int getRosy() {
            return rosy;
        }

        public void setRosy(int rosy) {
            this.rosy = rosy;
        }

        public int getSharpen() {
            return sharpen;
        }

        public void setSharpen(int sharpen) {
            this.sharpen = sharpen;
        }

        public int getWrinkles() {
            return wrinkles;
        }

        public void setWrinkles(int wrinkles) {
            this.wrinkles = wrinkles;
        }

        public int getDarkCircles() {
            return darkCircles;
        }

        public void setDarkCircles(int darkCircles) {
            this.darkCircles = darkCircles;
        }
    }

    public static class SmoothData {
        private int bigEyes = 0;
        private int faceLifting = 0;
        private int smallMouth = 0;
        private int eyesBrightening = 0;
        private int noseNarrowing = 0;
        private int teethWhitening = 0;
        private int longChin = 0;
        private int foreheadShortening = 0;
        private int mandibleSlimming = 0;
        private int cheekboneSlimming = 0;
        private int faceShortening = 0;
        private int noseLengthening = 0;

        public int getBigEyes() {
            return bigEyes;
        }

        public void setBigEyes(int bigEyes) {
            this.bigEyes = bigEyes;
        }

        public int getFaceLifting() {
            return faceLifting;
        }

        public void setFaceLifting(int faceLifting) {
            this.faceLifting = faceLifting;
        }

        public int getSmallMouth() {
            return smallMouth;
        }

        public void setSmallMouth(int smallMouth) {
            this.smallMouth = smallMouth;
        }

        public int getEyesBrightening() {
            return eyesBrightening;
        }

        public void setEyesBrightening(int eyesBrightening) {
            this.eyesBrightening = eyesBrightening;
        }

        public int getNoseNarrowing() {
            return noseNarrowing;
        }

        public void setNoseNarrowing(int noseNarrowing) {
            this.noseNarrowing = noseNarrowing;
        }

        public int getTeethWhitening() {
            return teethWhitening;
        }

        public void setTeethWhitening(int teethWhitening) {
            this.teethWhitening = teethWhitening;
        }

        public int getLongChin() {
            return longChin;
        }

        public void setLongChin(int longChin) {
            this.longChin = longChin;
        }

        public int getForeheadShortening() {
            return foreheadShortening;
        }

        public void setForeheadShortening(int foreheadShortening) {
            this.foreheadShortening = foreheadShortening;
        }

        public int getMandibleSlimming() {
            return mandibleSlimming;
        }

        public void setMandibleSlimming(int mandibleSlimming) {
            this.mandibleSlimming = mandibleSlimming;
        }

        public int getCheekboneSlimming() {
            return cheekboneSlimming;
        }

        public void setCheekboneSlimming(int cheekboneSlimming) {
            this.cheekboneSlimming = cheekboneSlimming;
        }

        public int getFaceShortening() {
            return faceShortening;
        }

        public void setFaceShortening(int faceShortening) {
            this.faceShortening = faceShortening;
        }

        public int getNoseLengthening() {
            return noseLengthening;
        }

        public void setNoseLengthening(int noseLengthening) {
            this.noseLengthening = noseLengthening;
        }
    }

    public static class MakeupsData {
        private String eyelinerRes;
        private String eyeShadowRes;
        private String eyelashesRes;
        private String blushRes;
        private String lipstickRes;
        private String lensesRes;
        private String styleMakeupRes;
        private String filterRes;
        private int eyeliner = 0;
        private int eyeShadow = 0;
        private int eyelashes = 0;
        private int blush = 0;
        private int lipstick = 0;
        private int lenses = 0;
        private int styleMakeup = 0;
        private int filter = 100;


        public String getEyelinerRes() {
            return eyelinerRes;
        }

        public void setEyelinerRes(String eyelinerRes) {
            this.eyelinerRes = eyelinerRes;
        }

        public String getEyeShadowRes() {
            return eyeShadowRes;
        }

        public void setEyeShadowRes(String eyeShadowRes) {
            this.eyeShadowRes = eyeShadowRes;
        }

        public String getEyelashesRes() {
            return eyelashesRes;
        }

        public void setEyelashesRes(String eyelashesRes) {
            this.eyelashesRes = eyelashesRes;
        }

        public String getBlushRes() {
            return blushRes;
        }

        public void setBlushRes(String blushRes) {
            this.blushRes = blushRes;
        }

        public String getLipstickRes() {
            return lipstickRes;
        }

        public void setLipstickRes(String lipstickRes) {
            this.lipstickRes = lipstickRes;
        }

        public String getLensesRes() {
            return lensesRes;
        }

        public void setLensesRes(String lensesRes) {
            this.lensesRes = lensesRes;
        }

        public String getStyleMakeupRes() {
            return styleMakeupRes;
        }

        public void setStyleMakeupRes(String styleMakeupRes) {
            this.styleMakeupRes = styleMakeupRes;
        }

        public String getFilterRes() {
            return filterRes;
        }

        public void setFilterRes(String filterRes) {
            this.filterRes = filterRes;
        }

        public int getEyeliner() {
            return eyeliner;
        }

        public void setEyeliner(int eyeliner) {
            this.eyeliner = eyeliner;
        }

        public int getEyeShadow() {
            return eyeShadow;
        }

        public void setEyeShadow(int eyeShadow) {
            this.eyeShadow = eyeShadow;
        }

        public int getEyelashes() {
            return eyelashes;
        }

        public void setEyelashes(int eyelashes) {
            this.eyelashes = eyelashes;
        }

        public int getBlush() {
            return blush;
        }

        public void setBlush(int blush) {
            this.blush = blush;
        }

        public int getLipstick() {
            return lipstick;
        }

        public void setLipstick(int lipstick) {
            this.lipstick = lipstick;
        }

        public int getLenses() {
            return lenses;
        }

        public void setLenses(int lenses) {
            this.lenses = lenses;
        }

        public int getStyleMakeup() {
            return styleMakeup;
        }

        public void setStyleMakeup(int styleMakeup) {
            this.styleMakeup = styleMakeup;
        }

        public int getFilter() {
            return filter;
        }

        public void setFilter(int filter) {
            this.filter = filter;
        }
    }

    public BeautyData getBeautyData() {
        return beautyData;
    }

    public void setBeautyData(BeautyData beautyData) {
        this.beautyData = beautyData;
    }

    public SmoothData getSmoothData() {
        return smoothData;
    }

    public void setSmoothData(SmoothData smoothData) {
        this.smoothData = smoothData;
    }

    public MakeupsData getMakeupsData() {
        return makeupsData;
    }

    public void setMakeupsData(MakeupsData makeupsData) {
        this.makeupsData = makeupsData;
    }
}
