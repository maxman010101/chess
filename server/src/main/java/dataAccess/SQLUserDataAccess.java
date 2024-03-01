package dataAccess;
import com.google.gson.Gson;
import models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import responses.ResponseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;
public class SQLUserDataAccess implements UserDataAccess{
    public SQLUserDataAccess() throws DataAccessException, ResponseException {
        configureDatabase();
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
            var statement = "SELECT id, json FROM users WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }
    private User readUser(ResultSet rs) throws SQLException {
        var user = rs.getString("username");
        return new Gson().fromJson(user, User.class);
    }

    @Override
    public User checkLogin(String username, String password) throws ResponseException {
        // read the previously hashed password from the database
        var hashedPassword = getUser(username).password;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(password, hashedPassword)){
            return getUser(username);
        }
        else
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
            CREATE TABLE IF NOT EXISTS  pet (
              `userName` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(String.format("Unable to configure database: %s", ex.getMessage()), 500);
        }
    }
    String hashUserPassword(String password) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
