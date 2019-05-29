package com.example.hittheshape;

import com.google.android.gms.ads.AdRequest;

public class Configuration {


    public static final int [] probabilityOfForbiddenShapeInRound={100,100,100,50,50,60,70,80,90, 100,100,100,50,50,60,70,80,90};
    public static final int shapeSize=6;
    public static final int [] checkClick={0,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000, 5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000};
    public static final int numberOfLevels=50;
    public static final int [] pointsToWinLevel={0,5,6,7,8,9,10,5,6,7,8,9,10,11,12,13,14,15,15,16,17,25,11,11,11,11,11,11,11,11,11};

    //public static final String DEVICE_ID = "336C7BC06BB587E1A7D28AA2724E204D"; //Kasia
    public static final String DEVICE_ID = AdRequest.DEVICE_ID_EMULATOR; //Emulator
    //public static final String DEVICE_ID = "336C7BC06BB587E1A7D28AA2724E204D"; //Piter

}
