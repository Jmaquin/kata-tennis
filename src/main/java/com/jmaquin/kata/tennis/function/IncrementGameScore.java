package com.jmaquin.kata.tennis.function;

import com.jmaquin.kata.tennis.domain.GameScore;
import java.util.Objects;
import java.util.function.BiFunction;

public class IncrementGameScore implements BiFunction<GameScore, Boolean, GameScore> {
  @Override
  public GameScore apply(GameScore gameScore, Boolean isDeuce) {
    Objects.requireNonNull(isDeuce);
    return gameScore.increment(isDeuce);
  }
}
