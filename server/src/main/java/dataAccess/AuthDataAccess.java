package dataAccess;

import models.Auth;

public interface AuthDataAccess {
    void clearAuths() throws DataAccessException;

    Auth createAuth(String username, String authToken) throws DataAccessException;
    Auth getAuth(String authToken) throws DataAccessException;

}