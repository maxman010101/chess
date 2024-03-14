package ui;

import com.google.gson.Gson;
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
                case "listGames" -> listGames();
                case "logout" -> logOut();
                case "createGame" -> createGame(params);
              //  case "joinGame" -> joinGame(params);
              //  case "joinGameAsObserver" -> joinGame(params);
                case "quit" -> exit();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    public String createGame(String...params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            //server.createGame();
            System.out.print("successfully made the game and added it to your list, press enter");
        }
        throw new ResponseException("Expected: <gameName>", 400);
    }
    public String exit(){
        System.out.print("exiting chess");
        System.exit(0);
        return "";
    }
    public String register(String... params) throws ResponseException{
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            //server.register();
            System.out.print("successfully registered, press enter");
        }
        throw new ResponseException("Expected: <yourname, password, email>", 400);
    }
    public String logIn(String... params) throws ResponseException {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            //server.logIn();
            System.out.print("logged in, press enter");
        }
        throw new ResponseException("Expected: <yourname, password>", 400);
    }
    public String listGames() throws ResponseException {
        assertSignedIn();
        //var games = server.listGames();
        var result = new StringBuilder();
        var gson = new Gson();
        //for (var game : games) {
          //result.append(gson.toJson(game)).append('\n');
        //}
        return result.toString();
    }
    public String logOut() throws ResponseException {
        assertSignedIn();
        state = State.SIGNEDOUT;
        //server.logOut();
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
                - joinGame <id, color>
                - joinAsObserver <id>
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

