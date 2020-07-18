package com.jmaquin.kata.tennis.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Game {
  private final GameScore playerOneScore = GameScore.ZERO;
  private final GameScore playerTwoScore = GameScore.ZERO;
}
