package com.example.leila.stateplatebingo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }


    public void gotoGame(View v){

        Intent intent = new Intent(this, Game.class);

        this.startActivity(intent);
    }

}
