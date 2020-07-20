package com.jmaquin.kata.tennis.application;

import com.jmaquin.kata.tennis.domain.Match;
import com.jmaquin.kata.tennis.domain.Set;
import com.jmaquin.kata.tennis.domain.enums.SetState;
import com.jmaquin.kata.tennis.domain.enums.State;
import com.jmaquin.kata.tennis.domain.enums.Winner;
import java.util.Objects;

public class MatchManager {
  private final SetManager setManager;

  public MatchManager(SetManager setManager) {
    Objects.requireNonNull(setManager, "setManager must not be null!");
    this.setManager = setManager;
  }

  public Match playerOneScores(Match match) {
    if (State.NOT_STARTED.equals(match.getState())) {
      return match
          .toBuilder()
          .set(setManager.playerOneScores(match.getSet()))
          .state(State.ONGOING)
          .build();
    } else if (State.ONGOING.equals(match.getState())) {
      final Set updatedSet = setManager.playerOneScores(match.getSet());
      if (SetState.FINISHED.equals(updatedSet.getState())) {
        return match
            .toBuilder()
            .set(updatedSet)
            .state(State.FINISHED)
            .winner(Winner.PLAYER_1)
            .build();
      } else {
        return match.toBuilder().set(updatedSet).state(State.ONGOING).build();
      }
    } else {
      return match;
    }
  }

  public Match playerTwoScores(Match match) {
    if (State.NOT_STARTED.equals(match.getState())) {
      return match
          .toBuilder()
          .set(setManager.playerTwoScores(match.getSet()))
          .state(State.ONGOING)
          .build();
    } else if (State.ONGOING.equals(match.getState())) {
      final Set updatedSet = setManager.playerTwoScores(match.getSet());
      if (SetState.FINISHED.equals(updatedSet.getState())) {
        return match
            .toBuilder()
            .set(updatedSet)
            .state(State.FINISHED)
            .winner(Winner.PLAYER_2)
            .build();
      } else {
        return match.toBuilder().set(updatedSet).state(State.ONGOING).build();
      }
    } else {
      return match;
    }
  }
}
