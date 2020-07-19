package com.jmaquin.kata.tennis.application;

import com.jmaquin.kata.tennis.domain.Game;
import com.jmaquin.kata.tennis.domain.enums.GameScore;
import com.jmaquin.kata.tennis.domain.enums.State;
import com.jmaquin.kata.tennis.function.UpdateGameScore;
import io.vavr.Tuple2;
import java.util.Objects;

public class GameManager {
  private final UpdateGameScore updateGameScore;

  public GameManager(UpdateGameScore updateGameScore) {
    Objects.requireNonNull(updateGameScore, "updateGameScore must not be null!");
    this.updateGameScore = updateGameScore;
  }

  public Game playerOneScores(Game game) {
    final Tuple2<GameScore, GameScore> updatedPlayerScores =
        updateGameScore.apply(game.getPlayerOneScore(), game.getPlayerTwoScore());
    if (GameScore.WIN_GAME.equals(updatedPlayerScores._1())) {
      return game.toBuilder()
          .playerOneScore(updatedPlayerScores._1())
          .playerTwoScore(updatedPlayerScores._2())
          .state(State.FINISHED)
          .build();
    } else {
      return game.toBuilder()
          .playerOneScore(updatedPlayerScores._1())
          .playerTwoScore(updatedPlayerScores._2())
          .state(State.ONGOING)
          .build();
    }
  }

  public Game playerTwoScores(Game game) {
    final Tuple2<GameScore, GameScore> updatedPlayerScores =
        updateGameScore.apply(game.getPlayerTwoScore(), game.getPlayerOneScore());
    if (GameScore.WIN_GAME.equals(updatedPlayerScores._1())) {
      return game.toBuilder()
          .playerOneScore(updatedPlayerScores._2())
          .playerTwoScore(updatedPlayerScores._1())
          .state(State.FINISHED)
          .build();
    } else {
      return game.toBuilder()
          .playerOneScore(updatedPlayerScores._2())
          .playerTwoScore(updatedPlayerScores._1())
          .state(State.ONGOING)
          .build();
    }
  }
}
