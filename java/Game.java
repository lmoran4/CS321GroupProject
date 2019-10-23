//package com.example.leila.stateplatebingo;

//NOTE: be sure to change the package to reflect yours!

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

    ArrayAdapter<String> spindapter;
    ArrayAdapter<String> adapter;

    //Spinner spin; // dropdown / dialog of selectable states

    Button selector;

    int score;

    //private AlertDialog.Builder statePicker;
    //private AlertDialog.Builder builderC;
    //AlertDialog actions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game); // links Game.java to activity_game.xml

        gl = (GridLayout) findViewById(R.id.board);

        gridStr = new String[20][3];
        stateAbbs = new HashMap<>();
        boardhelpr = new HashMap<>();

        states = new ArrayList<>();
        abbs = new ArrayList<>();
        setstates = new ArrayList<>();
        shuffleAbs = new ArrayList<>();

        selector = (Button) findViewById(R.id.selectstatebutton);

        //spin = (Spinner) findViewById(R.id.spinners);

        setupStates();

        buildGrid();


        /*statePicker = new AlertDialog.Builder(this);
        statePicker.setTitle("Select State/Province");

        statePicker.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //

            }
        });

        statePicker.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //

            }
        });

        actions = statePicker.create();*/
    }


    public void setupStates(){

       keys = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas",
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

       vals = new String[]{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE",
               "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS",
               "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO",
               "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND",
               "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX",
               "UT", "VT", "VA", "WA", "WV", "WI", "WY",
               "ON","QC","NS","NB","MB","BC","PE","SK","AB","NL"};

        //states = (ArrayList<String>) Arrays.asList(keys);
       // abbs = (ArrayList<String>) Arrays.asList(vals);

        // sets up state/abbreviation k/v pairs
        for(int i =0; i<keys.length; i++){
            stateAbbs.put(keys[i],vals[i]);
        }

        //states.addAll(Arrays.asList(vals));

        for(String str : keys){
            states.add(str);
        }

        for(String str : vals){
            abbs.add(str);
            shuffleAbs.add(str);
        }
        //TODO: MOVE SHUFFLER TO OWN METHOD FOR SAVE/LOAD/CONTINUE, OR CREATE SEPARATE METHOD TO SET UP FOR LOAD/SAVE/CONTINUE

        score = 0;

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

            }
        }
    }

    //TODO:
    //public int scoreCalculator(...){
    // ....
    // }
    public void bingoCheckr(){
    // ...

        // state: +1
        // row:   +3 bonus
        // col: + 20 bonus

        //full block/5 rows full: +10
        //block strip: 5 vertical cells full: +5

        /*

        check:

        rows     cols
        0-4       0-2
        5-9     0-2
        10-14   0-2
        15-19   0-2


        block 1:

        rows 0-4

         */


    }


    public void stateSetter(String stateStr){

        setstates.add(stateStr);
        score++;

        String stateAb = stateAbbs.get(stateStr);

        RowCol loc = boardhelpr.get(stateAb);

        int row = loc.getRow();
        int col = loc.getCol();

        cells[row][col].setBackgroundResource(R.drawable.marker);

    }

    // TODO: WIP
    // method that performs actions after user selects state
    public void selectState(View v) { //public void selectState(String stateStr)

        //TODO:
        // pop state from list of available states, rebuild spinner adapter
        // add state/abbr to lists of selected states/abbrs

        // score + 1
        // check for bingo(s), maybe merge scoreCalc with bingoCheckr?

        // Update score accordingly
        // mark state on board


        ArrayList<String> thing = new ArrayList<>();

        for(String str : states) {

            if (!setstates.contains(str)) {

                thing.add(str);
            }
        }

        final String[] sts = thing.toArray(new String[thing.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose State");

        ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.dialogcustom, R.id.dialogtext, thing);


        final int[] chosen = {0};
        final String[] statechosen = {""};

        final String[] statechosenS = {""};
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //String toaster = "Chose " + sts[which];

                String stateC = stateAbbs.get(sts[which]);

                String toaster = "Selected " + sts[which] + " " +stateC;

                chosen[0] = which;
                statechosen[0] = stateC;
                statechosenS[0] = sts[which];

                Toast.makeText(getApplicationContext(), toaster , Toast.LENGTH_SHORT).show();

            }
        });

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String stateF = statechosenS[0];

                stateSetter(stateF);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


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
