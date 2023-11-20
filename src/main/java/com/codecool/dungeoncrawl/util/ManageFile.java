package com.codecool.dungeoncrawl.util;

import com.codecool.dungeoncrawl.model.GameState;
import com.google.gson.Gson;

public class ManageFile {

    public void generateGameFile() {
        String json = new Gson().toJson(new GameState(null, null, null));

    }

    public  void readGameFile(){
        GameState gameStateFromFile = new Gson().fromJson("", GameState.class);
    }
}
