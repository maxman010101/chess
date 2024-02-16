package server;

import chess.ChessGame;

public class Game {
    public int gameID;
    public String gameName;
    public String whiteUsername;
    public String blackUsername;
    public ChessGame game;

    public Game(int gameID, String gameName, String whiteUsername, String blackUsername, ChessGame game) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.game = game;
        //this.gameID += 1;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameID='" + gameID + '\'' +
                ", gameName='" + gameName + '\'' +
                ", whiteUsername='" + whiteUsername + '\'' +
                ", blackUsername='" + blackUsername + '\'' +
                ", game=" + game +
                '}';
    }
}
