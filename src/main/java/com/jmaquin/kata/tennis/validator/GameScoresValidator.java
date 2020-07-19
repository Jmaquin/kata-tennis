package com.jmaquin.kata.tennis.validator;

import static io.vavr.API.*;

import com.jmaquin.kata.tennis.domain.GameScore;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Validation;
import java.util.function.BiFunction;

public class GameScoresValidator
    implements BiFunction<GameScore, GameScore, Validation<String, Tuple2<GameScore, GameScore>>> {
  @Override
  public Validation<String, Tuple2<GameScore, GameScore>> apply(
      GameScore scorerScore, GameScore opponentScore) {
    final Validation<String, Tuple2<GameScore, GameScore>> invalid =
        Validation.invalid(
            String.format(
                "Score and opponent scores are not coherent, %s - %s is not a possible score",
                scorerScore, opponentScore));

    return Match(Tuple.of(scorerScore, opponentScore))
        .of(
            Case($(Tuple.of(GameScore.ZERO, GameScore.DEUCE)), () -> invalid),
            Case($(Tuple.of(GameScore.ZERO, GameScore.ADVANTAGE)), () -> invalid),
            Case($(Tuple.of(GameScore.FIFTEEN, GameScore.DEUCE)), () -> invalid),
            Case($(Tuple.of(GameScore.FIFTEEN, GameScore.ADVANTAGE)), () -> invalid),
            Case($(Tuple.of(GameScore.THIRTY, GameScore.ADVANTAGE)), () -> invalid),
            Case($(Tuple.of(GameScore.THIRTY, GameScore.DEUCE)), () -> invalid),
            Case($(Tuple.of(GameScore.FORTY, GameScore.FORTY)), () -> invalid),
            Case($(Tuple.of(GameScore.FORTY, GameScore.DEUCE)), () -> invalid),
            Case($(Tuple.of(GameScore.DEUCE, GameScore.ZERO)), () -> invalid),
            Case($(Tuple.of(GameScore.DEUCE, GameScore.FIFTEEN)), () -> invalid),
            Case($(Tuple.of(GameScore.DEUCE, GameScore.THIRTY)), () -> invalid),
            Case($(Tuple.of(GameScore.DEUCE, GameScore.FORTY)), () -> invalid),
            Case($(Tuple.of(GameScore.DEUCE, GameScore.ADVANTAGE)), () -> invalid),
            Case($(Tuple.of(GameScore.ADVANTAGE, GameScore.ZERO)), () -> invalid),
            Case($(Tuple.of(GameScore.ADVANTAGE, GameScore.FIFTEEN)), () -> invalid),
            Case($(Tuple.of(GameScore.ADVANTAGE, GameScore.THIRTY)), () -> invalid),
            Case($(Tuple.of(GameScore.ADVANTAGE, GameScore.DEUCE)), () -> invalid),
            Case($(), () -> Validation.valid(Tuple.of(scorerScore, opponentScore))));
  }
}
