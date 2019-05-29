package com.example.hittheshape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private Button buttonPlay;
    private Button buttonSettings;
    AdView mAdview;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //AddMob - Google Adds
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mAdview = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
       // AdRequest adRequest = new AdRequest.Builder().addTestDevice("336C7BC06BB587E1A7D28AA2724E204D").build(); //Kasia
        //AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                openChooseLvLActivity();
                interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
            }
        });



        buttonPlay = findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                }
                else {
                    openChooseLvLActivity();
                }
            }
        });


//        buttonSettings = findViewById(R.id.buttonSettings);
//        buttonSettings.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                openSettingsActivity();
//            }
//        });
    }

    public void openChooseLvLActivity(){
        Intent intent = new Intent(this, ChoosLvlActivity.class);
        startActivity(intent);
    }

//    public void openSettingsActivity(){
//        Intent intent = new Intent(this, SettingsActivity.class);
//        startActivity(intent);
//    }


}
