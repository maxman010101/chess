package server;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;

import java.util.UUID;

public class UserServices {
    public UserServices() {
    }

    public RegisterResponse register(String username, String password, String email){
        DataAccess doa = new MemoryDataAccess();
        int passHash = password.hashCode();
        try {
            if(doa.getUser(username, passHash) == null) {
                doa.createUser(username, passHash, email);
                String authToken = UUID.randomUUID().toString();
                Auth auth = doa.createAuth(username, authToken);
                return new RegisterResponse(auth, null);
            }
            if(doa.getUser(username, passHash) != null){
                return new RegisterResponse(null, "Error: already taken");
            }
        } catch (DataAccessException e) {
            return new RegisterResponse(null, "Error: description");
        }
        return null;
    }
}
