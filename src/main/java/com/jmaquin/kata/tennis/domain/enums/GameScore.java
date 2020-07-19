package com.jmaquin.kata.tennis.domain.enums;

import static io.vavr.API.*;
import static io.vavr.Predicates.isIn;

import java.util.Random;

public enum GameScore {
  ZERO,
  FIFTEEN,
  THIRTY,
  FORTY,
  DEUCE,
  ADVANTAGE,
  WIN_GAME;

  private static final GameScore[] vals = values();

  public static GameScore getRandomScore() {
    Random random = new Random();
    return vals[random.nextInt(vals.length)];
  }

  public GameScore increment(boolean isDeuce) {
    if (isDeuce) {
      return Match(this)
          .of(
              Case(
                  $(isIn(GameScore.ZERO, GameScore.FIFTEEN)),
                  gameScore -> {
                    throw new IllegalArgumentException(
                        String.format(
                            "Can't increment value on %s when deuce rule enabled",
                            gameScore.name()));
                  }),
              Case($(GameScore.THIRTY), GameScore.DEUCE),
              Case($(GameScore.WIN_GAME), this),
              Case($(), () -> vals[(this.ordinal() + 1) % vals.length]));
    } else {
      return Match(this)
          .of(
              Case(
                  $(isIn(GameScore.DEUCE, GameScore.ADVANTAGE)),
                  gameScore -> {
                    throw new IllegalArgumentException(
                        String.format(
                            "Can't increment value on %s when deuce rule disabled",
                            gameScore.name()));
                  }),
              Case($(GameScore.FORTY), GameScore.WIN_GAME),
              Case($(GameScore.WIN_GAME), this),
              Case($(), () -> vals[(this.ordinal() + 1) % vals.length]));
    }
  }

  public GameScore decrement(boolean isDeuce) {
    if (isDeuce) {
      return Match(this)
          .of(
              Case(
                  $(isIn(GameScore.ZERO, GameScore.FIFTEEN, GameScore.THIRTY)),
                  gameScore -> {
                    throw new IllegalArgumentException(
                        String.format(
                            "Can't decrement value on %s when deuce rule enabled",
                            gameScore.name()));
                  }),
              Case($(GameScore.FORTY), GameScore.DEUCE),
              Case($(GameScore.WIN_GAME), this),
              Case($(), () -> vals[(this.ordinal() - 1) % vals.length]));
    } else {
      return Match(this)
          .of(
              Case($(GameScore.WIN_GAME), this),
              Case(
                  $(),
                  () -> {
                    throw new IllegalArgumentException(
                        "Can't decrement value when deuce rule disabled");
                  }));
    }
  }
}
