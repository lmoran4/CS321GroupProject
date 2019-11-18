package com.example.leila.stateplatebingo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class Game extends Activity {

    String hasCont;

    private GridLayout gl; // layout for grid

    // gridStr is for future unit tests/saving/etc
    private String[][] gridStr;

    StateObject[][] cellar;

    private boolean[][] cellCheck;

    private TextView[][] cells; // the grid cells

    ArrayList<String> states;

    ArrayList<String> setstates; // states chosen by user
    ArrayList<String> shuffleAbs; // shuffled abbreviations

    HashMap<String, String> AbbState; // (abbreviation,state)

    HashMap<String, String> stateAbbs; // (state,abbreviation)
    HashMap<String, StateObject> boardhelpr; // (abbreviation, (row,col))

    private Button selector;

    private int score;

    private TextView scoreView;

    boolean[] block1rows;
    boolean[] block2rows;
    boolean[] block3rows;
    boolean[] block4rows;

    boolean[] col1;
    boolean[] col2;
    boolean[] col3;

    boolean[] allCols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game); // links Game.java to activity_game.xml

        hasCont="";

        this.gl = (GridLayout) findViewById(R.id.board);

        this.gridStr = new String[20][3];
        stateAbbs = new HashMap<>();
        boardhelpr = new HashMap<>();
        AbbState = new HashMap<>();
        // stateFileStrs = new HashMap<>();

        cells = new TextView[20][3];
        cellCheck = new boolean[20][3];

        cellar = new StateObject[20][3]; // arrays of StateObjects corresponding to grid position

        states = new ArrayList<>();
        // abbs = new ArrayList<>();
        setstates = new ArrayList<>();
        shuffleAbs = new ArrayList<>();

        this.selector = (Button) findViewById(R.id.selectstatebutton);

        this.scoreView = (TextView) findViewById(R.id.scoreview);

        Intent intent = getIntent();

        hasCont = intent.getStringExtra("CONT");

        if(hasCont==null){
            newGame();
        }
        else if(hasCont.equals("yes")){
            loadGame();
        }

    }

    private void newGame(){
        setupStates();
        buildGrid();
        buildBlocks();

    }

    private void loadGame(){

        try {

            String thing = Environment.getExternalStorageDirectory().getAbsolutePath() + "/StatePlateBingo";
            File file = new File(thing, "continue.txt");

            if (!file.exists()) {
                Toast.makeText(getApplicationContext(), "No save found! Launching new game.", Toast.LENGTH_SHORT).show();

                newGame();
            }

            if (file.exists()) {

                if (file.length() == 0) {
                    newGame();
                }


                stateZipper();
                buildBlocks();
                gameReader();
                buildGridLoader();

            }
        }catch (Exception e){
            //e.printStackTrace();
        }

    }

    private void stateZipper(){
        String[] keys = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas",
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

        String[] vals = new String[]{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE",
                "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS",
                "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO",
                "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND",
                "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX",
                "UT", "VT", "VA", "WA", "WV", "WI", "WY",
                "ON","QC","NS","NB","MB","BC","PE","SK","AB","NL"};

        // sets up state/abbreviation k/v pairs,
        // abb/state
        for(int i =0; i<keys.length; i++){
            stateAbbs.put(keys[i],vals[i]);
            AbbState.put(vals[i],keys[i]);
        }

        for(String str : keys){
            states.add(str);
        }

    }


    private void setupStates(){

        String[] keys = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas",
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

        String[] vals = new String[]{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE",
                "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS",
                "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO",
                "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND",
                "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX",
                "UT", "VT", "VA", "WA", "WV", "WI", "WY",
                "ON","QC","NS","NB","MB","BC","PE","SK","AB","NL"};

        // sets up state/abbreviation k/v pairs,
        // abb/state
        for(int i =0; i<keys.length; i++){
            stateAbbs.put(keys[i],vals[i]);
            AbbState.put(vals[i],keys[i]);
        }


        for(String str : keys){
            states.add(str);
        }

        for(String str : vals){
            // abbs.add(str);
            shuffleAbs.add(str);
        }

        score = 0;

        scoreView.setText(String.valueOf(score));

        // randomizes the list of state abbreviations
        Collections.shuffle(shuffleAbs, new Random());
    }


    private void buildBlocks(){

        allCols = new boolean[] {false,false,false};

        col1 = new boolean[20];
        col2 = new boolean[20];
        col3 = new boolean[20];

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


    private void buildGrid(){

        // scales cells with screen size
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (75 * scale + 0.5f);

        int counter = 0; // tracks where we are in the list of shuffled states.

        for(int i = 0; i<20; i++){
            for(int j=0; j<3; j++){

                cells[i][j] = new TextView(this);
                cells[i][j].setBackgroundResource(R.drawable.gridbutton);
                cells[i][j].setForeground(getDrawable(R.drawable.gridbutton));

                String stateA = shuffleAbs.get(counter);

                cells[i][j].setText(shuffleAbs.get(counter));

                String stateName = AbbState.get(stateA);

                gridStr[i][j] = stateA;

                cellCheck[i][j] = false;

                StateObject thingy = new StateObject(i,j,stateName,stateA);
                thingy.setMarked(false);

                cellar[i][j] = thingy;

                boardhelpr.put(shuffleAbs.get(counter), thingy); // adds (stateAbbr, (row,col)) pair to hashmap

                cells[i][j].setGravity(Gravity.CENTER); //centers text in cell

                gl.addView(cells[i][j], pixels,pixels); // adds cell to grid with scaling

                counter++;

            }
        }
    }


    private void bingoCheckr(String stateStr){

        String stateAb = stateAbbs.get(stateStr);

        StateObject loc = boardhelpr.get(stateAb);

        int row = loc.getRow();
        int col = loc.getCol();
        colCheckr(row,col);

        stripCheckr(row,col);

        rowCheckr(row,col);

        scoreView.setText(String.valueOf(score));

    }


    // DO NOT CALL WHEN LOADING. Only call on NEWLY ADDED STATES.
    public void stateSetter(String stateStr){

        setstates.add(stateStr);
        score++;

        if(setstates.size()==60){
            selector.setEnabled(false);
        }


        String stateAb = stateAbbs.get(stateStr);

        boardhelpr.get(stateAb).setMarked(true);

        StateObject loc = boardhelpr.get(stateAb);

        int row = loc.getRow();
        int col = loc.getCol();

        cellar[row][col] = loc; // get the state at row/col

        cellCheck[row][col] = true;

        if(col == 0){
            col1[row] = true;
        }
        if (col==1){
            col2[row] = true;
        }
        if(col==2){
            col3[row] = true;
        }


        if(row<5){
            cells[row][col].setBackgroundResource(R.drawable.marker); //red
        }
        else if(row < 10 && row >=5) {
            cells[row][col].setBackgroundResource(R.drawable.marker2); //blue
        }

        else if(row < 15 && row >= 10) {
            cells[row][col].setBackgroundResource(R.drawable.marker3); //green/yellow
        }
        else  if(row < 20 && row >= 15) {
            cells[row][col].setBackgroundResource(R.drawable.marker4); // orange
        }
        else {
            cells[row][col].setBackgroundResource(R.drawable.errormark); //purple
        }

        bingoCheckr(stateStr);

    }





    private void colCheckr(int row, int col){

        // boolean colChanged = false;

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
                //colChanged =true;
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
                //colChanged =true;
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
                //colChanged =true;
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
    private void stripCheckr(int row, int col) {


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
    private void rowCheckr(int row, int col){

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



    // method that performs actions after user selects state
    public void selectState(View v) { //public void selectState(String stateStr)

        // pop state from list of available states, rebuild adapter
        // add state/abbr to lists of selected states/abbrs
        // check for bingo(s)
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

                if(!stateF.isEmpty()) {
                    stateSetter(stateF);
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void saveGameButton(View v){
        saveGame();
    }


    public void saveGame(){

        List<String> lines = new ArrayList<>();

        String scorestr = ""+ this.score + "\n";

        String sb = scorestr;

        lines.add(scorestr);

        for(StateObject[] ar : cellar){
            for(StateObject rc : ar){
                lines.add(rc.toString());

                sb = sb + rc.toString();

            }
        }

        try {

            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/StatePlateBingo");

            if(!dir.exists()){
                dir.mkdirs();
            }

            File file = new File(dir, "continue.txt");




            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream stream = new FileOutputStream(file);

            stream.write(sb.getBytes());

            stream.close();

            Toast.makeText(getApplicationContext(), "Saved!" , Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            //e.printStackTrace();
        }
    }


    public void quitGame(View v){

        Toast.makeText(getApplicationContext(), "This is a quit without saving feature. Coming soon!" , Toast.LENGTH_SHORT).show();
    }

    //TODO
    public void gameReader(){

        // IF THERE'S A CONTINUE, load it up!
        // If not, or something weird happens, catch exception, toast, and start new game.

        // FOR EACH STRING IN FILE...

        // State,Abb,Row,Col,marked

        ArrayList<String> loadedStrs = new ArrayList<>();

        // FIRST LINE IN ARRAY IS SCORE.

        try {

            String thing = Environment.getExternalStorageDirectory().getAbsolutePath()+"/StatePlateBingo";
            File file = new File(thing, "continue.txt");

            if (!file.exists()) {
                Toast.makeText(getApplicationContext(), "No save found! Launching new game." , Toast.LENGTH_SHORT).show();

                newGame();
            }

            if (file.exists()) {

                FileInputStream input = new FileInputStream(file);
                InputStreamReader rdr = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(rdr);
                String line=br.readLine();
                while (line != null) {

                    //System.out.println(line);

                    loadedStrs.add(line);

                    line = br.readLine();

                }

                br.close();

                loadHandler(loadedStrs);
            }


        }catch(Exception e){
            //e.printStackTrace();
        }


    }



    private void buildGridLoader(){

        //cells = new TextView[20][3];
        //cellCheck = new boolean[20][3];

        // cellar = new StateObject[20][3];

        // scales cells with screen size
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (75 * scale + 0.5f);

        for(int i = 0; i<20; i++){
            for(int j=0; j<3; j++){

                cells[i][j] = new TextView(this);
                cells[i][j].setBackgroundResource(R.drawable.gridbutton);
                cells[i][j].setForeground(getDrawable(R.drawable.gridbutton));

                StateObject ob = cellar[i][j];

                String ab = ob.getAbbr();
                cells[i][j].setText(ab);
                cells[i][j].setGravity(Gravity.CENTER); //centers text in cell

                gridStr[i][j] = ob.getName();
                cellCheck[i][j] = ob.isMarked();

                if(ob.isMarked()){
                    stateMarker(i,j);
                    rowCheckLoad(i,j);
                    colCheckLoad(i,j);
                }


                gl.addView(cells[i][j], pixels,pixels); // adds cell to grid with scaling

            }
        }
    }




    public void loadHandler(ArrayList<String> loadedStrs){

        ArrayList<String> ls = loadedStrs;

        String scorestr = ls.get(0).replaceAll("[^0-9]", "");

        score = Integer.parseInt(scorestr);

        scoreView.setText(String.valueOf(score));

        for(int i = 1; i<ls.size(); i++){

            String thing = ls.get(i);

            String[] sa = thing.split(",");

            String name = sa[0];
            String ab = sa[1];

            //shuffled.add(ab);

            int r = Integer.parseInt(sa[2]);
            int c = Integer.parseInt(sa[3]);

            boolean m = false;

            if(sa[4].equals("true")){
                setstates.add(name);
                m = true;
            }

            StateObject nuOb = new StateObject(r,c,name,ab);
            nuOb.setMarked(m);

            boardhelpr.put(ab, nuOb);

            cellar[r][c] = nuOb;

        }

    }


    public void stateMarker(int row, int col){


        if(setstates.size()==60){
            selector.setEnabled(false);
        }


        cellCheck[row][col] = true;

        if(col == 0){
            col1[row] = true;
        }
        if (col==1){
            col2[row] = true;
        }
        if(col==2){
            col3[row] = true;
        }


        if(row<5){
            cells[row][col].setBackgroundResource(R.drawable.marker); //red
        }
        else if(row < 10 && row >=5) {
            cells[row][col].setBackgroundResource(R.drawable.marker2); //blue
        }

        else if(row < 15 && row >= 10) {
            cells[row][col].setBackgroundResource(R.drawable.marker3); //green/yellow
        }
        else  if(row < 20 && row >= 15) {
            cells[row][col].setBackgroundResource(R.drawable.marker4); // orange
        }
        else {
            cells[row][col].setBackgroundResource(R.drawable.errormark); //purple
        }

    }


    // checks all rows and blocks. DOES NOT UPDATE SCORE.
    private void rowCheckLoad(int row, int col){

        if(row < 5) {
            boolean allTrue = true;
            for (int j = 0; j < 3; j++) {
                if (!cellCheck[row][j]) {
                    allTrue = false;
                    break;
                }
            }

            if (allTrue) {
                block1rows[row] = true;

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
                block2rows[row-5] = true;
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
                block3rows[row-10] = true;
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
                block4rows[row-15] = true;
            }
        }
    }

    // marks cols, DOES NOT UPDATE SCORE.
    private void colCheckLoad(int row, int col){

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

            }

        }

        // board is complete!
        if(allCols[0] && allCols[1] && allCols[2]) { }

    }



    // onClick for "Menu" button; goes to menu
    public void gotoSplash(View v){

        Intent intent = new Intent(this, Splash.class);

        intent.putExtra("CALLER", "Game");

        saveGame();

        this.startActivity(intent);
    }


    public void gotoHelp(View v){

        Intent intent = new Intent(this, HowTo.class);

        String caller = "Game";
        intent.putExtra("CALLER", caller);

        saveGame();

        startActivity(intent);
    }


    @Override
    public void onDestroy() {

        saveGame();

        super.onDestroy();

    }


    //Row/Col for cells
    public class StateObject {

        int row;
        int col;

        String name;
        String abbr;
        boolean marked;

        public StateObject(int row, int col, String name, String abbr){
            this.row=row;
            this.col=col;
            this.name=name;
            this.abbr=abbr;
            this.marked=false;
        }

        public String getName() {
            return this.name;
        }

        public String getAbbr() {
            return this.abbr;
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

            String thing = this.name + "," + this.abbr + "," + this.row + "," + this.col + "," + this.marked +"\n";

            return thing;
        }
    }
    
}
