package com.jmaquin.kata.tennis.function;

import static io.vavr.API.*;
import static io.vavr.Predicates.isIn;

import com.jmaquin.kata.tennis.domain.GameScore;
import com.jmaquin.kata.tennis.validator.GameScoresValidator;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Validation;
import java.util.Objects;
import java.util.function.BiFunction;

public class UpdateGameScore
    implements BiFunction<GameScore, GameScore, Tuple2<GameScore, GameScore>> {
  private final GameScoresValidator gameScoresValidator;
  private final IncrementGameScore incrementGameScore;
  private final DecrementGameScore decrementGameScore;

  public UpdateGameScore(
      GameScoresValidator gameScoresValidator,
      IncrementGameScore incrementGameScore,
      DecrementGameScore decrementGameScore) {
    Objects.requireNonNull(gameScoresValidator, "gameScoresValidator must not be null!");
    Objects.requireNonNull(incrementGameScore, "incrementGameScore must not be null!");
    Objects.requireNonNull(decrementGameScore, "decrementGameScore must not be null!");
    this.gameScoresValidator = gameScoresValidator;
    this.incrementGameScore = incrementGameScore;
    this.decrementGameScore = decrementGameScore;
  }

  @Override
  public Tuple2<GameScore, GameScore> apply(GameScore scorerScore, GameScore opponentScore) {
    final Validation<String, Tuple2<GameScore, GameScore>> validation =
        gameScoresValidator.apply(scorerScore, opponentScore);
    if (validation.isValid()) {
      return Match(validation.get())
          .of(
              Case(
                  $(
                      isIn(
                          Tuple.of(GameScore.THIRTY, GameScore.FORTY),
                          Tuple.of(GameScore.DEUCE, GameScore.DEUCE),
                          Tuple.of(GameScore.FORTY, GameScore.ADVANTAGE))),
                  () ->
                      Tuple.of(
                          incrementGameScore.apply(scorerScore, true),
                          decrementGameScore.apply(opponentScore, true))),
              Case(
                  $(Tuple.of(GameScore.ADVANTAGE, GameScore.FORTY)),
                  () -> Tuple.of(incrementGameScore.apply(scorerScore, true), opponentScore)),
              Case(
                  $(),
                  () -> Tuple.of(incrementGameScore.apply(scorerScore, false), opponentScore)));
    } else {
      throw new IllegalArgumentException(validation.getError());
    }
  }
}
