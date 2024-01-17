package chess;

import java.util.Collection;
import java.util.HashSet;

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
//        else if(getPieceType() == PieceType.ROOK){return rookMoves(board, myPosition);}
//        else if(getPieceType() == PieceType.KNIGHT){return knightMoves(board, myPosition);}
//        else if(getPieceType() == PieceType.BISHOP){return bishopMoves(board, myPosition);}
        else
            return null;
    }

//    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition pos) {
//
//    }
    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> possMoves = new HashSet<>();
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.WHITE){
            ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col);
            if(pos.row + 1 >= 0 && pos.row + 1 <= 7 && ((board.getPiece(possPosit) != null
                    && board.getPiece(possPosit).getTeamColor() == ChessGame.TeamColor.BLACK)
                    || board.getPiece(possPosit) == null)) {
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
            else if(pos.row - 1 >= 0 && pos.row - 1 <= 7){

            }
            else if(pos.col + 1 >+ 0 && pos.col + 1 <= 7){

            }
            else if(pos.col - 1 >= 0 && pos.col + 1 <= 7){

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
//    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition pos) {
//
//    }
//    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition pos) {
//
//    }
//    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition pos) {
//
//    }
}
