//package com.example.leila.stateplatebingo;

//NOTE: be sure to change the package to reflect yours!

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Game extends Activity {

    private GridLayout gl; // layout for grid

    // gridStr is for future unit tests
    private String[][] gridStr;

    private TextView[][] cells; // the grid cells

    String[] keys;
    String[] vals;

    ArrayList<String> states;
    ArrayList<String> abbs;

    ArrayList<String> setstates; // states chosen by user

    ArrayList<String> shuffleAbs; // shuffled abbreviations

    HashMap<String, String> stateAbbs; // (state,abbreviation)


    HashMap<String, RowCol> boardhelpr; // (abbreviation, (row,col))

    //ArrayAdapter<String> spindapter;
    //ArrayAdapter<String> adapter;

    Spinner spin; // dropdown / dialog of selectable states

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game); // links Game.java to activity_game.xml

        gl = (GridLayout) findViewById(R.id.board);

        gridStr = new String[20][3];
        stateAbbs = new HashMap<>();

        states = new ArrayList<>();
        abbs = new ArrayList<>();

        spin = (Spinner) findViewById(R.id.spinners);

        setupStates();

        buildGrid();
    }

    
    public void setupStates(){

       vals = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas",
               "California", "Colorado", "Connecticut", "Delaware",
               "Florida", "Georgia","Hawaii", "Idaho", "Illinois", "Indiana", "Iowa",
               "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan",
               "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada",
               "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota",
               "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
               "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah",
               "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming",
               "Ontario","Quebec", "Nova Scotia", "New Brunswick", "Manitoba", "British Columbia",
               "Prince Edward Island", "Saskatchewan", "Alberta", "Newfoundland & Labrador"};

       keys = new String[]{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE",
               "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS",
               "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO",
               "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND",
               "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX",
               "UT", "VT", "VA", "WA", "WV", "WI", "WY",
               "ON","QC","NS","NB","MB","BC","PE","SK","AB","NL"};

        //states = (ArrayList<String>) Arrays.asList(keys);
       // abbs = (ArrayList<String>) Arrays.asList(vals);

        // sets up abbreviation/state k/v pairs
        for(int i =0; i<keys.length; i++){
            stateAbbs.put(keys[i],vals[i]);
        }

        //states.addAll(Arrays.asList(vals));

        for(String str : vals){
            states.add(str);
        }

        for(String str : keys){
            abbs.add(str);
            shuffleAbs.add(str);
        }
        //TODO: MOVE SHUFFLER TO OWN METHOD FOR SAVE/LOAD/CONTINUE, OR CREATE SEPARATE METHOD TO SET UP FOR LOAD/SAVE/CONTINUE


        // randomizes the list of state abbreviations
        Collections.shuffle(shuffleAbs, new Random());
    }


    public void buildGrid(){

        cells = new TextView[20][3];

        // scales cells with screen size
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (75 * scale + 0.5f);

        int counter = 0; // tracks where we are in the list of shuffled states.

        for(int i = 0; i<20; i++){
            for(int j=0; j<3; j++){

                cells[i][j] = new TextView(this);
                cells[i][j].setBackgroundResource(R.drawable.gridbutton);
                cells[i][j].setForeground(getDrawable(R.drawable.gridbutton));

              //  cells[i][j].setText("");
                cells[i][j].setText(shuffleAbs.get(counter));

                gridStr[i][j] = shuffleAbs.get(counter);

                RowCol thingy = new RowCol(i,j);

                boardhelpr.put(shuffleAbs.get(counter), thingy); // adds (stateAbbr, (row,col)) pair to hashmap

                cells[i][j].setGravity(Gravity.CENTER); //centers text in cell

                gl.addView(cells[i][j], pixels,pixels); // adds cell to grid with scaling

                counter++;

                //TextView tv = new TextView(this);
                /*tv.setBackgroundResource(R.drawable.gridbutton);
                tv.setForeground(getDrawable(R.drawable.gridbutton));
                tv.setText("VA");
                tv.setGravity(Gravity.CENTER);
                gl.addView(tv,pixels,pixels);*/

            }
        }
    }

    //TODO:
    //public int scoreCalculator(...){
    // ....
    // }
    //public void bingoCheckr(...){
    // ...
    // }

    // TODO: WIP
    // method that performs actions after user selects state
    public void selectState() { //public void selectState(String stateStr)

        //TODO:
        // pop state from list of available states, rebuild spinner adapter
        // add state/abbr to lists of selected states/abbrs

        // score + 1
        // check for bingo(s), maybe merge scoreCalc with bingoCheckr?

        // Update score accordingly
        // mark state on board


    }


    public void onClickState(View v) {

        //IMPLEMENT SPINNER HERE
        // user selects state, state is passed to selectState

        selectState(); // selectState(state selected from dialog/spinner)

    }



    // onClick for "Menu" button; goes to menu
    //TODO: modify for save/continue support
    public void gotoSplash(View v){

        Intent intent = new Intent(this, Splash.class);

        this.startActivity(intent);
    }



    //Row/Col for cells
    public class RowCol {

        int row;
        int col;

        public RowCol(int row, int col){
            this.row=row;
            this.col=col;
        }

        public int getCol() {
            return this.col;
        }

        public int getRow() {
            return this.row;
        }
    }


}
