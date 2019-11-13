package com.example.stateplatebingo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.stateplatebingo.Game;

public class Splash extends AppCompatActivity {

    String calledby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        calledby="";

        try{

            Intent intent = getIntent();

            calledby = intent.getStringExtra("CALLER");

        } catch (Exception e){}


    }

    public void resumeGame(View v){

        try {

            if (calledby.equals("Game")) {

                Intent intent = new Intent(this, Game.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);

            }
        }catch (Exception e){ }

    }


    public void gotoGame(View v){

        //if(calledby.isEmpty()){

            Intent intent = new Intent(this, Game.class);

            this.startActivity(intent);
       // }



      //  else { }
    }

    public void goToHowTo(View v){

        Intent intent = new Intent(this, HowTo.class);

        String caller = "Splash";
        intent.putExtra("CALLER", caller);

        this.startActivity(intent);
    }

}
