package dataAccess;
import chess.ChessGame;
import com.google.gson.Gson;
import models.Game;
import responses.ResponseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDataAccess implements GameDataAccess{
    public SQLGameDataAccess() throws DataAccessException, ResponseException {
        gameConfigureDatabase();
    }
    @Override
    public void clearGames(){
        var statement = "TRUNCATE games";
        try {
            executeUpdate(statement);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Game> listGames(String authToken) throws ResponseException {
        var result = new ArrayList<Game>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, gameName, whiteUser, blackUser, game FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return result;
    }
    private Game readGame(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var gameName = rs.getString("gameName");
        var whiteUser = rs.getString("whiteUser");
        var blackUser = rs.getString("blackUser");
        var json = rs.getString("game");
        var game = new Gson().fromJson(json, ChessGame.class);
        return new Game(id, gameName, whiteUser, blackUser, game);
    }
    @Override
    public int createGame(String name) throws DataAccessException, ResponseException {
        var statement = "INSERT INTO games (gameName, game) VALUES (?, ?)";

        var json = new Gson().toJson(new ChessGame());
        var id = executeUpdate(statement, name, json);
        return id;
    }
    @Override
    public Game getGame(int id) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, gameName, whiteUser, blackUser, game FROM games WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return null;
    }
    @Override
    public void saveGame(int gameID, ChessGame.TeamColor clientColor, String username) throws DataAccessException {

    }
    @Override
    public boolean validColorToJoin(ChessGame.TeamColor color, ChessGame.TeamColor clientColor, String colorUsername) {
        return (clientColor == color && colorUsername == null);
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `gameName` varchar(256) NOT NULL,
              `whiteUser` varchar(256) DEFAULT NULL,
              `blackUser` varchar(256) DEFAULT NULL,
              'game' TEXT NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameName),
              INDEX(whiteUser),
              INDEX(blackUser),
              INDEX(game)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
    private void gameConfigureDatabase() throws ResponseException, DataAccessException {
        configDB(createStatements);
    }

    static void configDB(String[] createStatements) throws DataAccessException, ResponseException {
        confidDBHelper(createStatements);
    }

    static void confidDBHelper(String[] createStatements) throws DataAccessException, ResponseException {
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

    private int executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof Game p) ps.setString(i + 1, p.toString());
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
}
