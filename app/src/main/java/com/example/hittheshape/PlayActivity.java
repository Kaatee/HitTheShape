package com.example.hittheshape;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class PlayActivity extends AppCompatActivity {

    private GifImageView allowedShapeButton;
    private GifImageView forbiddenShapeButton;
    private int points=0;
    private Context context;
    private volatile boolean  clicked =false;
    private volatile boolean  endLevel =false;
    private int levelNo;
    private int size;
    private int lives;
    private Handler handler = new Handler();
    private ImageView[] livesViews;
    private InterstitialAd interstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //adding adds
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(Configuration.DEVICE_ID).build());


        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                Intent intent = new Intent(context, PlayActivity.class);
                intent.putExtra("levelNo", levelNo);
                intent.putExtra("lives", lives);
                startActivity(intent);
                interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(Configuration.DEVICE_ID).build());
            }
        });

        //check current level number
        if(getIntent()!=null){
            levelNo=getIntent().getIntExtra("levelNo", 1);
            lives = getIntent().getIntExtra("lives", 3);
        }

        allowedShapeButton = (GifImageView)findViewById(R.id.shape);
        forbiddenShapeButton= (GifImageView)findViewById(R.id.forbiddenshape);
        livesViews = new ImageView[] {findViewById(R.id.heart1), findViewById(R.id.heart2), findViewById(R.id.heart3)};

        //Log.e("haha", lives+" ");
        for(int i=0; i<3; i++){
            if(i>=lives){
                livesViews[i].setVisibility(View.INVISIBLE);
            }
        }


        setShapeRoundImage();

        formatShapes();
        drawAllowedShapeAtNewPosition();

        //check forbidden shape in first move
        if (randWithGivenProbability(Configuration.probabilityOfForbiddenShapeInRound[levelNo])) {
            enableForbiddenShape();
        } else {
            disableForbiddenShape();
        }

        //handler check for every Configuration.checkClick[levelNo] click
        context=this;
        measureTimeForNextClick();
    }

    public void hitAllowedShape(View view) {
        disableShape();

        //update points
        points+=1;
        updateDisplayPoints();
        clicked=true;

        //check end of current round
        if (points< Configuration.pointsToWinLevel[levelNo] ) {

            drawAllowedShapeAtNewPosition();

            if (randWithGivenProbability(Configuration.probabilityOfForbiddenShapeInRound[levelNo])) {
               drawForbiddenShapeAtNewPosition();
            } else {
                disableForbiddenShape();
            }

            enableAllowedShape();

        }
    }

    public void hitForbiddenShape(View view){
        lives--;
        if(lives<=0) {
           // Log.e("hehe", "forbidden: lives<0, pokazuje okno");
            showLostRoundStatement();
        }
        else{
            handler.removeCallbacksAndMessages(null); ///////
            Intent intent = new Intent(context, PlayActivity.class);
            intent.putExtra("levelNo", levelNo);
            intent.putExtra("lives", lives);
            startActivity(intent);
        }
    }

    private void measureTimeForNextClick(){
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                if(clicked) {
                    handler.postDelayed(this, Configuration.checkClick[levelNo]);
                    clicked=false;
                }
                else if(!clicked && !endLevel){
                    lives--;
                    if(lives>=0) {
                        livesViews[lives].setVisibility(View.INVISIBLE);
                    }
                    if(lives<=0) {
                        //Log.e("hehe", "lives<0, pokazuje okno");
                        showLostRoundStatement();
                    }
                    else{
                        //Log.e("hehe", "Nie pokazuje okna");
                        handler.removeCallbacksAndMessages(null); //////
                        Intent intent = new Intent(context, PlayActivity.class);
                        intent.putExtra("levelNo", levelNo);
                        intent.putExtra("lives", lives);
                        startActivity(intent);
                    }
                }
            }
        }, Configuration.checkClick[levelNo]);
    }

    private void drawAllowedShapeAtNewPosition(){
        Random r = new Random();
        int newAllowedShapePositionX = r.nextInt(getScreenWidth() - allowedShapeButton.getWidth() - 20) + 10;
        int newAllowedShapePositionY = r.nextInt(getScreenHeight() - allowedShapeButton.getHeight() - getNavigationBarHeight() - getStatusBarHeight() - 20) + 10;

        allowedShapeButton.setX(newAllowedShapePositionX);
        allowedShapeButton.setY(newAllowedShapePositionY);
    }

    private void drawForbiddenShapeAtNewPosition(){
        Random r = new Random();
        int newForbiddenShapePositionX = r.nextInt(getScreenWidth() - forbiddenShapeButton.getWidth() - 20) + 10;
        int newForbiddenShapePositionY = r.nextInt(getScreenHeight() - forbiddenShapeButton.getHeight() - getNavigationBarHeight() - getStatusBarHeight() - 20) + 10;

        //check overlapping
        while (allowedShapeButton.getX()- size <= newForbiddenShapePositionX && newForbiddenShapePositionX <= allowedShapeButton.getX() + size && allowedShapeButton.getY() - size <= newForbiddenShapePositionY && newForbiddenShapePositionY <= allowedShapeButton.getY() + size) {
            newForbiddenShapePositionX = r.nextInt(getScreenWidth() - forbiddenShapeButton.getWidth() - 20) + 10;
            newForbiddenShapePositionY = r.nextInt(getScreenHeight() - forbiddenShapeButton.getHeight() - getNavigationBarHeight() - getStatusBarHeight() - 20) + 10;
        }

        forbiddenShapeButton.setX(newForbiddenShapePositionX);
        forbiddenShapeButton.setY(newForbiddenShapePositionY);

    }

    private void formatShapes(){
        //set shape size
        size = (int) getScreenWidth()/ Configuration.shapeSize; //set size as 1/8 screen width size
        allowedShapeButton.getLayoutParams().height = size;
        allowedShapeButton.getLayoutParams().width = size;


        forbiddenShapeButton.getLayoutParams().height = size;
        forbiddenShapeButton.getLayoutParams().width = size;
    }

    private void setShapeRoundImage(){
        //set image background - different in each level (shape0-shape4)
        int mod = levelNo%4;
        String variableValue = "shape"+mod;
        //String variableValue = "coin";
        allowedShapeButton.setBackgroundResource(getResources().getIdentifier(variableValue, "drawable", getPackageName()));
    }

    private void showLostRoundStatement(){
        disableShape();
        disableForbiddenShape();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("You lose :( !");
                builder.setMessage("Do you want to play again this level?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        if(interstitialAd.isLoaded()){
                            interstitialAd.show();
                        }
                        else {
                            Intent intent = new Intent(context, PlayActivity.class);
                            intent.putExtra("levelNo", levelNo);
                            intent.putExtra("lives", lives);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, ChoosLvlActivity.class);
                        intent.putExtra("levelNo", levelNo);
                        intent.putExtra("lives", lives);

                        startActivity(intent);
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private int getNavigationBarHeight() {
        boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0 && !hasMenuKey)
        {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    
    private void updateDisplayPoints() {

        if(points< Configuration.pointsToWinLevel[levelNo]){
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(Integer.toString(points));
        }
        else{
            //start next level
            //show gratulation dialog and start new lvl after pressing "OK"

            endLevel=true;
            disableShape();
            disableForbiddenShape();

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(Integer.toString(points));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Congratulation! You finished lvl "+levelNo)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(context, PlayActivity.class);
                            intent.putExtra("levelNo", levelNo+1);
                            intent.putExtra("lives", lives);

                            startActivity(intent);
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    private boolean randWithGivenProbability(int probability) {
        int leftLimit = 1;
        int rightLimit = 100;
        int generatedInteger = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
        if(probability>=generatedInteger) return true;
        else{
            return false;
        }
    }


    private void disableForbiddenShape(){
        forbiddenShapeButton.setClickable(false);
        forbiddenShapeButton.setVisibility(View.INVISIBLE);
    }
    private void enableForbiddenShape(){
        forbiddenShapeButton.setVisibility(View.VISIBLE);
        forbiddenShapeButton.setClickable(true);
    }
    private void enableAllowedShape(){
        allowedShapeButton.setVisibility(View.VISIBLE);
        allowedShapeButton.setClickable(true);
    }
    private void disableShape(){
        allowedShapeButton.setClickable(false);
        allowedShapeButton.setVisibility(View.INVISIBLE);
    }


    private Animation prepareAnimation(int x, int y){
        Animation animation = new RotateAnimation(0.0f, 360.0f, x,  y );

        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(2000);               // duration in ms
        animation.setRepeatCount(-1);               // -1 = infinite repeated
        animation.setRepeatMode(Animation.INFINITE);
        animation.setFillAfter(true);

        return animation;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}
