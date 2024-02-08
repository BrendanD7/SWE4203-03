import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;

/** This class represents the game manager. It is responsible for processing HTTP Requests and returning responses */
class GameManager implements Disposer {
  /** Instance Variables */
  private final static Logger log = Logger.getLogger(Main.class.getName());
  List<Game> games = new ArrayList<>();

  /** Start the game by creating an access code and game code
   * @param exchange The HTTP Exchange to issue the response to
   */
  public void startGame(HttpExchange exchange) {
    Game game = new Game();
    this.games.add(game);

    Map<String, Object> response = new HashMap<>();
    response.put("accessCode", game.accessCode);
    response.put("gameCode", game.gameCode);
    Utils.sendSuccess(exchange, response);
  }

  /** Search for a gamecode given an access code in an HTTP Request
   * @param exchange The HTTP Exchange to receive the request from and issue a response to
   */
  public void searchForGame(HttpExchange exchange) {
    final Map<String, String> params = Utils.exchangeToParamMap(exchange);

    // Check if request contains an accessCode
    if (!params.containsKey("accessCode")) {
      throw new HttpError400("NO_ACCESS_CODE");
    }

    // Using the access code, find the game code
    String accessCode = params.get("accessCode");
    for (Game game : this.games) {
      if (game.accessCode.equals(accessCode)) {
        Map<String, Object> response = new HashMap<>();
        response.put("gameCode", game.gameCode);
        Utils.sendSuccess(exchange, response);
        return;
      }
    }

    throw new HttpError400("ACCESS_CODE_INVALID");
  }

  /** Join the game as a host by an HTTP Request
   * @param exchange The HTTP Exchange to receive the request from and issue a response to
   */
  public void joinAsHost(HttpExchange exchange) {
    String gameCode = this.getGameCodeOrThrow(exchange);
    Game game = this.getGameOrThrow(gameCode);
    // Check if game has a host already
    if (game.hasHost()) {
      throw new HttpError400("PLAYER_ALREADY_PRESENT");
    }

    // Join the game as a host
    GameManager.log.info(String.format("Player joined as host (%s)!", gameCode));
    Utils.sendSseStream(exchange);
    final OutputStream body = exchange.getResponseBody();
    game.setHost(body);
  }

  /** Join the game as an opponent by an HTTP Request
   * @param exchange The HTTP Exchange to receive the request from and issue a response to
   */
  public void joinAsOpponent(HttpExchange exchange) {
    String gameCode = this.getGameCodeOrThrow(exchange);
    Game game = this.getGameOrThrow(gameCode);
    // Check if the game has an opponent
    if (game.hasOpponent()) {
      throw new HttpError400("PLAYER_ALREADY_PRESENT");
    }

    // Join the game as an opponent
    GameManager.log.info(String.format("Player joined as opponent (%s)!", gameCode));
    Utils.sendSseStream(exchange);
    final OutputStream body = exchange.getResponseBody();
    game.setOpponent(body);
  }

  /** Allow a player to make a move through an HTTP request
   * @param The HTTP Exchange to receive a request from and issue a response to
   */
  public void move(HttpExchange exchange) {
    System.out.println("MOVE");
    String gameCode = this.getGameCodeOrThrow(exchange);
    Game game = this.getGameOrThrow(gameCode);

    // Check if Request is valid
    final Map<String, String> params = Utils.exchangeToParamMap(exchange);
    if (!params.containsKey("x")) {
      throw new HttpError400("NO_X");
    } else if (!params.containsKey("y")) {
      throw new HttpError400("NO_Y");
    } else if (!params.containsKey("player")) {
      throw new HttpError400("NO_PLAYER");
    }

    // Parse X and Y coordinates to integers
    int x = this.parseOrThrow(params.get("x"), "INVALID_X");
    int y = this.parseOrThrow(params.get("y"), "INVALID_Y");
    
    // Parse player from request
    Player player;
    try {
      player = Player.valueOf(params.get("player"));
    } catch (IllegalArgumentException e) {
      throw new HttpError400("INVALID_PLAYER");
    }

    // Make a move
    PlayResult result = game.play(player, x, y);

    // GAME_FINISHED and GAME_NOT_FINISHED are not error states
    // the rest are and should return 400 errors
    if (result != PlayResult.GAME_FINISHED && result != PlayResult.GAME_NOT_FINISHED) {
      throw new HttpError400(result.toString());
    }

    // Return a response indicating if the game is finished
    Map<String, Object> response = new HashMap<>();
    if (result == PlayResult.GAME_FINISHED) {
      this.games.remove(game);
      response.put("gameOver", true);
    } else {
      response.put("gameOver", false);
    }

    Utils.sendSuccess(exchange, response);
  }

  /** Parse the given string into a coordinate position or return an error
   * @param s The string representation of the coordinate
   * @param errorCode The error to issue if the string can not be parsed
   */
  private int parseOrThrow(String s, String errorCode) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      throw new HttpError400(errorCode);
    }
  }

  /** Retrieve a game code from an HTTP Request
   * @param The HTTP Exchange to retrieve a request from and issue a response to
   */
  private String getGameCodeOrThrow(HttpExchange exchange) {
    final Map<String, String> params = Utils.exchangeToParamMap(exchange);

    // Check if request contains a game code
    if (!params.containsKey("gameCode")) {
      throw new HttpError400("NO_GAME_CODE");
    }

    return params.get("gameCode");
  }

  /** Get a game object, or return an error
   * @param gameCode The gamecode to find a game for
   */
  private Game getGameOrThrow(String gameCode) {
    for (Game game : this.games) {
      if (game.gameCode.equals(gameCode)) {
        return game;
      }
    }

    throw new HttpError400("NO_GAME_FOUND");
  }

  /** Dispose of the game on completion */
  public void dispose() {
    this.games.forEach((game) -> {
      game.dispose();
    });
  }
}
