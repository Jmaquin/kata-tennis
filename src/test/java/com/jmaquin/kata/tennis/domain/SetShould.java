package com.jmaquin.kata.tennis.domain;

import static com.jmaquin.kata.tennis.DataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.jmaquin.kata.tennis.domain.enums.SetScore;
import com.jmaquin.kata.tennis.domain.enums.SetState;
import org.junit.jupiter.api.Test;

class SetShould {

  @Test
  void start_with_correct_initial_state() {
    // Given

    // When
    final Set result = Set.builder().build();

    // Then
    final Set expected =
        aSet(SetScore.ZERO, SetScore.ZERO, aGame(), aTieBreakGame(), SetState.NOT_STARTED);
    assertThat(result).isEqualTo(expected);
  }
}
