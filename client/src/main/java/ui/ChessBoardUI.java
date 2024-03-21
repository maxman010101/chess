package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static int rowNumber;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = "   ";
    private static final String K = " K ";
    private static final String Q = " Q ";
    private static final String R = " R ";
    private static final String N = " N ";
    private static final String B = " B ";
    private static final String P = " P ";
    private static ChessBoard gameBoard = new ChessBoard();

    public ChessBoardUI() { //fix this for phase 6, so it uses a playable board rather than an initial board
        gameBoard.resetBoard();
    }
    public static void main(String[] args) {
        drawBoard();
    }
    public static void drawBoard(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(out, "white");
        drawChessBoard(out, "white");
        drawHeaders(out, null);
        drawHeaders(out, "black");
        drawChessBoard(out, "black");
        out.print(SET_TEXT_COLOR_WHITE);
        //System.out.print("Press enter to return to command list");
    }
    private static void drawHeaders(PrintStream out, String color) {
        setBlack(out);
        String[] headers = {""};
        if(color == null){
            headers = new String[]{"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   "};
        }
        if(Objects.equals(color, "white")){
            headers = new String[]{" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        }
        if(Objects.equals(color, "black")){
            headers = new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
        }
        for(int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);
            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
            }
        }
        out.println();
    }
    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;
        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }
    private static void printHeaderText(PrintStream out, String piece) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(piece);
        setBlack(out);
    }
    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }
    private static void drawChessBoard(PrintStream out, String color) {
        int rowNum = 0;
        if(Objects.equals(color, "white")){
            rowNum = 0;
            for (int boardRow = rowNum; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
                rowNumber = boardRow;
                drawRowOfSquares(out, color);
                printBlackSpace(out);
                if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                    drawVerticalLine(out);
                    setBlack(out);
                }
            }
        }
        if(Objects.equals(color, "black")){
            rowNum = 7;
            for (int boardRow = rowNum; boardRow >= 0 ; --boardRow) {
                rowNumber = boardRow;
                drawRowOfSquares(out, color);
                printBlackSpace(out);
                if (boardRow <= BOARD_SIZE_IN_SQUARES - 1) {
                    drawVerticalLine(out);
                    setBlack(out);
                }
            }
        }
    }
    private static void printBlackSpace(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(EMPTY);
        setWhite(out);
    }
    private static void drawRowOfSquares(PrintStream out, String color) {
        gameBoard.resetBoard();
        String[] rowNumbs = {""};
        boolean rowNumSet = false;
        setBlack(out);
        if(Objects.equals(color, "white")){
        rowNumbs = new String[]{ " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 " };
            for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
                if(!rowNumSet){
                    printHeaderText(out, rowNumbs[rowNumber]);
                    rowNumSet = true;
                }
                else
                    printBlackSpace(out);
                for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                    addPiecesToBoard(out, squareRow, boardCol);
                    if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                        // Draw right line
                        setRed(out);
                        out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
                    }
                    setBlack(out);
                }
                out.println();
            }
    }
        if(Objects.equals(color, "black")){
            rowNumbs = new String[]{" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
            for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
                if(!rowNumSet){
                    printHeaderText(out, rowNumbs[rowNumber]);
                    rowNumSet = true;
                }
                else
                    printBlackSpace(out);
                for (int boardCol = 7; boardCol >= 0 ; --boardCol) {
                    addPiecesToBoard(out, squareRow, boardCol);
                    if (boardCol > 0) {
                        // Draw right line
                        setRed(out);
                        out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
                    }
                    setBlack(out);
                }
                out.println();
            }
        }
    }

    private static void addPiecesToBoard(PrintStream out, int squareRow, int boardCol) {
        if((rowNumber % 2 == 0 && boardCol % 2 == 0) || (rowNumber % 2 != 0 && boardCol % 2 != 0)){
            setWhite(out);
        }
        if((rowNumber % 2 == 0 && boardCol % 2 != 0) || (rowNumber % 2 != 0 && boardCol % 2 == 0)){
            setBlack(out);
        }
        if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
            int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
            int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;
            ChessPiece piece = gameBoard.pieces[rowNumber][boardCol];
            if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(EMPTY.repeat(prefixLength));
                printWhitePiece(out, K);
                out.print(EMPTY.repeat(suffixLength));
            }
            if(piece != null && piece.getPieceType() == ChessPiece.PieceType.QUEEN && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(EMPTY.repeat(prefixLength));
                printWhitePiece(out, Q);
                out.print(EMPTY.repeat(suffixLength));
            }
            if(piece != null && piece.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(EMPTY.repeat(prefixLength));
                printWhitePiece(out, P);
                out.print(EMPTY.repeat(suffixLength));
            }
            if(piece != null && piece.getPieceType() == ChessPiece.PieceType.ROOK && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(EMPTY.repeat(prefixLength));
                printWhitePiece(out, R);
                out.print(EMPTY.repeat(suffixLength));
            }
            if(piece != null && piece.getPieceType() == ChessPiece.PieceType.BISHOP && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(EMPTY.repeat(prefixLength));
                printWhitePiece(out, B);
                out.print(EMPTY.repeat(suffixLength));
            }
            if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KNIGHT && piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(EMPTY.repeat(prefixLength));
                printWhitePiece(out, N);
                out.print(EMPTY.repeat(suffixLength));
            }
            if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                out.print(EMPTY.repeat(prefixLength));
                printBlackPiece(out, K);
                out.print(EMPTY.repeat(suffixLength));
            }
            if(piece != null && piece.getPieceType() == ChessPiece.PieceType.QUEEN && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                out.print(EMPTY.repeat(prefixLength));
                printBlackPiece(out, Q);
                out.print(EMPTY.repeat(suffixLength));
            }
            if(piece != null && piece.getPieceType() == ChessPiece.PieceType.PAWN && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                out.print(EMPTY.repeat(prefixLength));
                printBlackPiece(out, P);
                out.print(EMPTY.repeat(suffixLength));
            }
            if(piece != null && piece.getPieceType() == ChessPiece.PieceType.ROOK && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                out.print(EMPTY.repeat(prefixLength));
                printBlackPiece(out, R);
                out.print(EMPTY.repeat(suffixLength));
            }
            if(piece != null && piece.getPieceType() == ChessPiece.PieceType.BISHOP && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                out.print(EMPTY.repeat(prefixLength));
                printBlackPiece(out, B);
                out.print(EMPTY.repeat(suffixLength));
            }
            if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KNIGHT && piece.getTeamColor() == ChessGame.TeamColor.BLACK){
                out.print(EMPTY.repeat(prefixLength));
                printBlackPiece(out, N);
                out.print(EMPTY.repeat(suffixLength));
            }
            if(piece == null){
                out.print(EMPTY.repeat(prefixLength));
                out.print(EMPTY.repeat(prefixLength));
                out.print(EMPTY.repeat(suffixLength));
            }
        }
        else {
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
        }
    }

    private static void drawVerticalLine(PrintStream out) {
        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_CHARS;
        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_CHARS; ++lineRow) {
            setRed(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));
            setBlack(out);
            out.println();
        }
    }
    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }
    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }
    private static void printWhitePiece(PrintStream out, String piece) {
        out.print(SET_TEXT_COLOR_BLUE);
        out.print(piece);
    }
    private static void printBlackPiece(PrintStream out, String piece) {
        out.print(SET_TEXT_COLOR_RED);
        out.print(piece);
    }
}
