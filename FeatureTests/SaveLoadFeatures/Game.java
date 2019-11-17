package com.example.chris.stateplatebingo;//package com.example.leila.stateplatebingo;

//NOTE: be sure to change the package to reflect yours!

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Game extends Activity {

    Button save;
    private String SavedTXT = "";
    //private String toSaveTXT;
    //Scanner fileTXT = new Scanner(SavedTXT);
    String[] lines;// = myString.split(System.getProperty("line.separator"));


    private GridLayout gl; // layout for grid

    // gridStr is for future unit tests/saving/etc
    private String[][] gridStr;                      // NEED TO SAVE

    private boolean[][] cellCheck;

    private TextView[][] cells; // the grid cells


    //private String[] stateFileStrs;

    HashMap<String,String> stateFileStrs; //state, str

    String[] keys;
    String[] vals;

    ArrayList<String> states;
    ArrayList<String> abbs;

    ArrayList<String> setstates; // states chosen by user           // NEED TO SAVE
    ArrayList<String> shuffleAbs; // shuffled abbreviations

    HashMap<String, String> stateAbbs; // (state,abbreviation)       // DO NOT NEED TO SAVE
    HashMap<String, RowCol> boardhelpr; // (abbreviation, (row,col)) // NEED TO SAVE
    HashMap<String,String> AbbState;

    //private Button selector;

    private int score;         // NEED TO SAVE

    private TextView scoreView;

    boolean[] block1rows;
    boolean[] block2rows;
    boolean[] block3rows;
    boolean[] block4rows;

    boolean[] col1;
    boolean[] col2;
    boolean[] col3;

    boolean[] allCols;

    boolean[] blocks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //if a continued game, we get the .txt file and put it into a String
        boolean isNewGame = GameState.getInstance().getValue();
        if(!isNewGame){
            readFile();
            lines = SavedTXT.split(System.getProperty("line.separator"));
        }

        save = findViewById(R.id.button4);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Game.this.getFilesDir(), "text");
                if (!file.exists()) {
                    file.mkdir();
                }
                try {
                    File gpxfile = new File(file, "sample");
                    FileWriter writer = new FileWriter(gpxfile);
                    writer.write(combineStrings());
                    writer.flush();
                    writer.close();
                    Toast.makeText(Game.this, "Saved your game", Toast.LENGTH_LONG).show();
                } catch (Exception e) { }
            }
        });

        this.gl = (GridLayout) findViewById(R.id.board);

        this.gridStr = new String[20][3];
        stateAbbs = new HashMap<>();
        boardhelpr = new HashMap<>();
        stateFileStrs = new HashMap<>();
        AbbState = new HashMap<>();

        states = new ArrayList<>();
        abbs = new ArrayList<>();
        setstates = new ArrayList<>();
        shuffleAbs = new ArrayList<>();

        //this.selector = (Button) findViewById(R.id.selectstatebutton);

        this.scoreView = (TextView) findViewById(R.id.scoreview);

        setupStates();

        buildGrid();

        buildBlocks();
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



        // sets up state/abbreviation k/v pairs
        for(int i =0; i<keys.length; i++){
            stateAbbs.put(keys[i],vals[i]);
            AbbState.put(vals[i],keys[i]);
        }

        //states.addAll(Arrays.asList(vals));

        for(String str : keys){
            states.add(str);
        }
        //TODO: MOVE SHUFFLER TO OWN METHOD FOR SAVE/LOAD/CONTINUE, OR CREATE SEPARATE METHOD TO SET UP FOR LOAD/SAVE/CONTINUE

        /*score = 0;

        scoreView.setText(String.valueOf(score));*/

        // randomizes the list of state abbreviations
        //Collections.shuffle(shuffleAbs, new Random());
        boolean isNewGame = GameState.getInstance().getValue();

        if(isNewGame){
        // randomizes the list of state abbreviations
            for(String str : vals){
                shuffleAbs.add(str);
            }
        Collections.shuffle(shuffleAbs, new Random());
        }
        else{
        //read text file to assign states to correct position

            Scanner scan = new Scanner(SavedTXT);
            for(int i = 0; i < 60; i++){
                shuffleAbs.add(lines[i]);

            }
            //buildGrid();

            //buildBlocks();
        }

        score = 0;

        scoreView.setText(String.valueOf(score));
    }

    public void buildBlocks(){

        allCols = new boolean[] {false,false,false};
        blocks = new boolean[] {false,false,false,false};

        col1 = new boolean[20];
        col2 = new boolean[20];
        col3 = new boolean[20];

        int counter = 0;

        for(int i = 0; i<20; i++){
            col1[i] = false;
            col2[i] = false;
            col3[i] = false;
        }

        block1rows = new boolean[5];
        block2rows = new boolean[5];
        block3rows = new boolean[5];
        block4rows = new boolean[5];


        for(int i = 0; i<5; i++) {
            block1rows[i] = false;
            block2rows[i] = false;
            block3rows[i] = false;
            block4rows[i] = false;
        }
    }

    // FIX FOR LOADING

    public void buildGrid(){

        cells = new TextView[20][3];
        cellCheck = new boolean[20][3];

        // scales cells with screen size
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (75 * scale + 0.5f);

        int counter = 0; // tracks where we are in the list of shuffled states.

        for(int i = 0; i<20; i++){
            for(int j=0; j<3; j++){

                cells[i][j] = new TextView(this);
                cells[i][j].setBackgroundResource(R.drawable.gridbutton);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cells[i][j].setForeground(getDrawable(R.drawable.gridbutton));
                }

                String stateA = shuffleAbs.get(counter);

                cells[i][j].setText(shuffleAbs.get(counter));

                String stateName = AbbState.get(stateA);

                gridStr[i][j] = stateA;

                //cellCheck[i][j] = false;

                RowCol thingy = new RowCol(i,j,stateName,stateA);
                //thingy.setMarked(false);
                boolean isNewGame = GameState.getInstance().getValue();

                if(isNewGame) {
                    cellCheck[i][j] = false;
                    thingy.setMarked(false);
                }
                else{
                    //read .txt file to get marked state.
                    String boolState;
                    boolState = lines[counter+60];
                    if(boolState.equals("true")){
                        cellCheck[i][j] = true;
                        thingy.setMarked(true);
                    }
                    else if(boolState.equals("false")){
                        cellCheck[i][j] = false;
                        thingy.setMarked(false);
                    }

                }

                boardhelpr.put(shuffleAbs.get(counter), thingy); // adds (stateAbbr, (row,col)) pair to hashmap

                cells[i][j].setGravity(Gravity.CENTER); //centers text in cell

                gl.addView(cells[i][j], pixels,pixels); // adds cell to grid with scaling

                counter++;

            }
        }
    }

    public void bingoCheckr(String stateStr){

        String stateAb = stateAbbs.get(stateStr);

        RowCol loc = boardhelpr.get(stateAb);

        int row = loc.getRow();
        int col = loc.getCol();
        //String name = loc.getName();

        colCheckr(row,col);

        stripCheckr(row,col);

        rowCheckr(row,col);

        scoreView.setText(String.valueOf(score));


    }


    public void stateSetter(String stateStr){

        setstates.add(stateStr);
        score++;

        String stateAb = stateAbbs.get(stateStr);

        boardhelpr.get(stateAb).setMarked(true);

        RowCol loc = boardhelpr.get(stateAb);

        int row = loc.getRow();
        int col = loc.getCol();

        cellCheck[row][col] = true;

        if(col == 0){ col1[row] = true; }
        if (col==1){ col2[row] = true; }
        if(col==2){ col3[row] = true; }

        if(row<5)                       { cells[row][col].setBackgroundResource(R.drawable.marker); } //red
        else if(row < 10 && row >=5)    { cells[row][col].setBackgroundResource(R.drawable.marker2); }//blue
        else if(row < 15 && row >= 10)  { cells[row][col].setBackgroundResource(R.drawable.marker3); }//green/yellow
        else  if(row < 20 && row >= 15) { cells[row][col].setBackgroundResource(R.drawable.marker4); }// orange
        else                            { cells[row][col].setBackgroundResource(R.drawable.errormark); }//purple

        bingoCheckr(stateStr);
    }

    public void colCheckr(int row, int col){

        boolean colChanged = false;

        if(col == 0){

            col1[row] = true;

            boolean allTrue=true;
            for(boolean b : col1){
                if (!b){
                    allTrue=false;
                    break;
                }
            }

            if(allTrue){
                allCols[col] = true;
                colChanged =true;
                score=score+20;
                //CHECK ALL COLS
            }

        }

        if (col==1){

            col2[row] = true;

            boolean allTrue=true;
            for(boolean b : col2){
                if (!b){
                    allTrue=false;
                    break;
                }
            }

            if(allTrue){
                allCols[col] = true;
                colChanged =true;
                score=score+20;
                //CHECK ALL COLS
            }
        }


        if(col==2){

            col3[row] = true;

            boolean allTrue=true;
            for(boolean b : col3){
                if (!b){
                    allTrue=false;
                    break;
                }
            }

            if(allTrue){
                allCols[col] = true;
                colChanged =true;
                score=score+20;
                //CHECK ALL COLS
            }
        }

        // board is complete!
        if(allCols[0] && allCols[1] && allCols[2]) {

            score = score + 60;
        }
    }

    // checks the strip the state is in
    public void stripCheckr(int row, int col) {

        if (row < 5) {

            boolean stripTrue = true;

            for (int i = 0; i < 5; i++) {

                if (!cellCheck[i][col]) {
                    stripTrue = false;
                    break;
                }

            }

            if (stripTrue) {
                score = score + 5;
            }

        }

        if(row < 10 && row >=5) {

            boolean stripTrue = true;

            for (int i = 5; i < 10; i++) {

                if (!cellCheck[i][col]) {
                    stripTrue = false;
                    break;
                }

            }

            if (stripTrue) {
                score = score + 5;
            }
        }


        if(row < 15 && row >= 10) {

            boolean stripTrue = true;

            for (int i = 10; i < 15; i++) {

                if (!cellCheck[i][col]) {
                    stripTrue = false;
                    break;
                }

            }

            if (stripTrue) {
                score = score + 5;
            }
        }

        if(row < 20 && row >= 15) {

            boolean stripTrue = true;

            for (int i = 15; i < 20; i++) {

                if (!cellCheck[i][col]) {
                    stripTrue = false;
                    break;
                }

            }

            if (stripTrue) {
                score = score + 5;
            }
        }

    }

    // checks all rows and blocks
    public void rowCheckr(int row, int col){

        if(row < 5) {

            boolean allTrue = true;

            for (int j = 0; j < 3; j++) {

                if (!cellCheck[row][j]) {
                    allTrue = false;
                    break;
                }

            }

            if (allTrue) {
                score = score + 3;
                block1rows[row] = true;

                boolean allRows=true;
                for(boolean b : block1rows){
                    if(!b){
                        allRows=false;
                        break;
                    }
                }

                if(allRows){
                    score = score + 15;
                }
            }
        }

        if(row < 10 && row >=5) {

            boolean allTrue = true;

            for (int j = 0; j < 3; j++) {

                if (!cellCheck[row][j]) {
                    allTrue = false;
                    break;
                }

            }

            if (allTrue) {
                score = score + 3;
                block2rows[row-5] = true;

                boolean allRows=true;
                for(boolean b : block2rows){
                    if(!b){
                        allRows=false;
                        break;
                    }
                }

                if(allRows){
                    score = score + 15;
                }
            }
        }


        if(row < 15 && row >= 10) {

            boolean allTrue = true;

            for (int j = 0; j < 3; j++) {

                if (!cellCheck[row][j]) {
                    allTrue = false;
                    break;
                }

            }

            if (allTrue) {
                score = score + 3;
                block3rows[row-10] = true;


                boolean allRows=true;
                for(boolean b : block3rows){
                    if(!b){
                        allRows=false;
                        break;
                    }
                }

                if(allRows){
                    score = score + 15;
                }
            }
        }


        if(row < 20 && row >= 15) {

            boolean allTrue = true;

            for (int j = 0; j < 3; j++) {

                if (!cellCheck[row][j]) {
                    allTrue = false;
                    break;
                }

            }

            if (allTrue) {
                score = score + 3;
                block4rows[row-15] = true;


                boolean allRows=true;
                for(boolean b : block4rows){
                    if(!b){
                        allRows=false;
                        break;
                    }
                }

                if(allRows){
                    score = score + 15;
                }
            }
        }

    }


    // TODO: WIP
    // method that performs actions after user selects state
    public void selectState(View v) { //public void selectState(String stateStr)

        //TODO:
        // pop state from list of available states, rebuild adapter
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

    public void readFile() {

        /*File file = new File("C:\\Users\\littl\\Desktop\\file.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine())
            SavedTXT = sc.nextLine();*/

        File fileEvents = new File(Game.this.getFilesDir()+"/text/sample");
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) { }
        String result = text.toString();
        SavedTXT = result.replace("\r", "");
    }

    public String combineStrings(){
        String states = getAbsOrder();
        String marks = getMarkedStates();
        String concanted = states.concat(marks);

        return concanted;
    }

    public String getAbsOrder(){
        StringBuilder build = new StringBuilder();
        String toSaveTXT = "";
        for(int i=0; i < keys.length; i++)
        {
            build.append(shuffleAbs.get(i));
            build.append('\n');
        }
        toSaveTXT = build.toString();

        return toSaveTXT;
    }

    public String getMarkedStates(){
        StringBuilder build = new StringBuilder();
        String toSaveTXT = "";
        //int counter = 0;
        boolean isMarked;

        //RowCol rc;
        for(int i = 0; i<20; i++) {
            for (int j = 0; j < 3; j++) {
                //rc = boardhelpr.get(counter);
                //isMarked = rc.marked;
                isMarked = cellCheck[i][j];

                if(isMarked){
                    build.append("true");
                    build.append('\n');
                }
                else if(!isMarked){
                    build.append("false");
                    build.append('\n');
                }
            }
        }
        toSaveTXT = build.toString();

        return toSaveTXT;
    }

    //TODO
    public void saveGame(){

        // CATCH EXCEPTION: when there isn't enough space,
        // toast: sum ting wong with saving bro


    }

    //TODO
    public void loadGame(){

        // IF THERE'S A CONTINUE, load it up!
        // If not, or something weird happens, catch exception, toast, and start new game.

    }



    // onClick for "Menu" button; goes to menu
    //TODO: modify for save/continue support
    public void gotoSplash(View v){

        Intent intent = new Intent(this, Splash.class);

        intent.putExtra("CALLER", "Game");

        this.startActivity(intent);
    }


    public void gotoHelp(View v){

        Intent intent = new Intent(this, HowTo.class);

        String caller = "Game";
        intent.putExtra("CALLER", caller);

        startActivity(intent);

        //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
       // startActivityIfNeeded(intent, 0);
    }


    //Row/Col for cells
    public class RowCol {

        int row;
        int col;

        String name;
        String abbr;
        boolean marked;

        //boolean isMarked

        public RowCol(int row, int col, String name, String abbr){
            this.row=row;
            this.col=col;
            this.name=name;
            this.abbr=abbr;
            this.marked=false;
        }

        public String getName() {
            return this.name;
        }

        public int getCol() {
            return this.col;
        }

        public int getRow() {
            return this.row;
        }

        public void setMarked(boolean marked) {
            this.marked=marked;
        }

        public boolean isMarked(){
            return this.marked;
        }

        @Override
        public String toString() {

            String thing = this.name + " " + this.abbr + " " + this.row + " " + this.col + " " + this.marked;

            // split by whitespace. NAME, ABBR, ROW, COL, MARKED
            // parse...

            return thing;
        }
    }


}