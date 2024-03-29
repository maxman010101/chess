package dataAccess;

import models.Auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryAuthDataAccess implements AuthDataAccess{

    static List<Auth> auths = new ArrayList<>();

    public MemoryAuthDataAccess() {
    }

    @Override
    public void clearAuths() throws DataAccessException {
        auths.clear();

    }

    @Override
    public Auth createAuth(String username, String authToken) throws DataAccessException {
        Auth auth = new Auth(authToken, username);
        auths.add(auth);
        return auth;
    }

    public void removeAuth(String authToken) throws DataAccessException{
        for(Auth auth : auths){
            if(Objects.equals(auth.authToken, authToken)){
                auths.remove(auth);
                break;
            }
        }
    }

    @Override
    public Auth getAuth(String authToken) throws DataAccessException {
        for (Auth auth : auths) {
            if (Objects.equals(auth.authToken, authToken)) {
                return auth;
            }
        }
        return null;
    }
}
