import org.junit.*;

import java.io.File;

import static org.junit.Assert.*;

public class GameTests {


    Game game1;
    String saveStr1;
    String loadStr1;

    Game game2;

    @Before
    public void setup() {

        game1 = new Game();
        game1.setGameFile("saved1");

        game1.stateSetter("Florida");
        game1.stateSetter("Ontario");
        game1.stateSetter("Maine");
        game1.stateSetter("Alaska");
        game1.stateSetter("New Mexico");
        game1.stateSetter("Colorado");
        game1.stateSetter("Kansas");
        game1.stateSetter("Ohio");

        saveStr1 = game1.saveGame();
        loadStr1 = game1.gameReader("saved1");

        game2 = loadGame("saved1");


       // gameStr2 = game2.toString();

    }


    // load from a specific file
    public Game loadGame(String filename){

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

                Game loaded = new Game(filename);

                return loaded;

            }
        }catch (Exception e){
            return null;
        }

        return null;
    }



    public void testShuffle() { }

    @Test
    public void testScore(){


        if(game2 != null){
            assertEquals(game1.score, game2.score);
        }
        else{
            Assert.fail("Something went wrong with loading game2");
        }

    }

    public void testSetstates(){ }

    @Test
    public void testSaveRead(){


        assertEquals(saveStr1,loadStr1);

    }

    public void testLoad(){

        if(game2 != null){

            assertEquals(saveStr1, game2.toString());

            assertEquals(game1.cellar[10][11], game2.cellar[10][11]);


        }
        else{
            Assert.fail("Something went wrong with loading game2");
        }


    }




}
