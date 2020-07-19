package com.jmaquin.kata.tennis.domain;

import com.jmaquin.kata.tennis.domain.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class TieBreakGame {
  private final int playerOneScore;
  private final int playerTwoScore;
  private final Status status;
}
