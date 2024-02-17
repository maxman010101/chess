package dataAccess;

import chess.ChessGame;
import server.Auth;
import server.Game;
import server.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryDataAccess implements DataAccess{

    static List<User> users = new ArrayList<>();

    static List<Auth> auths = new ArrayList<>();
    static List<Auth> session = new ArrayList<>();
    static List<Game> games = new ArrayList<>();

    public MemoryDataAccess() {
    }

    public void clearAll() throws DataAccessException {
        users.clear();
        auths.clear();
        games.clear();
    }


    @Override
    public User getUser(String username) throws DataAccessException {
        for (User user : users) {
            if (Objects.equals(user.username, username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        User newUser = new User(username, password, email);
        users.add(newUser);
    }

    @Override
    public Auth createAuth(String username, String authToken) throws DataAccessException {
        Auth auth = new Auth(authToken, username);
        auths.add(auth);
        return auth;
    }

    @Override
    public void logOutUser(String authToken) throws DataAccessException {
        for(int i = 0; i < session.size(); i++){
            if(Objects.equals(session.get(i).authToken, authToken)){
                session.remove(session.get(i));
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

    @Override
    public List<Game> listGames(String authToken) throws DataAccessException {
        return games;
    }

    @Override
    public boolean verifyLogIn(String authToken) throws DataAccessException {
        for (Auth auth : session) {
            if (Objects.equals(auth.authToken, authToken)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Game createGame(String name, String whiteUser, String blackUser, ChessGame chessgame) throws DataAccessException {
        Game game = new Game(games.size()+1, name, null, null, chessgame);
        games.add(game);
        return game;
    }

    @Override
    public Game getGame(int id) throws DataAccessException {
        for (Game game : games) {
            if (Objects.equals(game.gameID, id)) {
                return game;
            }
        }
    }

    @Override
    public void saveGame(String clientColor, ChessGame game) throws DataAccessException {
        for (Game value : games) {
            if (Objects.equals(value.game, game)) {
                if (validColorToJoin(clientColor)){
                    
                }
            }
        }
    }

    public boolean validColorToJoin(String color){
        return true;
    }
}
