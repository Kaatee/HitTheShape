package com.example.hittheshape;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        shapeButton = (Button)findViewById(R.id.shape);
        Button shapeButton = findViewById(R.id.shape);

        if(getIntent()!=null){
            levelNo=getIntent().getIntExtra("levelNo", 0);
            Toast.makeText(this, "Level No: " +Integer.toString(levelNo), Toast.LENGTH_SHORT).show();
        }

        //set image background
        shapeButton.setBackgroundResource(R.drawable.shape3);

        //set shape size
        int size = (int) getScreenWidth()/TemporaryConfiguration.shapeSize; //set size as 1/8 screen width size
        shapeButton.getLayoutParams().height = size;
        shapeButton.getLayoutParams().width = size;

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
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(clicked) {
                    handler.postDelayed(this, TemporaryConfiguration.checkClick[levelNo]);
                    clicked=false;
                }
                else {
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

    
    private void updateTextView() {
        if(points<TemporaryConfiguration.pointsToWinLevel[levelNo]){
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(Integer.toString(points));
        }
        else{
            //start next level
            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra("levelNo", levelNo+1);
            startActivity(intent);
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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shapeButton.setVisibility(View.VISIBLE);
                shapeButton.setClickable(true);
            }
    }, TemporaryConfiguration.nextShapeAppear[levelNo]);


    }
}
