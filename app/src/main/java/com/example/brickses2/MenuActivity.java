package com.example.brickses2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);
    }

    public void ButtonStartClick(View view)
    {
        Intent myIntent = new Intent(MenuActivity.this, MainActivity.class);

        MenuActivity.this.startActivity(myIntent);
    }
}
