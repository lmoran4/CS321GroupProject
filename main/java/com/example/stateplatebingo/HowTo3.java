package com.example.stateplatebingo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HowTo3 extends AppCompatActivity {

    String calledby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_3);

        Intent intent = getIntent();

        calledby = intent.getStringExtra("CALLER");
    }

    public void goToPrevPage(View v){
        Intent intent = new Intent(this, HowTo2.class);
        intent.putExtra("CALLER", calledby);

        this.startActivity(intent);
    }


    public void returnTo(View v){

        if(calledby.equals("Game")){

            Intent intent = new Intent(this, Game.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(intent, 0);

        }

        else{

            Intent intent = new Intent(this, Splash.class);

            //intent.putExtra("CALLER", calledby);

            this.startActivity(intent);



        }




    }
}