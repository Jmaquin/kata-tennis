package com.jmaquin.kata.tennis.domain;

public enum GameScore {
  ZERO,
  FIFTEEN,
  THIRTY,
  FORTY,
  WIN_GAME;

  private static final GameScore[] vals = values();

  public GameScore nextValue() {
    if (this.equals(GameScore.WIN_GAME)) {
      return this;
    } else {
      return vals[(this.ordinal() + 1) % vals.length];
    }
  }
}
