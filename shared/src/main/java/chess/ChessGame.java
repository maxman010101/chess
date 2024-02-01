package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {
    }
    private ChessGame.TeamColor team;
    private ChessBoard gameBoard;

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //return ChessPiece.pieceMoves(gameBoard, startPosition);
        ChessPiece currentPiece = gameBoard.getPiece(startPosition);
        Collection<ChessMove> valid = new HashSet<>();
        if (currentPiece != null){
            for(ChessMove move : currentPiece.pieceMoves(gameBoard, startPosition)){
                if(!(isInCheck(currentPiece.getTeamColor())) && team == currentPiece.getTeamColor()){
                    valid.add(move);
                }
            }
            return valid;
        }
        else
            return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        if(teamColor == TeamColor.WHITE){
            for(int i = 1; i <= 8; i++){
                for(int c = 1; c <= 8; c++){
                    if(gameBoard.getPiece(new ChessPosition(i,c)) != null
                            && gameBoard.getPiece(new ChessPosition(i,c)).pieceColor == TeamColor.BLACK){
                        for(ChessMove move : gameBoard.getPiece(new ChessPosition(i,c)).pieceMoves(gameBoard, new ChessPosition(i,c))){
                            if(gameBoard.getPiece(new ChessPosition(move.endPosition.row, move.endPosition.col)) != null &&
                                    gameBoard.getPiece(new ChessPosition(move.endPosition.row, move.endPosition.col)).getPieceType() == ChessPiece.PieceType.KING
                                    && gameBoard.getPiece(new ChessPosition(move.endPosition.row, move.endPosition.col)).getTeamColor() == TeamColor.WHITE){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        if(teamColor == TeamColor.BLACK){
            for(int i = 1; i <= 8; i++){
                for(int c = 1; c <= 8; c++){
                    if(gameBoard.getPiece(new ChessPosition(i,c)) != null
                            && gameBoard.getPiece(new ChessPosition(i,c)).pieceColor == TeamColor.WHITE){
                        for(ChessMove move : gameBoard.getPiece(new ChessPosition(i,c)).pieceMoves(gameBoard, new ChessPosition(i,c))){
                            if(gameBoard.getPiece(new ChessPosition(move.endPosition.row, move.endPosition.col)) != null &&
                                    gameBoard.getPiece(new ChessPosition(move.endPosition.row, move.endPosition.col)).getPieceType() == ChessPiece.PieceType.KING
                                    && gameBoard.getPiece(new ChessPosition(move.endPosition.row, move.endPosition.col)).getTeamColor() == TeamColor.BLACK){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        if(isInCheck(teamColor) && isInStalemate(teamColor)){
            return true;
        }
        else
            return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
            for(int i = 1; i <= 8; i++){
                for(int c = 1; c <= 8; c++){
                    if(gameBoard.getPiece(new ChessPosition(i,c)) != null
                            && gameBoard.getPiece(new ChessPosition(i,c)).pieceColor == teamColor){
                        if(!(validMoves(new ChessPosition(i,c)).isEmpty())){
                        return false;
                        }
                    }
                }
            }
            return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
