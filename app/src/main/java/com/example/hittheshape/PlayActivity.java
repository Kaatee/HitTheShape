package com.example.hittheshape;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    private Button shapeButton;
    private int points=0;
    private Context context;
    private boolean clicked=false;
    private int levelNo;
    int size;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        shapeButton = (Button)findViewById(R.id.shape);
        Button shapeButton = findViewById(R.id.shape);


        if(getIntent()!=null){
            levelNo=getIntent().getIntExtra("levelNo", 0);
            //Toast.makeText(this, "Level No: " +Integer.toString(levelNo), Toast.LENGTH_SHORT).show();
        }

        //set image background - different in each level (shape0-shape4)
        int mod = levelNo%5;
        String variableValue = "shape"+mod;
        shapeButton.setBackgroundResource(getResources().getIdentifier(variableValue, "drawable", getPackageName()));

        //shapeButton.setBackgroundResource(R.drawable.shape1);

        //set shape size
        size = (int) getScreenWidth()/TemporaryConfiguration.shapeSize; //set size as 1/8 screen width size
        shapeButton.getLayoutParams().height = size;
        shapeButton.getLayoutParams().width = size;

        //Animation - rotate
        Animation an = prepareAnimation((int) size/2, (int) size/2);
        shapeButton.startAnimation(an);

        context=this;

        //measure round time
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//                builder.setTitle("Time is over");
//                builder.setMessage("Do you want to play again this level?");
//
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do nothing but close the dialog
//                        Intent intent = new Intent(context, PlayActivity.class);
//                        startActivity(intent);
//                        dialog.dismiss();
//                    }
//                });
//
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(context, MainActivity.class);
//                        startActivity(intent);
//                        // Do nothing
//                        dialog.dismiss();
//                    }
//                });
//
//                AlertDialog alert = builder.create();
//                alert.setCanceledOnTouchOutside(false);
//                alert.show();
//            }
//        }, TemporaryConfiguration.roundTime);
        handler = new Handler();
        //handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(clicked) {
                    handler.postDelayed(this, TemporaryConfiguration.checkClick[levelNo]);
                    clicked=false;
                    //handler.removeCallbacksAndMessages(null);
                }
                else {
                    //handler.removeCallbacksAndMessages(null);
                    showDialog();
                }
            }
        }, TemporaryConfiguration.checkClick[levelNo]);
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Time is over");
                builder.setMessage("Do you want to play again this level?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        Intent intent = new Intent(context, PlayActivity.class);
                        intent.putExtra("levelNo", levelNo);
                       // handler.removeCallbacksAndMessages(null);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, ChoosLvlActivity.class);
                        intent.putExtra("levelNo", levelNo);
                        //handler.removeCallbacksAndMessages(null);
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

    
    private void updateTextView() {
        if(points<TemporaryConfiguration.pointsToWinLevel[levelNo]){
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(Integer.toString(points));
        }
        else{
            //start next level
            //show gratulation dialog and start new lvl after pressing "OK"
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Congratulation! You finished lvl "+levelNo)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(context, PlayActivity.class);
                            intent.putExtra("levelNo", levelNo+1);

                            //handler.removeCallbacksAndMessages(null);
                            startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    public void hitTheShape(View view) {
        shapeButton.setClickable(false);
        shapeButton.setVisibility(View.GONE);

        points+=1;
        updateTextView();
        clicked=true;

        Random r = new Random();
        int newX= r.nextInt(getScreenWidth()-shapeButton.getWidth()-20)+10;
        int newY = r.nextInt(getScreenHeight()-shapeButton.getHeight()-getNavigationBarHeight()-getStatusBarHeight()-20)+10;

        shapeButton.setX(newX);
        shapeButton.setY(newY);

        int animationCenterX = (int) (newX + size/2);
        int animationCenterY = (int) (newY + size/2);

        //Animation - rotate
        Animation an = prepareAnimation(animationCenterX, animationCenterY);
        shapeButton.startAnimation(an);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shapeButton.setVisibility(View.VISIBLE);
                shapeButton.setClickable(true);
                //handler.removeCallbacksAndMessages(null);
            }
    }, TemporaryConfiguration.nextShapeAppear[levelNo]);


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
}
