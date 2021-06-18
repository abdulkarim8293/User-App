package com.abdulkarim.userapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.varunest.sparkbutton.SparkButton;

public class ConfirmationActivity extends AppCompatActivity {

    private SparkButton sparkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        sparkButton = findViewById(R.id.spark_button);
        sparkButton.setChecked(true);
        sparkButton.playAnimation();


        sparkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (sparkButton.isChecked()){
                    sparkButton.setChecked(true);

                }*/

            }
        });

        findViewById(R.id.button_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfirmationActivity.this,MainActivity.class));
                finish();
            }
        });
    }
}