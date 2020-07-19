package com.jmaquin.kata.tennis.domain;

import com.jmaquin.kata.tennis.domain.enums.State;
import com.jmaquin.kata.tennis.domain.enums.Winner;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Match {
  @Builder.Default private final Set set = Set.builder().build();
  @Builder.Default private final State state = State.NOT_STARTED;
  @Builder.Default private final Winner winner = Winner.UNKNOWN;
}
