package com.jmaquin.kata.tennis.domain;

import com.jmaquin.kata.tennis.domain.enums.State;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class TieBreakGame {
  private final int playerOneScore;
  private final int playerTwoScore;
  @Builder.Default private final State state = State.NOT_STARTED;
}
