package ui;

import com.google.gson.Gson;
import models.Game;
import ui.ResponseException;

import java.util.Arrays;

public class ChessClientMenu {
    private String user = null;
    private final ChessServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public ChessClientMenu(ChessServerFacade server, String serverUrl) {
        this.server = server;
        this.serverUrl = serverUrl;
    }
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            System.out.print("[" + state + "]>>>");
            return switch (cmd) {
                case "login" -> logIn(params);
                case "register" -> register(params);
                case "listgames" -> listGames();
                case "logout" -> logOut();
                case "creategame" -> createGame(params);
                case "joinGame" -> joinGame(params);
                case "joinGameAsObserver" -> joinGame(params);
                case "quit" -> exit();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    private Game getGame(int id) throws ResponseException {
        for (var game : server.listGames()) {
            if(game.getGameID() == id) {
                return game;
            }
        }
        return null;
    }
    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
                var gameID = Integer.parseInt(params[0]);
                var playerColor = params[1];
                var game = getGame(gameID);
                if (game != null) {
                    server.joinGame(gameID, playerColor);
                    ChessBoardUI.drawBoard();
                }
                else{
                    System.out.print("please enter the id of an existing game");
                }
        }
        else{
            System.out.print("please enter a the correct id of the game you want to join and either white or black for color, " +
                    "unless you are observing, then leave empty, press enter");}
        return "";
    }
    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            server.createGame(params[0]);
            System.out.print("successfully made the game and added it to your list, press enter");
        }
        else{
            System.out.print("please enter a name for your game");
            throw new ResponseException("Expected: <gameName>", 400);
        }
        return "";
    }
    public String exit(){
        System.out.print("exiting chess");
        System.exit(0);
        return "";
    }
    public String register(String... params) throws ResponseException{
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            server.register(params[0], params[1], params[2]);
            System.out.print("successfully registered, press enter");
        }
        else{
            System.out.print("please enter your username, password, and email");
            throw new ResponseException("Expected: <yourname, password, email>", 400);
        }
        return "";
    }
    public String logIn(String... params) throws ResponseException {
        if (params.length >= 2) {
            state = State.SIGNEDIN;
            server.login(params[0], params[1]);
            System.out.print("logged in, press enter");
        }
        else{
            System.out.print("please enter in your username and password");
            throw new ResponseException("Expected: <yourname, password>", 400);
        }
        return "";
    }
    public String listGames() throws ResponseException {
        assertSignedIn();
        int gameNumb = 1;
        var games = server.listGames();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games){
            game.setGameID(gameNumb);
            result.append(gson.toJson(game)).append('\n');
            System.out.print("gameNumb = " + gameNumb + " " + game);
            gameNumb++;}
        return result.toString();
    }
    public String logOut() throws ResponseException {
        assertSignedIn();
        state = State.SIGNEDOUT;
        server.logOut();
        System.out.print("logged out, press enter");
        return "";
    }
    public String help() {
        if (state == State.SIGNEDOUT) {
            System.out.print("""
                    \n- register <username, password, email>
                    - help
                    - login <username, password>
                    - quit
                    """);
        }
        else {
        System.out.print( """
                \n- help
                - logout
                - createGame <gameName>
                - listGames
                - joinGame <gameID, color>
                - joinAsObserver <gameID, (leave empty as observer)>
                """);
        }
        return "";
    }
    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException("You must sign in", 400);
        }
    }
}

