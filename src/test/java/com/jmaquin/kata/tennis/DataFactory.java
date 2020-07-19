package com.jmaquin.kata.tennis;

import com.jmaquin.kata.tennis.domain.Game;
import com.jmaquin.kata.tennis.domain.Set;
import com.jmaquin.kata.tennis.domain.enums.GameScore;
import com.jmaquin.kata.tennis.domain.enums.SetScore;

public class DataFactory {
  public static Game aGame(GameScore aPlayerOneScore, GameScore aPlayerTwoScore) {
    return Game.builder().playerOneScore(aPlayerOneScore).playerTwoScore(aPlayerTwoScore).build();
  }

  public static Set aSet(SetScore aPlayerOneScore, SetScore aPlayerTwoScore) {
    return Set.builder().playerOneScore(aPlayerOneScore).playerTwoScore(aPlayerTwoScore).build();
  }
}
