package com.jmaquin.kata.tennis.domain;

import com.jmaquin.kata.tennis.domain.enums.SetScore;
import com.jmaquin.kata.tennis.domain.enums.SetState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Set {
  @Builder.Default private final SetScore playerOneScore = SetScore.ZERO;
  @Builder.Default private final SetScore playerTwoScore = SetScore.ZERO;
  @Builder.Default private final Game currentGame = Game.builder().build();
  @Builder.Default private final TieBreakGame tieBreakGame = TieBreakGame.builder().build();
  @Builder.Default private final SetState state = SetState.NOT_STARTED;
}
