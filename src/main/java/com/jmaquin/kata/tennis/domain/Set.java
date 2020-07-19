package com.jmaquin.kata.tennis.domain;

import com.jmaquin.kata.tennis.domain.enums.SetScore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Set {
  @Builder.Default private final SetScore playerOneScore = SetScore.ZERO;
  @Builder.Default private final SetScore playerTwoScore = SetScore.ZERO;
}
