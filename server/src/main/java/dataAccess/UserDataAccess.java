package dataAccess;

import models.User;
import responses.ResponseException;


public interface UserDataAccess {
    void clearUsers() throws DataAccessException;

    User getUser(String username) throws DataAccessException, ResponseException;

    User checkLogin(String username, String password) throws DataAccessException, ResponseException;

    void createUser(String username, String password, String email) throws DataAccessException, ResponseException;



}
