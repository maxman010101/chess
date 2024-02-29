package dataAccess;

import models.User;


public interface UserDataAccess {
    void clearUsers() throws DataAccessException;

    User getUser(String username) throws DataAccessException;

    User checkLogin(String username, String password) throws DataAccessException;

    void createUser(String username, String password, String email) throws DataAccessException;



}
