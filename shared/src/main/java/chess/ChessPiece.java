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
        if(getPieceType() == PieceType.PAWN){return pawnMoves(board, myPosition);}
        else if(getPieceType() == PieceType.KING){return kingMoves(board, myPosition);}
        else if(getPieceType() == PieceType.QUEEN){return queenMoves(board, myPosition);}
        else if(getPieceType() == PieceType.ROOK){return rookMoves(board, myPosition);}
        else if(getPieceType() == PieceType.KNIGHT){return knightMoves(board, myPosition);}
        else if(getPieceType() == PieceType.BISHOP){return bishopMoves(board, myPosition);}
        else
            return null;
    }

    /** helper function that checks the availability/validity of possible moves for pieces
     * @return true or false
     */
    public boolean isValidMove(ChessPosition possiblePosition, ChessGame.TeamColor teamColor, ChessBoard board){
        if(teamColor == ChessGame.TeamColor.WHITE){
            return (possiblePosition.row > 0 && possiblePosition.row <= 8
                    && possiblePosition.col > 0 && possiblePosition.col <= 8
                    && ((board.getPiece(possiblePosition) != null
                    && board.getPiece(possiblePosition).getTeamColor() == ChessGame.TeamColor.BLACK)
                    || board.getPiece(possiblePosition) == null));
        }
        else if(teamColor == ChessGame.TeamColor.BLACK){
            return (possiblePosition.row > 0 && possiblePosition.row <= 8
                    && possiblePosition.col > 0 && possiblePosition.col <= 8
                    && ((board.getPiece(possiblePosition) != null
                    && board.getPiece(possiblePosition).getTeamColor() == ChessGame.TeamColor.WHITE)
                    || board.getPiece(possiblePosition) == null));
        }
        else
            return false;
    }

    public boolean isValidPawnMove(ChessPosition possiblePosition, ChessGame.TeamColor teamColor, ChessBoard board){
        if(teamColor == ChessGame.TeamColor.WHITE){
            return (possiblePosition.row > 0 && possiblePosition.row <= 8
                    && possiblePosition.col > 0 && possiblePosition.col <= 8
                    && board.getPiece(possiblePosition) == null);
        }
        else if(teamColor == ChessGame.TeamColor.BLACK){
            return (possiblePosition.row > 0 && possiblePosition.row <= 8
                    && possiblePosition.col > 0 && possiblePosition.col <= 8
                    && board.getPiece(possiblePosition) == null);
        }
        else
            return false;
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> possMoves = new HashSet<>();
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.WHITE){
            if(pos.row == 2){
                ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col);
                if(isValidPawnMove(possPosit, ChessGame.TeamColor.WHITE, board)){
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    possPosit = new ChessPosition(pos.row + 2, pos.col);
                    if(isValidPawnMove(possPosit, ChessGame.TeamColor.WHITE, board)){
                        possMoves.add(new ChessMove(pos, possPosit, null));
                    }
                }
            }
            ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col);
            if(isValidPawnMove(possPosit, ChessGame.TeamColor.WHITE, board) && possPosit.row == 8){
                addPromotionMoves(possPosit, pos, possMoves);

            }
            else{
                if(isValidPawnMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                    possMoves.add(new ChessMove(pos, possPosit, null));
                }
            }
            if(pos.row + 1 <= 8 && pos.col - 1 > 0){
                possPosit = new ChessPosition(pos.row + 1, pos.col - 1);
                whitePawnAttack(board, pos, possMoves, possPosit);
            }
            if(pos.row + 1 <= 8 && pos.col + 1 <= 8){
                possPosit = new ChessPosition(pos.row + 1, pos.col + 1);
                whitePawnAttack(board, pos, possMoves, possPosit);
            }
        }
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.BLACK){
            if(pos.row == 7){
                ChessPosition possPosit = new ChessPosition(pos.row - 1, pos.col);
                if(isValidPawnMove(possPosit, ChessGame.TeamColor.BLACK, board)){
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    possPosit = new ChessPosition(pos.row - 2, pos.col);
                    if(isValidPawnMove(possPosit, ChessGame.TeamColor.BLACK, board)){
                        possMoves.add(new ChessMove(pos, possPosit, null));
                    }
                }
            }
            ChessPosition possPosit = new ChessPosition(pos.row - 1, pos.col);
            if(isValidPawnMove(possPosit, ChessGame.TeamColor.BLACK, board) && possPosit.row == 1){
                addPromotionMoves(possPosit, pos, possMoves);

            }
            else{
                if(isValidPawnMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                    possMoves.add(new ChessMove(pos, possPosit, null));
                }
            }
            if(pos.row - 1 > 0 && pos.col - 1 > 0){
                possPosit = new ChessPosition(pos.row - 1, pos.col - 1);
                blackPawnAttack(board, pos, possMoves, possPosit);
            }
            if(pos.row - 1 > 0 && pos.col + 1 <= 8){
                possPosit = new ChessPosition(pos.row - 1, pos.col + 1);
                blackPawnAttack(board, pos, possMoves, possPosit);
            }
        }
        return possMoves;
    }

    public void addPromotionMoves(ChessPosition possPosit, ChessPosition pos, Collection<ChessMove> possMoves){
        possMoves.add(new ChessMove(pos, possPosit, PieceType.QUEEN));
        possMoves.add(new ChessMove(pos, possPosit, PieceType.BISHOP));
        possMoves.add(new ChessMove(pos, possPosit, PieceType.ROOK));
        possMoves.add(new ChessMove(pos, possPosit, PieceType.KNIGHT));
    }

    private void blackPawnAttack(ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves, ChessPosition possPosit) {
        ChessPiece tempPiece = board.getPiece(possPosit);
        if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            if(possPosit.row == 1){
                addPromotionMoves(possPosit, pos, possMoves);
            }
            else{
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
        }
    }

    private void whitePawnAttack(ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves, ChessPosition possPosit) {
        ChessPiece tempPiece = board.getPiece(possPosit);
        if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
            if(possPosit.row == 8){
                addPromotionMoves(possPosit, pos, possMoves);

            }
            else{
                possMoves.add(new ChessMove(pos, possPosit, null));
            }
        }
    }

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
        return possMoves;
    }
    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> possMoves = new HashSet<>();
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.WHITE){
            vertAndHorizMoves(board, pos, possMoves);
            whiteDiagMoves(board, pos, possMoves);
        }
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.BLACK){
            blackHorizAndVertMoves(board, pos, possMoves);
            blackVertMoves(board, pos, possMoves);
        }
        return possMoves;
    }

    private void blackVertMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves) {
        if(pos.row + 1 <= 8 && pos.col + 1 <= 8){
            ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col + 1);
            while(possPosit.row <= 8 && possPosit.col <= 8){
                ChessPiece tempPiece = board.getPiece(possPosit);
                if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    break;
                }
                else if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    possPosit = new ChessPosition(possPosit.row + 1, possPosit.col + 1);
                }
                else
                    break;
            }
        }
        if(pos.row + 1 <= 8 && pos.col - 1 > 0){
            ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col - 1);
            while(possPosit.row <= 8 && possPosit.col > 0){
                ChessPiece tempPiece = board.getPiece(possPosit);
                if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    break;
                }
                else if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    possPosit = new ChessPosition(possPosit.row + 1, possPosit.col - 1);
                }
                else
                    break;
            }
        }
        if(pos.row - 1 > 0 && pos.col + 1 <= 8){
            ChessPosition possPosit = new ChessPosition(pos.row - 1, pos.col + 1);
            while(possPosit.row > 0 && possPosit.col <= 8){
                ChessPiece tempPiece = board.getPiece(possPosit);
                if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    break;
                }
                else if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    possPosit = new ChessPosition(possPosit.row - 1, possPosit.col + 1);
                }
                else
                    break;
            }
        }
        if(pos.row - 1 > 0 && pos.col - 1 > 0){
            ChessPosition possPosit = new ChessPosition(pos.row - 1, pos.col - 1);
            while(possPosit.row > 0 && possPosit.col > 0){
                ChessPiece tempPiece = board.getPiece(possPosit);
                if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    break;
                }
                else if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    possPosit = new ChessPosition(possPosit.row - 1, possPosit.col - 1);
                }
                else
                    break;
            }
        }
    }

    private void blackHorizAndVertMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves) {
        if(pos.row + 1 <= 8){
            for(int r = pos.row + 1; r <= 8; r++) {
                if (goodBlackMoveUpDown(board, pos, possMoves, r)) break;
            }
        }
        if(pos.row - 1 > 0){
            for(int r = pos.row - 1; r > 0; r--){
                if (goodBlackMoveUpDown(board, pos, possMoves, r)) break;
            }
        }
        if(pos.col + 1 <= 8){
            for(int c = pos.col + 1; c <= 8; c++){
                if (goodSideBlackMove(board, pos, possMoves, c)) break;
            }
        }
        if(pos.col - 1 > 0) {
            for (int c = pos.col - 1; c > 0; c--) {
                if (goodSideBlackMove(board, pos, possMoves, c)) break;
            }
        }
    }

    private boolean goodSideBlackMove(ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves, int c) {
        ChessPosition possPosit = new ChessPosition(pos.row, c);
        ChessPiece tempPiece = board.getPiece(possPosit);
        return blackSideMove(board, pos, possMoves, possPosit, tempPiece);
    }

    private boolean blackSideMove(ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves, ChessPosition possPosit, ChessPiece tempPiece) {
        if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            possMoves.add(new ChessMove(pos, possPosit, null));
            return true;
        }
        else if(isValidMove(possPosit, ChessGame.TeamColor.BLACK, board)) {
            possMoves.add(new ChessMove(pos, possPosit, null));
        }
        else
            return true;
        return false;
    }

    private boolean goodBlackMoveUpDown(ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves, int r) {
        ChessPosition possPosit = new ChessPosition(r, pos.col);
        ChessPiece tempPiece = board.getPiece(possPosit);
        return blackSideMove(board, pos, possMoves, possPosit, tempPiece);
    }

    private void whiteDiagMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves) {
        if(pos.row + 1 <= 8 && pos.col + 1 <= 8){
            ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col + 1);
            while(possPosit.row <= 8 && possPosit.col <= 8){
                ChessPiece tempPiece = board.getPiece(possPosit);
                if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    break;
                }
                else if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    possPosit = new ChessPosition(possPosit.row + 1, possPosit.col + 1);
                }
                else
                    break;
            }
        }
        if(pos.row + 1 <= 8 && pos.col - 1 > 0){
            ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col - 1);
            while(possPosit.row <= 8 && possPosit.col > 0){
                ChessPiece tempPiece = board.getPiece(possPosit);
                if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    break;
                }
                else if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    possPosit = new ChessPosition(possPosit.row + 1, possPosit.col - 1);
                }
                else
                    break;
            }
        }
        if(pos.row - 1 > 0 && pos.col + 1 <= 8){
            ChessPosition possPosit = new ChessPosition(pos.row - 1, pos.col + 1);
            while(possPosit.row > 0 && possPosit.col <= 8){
                ChessPiece tempPiece = board.getPiece(possPosit);
                if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    break;
                }
                else if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    possPosit = new ChessPosition(possPosit.row - 1, possPosit.col + 1);
                }
                else
                    break;
            }
        }
        if(pos.row - 1 > 0 && pos.col - 1 > 0){
            ChessPosition possPosit = new ChessPosition(pos.row - 1, pos.col - 1);
            while(possPosit.row > 0 && possPosit.col > 0){
                ChessPiece tempPiece = board.getPiece(possPosit);
                if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    break;
                }
                else if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
                    possMoves.add(new ChessMove(pos, possPosit, null));
                    possPosit = new ChessPosition(possPosit.row - 1, possPosit.col - 1);
                }
                else
                    break;
            }
        }
    }

    private void vertAndHorizMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves) {
        if(pos.row + 1 <= 8){
            for(int r = pos.row + 1; r <= 8; r++) {
                if (goodMovingSideways(board, pos, possMoves, r)) break;
            }
        }
        if(pos.row - 1 > 0){
            for(int r = pos.row - 1; r > 0; r--){
                if (goodMovingSideways(board, pos, possMoves, r)) break;
            }
        }
        if(pos.col + 1 <= 8){
            for(int c = pos.col + 1; c <= 8; c++){
                if (goodMovingVertically(board, pos, possMoves, c)) break;
            }
        }
        if(pos.col - 1 > 0) {
            for (int c = pos.col - 1; c > 0; c--) {
                if (goodMovingVertically(board, pos, possMoves, c)) break;
            }
        }
    }

    private boolean goodMovingVertically(ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves, int c) {
        ChessPosition possPosit = new ChessPosition(pos.row, c);
        ChessPiece tempPiece = board.getPiece(possPosit);
        return movingVertical(board, pos, possMoves, possPosit, tempPiece);
    }

    private boolean movingVertical(ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves, ChessPosition possPosit, ChessPiece tempPiece) {
        if(tempPiece != null && tempPiece.getTeamColor() == ChessGame.TeamColor.BLACK){
            possMoves.add(new ChessMove(pos, possPosit, null));
            return true;
        }
        else if(isValidMove(possPosit, ChessGame.TeamColor.WHITE, board)) {
            possMoves.add(new ChessMove(pos, possPosit, null));
        }
        else
            return true;
        return false;
    }

    private boolean goodMovingSideways(ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves, int r) {
        ChessPosition possPosit = new ChessPosition(r, pos.col);
        ChessPiece tempPiece = board.getPiece(possPosit);
        return movingVertical(board, pos, possMoves, possPosit, tempPiece);
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> possMoves = new HashSet<>();
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.WHITE){
            vertAndHorizMoves(board, pos, possMoves);
        }
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.BLACK){
            blackHorizAndVertMoves(board, pos, possMoves);
        }
        return possMoves;
    }
    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> possMoves = new HashSet<>();
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if(pos.row + 2 <= 8 && pos.col + 1 <= 8) {
                ChessPosition possPosit = new ChessPosition(pos.row + 2, pos.col + 1);
                knightAddAndCheckMove(ChessGame.TeamColor.WHITE, possPosit, board, pos, possMoves);

            }
            if(pos.row + 2 <= 8 && pos.col - 1 > 0) {
                ChessPosition possPosit = new ChessPosition(pos.row + 2, pos.col - 1);
                knightAddAndCheckMove(ChessGame.TeamColor.WHITE, possPosit, board, pos, possMoves);

            }
            if(pos.row + 1 <= 8 && pos.col + 2 <= 8) {
                ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col + 2);
                knightAddAndCheckMove(ChessGame.TeamColor.WHITE, possPosit, board, pos, possMoves);

            }
            if(pos.row - 1 > 0 && pos.col + 2 <= 8) {
                ChessPosition possPosit = new ChessPosition(pos.row - 1, pos.col + 2);
                knightAddAndCheckMove(ChessGame.TeamColor.WHITE, possPosit, board, pos, possMoves);

            }
            if(pos.row - 2 > 0 && pos.col + 1 <= 8) {
                ChessPosition possPosit = new ChessPosition(pos.row - 2, pos.col + 1);
                knightAddAndCheckMove(ChessGame.TeamColor.WHITE, possPosit, board, pos, possMoves);

            }
            if(pos.row - 2 > 0 && pos.col - 1 > 0) {
                ChessPosition possPosit = new ChessPosition(pos.row - 2, pos.col - 1);
                knightAddAndCheckMove(ChessGame.TeamColor.WHITE, possPosit, board, pos, possMoves);

            }
            if(pos.row + 1 <= 8 && pos.col - 2 > 0) {
                ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col - 2);
                knightAddAndCheckMove(ChessGame.TeamColor.WHITE, possPosit, board, pos, possMoves);

            }
            if(pos.row - 1 > 0 && pos.col - 2 > 0) {
                ChessPosition possPosit = new ChessPosition(pos.row - 1, pos.col - 2);
                knightAddAndCheckMove(ChessGame.TeamColor.WHITE, possPosit, board, pos, possMoves);

            }
        }
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.BLACK){
            if(pos.row + 2 <= 8 && pos.col + 1 <= 8) {
                ChessPosition possPosit = new ChessPosition(pos.row + 2, pos.col + 1);
                knightAddAndCheckMove(ChessGame.TeamColor.BLACK, possPosit, board, pos, possMoves);

            }
            if(pos.row + 2 <= 8 && pos.col - 1 > 0) {
                ChessPosition possPosit = new ChessPosition(pos.row + 2, pos.col - 1);
                knightAddAndCheckMove(ChessGame.TeamColor.BLACK, possPosit, board, pos, possMoves);

            }
            if(pos.row + 1 <= 8 && pos.col + 2 <= 8) {
                ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col + 2);
                knightAddAndCheckMove(ChessGame.TeamColor.BLACK, possPosit, board, pos, possMoves);

            }
            if(pos.row - 1 > 0 && pos.col + 2 <= 8) {
                ChessPosition possPosit = new ChessPosition(pos.row - 1, pos.col + 2);
                knightAddAndCheckMove(ChessGame.TeamColor.BLACK, possPosit, board, pos, possMoves);

            }
            if(pos.row - 2 > 0 && pos.col + 1 <= 8) {
                ChessPosition possPosit = new ChessPosition(pos.row - 2, pos.col + 1);
                knightAddAndCheckMove(ChessGame.TeamColor.BLACK, possPosit, board, pos, possMoves);

            }
            if(pos.row - 2 > 0 && pos.col - 1 > 0) {
                ChessPosition possPosit = new ChessPosition(pos.row - 2, pos.col - 1);
                knightAddAndCheckMove(ChessGame.TeamColor.BLACK, possPosit, board, pos, possMoves);

            }
            if(pos.row + 1 <= 8 && pos.col - 2 > 0) {
                ChessPosition possPosit = new ChessPosition(pos.row + 1, pos.col - 2);
                knightAddAndCheckMove(ChessGame.TeamColor.BLACK, possPosit, board, pos, possMoves);

            }
            if(pos.row - 1 > 0 && pos.col - 2 > 0) {
                ChessPosition possPosit = new ChessPosition(pos.row - 1, pos.col - 2);
                knightAddAndCheckMove(ChessGame.TeamColor.BLACK, possPosit, board, pos, possMoves);
            }
        }
        return possMoves;
    }

    public void knightAddAndCheckMove(ChessGame.TeamColor teamColor, ChessPosition possPosit, ChessBoard board, ChessPosition pos, Collection<ChessMove> possMoves){
        if(isValidMove(possPosit, teamColor, board)){
            possMoves.add(new ChessMove(pos, possPosit, null));
        }
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> possMoves = new HashSet<>();
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.WHITE){
            whiteDiagMoves(board, pos, possMoves);
        }
        if(board.getPiece(pos).getTeamColor() == ChessGame.TeamColor.BLACK){
            blackVertMoves(board, pos, possMoves);
        }
        return possMoves;
    }
}
