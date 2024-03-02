package dataAccess;

import models.Auth;
import responses.ResponseException;

public interface AuthDataAccess {
    void clearAuths() throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;

    Auth createAuth(String username, String authToken) throws DataAccessException, ResponseException;
    Auth getAuth(String authToken) throws DataAccessException;

}
