package dataAccess;

import chess.ChessGame;
import models.Auth;
import models.Game;
import models.User;
import java.util.List;


public interface UserDataAccess {
    void clearUsers() throws DataAccessException;

    User getUser(String username) throws DataAccessException;

    User checkLogin(String username, String password) throws DataAccessException;

    void createUser(String username, String password, String email) throws DataAccessException;



}
