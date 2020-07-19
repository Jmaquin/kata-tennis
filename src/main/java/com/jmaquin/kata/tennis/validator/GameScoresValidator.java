package com.jmaquin.kata.tennis.validator;

import static io.vavr.API.*;
import static io.vavr.Predicates.isIn;

import com.jmaquin.kata.tennis.domain.enums.GameScore;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Validation;
import java.util.function.BiFunction;

public class GameScoresValidator
    implements BiFunction<GameScore, GameScore, Validation<String, Tuple2<GameScore, GameScore>>> {
  @Override
  public Validation<String, Tuple2<GameScore, GameScore>> apply(
      GameScore scorerScore, GameScore opponentScore) {
    return Match(Tuple.of(scorerScore, opponentScore))
        .of(
            Case(
                $(
                    isIn(
                        Tuple.of(GameScore.ZERO, GameScore.DEUCE),
                        Tuple.of(GameScore.ZERO, GameScore.ADVANTAGE),
                        Tuple.of(GameScore.FIFTEEN, GameScore.DEUCE),
                        Tuple.of(GameScore.FIFTEEN, GameScore.ADVANTAGE),
                        Tuple.of(GameScore.THIRTY, GameScore.ADVANTAGE),
                        Tuple.of(GameScore.THIRTY, GameScore.DEUCE),
                        Tuple.of(GameScore.FORTY, GameScore.FORTY),
                        Tuple.of(GameScore.FORTY, GameScore.DEUCE),
                        Tuple.of(GameScore.DEUCE, GameScore.ZERO),
                        Tuple.of(GameScore.DEUCE, GameScore.FIFTEEN),
                        Tuple.of(GameScore.DEUCE, GameScore.THIRTY),
                        Tuple.of(GameScore.DEUCE, GameScore.FORTY),
                        Tuple.of(GameScore.DEUCE, GameScore.ADVANTAGE),
                        Tuple.of(GameScore.ADVANTAGE, GameScore.ZERO),
                        Tuple.of(GameScore.ADVANTAGE, GameScore.FIFTEEN),
                        Tuple.of(GameScore.ADVANTAGE, GameScore.THIRTY),
                        Tuple.of(GameScore.ADVANTAGE, GameScore.DEUCE))),
                () ->
                    Validation.invalid(
                        String.format(
                            "Score and opponent scores are not coherent, %s - %s is not a possible score",
                            scorerScore, opponentScore))),
            Case($(), () -> Validation.valid(Tuple.of(scorerScore, opponentScore))));
  }
}
