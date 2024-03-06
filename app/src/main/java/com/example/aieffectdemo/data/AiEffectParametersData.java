package com.example.aieffectdemo.data;

import com.example.aieffectdemo.data.ResAiEffectData;

public class AiEffectParametersData {

    private BeautyData beautyData;

    private SmoothData smoothData;

    private MakeupsData makeupsData;


    public static class BeautyData {
        private int whiten;
        private int smooth;
        private int rosy;
        private int sharpen;
        private int wrinkles;
        private int darkCircles;

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
        private int bigEyes;
        private int faceLifting;
        private int smallMouth;
        private int eyesBrightening;
        private int noseNarrowing;
        private int teethWhitening;
        private int longChin;
        private int foreheadShortening;
        private int mandibleSlimming;
        private int cheekboneSlimming;
        private int faceShortening;
        private int noseLengthening;

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
        private String eyeliner;
        private String eyeShadow;
        private String eyelashes;
        private String blush;
        private String lipstick;
        private String lenses;
        private String styleMakeup;

        public String getEyeliner() {
            return eyeliner;
        }

        public void setEyeliner(String eyeliner) {
            this.eyeliner = eyeliner;
        }

        public String getEyeShadow() {
            return eyeShadow;
        }

        public void setEyeShadow(String eyeShadow) {
            this.eyeShadow = eyeShadow;
        }

        public String getEyelashes() {
            return eyelashes;
        }

        public void setEyelashes(String eyelashes) {
            this.eyelashes = eyelashes;
        }

        public String getBlush() {
            return blush;
        }

        public void setBlush(String blush) {
            this.blush = blush;
        }

        public String getLipstick() {
            return lipstick;
        }

        public void setLipstick(String lipstick) {
            this.lipstick = lipstick;
        }

        public String getLenses() {
            return lenses;
        }

        public void setLenses(String lenses) {
            this.lenses = lenses;
        }

        public String getStyleMakeup() {
            return styleMakeup;
        }

        public void setStyleMakeup(String styleMakeup) {
            this.styleMakeup = styleMakeup;
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
