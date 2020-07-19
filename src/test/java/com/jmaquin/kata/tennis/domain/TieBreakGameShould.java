package com.jmaquin.kata.tennis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TieBreakGameShould {
  @Test
  void start_with_a_score_of_zero_for_each_player() {
    // Given

    // When
    final TieBreakGame result = TieBreakGame.builder().build();

    // Then
    assertThat(result.getPlayerOneScore()).isEqualTo(0);
    assertThat(result.getPlayerTwoScore()).isEqualTo(0);
  }
}
