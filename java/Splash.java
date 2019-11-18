package com.example.leila.stateplatebingo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;

//import androidx.appcompat.app.AppCompatActivity;


/*
replace android.support.v7... with the commented import if you're having issues.
 */

public class Splash extends AppCompatActivity {

    String calledby;

    //Button trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //trips = (Button) findViewById(R.id.trips);
        //trips.setEnabled(false);

                /*
    NOTE:

    if there's an existing continue, POPUP:
            "Starting a new game will make your current game unplayable. Continue?"
            YES:
                    NEW POPUP:
                    "Save previous game to scoreboard?"

                    YES: old continue saved to "saved games", "continue" wiped, new game started
                    NO: "continue" wiped, new game started

            NO: nothing happens


     */

        this.calledby="";

        try{

            Intent intent = getIntent();

            calledby = intent.getStringExtra("CALLER");

        } catch (Exception e){}


    }

    public void savedTrips(View v){

        Toast.makeText(getApplicationContext(), "Feature: Scoreboard/saved trips. Coming soon!" , Toast.LENGTH_SHORT).show();
    }


    //CONTINUE button. Only works if user goes from game to menu--allows user to resume current game.
    // Does NOT work if user closes the app.
    public void resumeGame(View v){

        try {

            if(calledby==null){

                String thing = Environment.getExternalStorageDirectory().getAbsolutePath()+"/StatePlateBingo";
                File file = new File(thing, "continue.txt");

                if (!file.exists()) {
                    Toast.makeText(getApplicationContext(), "No save found!" , Toast.LENGTH_SHORT).show();
                }

                else {
                    Intent intent = new Intent(this, Game.class);

                    String cont = "yes";
                    intent.putExtra("CONT", cont);

                    this.startActivity(intent);

                }

            }

            else if (calledby.equals("Game")) {

                Intent intent = new Intent(this, Game.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);

            }



        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Feature: Resume game after closing app. Coming soon!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    public void gotoGame(View v){

        Intent intent = new Intent(this, Game.class);

        this.startActivity(intent);

    }

    public void goToHowTo(View v){

        Intent intent = new Intent(this, HowTo.class);

        String caller = "Splash";
        intent.putExtra("CALLER", caller);

        this.startActivity(intent);
    }

}




