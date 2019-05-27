package com.example.hittheshape;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    private Button shapeButton;
    private Button forbiddenShapeButton;
    private int points=0;
    private Context context;
    private volatile boolean  clicked =false;
    private volatile boolean  endLevel =false;
    private int levelNo;
    private int size;
    private Handler handler = new Handler();
    private Handler buttonHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        shapeButton = (Button)findViewById(R.id.shape);
        forbiddenShapeButton= (Button)findViewById(R.id.forbiddenshape);

        if(getIntent()!=null){
            levelNo=getIntent().getIntExtra("levelNo", 0);
        }

        //set image background - different in each level (shape0-shape4)
        int mod = levelNo%5;
        String variableValue = "shape"+mod;
        shapeButton.setBackgroundResource(getResources().getIdentifier(variableValue, "drawable", getPackageName()));


        //set shape size
        size = (int) getScreenWidth()/TemporaryConfiguration.shapeSize; //set size as 1/8 screen width size
        shapeButton.getLayoutParams().height = size;
        shapeButton.getLayoutParams().width = size;


        forbiddenShapeButton.getLayoutParams().height = size;
        forbiddenShapeButton.getLayoutParams().width = size;

        //Animation - rotate
        Animation an = prepareAnimation((int) size/2, (int) size/2);
        shapeButton.startAnimation(an);
        //forbiddenShapeButton.startAnimation(an);

        context=this;

        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                if(clicked) {
                    handler.postDelayed(this, TemporaryConfiguration.checkClick[levelNo]);
                    clicked=false;
                }
                else if(!clicked && !endLevel){
                    showDialog();
                }
            }
        }, TemporaryConfiguration.checkClick[levelNo]);

    }

    private void showDialog(){


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("You lose :( !");
                builder.setMessage("Do you want to play again this level?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        Intent intent = new Intent(context, PlayActivity.class);
                        intent.putExtra("levelNo", levelNo);

                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, ChoosLvlActivity.class);
                        intent.putExtra("levelNo", levelNo);

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

    
    private void updateDisplayPoints(View view) {

        if(points<TemporaryConfiguration.pointsToWinLevel[levelNo]){
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(Integer.toString(points));
        }
        else{
            //start next level
            //show gratulation dialog and start new lvl after pressing "OK"
            endLevel=true;
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(Integer.toString(points));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Congratulation! You finished lvl "+levelNo)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(context, PlayActivity.class);
                            intent.putExtra("levelNo", levelNo+1);

                            startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    public void hitTheShape(View view) {
        shapeButton.setClickable(false);
        shapeButton.setVisibility(View.INVISIBLE);

        points+=1;
        updateDisplayPoints(view);
        clicked=true;

        Random r = new Random();
        int newX= r.nextInt(getScreenWidth()-shapeButton.getWidth()-20)+10;
        int newY = r.nextInt(getScreenHeight()-shapeButton.getHeight()-getNavigationBarHeight()-getStatusBarHeight()-20)+10;

        shapeButton.setX(newX);
        shapeButton.setY(newY);

        Random r1 = new Random();
        int newForbiddenX=r1.nextInt(getScreenWidth()-forbiddenShapeButton.getWidth()-20)+10;
        int newForbiddenY=r1.nextInt(getScreenHeight()-forbiddenShapeButton.getHeight()-getNavigationBarHeight()-getStatusBarHeight()-20)+10;

        while(newX-size<=newForbiddenX && newForbiddenX<=newX+size && newY-size<=newForbiddenY && newForbiddenY<=newY+size){
            newForbiddenX=r1.nextInt(getScreenWidth()-forbiddenShapeButton.getWidth()-20)+10;
            newForbiddenY=r1.nextInt(getScreenHeight()-forbiddenShapeButton.getHeight()-getNavigationBarHeight()-getStatusBarHeight()-20)+10;
        }


        forbiddenShapeButton.setX(newForbiddenX);
        forbiddenShapeButton.setY(newForbiddenY);

        int animationCenterX = (int) (newX + size/2);
        int animationCenterY = (int) (newY + size/2);

//        int animationCenterX2 = (int) (newForbiddenX + size/2);
//        int animationCenterY2 = (int) (newForbiddenY + size/2);
//
//        //Animation - rotate
//        Animation an1 = prepareAnimation(animationCenterX2, animationCenterY2);
//        forbiddenShapeButton.startAnimation(an1);

        //Animation - rotate
        Animation an = prepareAnimation(animationCenterX, animationCenterY);
        shapeButton.startAnimation(an);


        buttonHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shapeButton.setVisibility(View.INVISIBLE);
                shapeButton.setClickable(true);
            }
        }, TemporaryConfiguration.nextShapeAppear[levelNo]);

    }

    public void hitForbiddenShape(View view){
        shapeButton.setClickable(false);
        shapeButton.setVisibility(View.INVISIBLE);
        forbiddenShapeButton.setClickable(false);
        forbiddenShapeButton.setVisibility(View.INVISIBLE);

         showDialog();
    }

    private Animation prepareAnimation(int x, int y){
        Animation an = new RotateAnimation(0.0f, 360.0f, x,  y );

        an.setInterpolator(new LinearInterpolator());
        an.setDuration(2000);               // duration in ms
        an.setRepeatCount(-1);               // -1 = infinite repeated
        an.setRepeatMode(Animation.INFINITE);
        an.setFillAfter(true);

        return an;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        buttonHandler.removeCallbacksAndMessages(null);
    }

}
