package com.jmaquin.kata.tennis.function;

import static org.assertj.core.api.Assertions.assertThat;

import com.jmaquin.kata.tennis.domain.enums.SetScore;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.jqwik.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IncrementSetScoreShould {
  private final IncrementSetScore incrementSetScore = new IncrementSetScore();

  private static Stream<Arguments> provideArguments() {
    return Stream.of(
        Arguments.of(SetScore.SIX, SetScore.FIVE),
        Arguments.of(SetScore.SIX, SetScore.SIX),
        Arguments.of(SetScore.SEVEN, SetScore.FIVE),
        Arguments.of(SetScore.SEVEN, SetScore.SIX));
  }

  @Provide
  private Arbitrary<Tuple.Tuple2<SetScore, SetScore>> setScores() {
    final List<SetScore> scorerSetScores = List.of(SetScore.values());
    final List<SetScore> opponentSetScores = List.of(SetScore.values());
    final Collection<Tuple.Tuple2<SetScore, SetScore>> scores =
        scorerSetScores.stream()
            .flatMap(
                scorerGameScore ->
                    opponentSetScores.stream()
                        .map(opponentGameScore -> Tuple.of(scorerGameScore, opponentGameScore)))
            .filter(tuple -> !Tuple.of(SetScore.SIX, SetScore.FIVE).equals(tuple))
            .filter(tuple -> !Tuple.of(SetScore.SIX, SetScore.SIX).equals(tuple))
            .filter(tuple -> !Tuple.of(SetScore.SEVEN, SetScore.FIVE).equals(tuple))
            .filter(tuple -> !Tuple.of(SetScore.SEVEN, SetScore.SIX).equals(tuple))
            .collect(Collectors.toList());
    return Arbitraries.of(scores);
  }

  @Property
  void score_game_for_player_one_when_player_two_score_is_less_than_five(
      @ForAll("setScores") Tuple.Tuple2<SetScore, SetScore> setScores) {
    // Given

    // When
    final SetScore result = incrementSetScore.apply(setScores.get1(), setScores.get2());

    // Then
    assertThat(result).isEqualTo(setScores.get1().increment(setScores.get2()));
    assertThat(result).isNotEqualTo(SetScore.SEVEN);
  }

  @ParameterizedTest
  @MethodSource("provideArguments")
  void score_game_for_player_one_when_player_two_score_is_more_than_four(
      SetScore playerOneScore, SetScore playerTwoScore) {
    // Given

    // When
    final SetScore result = incrementSetScore.apply(playerOneScore, playerTwoScore);

    // Then
    assertThat(result).isEqualTo(playerOneScore.increment(playerTwoScore));
    assertThat(result).isEqualTo(SetScore.SEVEN);
  }
}
