package com.example.hittheshape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class ChoosLvlActivity extends AppCompatActivity {
    private GridView levelsGidView;
    private int levelNo;
    private int lives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choos_lvl);

        if(getIntent()!=null){
            levelNo=getIntent().getIntExtra("levelNo", 0);
            lives =getIntent().getIntExtra("lives", 3);

            //Toast.makeText(this, Integer.toString(levelNo), Toast.LENGTH_LONG).show();
        }

        levelsGidView = findViewById(R.id.gridViewLevels);

        LevelsGridAdapter adapter = new LevelsGridAdapter(ChoosLvlActivity.this,levelNo);
        levelsGidView.setAdapter(adapter);


        levelsGidView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "You choosed "+position+ ". lvl", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(view.getContext(), PlayActivity.class);
                intent.putExtra("levelNo", position+1);
                intent.putExtra("lives", lives);
                startActivity(intent);
            }
        });
    }
}
