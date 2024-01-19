package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    public ChessGame.TeamColor pieceColor;
    public ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
//        if(getPieceType() == PieceType.PAWN){return pawnMoves(board, myPosition);}
        if(getPieceType() == PieceType.KING){return kingMoves(board, myPosition);}
//        else if(getPieceType() == PieceType.QUEEN){return queenMoves(board, myPosition);}
        else if(getPieceType() == PieceType.ROOK){return rookMoves(board, myPosition);}
//        else if(getPieceType() == PieceType.KNIGHT){return knightMoves(board, myPosition);}
//        else if(getPieceType() == PieceType.BISHOP){return bishopMoves(board, myPosition);}
        else
            return null;
    }

    /** helper function that checks the availability/validity of possible moves for pieces
     * @return true or false
     */
    public boolean isValidMove(ChessPosition possiblePosition, ChessGame.TeamColor team_color, ChessBoard board){
        if(team_color == ChessGame.TeamColor.WHITE){
            return (possiblePosition.row > 0 && possiblePosition.row <= 8
                    && possiblePosition.col > 0 && possiblePosition.col <= 8
                    && ((board.getPiece(possiblePosition) != null
                    && board.getPiece(possiblePosition).getTeamColor() == ChessGame.TeamColor.BLACK)
                    || board.getPiece(possiblePosition) == null));
        }
        else if(team_color == ChessGame.TeamColor.BLACK){
            return (possiblePosition.row > 0 && possiblePosition.row <= 8
                    && possiblePosition.col > 0 && possiblePosition.col <= 8
                    && ((board.getPiece(possiblePosition) != null
                    && board.getPiece(possiblePosition).getTeamColor() == ChessGame.TeamColor.WHITE)
                    || board.getPiece(possiblePosition) == null));
        }
        else
            return false;
    }

//    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition pos) {
//
//    }
    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> possMoves = new HashSet<>();
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.WHITE){
            ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col);
            if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row - 1, pos.col);
            if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row, pos.col + 1);
            if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row, pos.col - 1);
            if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row + 1, pos.col + 1);
            if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)){
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row + 1, pos.col - 1);
            if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)){
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row - 1, pos.col + 1);
            if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)){
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row - 1, pos.col - 1);
            if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)){
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
        }
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.BLACK){
            ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col);
            if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row - 1, pos.col);
            if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row, pos.col + 1);
            if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row, pos.col - 1);
            if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row + 1, pos.col + 1);
            if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)){
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row + 1, pos.col - 1);
            if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)){
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row - 1, pos.col + 1);
            if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)){
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            possPosit = new ChessPosition(pos.row - 1, pos.col - 1);
            if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)){
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
        }
        for(ChessMove moves : possMoves){
            System.out.println(moves);
        }
        return possMoves;
    }
//    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition pos) {
//
//    }
    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> possMoves = new HashSet<>();
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.WHITE){
            if(pos.row + 1 <= 8){
                for(int r = pos.row + 1; r <= 8; r++) {
                    ChessPosition possPosit = new ChessPosition(r, pos.col);
                    ChessPiece tempPiece = board.getPiece(possPosit);
                    if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                        possMoves.add(new ChessMove(pos, possPosit, null));
                        break;
                    }
                    else if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                        possMoves.add(new ChessMove(pos, possPosit, null));
                    }
                    else
                        break;
                }
            }
            if(pos.row - 1 > 0){
                for(int r = pos.row - 1; r > 0; r--){
                    ChessPosition possPosit = new ChessPosition(r, pos.col);
                    ChessPiece tempPiece = board.getPiece(possPosit);
                    if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                        possMoves.add(new ChessMove(pos, possPosit, null));
                        break;
                    }
                    else if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                        possMoves.add(new ChessMove(pos, possPosit, null));
                    }
                    else
                        break;
                }
            }
            if(pos.col + 1 <= 8){
                for(int c = pos.col + 1; c <= 8; c++){
                    ChessPosition possPosit = new ChessPosition(pos.row, c);
                    ChessPiece tempPiece = board.getPiece(possPosit);
                    if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                        possMoves.add(new ChessMove(pos, possPosit, null));
                        break;
                    }
                    else if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                        possMoves.add(new ChessMove(pos, possPosit, null));
                    }
                    else
                        break;
                }
            }
            if(pos.col - 1 > 0) {
                for (int c = pos.col - 1; c > 0; c--) {
                    ChessPosition possPosit = new ChessPosition(pos.row, c);
                    ChessPiece tempPiece = board.getPiece(possPosit);
                    if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                        possMoves.add(new ChessMove(pos, possPosit, null));
                        break;
                    }
                    else if (isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                        possMoves.add(new ChessMove(pos, possPosit, null));
                    } else
                        break;
                }
            }
        }
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.BLACK){
            if(pos.row + 1 <= 8) {
                for(int r = pos.row + 1; r <= 8; r++) {
                    ChessPosition possPosit = new ChessPosition(r, pos.col);
                    ChessPiece tempPiece = board.getPiece(possPosit);
                    if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        possMoves.add(new ChessMove(pos, possPosit, null));
                        break;
                    }
                    else if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                        possMoves.add(new ChessMove(pos, possPosit, null));
                    }
                    else
                        break;
                }
            }
            if(pos.row - 1 > 0) {
                for(int r = pos.row - 1; r > 0; r--){
                    ChessPosition possPosit = new ChessPosition(r, pos.col);
                    ChessPiece tempPiece = board.getPiece(possPosit);
                    if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        possMoves.add(new ChessMove(pos, possPosit, null));
                        break;
                    }
                    else if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                        possMoves.add(new ChessMove(pos, possPosit, null));
                    }
                    else
                        break;
                }
            }
            if(pos.col + 1 <= 8) {
                for(int c = pos.col + 1; c <= 8; c++){
                    ChessPosition possPosit = new ChessPosition(pos.row, c);
                    ChessPiece tempPiece = board.getPiece(possPosit);
                    if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        possMoves.add(new ChessMove(pos, possPosit, null));
                        break;
                    }
                    else if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                        possMoves.add(new ChessMove(pos, possPosit, null));
                    }
                    else
                        break;
                }
            }
            if(pos.col - 1 > 0) {
                for(int c = pos.col - 1; c > 0; c--){
                    ChessPosition possPosit = new ChessPosition(pos.row, c);
                    ChessPiece tempPiece = board.getPiece(possPosit);
                    if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        possMoves.add(new ChessMove(pos, possPosit, null));
                        break;
                    }
                    else if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                        possMoves.add(new ChessMove(pos, possPosit, null));
                    }
                    else
                        break;
                }
            }
        }
        return possMoves;
    }
//    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition pos) {
//
//    }
//    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition pos) {
//
//    }
}
