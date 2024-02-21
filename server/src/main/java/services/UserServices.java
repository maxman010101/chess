package services;

import dataAccess.*;
import models.Auth;
import responses.ResponseException;

import java.util.UUID;

public class UserServices {
    public UserServices() {
    }

    public Auth register(String username, String password, String email) throws ResponseException {
        MemoryUserDataAccess userDoa = new MemoryUserDataAccess();
        MemoryAuthDataAccess authDoa = new MemoryAuthDataAccess();
        //int passHash = password.hashCode();
        if(username == null || password == null || email == null){throw new ResponseException("Error: bad request", 400);}
        try {
            if(userDoa.getUser(username, password) == null) {
                userDoa.createUser(username, password, email);
                String authToken = UUID.randomUUID().toString();
                return authDoa.createAuth(username, authToken);
            }
            else{
                throw new ResponseException("Error: already taken", 403);
            }
        } catch (DataAccessException e) {
            throw new ResponseException("Error: cannot access DB", 500);
        }
    }

    public Auth login(String username, String password) throws ResponseException {
        MemoryUserDataAccess userDoa = new MemoryUserDataAccess();
        try {
            if(userDoa.getUser(username, password) != null) {
                String authToken = UUID.randomUUID().toString();
                return userDoa.loginUser(username, authToken);
            }
            else{
                throw new ResponseException("Error: unauthorized", 401);
            }
        } catch (DataAccessException e) {
            throw new ResponseException("Error: cannot access DB", 500);
        }
    }

}
