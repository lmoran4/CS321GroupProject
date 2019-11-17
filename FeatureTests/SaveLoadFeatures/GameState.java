package com.example.chris.stateplatebingo;

import android.app.Application;

/*public class GameState extends Application
{
    private boolean isNewGame;


    public boolean getIsNewGame() {
        return isNewGame;
    }

    public void setIsNewGame(boolean isNewGame) {
        this.isNewGame = isNewGame;
    }
}*/

public class GameState {

    private static GameState instance;

    public static GameState getInstance() {
        if (instance == null)
            instance = new GameState();
        return instance;
    }

    private GameState() {
    }

    private boolean val;

    public boolean getValue() {
        return val;
    }

    public void setValue(boolean value) {
        this.val = value;
    }
}