package responses;

import models.Game;

import java.util.List;

public record GameListResponse(List<Game> games, String message) {
}
