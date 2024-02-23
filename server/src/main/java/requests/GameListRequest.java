package requests;

import chess.ChessGame;

public record GameListRequest(String gameName, String whiteUsername, String blackUsername, ChessGame game) {
}
