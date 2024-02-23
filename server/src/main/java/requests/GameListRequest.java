package requests;

import chess.ChessGame;

public record GameListRequest(int gameID, String gameName, String whiteUsername, String blackUsername, ChessGame game) {
}
