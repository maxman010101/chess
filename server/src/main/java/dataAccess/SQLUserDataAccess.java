package dataAccess;
import com.google.gson.Gson;
import models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import responses.ResponseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
public class SQLUserDataAccess implements UserDataAccess{
    public SQLUserDataAccess() throws DataAccessException, ResponseException {
        userConfigureDatabase();
    }
    @Override
    public void clearUsers() throws DataAccessException {
        var statement = "TRUNCATE users";
        try {
            executeUpdate(statement);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUser(String username) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT userName, password, email FROM users WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                    else
                        return null;
                }
            }
        } catch (Exception e) {
            throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        //return null;
    }
    private User readUser(ResultSet rs) throws SQLException {
        var user = rs.getString("userName");
        var pass = rs.getString("password");
        var email = rs.getString("email");
        return new User(user, pass, email);
    }

    @Override
    public User checkLogin(String username, String password) throws ResponseException {
        // read the previously hashed password from the database and compare it to a given password when logging in
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(getUser(username) != null) {
            String registeredUserName = getUser(username).username;
            String registeredPassword = getUser(username).password;
            if (encoder.matches(password, registeredPassword) && (Objects.equals(username, registeredUserName))) {
                return getUser(username);
            }
        }
        return null;
    }
    private int executeUpdate(String statement, Object... params) throws ResponseException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new ResponseException(String.format("unable to update database: %s, %s", statement, e.getMessage()), 500);
        }
    }
    @Override
    public void createUser(String username, String password, String email) throws DataAccessException, ResponseException {
        var statement = "INSERT INTO users (userName, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, username, hashUserPassword(password), email);
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `userName` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
            ) 
            """
    };
    private void userConfigureDatabase() throws ResponseException, DataAccessException {
        SQLGameDataAccess.configDB(createStatements);
    }
    String hashUserPassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
