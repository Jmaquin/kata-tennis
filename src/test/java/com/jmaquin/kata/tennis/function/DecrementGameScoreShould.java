package com.jmaquin.kata.tennis.function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jmaquin.kata.tennis.domain.GameScore;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class DecrementGameScoreShould {
  private final DecrementGameScore decrementGameScore = new DecrementGameScore();

  private static Stream<Arguments> provideArgumentsDecrementDeuceRuleEnabled() {
    return Stream.of(
        Arguments.of(GameScore.ADVANTAGE, GameScore.DEUCE),
        Arguments.of(GameScore.DEUCE, GameScore.FORTY));
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsDecrementDeuceRuleEnabled")
  void change_to_correct_previous_value_when_deuce_rule_enable(
      GameScore initialGameScore, GameScore expectedGameScore) {
    // Given
    final boolean isDeuce = true;

    // When
    final GameScore result = decrementGameScore.apply(initialGameScore, isDeuce);

    // Then
    assertThat(result).isEqualTo(expectedGameScore);
  }

  @ParameterizedTest
  @EnumSource(
      value = GameScore.class,
      names = {"ZERO", "FIFTEEN", "THIRTY"})
  void throw_illegal_argument_exception_when_decrement_deuce_rule_enabled(GameScore aGameScore) {
    // Given
    final boolean isDeuce = true;

    // When
    assertThatThrownBy(() -> decrementGameScore.apply(aGameScore, isDeuce))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(
            String.format(
                "Can't decrement value on %s when deuce rule enabled", aGameScore.name()));

    // Then
  }

  @Test
  void not_change_value() {
    // Given
    final boolean isDeuce = false;
    final GameScore aGameScore = GameScore.WIN_GAME;

    // When
    final GameScore result = decrementGameScore.apply(aGameScore, isDeuce);

    // Then
    assertThat(result).isEqualTo(GameScore.WIN_GAME);
  }

  @ParameterizedTest
  @EnumSource(
      value = GameScore.class,
      names = {"ZERO", "FIFTEEN", "THIRTY", "FORTY"})
  void throw_illegal_argument_exception_when_decrement_deuce_rule_disabled(GameScore aGameScore) {
    // Given
    final boolean isDeuce = false;

    // When
    assertThatThrownBy(() -> decrementGameScore.apply(aGameScore, isDeuce))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Can't decrement value when deuce rule disabled");

    // Then
  }

  @Test
  void thrown_npe() {
    // Given
    final GameScore aGameScore = GameScore.getRandomScore();

    // When
    assertThatThrownBy(() -> decrementGameScore.apply(aGameScore, null))
        .isInstanceOf(NullPointerException.class);

    // Then
  }
}
