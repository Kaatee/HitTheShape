package com.example.hittheshape;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        shapeButton = (Button)findViewById(R.id.shape);
        Button shapeButton = findViewById(R.id.shape);


        //set image background
        shapeButton.setBackgroundResource(R.drawable.shape3);

        //set shape size
        int size = (int) getScreenWidth()/8; //set size as 1/8 screen width size
        shapeButton.getLayoutParams().height = size;
        shapeButton.getLayoutParams().width = size;
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

    private int convertDpToPx(float valueDp){

        float density = getResources().getDisplayMetrics().density;
        float px = valueDp * density;
        return (int)px;

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
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(Integer.toString(points));
    }
    public void hitTheShape(View view) {
        shapeButton.setClickable(false);
        shapeButton.setVisibility(View.GONE);

        points+=10;
        updateTextView();

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
    }, 500);


    }
}
