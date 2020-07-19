package com.jmaquin.kata.tennis.application;

import static com.jmaquin.kata.tennis.DataFactory.aTieBreakGame;
import static org.assertj.core.api.Assertions.assertThat;

import com.jmaquin.kata.tennis.domain.TieBreakGame;
import com.jmaquin.kata.tennis.domain.enums.State;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.jqwik.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TieBreakGameManagerShould {
  private final TieBreakGameManager tieBreakGameManager = new TieBreakGameManager();

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, 3, 4})
  void end_game_when_player_one_has_six_point_and_two_point_difference_with_player_two(
      int aPlayerTwoScore) {
    // Given
    final int aPlayerOneScore = 5;
    final TieBreakGame aTieBreakGame =
        aTieBreakGame(aPlayerOneScore, aPlayerTwoScore, State.ONGOING);

    // When
    final TieBreakGame result = tieBreakGameManager.playerOneScores(aTieBreakGame);

    // Then
    final TieBreakGame expected = aTieBreakGame(6, aPlayerTwoScore, State.FINISHED);
    assertThat(result).isEqualTo(expected);
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, 3, 4})
  void end_game_when_player_two_has_six_point_and_two_point_difference_with_player_one(
      int aPlayerOneScore) {
    // Given
    final int aPlayerTwoScore = 5;
    final TieBreakGame aTieBreakGame =
        aTieBreakGame(aPlayerOneScore, aPlayerTwoScore, State.ONGOING);

    // When
    final TieBreakGame result = tieBreakGameManager.playerTwoScores(aTieBreakGame);

    // Then
    final TieBreakGame expected = aTieBreakGame(aPlayerOneScore, 6, State.FINISHED);
    assertThat(result).isEqualTo(expected);
  }

  @Provide
  private Arbitrary<Integer> playerScore() {
    return Arbitraries.integers().between(6, 100);
  }

  @Property
  void
      score_point_when_player_one_has_at_least_six_point_and_less_than_two_point_difference_with_player_two(
          @ForAll("playerScore") int aPlayerOneScore) {
    // Given
    final TieBreakGame aTieBreakGame =
        aTieBreakGame(aPlayerOneScore, aPlayerOneScore, State.ONGOING);

    // When
    final TieBreakGame result = tieBreakGameManager.playerOneScores(aTieBreakGame);

    // Then
    final TieBreakGame expected =
        aTieBreakGame(aPlayerOneScore + 1, aPlayerOneScore, State.ONGOING);
    assertThat(result).isEqualTo(expected);
  }

  @Property
  void end_game_when_player_one_has_at_least_six_point_and_two_point_difference_with_player_two(
      @ForAll("playerScore") int aPlayerOneScore) {
    // Given
    final int aPlayerTwoScore = aPlayerOneScore - 1;
    final TieBreakGame aTieBreakGame =
        aTieBreakGame(aPlayerOneScore, aPlayerTwoScore, State.ONGOING);

    // When
    final TieBreakGame result = tieBreakGameManager.playerOneScores(aTieBreakGame);

    // Then
    final TieBreakGame expected =
        aTieBreakGame(aPlayerOneScore + 1, aPlayerTwoScore, State.FINISHED);
    assertThat(result).isEqualTo(expected);
  }

  @Property
  void
      score_point_when_player_two_has_at_least_six_point_and_less_than_two_point_difference_with_player_one(
          @ForAll("playerScore") int aPlayerTwoScore) {
    // Given
    final TieBreakGame aTieBreakGame =
        aTieBreakGame(aPlayerTwoScore, aPlayerTwoScore, State.ONGOING);

    // When
    final TieBreakGame result = tieBreakGameManager.playerTwoScores(aTieBreakGame);

    // Then
    final TieBreakGame expected =
        aTieBreakGame(aPlayerTwoScore, aPlayerTwoScore + 1, State.ONGOING);
    assertThat(result).isEqualTo(expected);
  }

  @Property
  void end_game_when_player_two_has_at_least_six_point_and_two_point_difference_with_player_one(
      @ForAll("playerScore") int aPlayerTwoScore) {
    // Given
    final int aPlayerOneScore = aPlayerTwoScore - 1;
    final TieBreakGame aTieBreakGame =
        aTieBreakGame(aPlayerOneScore, aPlayerTwoScore, State.ONGOING);

    // When
    final TieBreakGame result = tieBreakGameManager.playerTwoScores(aTieBreakGame);

    // Then
    final TieBreakGame expected =
        aTieBreakGame(aPlayerOneScore, aPlayerTwoScore + 1, State.FINISHED);
    assertThat(result).isEqualTo(expected);
  }

  @Provide
  private Arbitrary<Tuple.Tuple2<Integer, Integer>> playerScores() {
    final List<Integer> playerOneScores =
        IntStream.range(0, 4).boxed().collect(Collectors.toList());
    final List<Integer> playerTwoScores =
        IntStream.range(0, 5).boxed().collect(Collectors.toList());
    final Collection<Tuple.Tuple2<Integer, Integer>> scores =
        playerOneScores.stream()
            .flatMap(
                scorerGameScore ->
                    playerTwoScores.stream()
                        .map(opponentGameScore -> Tuple.of(scorerGameScore, opponentGameScore)))
            .collect(Collectors.toList());
    return Arbitraries.of(scores);
  }

  @Property
  void score_point_for_player_one_when_no_player_has_more_than_six_points(
      @ForAll("playerScores") Tuple.Tuple2<Integer, Integer> playerScores) {
    // Given
    final TieBreakGame aTieBreakGame =
        aTieBreakGame(playerScores.get1(), playerScores.get2(), State.ONGOING);

    // When
    final TieBreakGame result = tieBreakGameManager.playerOneScores(aTieBreakGame);

    // Then
    final TieBreakGame expected =
        aTieBreakGame(playerScores.get1() + 1, playerScores.get2(), State.ONGOING);
    assertThat(result).isEqualTo(expected);
  }

  @Property
  void score_point_for_player_two_when_no_player_has_more_than_six_points(
      @ForAll("playerScores") Tuple.Tuple2<Integer, Integer> playerScores) {
    // Given
    final TieBreakGame aTieBreakGame =
        aTieBreakGame(playerScores.get2(), playerScores.get1(), State.ONGOING);

    // When
    final TieBreakGame result = tieBreakGameManager.playerTwoScores(aTieBreakGame);

    // Then
    final TieBreakGame expected =
        aTieBreakGame(playerScores.get2(), playerScores.get1() + 1, State.ONGOING);
    assertThat(result).isEqualTo(expected);
  }
}
