package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import java.util.List;

public interface PlayerDao {
    void add(PlayerModel player);
    void update(PlayerModel player);
    int getPlayerId(String player_name);
    PlayerModel get(int id);
    List<PlayerModel> getAll();
    List<String>getPlayerNames();
}
