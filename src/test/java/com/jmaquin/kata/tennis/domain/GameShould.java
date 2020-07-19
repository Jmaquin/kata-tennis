package com.jmaquin.kata.tennis.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jmaquin.kata.tennis.DataFactory;
import com.jmaquin.kata.tennis.domain.enums.GameScore;
import com.jmaquin.kata.tennis.domain.enums.State;
import org.junit.jupiter.api.Test;

class GameShould {

  @Test
  void start_with_correct_initial_state() {
    // Given

    // When
    final Game result = Game.builder().build();

    // Then
    final Game expected = DataFactory.aGame(GameScore.ZERO, GameScore.ZERO, State.NOT_STARTED);
    assertThat(result).isEqualTo(expected);
  }
}
