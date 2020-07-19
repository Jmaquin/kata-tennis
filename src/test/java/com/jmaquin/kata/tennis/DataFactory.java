package com.jmaquin.kata.tennis;

import com.jmaquin.kata.tennis.domain.Game;
import com.jmaquin.kata.tennis.domain.Set;
import com.jmaquin.kata.tennis.domain.TieBreakGame;
import com.jmaquin.kata.tennis.domain.enums.GameScore;
import com.jmaquin.kata.tennis.domain.enums.SetScore;
import com.jmaquin.kata.tennis.domain.enums.Status;

public class DataFactory {
  public static Game aGame(GameScore aPlayerOneScore, GameScore aPlayerTwoScore) {
    return Game.builder().playerOneScore(aPlayerOneScore).playerTwoScore(aPlayerTwoScore).build();
  }

  public static TieBreakGame aTieBreakGame(
      int aPlayerOneScore, int aPlayerTwoScore, Status aStatus) {
    return TieBreakGame.builder()
        .playerOneScore(aPlayerOneScore)
        .playerTwoScore(aPlayerTwoScore)
        .status(aStatus)
        .build();
  }

  public static Set aSet(SetScore aPlayerOneScore, SetScore aPlayerTwoScore) {
    return Set.builder().playerOneScore(aPlayerOneScore).playerTwoScore(aPlayerTwoScore).build();
  }
}
