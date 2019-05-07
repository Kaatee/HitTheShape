package com.example.hittheshape;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity {

    private Button shapeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        shapeButton = (Button)findViewById(R.id.shape);
    }



    public void hitTheShape(View view) {
        Toast.makeText(this, "Shape pressed", Toast.LENGTH_LONG).show();
        shapeButton.setClickable(false);
        shapeButton.setVisibility(View.GONE);

        float buttonX=shapeButton.getX()+50;
        float buttonY=shapeButton.getY()+50;
        shapeButton.setX(buttonX);
        shapeButton.setY(buttonY);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                shapeButton.setVisibility(View.VISIBLE);
                shapeButton.setClickable(true);
            }
    }, 2000);


    }
}
