package com.jmaquin.kata.tennis.validator;

import static org.assertj.core.api.Assertions.assertThat;

import com.jmaquin.kata.tennis.domain.GameScore;
import io.vavr.Tuple2;
import io.vavr.control.Validation;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.jqwik.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GameScoreValidatorShould {
  private final GameScoresValidator gameScoresValidator = new GameScoresValidator();

  private static Stream<Arguments> provideArguments() {
    return Stream.of(
        Arguments.of(GameScore.ZERO, GameScore.DEUCE),
        Arguments.of(GameScore.ZERO, GameScore.ADVANTAGE),
        Arguments.of(GameScore.FIFTEEN, GameScore.DEUCE),
        Arguments.of(GameScore.FIFTEEN, GameScore.ADVANTAGE),
        Arguments.of(GameScore.THIRTY, GameScore.ADVANTAGE),
        Arguments.of(GameScore.THIRTY, GameScore.DEUCE),
        Arguments.of(GameScore.FORTY, GameScore.FORTY),
        Arguments.of(GameScore.FORTY, GameScore.DEUCE),
        Arguments.of(GameScore.DEUCE, GameScore.ZERO),
        Arguments.of(GameScore.DEUCE, GameScore.FIFTEEN),
        Arguments.of(GameScore.DEUCE, GameScore.THIRTY),
        Arguments.of(GameScore.DEUCE, GameScore.FORTY),
        Arguments.of(GameScore.DEUCE, GameScore.ADVANTAGE),
        Arguments.of(GameScore.ADVANTAGE, GameScore.ZERO),
        Arguments.of(GameScore.ADVANTAGE, GameScore.FIFTEEN),
        Arguments.of(GameScore.ADVANTAGE, GameScore.THIRTY),
        Arguments.of(GameScore.ADVANTAGE, GameScore.DEUCE));
  }

  @ParameterizedTest
  @MethodSource("provideArguments")
  void return_invalid(GameScore aScorerGameScore, GameScore anOpponentGameScore) {
    // Given

    // When
    final Validation<String, Tuple2<GameScore, GameScore>> result =
        gameScoresValidator.apply(aScorerGameScore, anOpponentGameScore);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.isInvalid()).isTrue();
    assertThat(result.getError())
        .isEqualTo(
            String.format(
                "Score and opponent scores are not coherent, %s - %s is not a possible score",
                aScorerGameScore, anOpponentGameScore));
  }

  @Provide
  Arbitrary<Tuple.Tuple2<GameScore, GameScore>> validGameScores() {
    List<GameScore> scorerGameScores = List.of(GameScore.values());
    List<GameScore> opponentGameScores = List.of(GameScore.values());
    final Collection<Tuple.Tuple2<GameScore, GameScore>> validGameScores =
        scorerGameScores.stream()
            .flatMap(
                scorerGameScore ->
                    opponentGameScores.stream()
                        .map(opponentGameScore -> Tuple.of(scorerGameScore, opponentGameScore)))
            .filter(tuple -> !Tuple.of(GameScore.ZERO, GameScore.DEUCE).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.ZERO, GameScore.ADVANTAGE).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.FIFTEEN, GameScore.DEUCE).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.FIFTEEN, GameScore.ADVANTAGE).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.THIRTY, GameScore.ADVANTAGE).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.THIRTY, GameScore.DEUCE).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.FORTY, GameScore.FORTY).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.FORTY, GameScore.DEUCE).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.DEUCE, GameScore.ZERO).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.DEUCE, GameScore.FIFTEEN).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.DEUCE, GameScore.THIRTY).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.DEUCE, GameScore.FORTY).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.DEUCE, GameScore.ADVANTAGE).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.ADVANTAGE, GameScore.ZERO).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.ADVANTAGE, GameScore.FIFTEEN).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.ADVANTAGE, GameScore.THIRTY).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.ADVANTAGE, GameScore.DEUCE).equals(tuple))
            .collect(Collectors.toList());
    return Arbitraries.of(validGameScores);
  }

  @Property
  boolean return_valid(@ForAll("validGameScores") Tuple.Tuple2<GameScore, GameScore> gameScores) {
    return gameScoresValidator.apply(gameScores.get1(), gameScores.get2()).isValid();
  }
}
