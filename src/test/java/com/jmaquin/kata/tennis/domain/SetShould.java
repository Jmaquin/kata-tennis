package com.jmaquin.kata.tennis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jmaquin.kata.tennis.domain.enums.SetScore;
import org.junit.jupiter.api.Test;

class SetShould {

  @Test
  void start_with_a_score_of_zero_for_each_player() {
    // Given

    // When
    final Set result = Set.builder().build();

    // Then
    assertThat(result.getPlayerOneScore()).isEqualTo(SetScore.ZERO);
    assertThat(result.getPlayerTwoScore()).isEqualTo(SetScore.ZERO);
  }
}
