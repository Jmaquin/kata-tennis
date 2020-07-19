package com.jmaquin.kata.tennis.application;

import com.jmaquin.kata.tennis.domain.TieBreakGame;
import com.jmaquin.kata.tennis.domain.enums.Status;

public class TieBreakGameManager {
  public TieBreakGame playerOneScores(TieBreakGame tieBreakGame) {
    final int updatedPlayerOneScore = tieBreakGame.getPlayerOneScore() + 1;
    if (updatedPlayerOneScore >= 6
        && (updatedPlayerOneScore - tieBreakGame.getPlayerTwoScore()) >= 2) {
      return tieBreakGame
          .toBuilder()
          .playerOneScore(updatedPlayerOneScore)
          .status(Status.FINISHED)
          .build();
    } else {
      return tieBreakGame
          .toBuilder()
          .playerOneScore(updatedPlayerOneScore)
          .status(Status.ONGOING)
          .build();
    }
  }

  public TieBreakGame playerTwoScores(TieBreakGame tieBreakGame) {
    final int updatedPlayerTwoScore = tieBreakGame.getPlayerTwoScore() + 1;

    if (updatedPlayerTwoScore >= 6
        && (updatedPlayerTwoScore - tieBreakGame.getPlayerOneScore()) >= 2) {
      return tieBreakGame
          .toBuilder()
          .playerTwoScore(updatedPlayerTwoScore)
          .status(Status.FINISHED)
          .build();
    } else {
      return tieBreakGame
          .toBuilder()
          .playerTwoScore(updatedPlayerTwoScore)
          .status(Status.ONGOING)
          .build();
    }
  }
}
