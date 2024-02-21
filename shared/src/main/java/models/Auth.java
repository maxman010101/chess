package models;

import java.util.UUID;

public class Auth {
    public String authToken;
    public String username;

    public Auth(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "authToken='" + authToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
