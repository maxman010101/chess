import chess.*;
import server.Server;
import ui.ChessClientMenu;
import ui.ChessServerFacade;

import java.util.Scanner;

public class Main {
    static ChessClientMenu client = new ChessClientMenu(new ChessServerFacade("http://localhost:8080"), "http://localhost:8080");
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("Welcome to the amazing♕ 240 Chess Client!!! Press enter to see command list to get started!");
        Scanner scanner = new Scanner(System.in);
        while(true){
            String line  = scanner.nextLine();
            client.eval(line);
        }
    }
}