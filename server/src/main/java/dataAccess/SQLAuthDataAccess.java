package dataAccess;

import com.google.gson.Gson;
import models.Auth;
import models.User;
import responses.ResponseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDataAccess implements AuthDataAccess{
    public SQLAuthDataAccess() throws DataAccessException, ResponseException {
        authConfigureDatabase();
    }

    @Override
    public void clearAuths() throws DataAccessException {
        var statement = "TRUNCATE games";
        try {
            executeUpdate(statement);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeAuth(String authToken) throws ResponseException {
        var statement = "DELETE FROM auths WHERE token=?";
        executeUpdate(statement, authToken);
    }

    @Override
    public Auth createAuth(String username, String authToken) throws ResponseException {
        var statement = "INSERT INTO auths (token, userName) VALUES (?, ?)";
        executeUpdate(statement, authToken, username);
        return new Auth(authToken, username);
    }

    @Override
    public Auth getAuth(String authToken) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM auths WHERE token=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }
    private Auth readAuth(ResultSet rs) throws SQLException {
        var token = rs.getString("token");
        var username = rs.getString("username");
        return new Auth(token, username);
    }

    private int executeUpdate(String statement, Object... params) throws ResponseException {
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
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(String.format("unable to update database: %s, %s", statement, e.getMessage()), 500);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auths (
              'token' varchar(256) NOT NULL,
              `name` varchar(256) NOT NULL,
              PRIMARY KEY (`token`),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void authConfigureDatabase() throws ResponseException, DataAccessException {
        SQLGameDataAccess.confidDBHelper(createStatements);
    }
}
