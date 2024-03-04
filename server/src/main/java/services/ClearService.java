package services;

import dataAccess.*;
import responses.ClearResponse;
import responses.ResponseException;

public class ClearService {
    public ClearService() {
    }

    public ClearResponse clearData() throws ResponseException, DataAccessException {
        UserDataAccess userDao = new SQLUserDataAccess();
        AuthDataAccess authDao = new SQLAuthDataAccess();
        GameDataAccess gameDao = new SQLGameDataAccess();
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
