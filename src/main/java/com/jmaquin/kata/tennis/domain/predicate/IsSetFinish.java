package com.jmaquin.kata.tennis.domain.predicate;

import com.jmaquin.kata.tennis.domain.Set;
import com.jmaquin.kata.tennis.domain.enums.SetScore;
import java.util.function.Predicate;

public class IsSetFinish implements Predicate<Set> {
  @Override
  public boolean test(Set set) {
    return (SetScore.SIX.equals(set.getPlayerOneScore())
            && set.getPlayerTwoScore().compareTo(SetScore.FIVE) < 0)
        || (SetScore.SEVEN.equals(set.getPlayerOneScore())
            && set.getPlayerTwoScore().compareTo(SetScore.FOUR) > 0)
        || (SetScore.SIX.equals(set.getPlayerTwoScore())
            && set.getPlayerOneScore().compareTo(SetScore.FIVE) < 0)
        || (SetScore.SEVEN.equals(set.getPlayerTwoScore())
            && set.getPlayerOneScore().compareTo(SetScore.FOUR) > 0);
  }
}
