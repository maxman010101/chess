package services;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDataAccess;
import dataAccess.MemoryGameDataAccess;
import models.Game;
import responses.GameListResponse;
import responses.LogOutResponse;
import responses.ResponseException;

import java.util.List;

public class GameServices {
    public GameServices() {
    }

    public GameListResponse getGames(String authToken) throws DataAccessException, ResponseException {
        MemoryGameDataAccess gameDoa = new MemoryGameDataAccess();
        MemoryAuthDataAccess authDoa = new MemoryAuthDataAccess();
        try{
            if(authDoa.getAuth(authToken) != null){
                List<Game> games = gameDoa.listGames(authToken);
                return new GameListResponse(games,null);
            }

            else{
                throw new ResponseException("Error: unauthorized", 401);
            }
        }
        catch(DataAccessException | ResponseException e){
            throw new ResponseException("Error: cannot access DB", 500);
        }
    }
}

