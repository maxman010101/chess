package ui;

import webSocketMessages.*;
import webSocketMessages.userCommands.UserGameCommand;

public interface NotificationHandler {
    void notify(UserGameCommand gameAction);
}