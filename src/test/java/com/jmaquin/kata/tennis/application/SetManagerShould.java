package com.jmaquin.kata.tennis.application;

import static com.jmaquin.kata.tennis.DataFactory.aSet;
import static org.assertj.core.api.Assertions.assertThat;

import com.jmaquin.kata.tennis.domain.Set;
import com.jmaquin.kata.tennis.domain.enums.SetScore;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.jqwik.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SetManagerShould {
  private final SetManager setManager = new SetManager();

  private static Stream<Arguments> provideArgumentsPlayerOneScores() {
    return Stream.of(
        Arguments.of(SetScore.SIX, SetScore.FIVE),
        Arguments.of(SetScore.SIX, SetScore.SIX),
        Arguments.of(SetScore.SEVEN, SetScore.FIVE),
        Arguments.of(SetScore.SEVEN, SetScore.SIX));
  }

  private static Stream<Arguments> provideArgumentsPlayerTwoScores() {
    return Stream.of(
        Arguments.of(SetScore.FIVE, SetScore.SIX),
        Arguments.of(SetScore.SIX, SetScore.SIX),
        Arguments.of(SetScore.FIVE, SetScore.SEVEN),
        Arguments.of(SetScore.SIX, SetScore.SEVEN));
  }

  @Provide
  private Arbitrary<Tuple.Tuple2<SetScore, SetScore>> setScoresPlayerOne() {
    final List<SetScore> scorerSetScores = List.of(SetScore.values());
    final List<SetScore> opponentSetScores = List.of(SetScore.values());
    final Collection<Tuple.Tuple2<SetScore, SetScore>> validGameScores =
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
    return Arbitraries.of(validGameScores);
  }

  @Property
  void score_game_for_player_one_when_player_two_score_is_less_than_five(
      @ForAll("setScoresPlayerOne") Tuple.Tuple2<SetScore, SetScore> setScores) {
    // Given
    final Set aSet = aSet(setScores.get1(), setScores.get2());

    // When
    final Set result = setManager.playerOneWinGame(aSet);

    // Then
    final Set expected = aSet(setScores.get1().increment(setScores.get2()), setScores.get2());
    assertThat(result).isEqualTo(expected);
    assertThat(result.getPlayerOneScore()).isNotEqualTo(SetScore.SEVEN);
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsPlayerOneScores")
  void score_game_for_player_one_when_player_two_score_is_more_than_four(
      SetScore playerOneScore, SetScore playerTwoScore) {
    // Given
    final Set aSet = aSet(playerOneScore, playerTwoScore);

    // When
    final Set result = setManager.playerOneWinGame(aSet);

    // Then
    final Set expected = aSet(playerOneScore.increment(playerTwoScore), playerTwoScore);
    assertThat(result).isEqualTo(expected);
    assertThat(result.getPlayerOneScore()).isEqualTo(SetScore.SEVEN);
  }

  @Provide
  private Arbitrary<Tuple.Tuple2<SetScore, SetScore>> setScoresPlayerTwo() {
    final List<SetScore> scorerSetScores = List.of(SetScore.values());
    final List<SetScore> opponentSetScores = List.of(SetScore.values());
    final Collection<Tuple.Tuple2<SetScore, SetScore>> validGameScores =
        scorerSetScores.stream()
            .flatMap(
                scorerGameScore ->
                    opponentSetScores.stream()
                        .map(opponentGameScore -> Tuple.of(scorerGameScore, opponentGameScore)))
            .filter(tuple -> !Tuple.of(SetScore.FIVE, SetScore.SIX).equals(tuple))
            .filter(tuple -> !Tuple.of(SetScore.SIX, SetScore.SIX).equals(tuple))
            .filter(tuple -> !Tuple.of(SetScore.FIVE, SetScore.SEVEN).equals(tuple))
            .filter(tuple -> !Tuple.of(SetScore.SIX, SetScore.SEVEN).equals(tuple))
            .collect(Collectors.toList());
    return Arbitraries.of(validGameScores);
  }

  @Property
  void score_game_for_player_two_when_player_one_score_is_less_than_five(
      @ForAll("setScoresPlayerTwo") Tuple.Tuple2<SetScore, SetScore> setScores) {
    // Given
    final Set aSet = aSet(setScores.get1(), setScores.get2());

    // When
    final Set result = setManager.playerTwoWinGame(aSet);

    // Then
    final Set expected = aSet(setScores.get1(), setScores.get2().increment(setScores.get1()));
    assertThat(result).isEqualTo(expected);
    assertThat(result.getPlayerTwoScore()).isNotEqualTo(SetScore.SEVEN);
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsPlayerTwoScores")
  void score_game_for_player_two_when_player_one_score_is_more_than_four(
      SetScore playerOneScore, SetScore playerTwoScore) {
    // Given
    final Set aSet = aSet(playerOneScore, playerTwoScore);

    // When
    final Set result = setManager.playerTwoWinGame(aSet);

    // Then
    final Set expected = aSet(playerOneScore, playerTwoScore.increment(playerOneScore));
    assertThat(result).isEqualTo(expected);
    assertThat(result.getPlayerTwoScore()).isEqualTo(SetScore.SEVEN);
  }
}
