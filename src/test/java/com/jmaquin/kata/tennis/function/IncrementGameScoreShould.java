package com.jmaquin.kata.tennis.function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jmaquin.kata.tennis.domain.enums.GameScore;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class IncrementGameScoreShould {
  private final IncrementGameScore incrementGameScore = new IncrementGameScore();

  private static Stream<Arguments> provideArgumentsIncrementDeuceRuleDisabled() {
    return Stream.of(
        Arguments.of(GameScore.ZERO, GameScore.FIFTEEN),
        Arguments.of(GameScore.FIFTEEN, GameScore.THIRTY),
        Arguments.of(GameScore.THIRTY, GameScore.FORTY),
        Arguments.of(GameScore.FORTY, GameScore.WIN_GAME),
        Arguments.of(GameScore.WIN_GAME, GameScore.WIN_GAME));
  }

  private static Stream<Arguments> provideArgumentsIncrementDeuceRuleEnabled() {
    return Stream.of(
        Arguments.of(GameScore.FORTY, GameScore.DEUCE),
        Arguments.of(GameScore.DEUCE, GameScore.ADVANTAGE),
        Arguments.of(GameScore.WIN_GAME, GameScore.WIN_GAME));
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsIncrementDeuceRuleDisabled")
  void change_to_correct_next_value_when_deuce_rule_disabled(
      GameScore initialGameScore, GameScore expectedGameScore) {
    // Given
    final boolean isDeuce = false;

    // When
    final GameScore result = incrementGameScore.apply(initialGameScore, isDeuce);

    // Then
    assertThat(result).isEqualTo(expectedGameScore);
  }

  @ParameterizedTest
  @EnumSource(
      value = GameScore.class,
      names = {"DEUCE", "ADVANTAGE"})
  void throw_illegal_argument_exception_when_increment_deuce_rule_disabled(GameScore aGameScore) {
    // Given
    final boolean isDeuce = false;

    // When
    assertThatThrownBy(() -> incrementGameScore.apply(aGameScore, isDeuce))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(
            String.format(
                "Can't increment value on %s when deuce rule disabled", aGameScore.name()));

    // Then
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsIncrementDeuceRuleEnabled")
  void change_to_correct_next_value_when_deuce_rule_enable(
      GameScore initialGameScore, GameScore expectedGameScore) {
    // Given
    final boolean isDeuce = true;

    // When
    final GameScore result = incrementGameScore.apply(initialGameScore, isDeuce);

    // Then
    assertThat(result).isEqualTo(expectedGameScore);
  }

  @ParameterizedTest
  @EnumSource(
      value = GameScore.class,
      names = {"ZERO", "FIFTEEN", "THIRTY"})
  void throw_illegal_argument_exception_when_increment_deuce_rule_enabled(GameScore aGameScore) {
    // Given
    final boolean isDeuce = true;

    // When
    assertThatThrownBy(() -> incrementGameScore.apply(aGameScore, isDeuce))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(
            String.format(
                "Can't increment value on %s when deuce rule enabled", aGameScore.name()));

    // Then
  }

  @Test
  void thrown_npe() {
    // Given
    final GameScore aGameScore = GameScore.getRandomScore();

    // When
    assertThatThrownBy(() -> incrementGameScore.apply(aGameScore, null))
        .isInstanceOf(NullPointerException.class);

    // Then
  }
}
