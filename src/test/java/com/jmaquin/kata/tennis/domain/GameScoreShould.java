package com.jmaquin.kata.tennis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GameScoreShould {

  private static Stream<Arguments> providePropertyBasedTestingArguments() {
    return Stream.of(
        Arguments.of(GameScore.ZERO, GameScore.FIFTEEN),
        Arguments.of(GameScore.FIFTEEN, GameScore.THIRTY),
        Arguments.of(GameScore.THIRTY, GameScore.FORTY),
        Arguments.of(GameScore.FORTY, GameScore.WIN_GAME),
        Arguments.of(GameScore.WIN_GAME, GameScore.WIN_GAME));
  }

  @ParameterizedTest
  @MethodSource("providePropertyBasedTestingArguments")
  void change_to_correct_next_value(GameScore initialGameScore, GameScore expectedGameScore) {
    // Given

    // When
    final GameScore result = initialGameScore.nextValue();

    // Then
    assertThat(result).isEqualTo(expectedGameScore);
  }
}
