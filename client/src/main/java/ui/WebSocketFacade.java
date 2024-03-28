package ui;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import webSocketMessages.userCommands.UserGameCommand;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.*;

public class WebSocketFacade extends  Endpoint{
    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
                UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
                notificationHandler.notify(action);
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage(), 500);
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinAsPlayer(int gameID, ChessGame.TeamColor color) throws ResponseException {
        try {
            var action = UserGameCommand.CommandType.JOIN_PLAYER;
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage(), 500);
        }
    }

    public void leaveGame(int gameID) throws ResponseException {
        try {
            var action = UserGameCommand.CommandType.LEAVE;
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage(), 500);
        }
    }
    public void joinAsObserver(int gameID) throws ResponseException {
        try {
            var action = UserGameCommand.CommandType.JOIN_OBSERVER;
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage(), 500);
        }
    }
    public void resign(int gameID) throws ResponseException {
        try {
            var action = UserGameCommand.CommandType.RESIGN;
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage(), 500);
        }
    }
    public void makeMove(int gameID, ChessMove move) throws ResponseException {
        try {
            var action = UserGameCommand.CommandType.MAKE_MOVE;
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage(), 500);
        }
    }
}
