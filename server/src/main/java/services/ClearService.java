package services;

import dataAccess.*;
import responses.ClearResponse;

public class ClearService {
    public ClearService() {
    }

    public ClearResponse clearData() {
        UserDataAccess userDao = new MemoryUserDataAccess();
        AuthDataAccess authDao = new MemoryAuthDataAccess();
        GameDataAccess gameDao = new MemoryGameDataAccess();
        try {
            userDao.clearUsers();
            authDao.clearAuths();
            gameDao.clearGames();
            return new ClearResponse(null);
        } catch (DataAccessException e) {
            return new ClearResponse("Error: description");
        }
    }
}
