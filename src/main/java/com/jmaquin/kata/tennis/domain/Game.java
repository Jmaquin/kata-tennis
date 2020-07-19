package com.jmaquin.kata.tennis.domain;

import com.jmaquin.kata.tennis.domain.enums.GameScore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Game {
  @Builder.Default private final GameScore playerOneScore = GameScore.ZERO;
  @Builder.Default private final GameScore playerTwoScore = GameScore.ZERO;
}
