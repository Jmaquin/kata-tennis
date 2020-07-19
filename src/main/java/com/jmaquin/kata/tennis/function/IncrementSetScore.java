package com.jmaquin.kata.tennis.function;

import com.jmaquin.kata.tennis.domain.enums.SetScore;
import java.util.function.BiFunction;

public class IncrementSetScore implements BiFunction<SetScore, SetScore, SetScore> {
  @Override
  public SetScore apply(SetScore scorerScore, SetScore opponentScore) {
    return scorerScore.increment(opponentScore);
  }
}
