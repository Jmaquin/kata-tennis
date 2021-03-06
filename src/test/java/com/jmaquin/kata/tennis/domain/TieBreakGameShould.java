package com.jmaquin.kata.tennis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jmaquin.kata.tennis.DataFactory;
import com.jmaquin.kata.tennis.domain.enums.State;
import org.junit.jupiter.api.Test;

class TieBreakGameShould {
  @Test
  void start_with_correct_initial_state() {
    // Given

    // When
    final TieBreakGame result = TieBreakGame.builder().build();

    // Then
    final TieBreakGame expected = DataFactory.aTieBreakGame(0, 0, State.NOT_STARTED);
    assertThat(result).isEqualTo(expected);
  }
}
