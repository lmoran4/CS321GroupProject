
import java.io.*;

import java.util.*;


public class Game {

    String[][] gridStr;

    private String[][] cells; // abs

    StateObject[][] cellar;

    private boolean[][] cellCheck;

    ArrayList<String> states;

    ArrayList<String> setstates; // states chosen by user
    ArrayList<String> shuffleAbs; // shuffled abbreviations

    HashMap<String, String> AbbState; // (abbreviation,state)

    HashMap<String, String> stateAbbs; // (state,abbreviation)
    HashMap<String, StateObject> boardhelpr; // (abbreviation, (row,col))

    int score;

    boolean[] block1rows;
    boolean[] block2rows;
    boolean[] block3rows;
    boolean[] block4rows;

    boolean[] col1;
    boolean[] col2;
    boolean[] col3;

    boolean[] allCols;

    boolean isComplete;

    HashMap<String,String> stateFileStrs; //state, str

    boolean[] blocks;

    String gameFile;

    public String getGameFile() {
        return gameFile;
    }

    public void setGameFile(String gameFile) {
        this.gameFile = gameFile;
    }



    // new game
    public Game(){

        this.gridStr = new String[20][3];
        this.cells = new String[20][3];
        this.stateAbbs = new HashMap<>();
        this.boardhelpr = new HashMap<>();
        this.stateFileStrs = new HashMap<>();
        this.AbbState = new HashMap<>();

        this.states = new ArrayList<>();
        this.setstates = new ArrayList<>();
        this.shuffleAbs = new ArrayList<>();

        this.cellCheck = new boolean[20][3];
        this.cellar = new StateObject[20][3];

        this.isComplete = false;

        buildBlocks();
        setupStates();
        buildGrid();

    }

    // load from a specific file; continue
    public Game(String filename){

        this.gridStr = new String[20][3];
        this.cells = new String[20][3];
        this.stateAbbs = new HashMap<>();
        this.boardhelpr = new HashMap<>();
        this.stateFileStrs = new HashMap<>();
        this.AbbState = new HashMap<>();

        this.states = new ArrayList<>();
        this.setstates = new ArrayList<>();

        this.cellCheck = new boolean[20][3];
        this.cellar = new StateObject[20][3];

        this.isComplete = false;

        this.gameFile = filename;

        stateZipper();
        buildBlocks();
        gameReader();
        buildGridLoader();

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

        //states.addAll(Arrays.asList(vals));

        for(String str : keys){
            states.add(str);
        }

        for(String str : vals){
            //abbs.add(str);
            shuffleAbs.add(str);
        }
        score = 0;

        // randomizes the list of state abbreviations
        Collections.shuffle(shuffleAbs, new Random());
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



    private void buildBlocks(){

        allCols = new boolean[] {false,false,false};
        blocks = new boolean[] {false,false,false,false};

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

        int counter = 0; // tracks where we are in the list of shuffled states.

        for(int i = 0; i<20; i++){
            for(int j=0; j<3; j++){

                String stateA = shuffleAbs.get(counter);
                String stateName = AbbState.get(stateA);

                gridStr[i][j] = stateName;
                cells[i][j] = stateA;

                cellCheck[i][j] = false;

                StateObject thingy = new StateObject(i,j,stateName,stateA);
                thingy.setMarked(false);

                cellar[i][j] = thingy;

                String fileStr = thingy.toString();

                stateFileStrs.put(stateA,fileStr);

                boardhelpr.put(shuffleAbs.get(counter), thingy); // adds (stateAbbr, (row,col)) pair to hashmap

                counter++;

            }
        }
    }


    // read contents of specific file
    public String gameReader(String filename){

        try {

            String f = filename + ".txt";

            File file = new File(f);

            if (!file.exists()) {
                return null;
            }

            if (file.exists()) {

                if (file.length() == 0) {
                    return null;
                }

                FileInputStream input = new FileInputStream(file);
                InputStreamReader rdr = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(rdr);
                StringBuilder sb = new StringBuilder();
                String line=br.readLine();
                while (line != null) {

                    sb.append(line);
                    sb.append("\n");

                    line = br.readLine();

                }

                br.close();

                return sb.toString();

            }


        }catch(Exception e){ }

        return null;

    }


    public void gameReader(){

        // IF THERE'S A CONTINUE, load it up!

        // FOR EACH STRING IN FILE...

        // State,Abb,Row,Col,marked

        ArrayList<String> loadedStrs = new ArrayList<>();

        // FIRST LINE IN ARRAY IS SCORE.

        try {

            String filename = this.gameFile + ".txt";

            File file = new File(filename);

            if (!file.exists()) {
                return;
            }

            if (file.exists()) {

                if (file.length() == 0) {
                    return;
                }

                FileInputStream input = new FileInputStream(file);
                InputStreamReader rdr = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(rdr);
                String line=br.readLine();
                while (line != null) {

                    loadedStrs.add(line);

                    line = br.readLine();

                }

                br.close();

                loadHandler(loadedStrs);
            }


        }catch(Exception e){
        }

    }

    private void buildGridLoader(){

        for(int i = 0; i<20; i++){
            for(int j=0; j<3; j++){

                StateObject ob = cellar[i][j];

                String ab = ob.getAbbr();
                cells[i][j] = ab;

                gridStr[i][j] = ob.getName();
                cellCheck[i][j] = ob.isMarked();

                if(ob.isMarked()){
                    stateMarker(i,j);
                    rowCheckLoad(i,j);
                    colCheckLoad(i,j);
                }

            }
        }
    }


    public void loadHandler(ArrayList<String> loadedStrs){

        ArrayList<String> ls = loadedStrs;

        String scorestr = ls.get(0).replaceAll("[^0-9]", "");

        score = Integer.parseInt(scorestr);

        for(int i = 1; i<ls.size(); i++){

            String thing = ls.get(i);

            String[] sa = thing.split(",");

            String name = sa[0];
            String ab = sa[1];

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


    public String saveGame(){

        String scorestr = ""+ this.score + "\n";
        String sb = scorestr;

        for(StateObject[] ar : cellar){
            for(StateObject rc : ar){
                sb = sb + rc.toString();

            }
        }

        try {

            String filename = this.gameFile + ".txt";

            File file = new File(filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream stream = new FileOutputStream(file);

            stream.write(sb.getBytes());

            stream.close();


        } catch (IOException e) { }

        return sb;
    }


    @Override
    public String toString(){

        String scorestr = ""+ this.score + "\n";

        String sb = scorestr;

        for(StateObject[] ar : this.cellar){
            for(StateObject rc : ar){
                sb = sb + rc.toString();

            }
        }

        return sb;
    }




    public void stateMarker(int row, int col){

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
        if(allCols[0] && allCols[1] && allCols[2]) { this.isComplete = true; }

    }





    public void bingoCheckr(String stateStr){

        String stateAb = stateAbbs.get(stateStr);

        StateObject loc = boardhelpr.get(stateAb);

        int row = loc.getRow();
        int col = loc.getCol();

        colCheckr(row,col);

        stripCheckr(row,col);

        rowCheckr(row,col);

    }


    public void stateSetter(String stateStr){


        if(setstates.contains(stateStr)){
            //return false
        }

        setstates.add(stateStr);
        score++;

        String stateAb = stateAbbs.get(stateStr);

        boardhelpr.get(stateAb).setMarked(true);

        String update = boardhelpr.get(stateAb).toString();

        stateFileStrs.put(stateAb,update);

        StateObject loc = boardhelpr.get(stateAb);

        int row = loc.getRow();
        int col = loc.getCol();


        cellar[row][col] = loc;


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


        //bingoCheckr(stateStr);
    }

    private void colCheckr(int row, int col){

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



    public void selectState() { }

    //Class for cells
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
