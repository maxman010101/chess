package ui;

import chess.*;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import models.Game;

import java.util.Arrays;
import java.util.Objects;

public class ChessClientMenu {
    private String userName = null;
    private Game currGame = null;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private final ChessServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    public ChessClientMenu(NotificationHandler notificationHandler, ChessServerFacade server, String serverUrl) {
        this.notificationHandler = notificationHandler;
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
                case "redrawboard" -> redrawBoard(params);
                case "leave" -> leaveGame();
                case "makemove" -> makeAMove(params);
                case "resign" -> resignGame();
                case "highlightlegalmoves" -> highlightMoves(params);
                default -> help();
            };
        } catch (ResponseException | responses.ResponseException | DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String highlightMoves(String[] params) {
        var pieceRow = Integer.parseInt(params[0]);
        var pieceColumn = Integer.parseInt(params[1]);
        return "";
    }
    public String resignGame() throws ResponseException {
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        ws.resign(currGame.gameID);
        return "";
    }
    public String makeAMove(String[] params) throws ResponseException {
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        var pieceRow = Integer.parseInt(params[0]);
        var pieceColumn = Integer.parseInt(params[1]);
        var moveRow = Integer.parseInt(params[2]);
        var moveColumn = Integer.parseInt(params[3]);
        ws.makeMove(currGame.gameID, new ChessMove(new ChessPosition(pieceRow, pieceColumn), new ChessPosition(moveRow, moveColumn), null));
        return "";
    }
    public String leaveGame() throws ResponseException {
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        ws.leaveGame(currGame.gameID);
        state = State.SIGNEDIN;
        return "";
    }
    public String redrawBoard(String... params) {
        if (params.length >= 1) {
            var team = params[0];
            if(Objects.equals(team, "white")){
                ChessBoardUI.drawWhiteBoard();
            }
            if(Objects.equals(team, "black")){
                ChessBoardUI.drawBlackBoard();
            }
            if(Objects.equals(team, "observer")){
                ChessBoardUI.drawWhiteBoard();
            }
        }
        return "";
    }
    private String getHelp() {
        if(state == State.PLAYGAME){
            System.out.print("""
                    \n- redrawboard -> draws the game board again, specify which team you are on ('white' or 'black') or if you are an 'observer'
                    - leave -> exit the current game, opens up your team for another player if you are on a team
                    - makemove -> enter in the coordinates of the piece you wish to move, then enter in the coordinates of the move\n you wish to make, example 'a 8 (the desired piece) a 7 (the desired move)'
                    - resign -> you surrender the game, game ends
                    - highlightlegalmoves -> choose which piece to look at, legal moves for that piece at highlighted, example, 'a 8'
                    - help -> get info about the commands
                    If a command doesnt work, it will give you a message or it will reset you to the menu again
                    press enter to return to command list
                    
                    """);
        }
        if(state == State.WATCHGAME){
            System.out.print("""
                    \n- redrawboard -> draws the game board again, specify which team you are on ('white' or 'black') or if you are an 'observer'
                    - leave -> exit the current game, opens up your team for another player if you are on a team
                    - help -> get info about the commands
                    If a command doesnt work, it will give you a message or it will reset you to the menu again
                    press enter to return to command list
                    
                    """);
        }
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
        assertSignedIn();
        if (params.length == 2) {
            var gameID = Integer.parseInt(params[0]);
            var playerColorString = params[1];
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ChessGame.TeamColor color = null;
            if(Objects.equals(playerColorString, "observe")){
                state = State.WATCHGAME;
                ws.joinAsPlayer(currGame.gameID, color);
            }
            if(Objects.equals(playerColorString, "white")){
                color = ChessGame.TeamColor.WHITE;
                state = State.PLAYGAME;
                ws.joinAsPlayer(currGame.gameID, color);

            }
            if(Objects.equals(playerColorString, "black")){
                color = ChessGame.TeamColor.BLACK;
                state = State.PLAYGAME;
                ws.joinAsPlayer(currGame.gameID, color);
            }
            var game = getGame(gameID);
            currGame = game;
            if (game != null) {
                server.joinGame(gameID, color, game, game.gameName);
                System.out.print("Here is your game board from " + color + "'s perspectives!\n");
                if (color == ChessGame.TeamColor.WHITE || color == null) {
                    ChessBoardUI.drawWhiteBoard();
                }
                if(color == ChessGame.TeamColor.BLACK){
                    ChessBoardUI.drawBlackBoard();
                }
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
        if (params.length >= 2 && params[0] != null && !params[0].isEmpty() && params[1] != null && !params[1].isEmpty()) {
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
            String whiteUser = game.whiteUsername;
            String blackUser = game.blackUsername;
            if(game.whiteUsername == null){whiteUser = "EMPTY";}
            if(game.blackUsername == null){blackUser = "EMPTY";}
            result.append(gson.toJson(game)).append('\n');
            System.out.print("\n" + gameNumb + " -- Game Name: " + game.gameName + ", White Player: " + whiteUser + ", Black Player: " + blackUser);
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
        if(state == State.PLAYGAME){
            System.out.print("""
                    \n- redrawBoard <teamColor>
                    - leave
                    - makeMove <pieceRow, pieceColumn, moveRow, pieceColumn>
                    - resign
                    - highlightLegalMoves <row, column>
                    - help
                    """);
        }
        if(state == State.WATCHGAME) {
            System.out.print("""
                    \n- redrawBoard <'observer'>
                    - leave
                    - help
                    """);
        }
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

