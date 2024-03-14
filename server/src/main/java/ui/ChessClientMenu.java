package ui;

import com.google.gson.Gson;
import responses.ResponseException;
import server.ChessServerFacade;

import java.util.Arrays;

public class ChessClientMenu {
    private String userName = null;
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
            return switch (cmd) {
                case "login" -> logIn(params);
                case "Register" -> register(params);
                case "list Games" -> listGames();
                case "Logout" -> logOut();
                case "Create Game" -> createGame(params);
                case "Join Game" -> joinGame(params);
                case "Join as Observer" -> joinObserver(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    public String logIn(String... params) throws ResponseException {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            userName = String.join("-", params);
            return String.format("You signed in as %s.", userName);
        }
        throw new ResponseException("Expected: <yourname, password, email>", 400);
    }
    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games) {
          result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }
    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - Register <username, password, email>
                    - Help
                    - Login <username, password>
                    - Quit
                    """;
        }
        return """
                - Help
                - Logout
                - Create Game <name>
                - List Games
                - Join game <id, color>
                - Join as Observer <id>
                """;
    }
    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException("You must sign in", 400);
        }
    }
}

