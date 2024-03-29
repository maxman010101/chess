package models;

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
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
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
