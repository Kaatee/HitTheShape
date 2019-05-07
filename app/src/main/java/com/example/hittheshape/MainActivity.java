package com.example.hittheshape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button buttonPlay;
    private Button buttonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlay = findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                openChooseLvLActivity();
            }
        });


        buttonSettings = findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });
    }

    public void openChooseLvLActivity(){
        Intent intent = new Intent(this, ChoosLvlActivity.class);
        startActivity(intent);
    }

    public void openSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


}
