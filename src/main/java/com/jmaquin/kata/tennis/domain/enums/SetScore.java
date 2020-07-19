package com.jmaquin.kata.tennis.domain.enums;

import static io.vavr.API.*;
import static io.vavr.Predicates.isIn;

import io.vavr.Tuple;
import java.util.Random;

public enum SetScore {
  ZERO,
  ONE,
  TWO,
  THREE,
  FOUR,
  FIVE,
  SIX,
  SEVEN;

  private static final SetScore[] vals = values();

  public static SetScore getRandomScore() {
    Random random = new Random();
    return vals[random.nextInt(vals.length)];
  }

  public SetScore increment(SetScore opponentScore) {
    return Match(Tuple.of(this, opponentScore))
        .of(
            Case(
                $(
                    isIn(
                        Tuple.of(SetScore.FIVE, SetScore.SEVEN),
                        Tuple.of(SetScore.SIX, SetScore.ZERO),
                        Tuple.of(SetScore.SIX, SetScore.ONE),
                        Tuple.of(SetScore.SIX, SetScore.TWO),
                        Tuple.of(SetScore.SIX, SetScore.THREE),
                        Tuple.of(SetScore.SIX, SetScore.FOUR),
                        Tuple.of(SetScore.SIX, SetScore.SEVEN),
                        Tuple.of(SetScore.SEVEN, SetScore.SIX),
                        Tuple.of(SetScore.SEVEN, SetScore.FIVE))),
                this),
            Case($(), () -> vals[(this.ordinal() + 1) % vals.length]));
  }
}
