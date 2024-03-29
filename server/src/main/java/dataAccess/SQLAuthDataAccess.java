package dataAccess;
import models.Auth;
import responses.ResponseException;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDataAccess implements AuthDataAccess{
    public SQLAuthDataAccess() throws DataAccessException, ResponseException {
        authConfigureDatabase();
    }

    @Override
    public void clearAuths() throws DataAccessException {
        var statement = "TRUNCATE auths";
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
            var statement = "SELECT token, userName FROM auths WHERE token=?";
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
        var username = rs.getString("userName");
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
              `token` varchar(256) NOT NULL,
              `userName` varchar(256) NOT NULL,
              PRIMARY KEY (`token`),
              INDEX(userName)
            ) 
            """
    };


    private void authConfigureDatabase() throws ResponseException, DataAccessException {
        configDB(createStatements);
    }

    static void configDB(String[] createStatements) throws DataAccessException, ResponseException {
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
}

