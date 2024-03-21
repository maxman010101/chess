package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import models.Game;
import ui.ResponseException;

import java.util.Arrays;
import java.util.Objects;

public class ChessClientMenu {
    private String user = null;
    private final ChessServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    //private int[] gameNumbs = new int[]{};

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
                case "joingame", "observe" -> joinGame(params);
                case "login" -> logIn(params);
                case "register" -> register(params);
                case "listgames" -> listGames();
                case "logout" -> logOut();
                case "creategame" -> createGame(params);
                case "help" -> getHelp();
                case "quit" -> exit();
                default -> help();
            };
        } catch (ResponseException | responses.ResponseException | DataAccessException ex) {
            return ex.getMessage();
        }
    }

    private String getHelp() {
        if(state == State.SIGNEDIN){
            System.out.print("""
                    \n- register -> register a new user with a unique username, a password, and email
                    - help -> get info about the commands
                    - login -> login with an existing username and password
                    - quit -> exit the program
                    If a command doesnt work, it will give you a message or it will reset you to the menu again
                    press enter to return to command list
                    
                    """);
        }
        else
            System.out.print("""
                \n- help -> get info about these commands
                - logout -> logout from the current user
                - createGame -> make and name a new chess game
                - listGames -> get a list of your created games
                - joinGame -> join a game specifying which number game from the list and what color you would like to be
                - observe -> same as above, but type 'observe' in color spot in order to join a game as a spectator
                If a command doesnt work, it will give you a message or it will reset you to the menu again
                press enter to return to command list
                
                """);
        return "";
    }

    private Game getGame(int id) throws ResponseException {
        var games = server.listGames().games();
        return games.get(id - 1);
    }
    public String joinGame(String... params) throws ResponseException, responses.ResponseException, DataAccessException {
        //System.out.print("TESTING GOT IN");
        assertSignedIn();
        if (params.length == 2) {
                var gameID = Integer.parseInt(params[0]);
                var playerColorString = params[1];
                ChessGame.TeamColor color = null;
                if(Objects.equals(playerColorString, "observe")){color = null;};
                if(Objects.equals(playerColorString, "white")){color = ChessGame.TeamColor.WHITE;}
                if(Objects.equals(playerColorString, "black")){color = ChessGame.TeamColor.BLACK;}
                var game = getGame(gameID);
                if (game != null) {
                    server.joinGame(gameID, color, game, game.gameName);
                    System.out.print("Here are your game boards from both perspectives!\n");
                    ChessBoardUI.drawBoard();
                    System.out.print("\npress enter to return to commands.");
                }
                else{
                    System.out.print("please enter the id of an existing game");
                }
        }
        else{
            System.out.print("please enter a the correct id of the game you want to join and either white or black for color, " +
                    "\nunless you are observing, then use the observe command with its inputs, press enter to return");}
        return "";
    }
    public String createGame(String... params) throws ResponseException, responses.ResponseException, DataAccessException {
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
    public String register(String... params) throws ResponseException, responses.ResponseException, DataAccessException {
        if (params.length == 3) {
            var response = server.register(params[0], params[1], params[2]);
            if(response.authToken != null){
                state = State.SIGNEDIN;
                System.out.print("successfully registered, press enter");
            }
        }
        else{
            System.out.print("please enter a new username, password, and email");
            throw new ResponseException("Expected: <yourname, password, email>", 400);
        }
        return "";
    }
    public String logIn(String... params) throws ResponseException, responses.ResponseException, DataAccessException {
        if (params.length >= 2) {
            var response = server.login(params[0], params[1]);
            if(response.authToken != null){
                state = State.SIGNEDIN;
                System.out.print("successfully logged in, press enter");
            }
        }
        else{
            System.out.print("please enter in your username and password");
            throw new ResponseException("Expected: <yourname, password>", 400);
        }
        return "";
    }
    public String listGames() throws ResponseException, responses.ResponseException, DataAccessException {
        assertSignedIn();
        int gameNumb = 1;
        var games = server.listGames().games();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games){
            result.append(gson.toJson(game)).append('\n');
            System.out.print("\n" + gameNumb + " " + game);
            gameNumb++;
        }
        System.out.print("\nHere are your games, press enter to return to command list.");
        return result.toString();
    }
    public String logOut() throws ResponseException, responses.ResponseException, DataAccessException {
        assertSignedIn();
        server.logOut();
        state = State.SIGNEDOUT;
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
                - observe <gameID, 'observe'>
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

