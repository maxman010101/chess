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
            var statement = "SELECT gameID, gameName, whiteUser, blackUser, game FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()), 500);
        }
        return result;
    }
    private Game readGame(ResultSet rs) throws SQLException {
        var id = rs.getInt("gameID");
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
            var statement = "SELECT gameID, gameName, whiteUser, blackUser, game FROM games WHERE gameID=?";
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
    public void saveGame(int gameID, ChessGame.TeamColor clientColor, String username) throws DataAccessException, ResponseException {
        if(gameID <= 0){throw new ResponseException("Error: bad request", 400);}
        if (clientColor == ChessGame.TeamColor.BLACK) {
            try (var conn = DatabaseManager.getConnection()) {
                var statement = "UPDATE games SET blackUser = ? WHERE gameID = ?";
                try (var ps = conn.prepareStatement(statement)) {
                    ps.setString(1, username);
                    ps.setInt(2, gameID);
                    ps.executeUpdate();
                }
            }catch (Exception e) {
                throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()), 500);
            }
        }

        if (clientColor == ChessGame.TeamColor.WHITE) {
            try (var conn = DatabaseManager.getConnection()) {
                var statement = "UPDATE games SET whiteUser = ? WHERE gameID = ?";
                try (var ps = conn.prepareStatement(statement)) {
                    ps.setString(1, username);
                    ps.setInt(2, gameID);
                    ps.executeUpdate();
                }
            }catch (Exception e) {
                throw new ResponseException(String.format("Unable to read data: %s", e.getMessage()), 500);
            }
        }
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
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
    };
    private void gameConfigureDatabase() throws ResponseException, DataAccessException {
        configDB(createStatements);
    }

    static void configDB(String[] createStatements) throws DataAccessException, ResponseException {
        confidDBHelper(createStatements);
    }

    static void confidDBHelper(String[] createStatements) throws DataAccessException, ResponseException {
        SQLAuthDataAccess.configDB(createStatements);
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
