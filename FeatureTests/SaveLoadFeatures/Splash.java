package com.example.chris.stateplatebingo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.chris.stateplatebingo.Game;

public class Splash extends AppCompatActivity {

    boolean gs;
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
        Intent intent = new Intent(this, Game.class);

        GameState.getInstance().setValue(false);

        this.startActivity(intent);

    }


    public void gotoGame(View v){

        //if(calledby.isEmpty()){

            Intent intent = new Intent(this, Game.class);

            GameState.getInstance().setValue(true);

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
