package com.jmaquin.kata.tennis.domain;

import static com.jmaquin.kata.tennis.DataFactory.aMatch;
import static com.jmaquin.kata.tennis.DataFactory.aSet;
import static org.assertj.core.api.Assertions.assertThat;

import com.jmaquin.kata.tennis.domain.enums.State;
import com.jmaquin.kata.tennis.domain.enums.Winner;
import org.junit.jupiter.api.Test;

class MatchShould {

  @Test
  void start_with_correct_initial_state() {
    // Given

    // When
    final Match result = Match.builder().build();

    // Then
    final Match expected = aMatch(aSet(), State.NOT_STARTED, Winner.UNKNOWN);
    assertThat(result).isEqualTo(expected);
  }
}
