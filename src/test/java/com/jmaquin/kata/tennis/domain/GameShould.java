package com.jmaquin.kata.tennis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jmaquin.kata.tennis.domain.enums.GameScore;
import org.junit.jupiter.api.Test;

class GameShould {

  @Test
  void start_with_a_score_of_zero_for_each_player() {
    // Given

    // When
    final Game result = Game.builder().build();

    // Then
    assertThat(result.getPlayerOneScore()).isEqualTo(GameScore.ZERO);
    assertThat(result.getPlayerTwoScore()).isEqualTo(GameScore.ZERO);
  }
}
