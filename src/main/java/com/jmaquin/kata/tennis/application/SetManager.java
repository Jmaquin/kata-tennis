package com.jmaquin.kata.tennis.application;

import com.jmaquin.kata.tennis.domain.Set;

public class SetManager {
  public Set playerOneWinGame(Set set) {
    return set.toBuilder()
        .playerOneScore(set.getPlayerOneScore().increment(set.getPlayerTwoScore()))
        .build();
  }

  public Set playerTwoWinGame(Set set) {
    return set.toBuilder()
        .playerTwoScore(set.getPlayerTwoScore().increment(set.getPlayerOneScore()))
        .build();
  }
}
