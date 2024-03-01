package dataAccess;

import models.Auth;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class SQLAuthDataAccess implements AuthDataAccess{
    public SQLAuthDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @Override
    public void clearAuths() throws DataAccessException {

    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {

    }

    @Override
    public Auth createAuth(String username, String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public Auth getAuth(String authToken) throws DataAccessException {
        return null;
    }
}
