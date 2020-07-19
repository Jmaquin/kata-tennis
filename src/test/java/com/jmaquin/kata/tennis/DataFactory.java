package com.jmaquin.kata.tennis;

import com.jmaquin.kata.tennis.domain.Game;
import com.jmaquin.kata.tennis.domain.GameScore;

public class DataFactory {
  public static Game aGame(GameScore aPlayerOneScore, GameScore aPlayerTwoScore) {
    return Game.builder().playerOneScore(aPlayerOneScore).playerTwoScore(aPlayerTwoScore).build();
  }
}
