package com.jmaquin.kata.tennis.application;

import static com.jmaquin.kata.tennis.DataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jmaquin.kata.tennis.domain.Game;
import com.jmaquin.kata.tennis.domain.Set;
import com.jmaquin.kata.tennis.domain.TieBreakGame;
import com.jmaquin.kata.tennis.domain.enums.SetScore;
import com.jmaquin.kata.tennis.domain.enums.SetState;
import com.jmaquin.kata.tennis.domain.enums.State;
import com.jmaquin.kata.tennis.domain.predicate.IsSetFinish;
import com.jmaquin.kata.tennis.function.IncrementSetScore;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SetManagerShould {
  @Mock private GameManager gameManager;
  @Mock private TieBreakGameManager tieBreakGameManager;
  @Mock private IncrementSetScore incrementSetScore;
  @Mock private IsSetFinish isSetFinish;
  @InjectMocks private SetManager setManager;

  @Test
  void update_state_to_finished_when_score_is_6_to_6_and_player_one_win_game() {
    // Given
    final Set aSet = aSetWithScoresAndTieBreakGame(SetScore.SIX, SetScore.SIX, aTieBreakGame());
    final TieBreakGame aTieBreakGame = aTieBreakGameWithState(State.FINISHED);
    when(tieBreakGameManager.playerOneScores(aSet.getTieBreakGame())).thenReturn(aTieBreakGame);
    final SetScore aSetScore = SetScore.getRandomScore();
    when(incrementSetScore.apply(aSet.getPlayerOneScore(), aSet.getPlayerTwoScore()))
        .thenReturn(aSetScore);

    // When
    final Set result = setManager.playerOneScores(aSet);

    // Then
    final Set expected =
        aSetWithScoresAndTieBreakGameAndState(
            aSetScore, aSet.getPlayerTwoScore(), aTieBreakGame, SetState.FINISHED);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void update_state_to_finished_when_score_is_6_to_6_and_player_two_win_game() {
    // Given
    final Set aSet = aSetWithScoresAndTieBreakGame(SetScore.SIX, SetScore.SIX, aTieBreakGame());
    final TieBreakGame aTieBreakGame = aTieBreakGameWithState(State.FINISHED);
    when(tieBreakGameManager.playerTwoScores(aSet.getTieBreakGame())).thenReturn(aTieBreakGame);
    final SetScore aSetScore = SetScore.getRandomScore();
    when(incrementSetScore.apply(aSet.getPlayerTwoScore(), aSet.getPlayerOneScore()))
        .thenReturn(aSetScore);

    // When
    final Set result = setManager.playerTwoScores(aSet);

    // Then
    final Set expected =
        aSetWithScoresAndTieBreakGameAndState(
            aSet.getPlayerOneScore(), aSetScore, aTieBreakGame, SetState.FINISHED);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void update_state_to_tiebreak_when_score_is_6_to_6_and_player_one_scores() {
    // Given
    final Set aSet = aSetWithScoresAndTieBreakGame(SetScore.SIX, SetScore.SIX, aTieBreakGame());
    final TieBreakGame aTieBreakGame = aTieBreakGameWithState(State.ONGOING);
    when(tieBreakGameManager.playerOneScores(aSet.getTieBreakGame())).thenReturn(aTieBreakGame);

    // When
    final Set result = setManager.playerOneScores(aSet);

    // Then
    final Set expected =
        aSetWithScoresAndTieBreakGameAndState(
            aSet.getPlayerOneScore(), aSet.getPlayerTwoScore(), aTieBreakGame, SetState.TIEBREAK);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void update_state_to_tiebreak_when_score_is_6_to_6_and_player_two_scores() {
    // Given
    final Set aSet = aSetWithScoresAndTieBreakGame(SetScore.SIX, SetScore.SIX, aTieBreakGame());
    final TieBreakGame aTieBreakGame = aTieBreakGameWithState(State.ONGOING);
    when(tieBreakGameManager.playerTwoScores(aSet.getTieBreakGame())).thenReturn(aTieBreakGame);

    // When
    final Set result = setManager.playerTwoScores(aSet);

    // Then
    final Set expected =
        aSetWithScoresAndTieBreakGameAndState(
            aSet.getPlayerOneScore(), aSet.getPlayerTwoScore(), aTieBreakGame, SetState.TIEBREAK);
    assertThat(result).isEqualTo(expected);
  }

  @Provide
  private Arbitrary<Tuple.Tuple2<SetScore, SetScore>> setScores() {
    final List<SetScore> scorerSetScores = List.of(SetScore.values());
    final List<SetScore> opponentSetScores = List.of(SetScore.values());
    final Collection<Tuple.Tuple2<SetScore, SetScore>> scores =
        scorerSetScores.stream()
            .flatMap(
                scorerSetScore ->
                    opponentSetScores.stream()
                        .map(opponentSetScore -> Tuple.of(scorerSetScore, opponentSetScore)))
            .filter(tuple -> !Tuple.of(SetScore.SIX, SetScore.SIX).equals(tuple))
            .collect(Collectors.toList());
    return Arbitraries.of(scores);
  }

  @Property
  void update_state_to_finished_when_player_one_win_last_game(
      @ForAll("setScores") Tuple.Tuple2<SetScore, SetScore> setScores) {
    // Given
    gameManager = mock(GameManager.class);
    tieBreakGameManager = mock(TieBreakGameManager.class);
    incrementSetScore = mock(IncrementSetScore.class);
    isSetFinish = mock(IsSetFinish.class);
    setManager = new SetManager(gameManager, tieBreakGameManager, incrementSetScore, isSetFinish);

    final Set aSet = aSetWithScoresAndGame(setScores.get1(), setScores.get2(), aGame());
    final Game aGame = aGameWithState(State.FINISHED);
    when(gameManager.playerOneScores(aSet.getCurrentGame())).thenReturn(aGame);
    final SetScore aSetScore = SetScore.getRandomScore();
    when(incrementSetScore.apply(aSet.getPlayerOneScore(), aSet.getPlayerTwoScore()))
        .thenReturn(aSetScore);
    when(isSetFinish.test(any())).thenReturn(true);

    // When
    final Set result = setManager.playerOneScores(aSet);

    // Then
    final Set expected =
        aSetWithScoresAndGameAndState(
            aSetScore, aSet.getPlayerTwoScore(), aGame, SetState.FINISHED);
    assertThat(result).isEqualTo(expected);
  }

  @Property
  void update_state_to_finished_when_player_two_win_last_game(
      @ForAll("setScores") Tuple.Tuple2<SetScore, SetScore> setScores) {
    // Given
    gameManager = mock(GameManager.class);
    tieBreakGameManager = mock(TieBreakGameManager.class);
    incrementSetScore = mock(IncrementSetScore.class);
    isSetFinish = mock(IsSetFinish.class);
    setManager = new SetManager(gameManager, tieBreakGameManager, incrementSetScore, isSetFinish);

    final Set aSet = aSetWithScoresAndGame(setScores.get1(), setScores.get2(), aGame());
    final Game aGame = aGameWithState(State.FINISHED);
    when(gameManager.playerTwoScores(aSet.getCurrentGame())).thenReturn(aGame);
    final SetScore aSetScore = SetScore.getRandomScore();
    when(incrementSetScore.apply(aSet.getPlayerTwoScore(), aSet.getPlayerOneScore()))
        .thenReturn(aSetScore);
    when(isSetFinish.test(any())).thenReturn(true);

    // When
    final Set result = setManager.playerTwoScores(aSet);

    // Then
    final Set expected =
        aSetWithScoresAndGameAndState(
            aSet.getPlayerOneScore(), aSetScore, aGame, SetState.FINISHED);
    assertThat(result).isEqualTo(expected);
  }

  @Property
  void update_state_to_ongoing_when_player_one_scores(
      @ForAll("setScores") Tuple.Tuple2<SetScore, SetScore> setScores) {
    // Given
    gameManager = mock(GameManager.class);
    tieBreakGameManager = mock(TieBreakGameManager.class);
    incrementSetScore = mock(IncrementSetScore.class);
    isSetFinish = mock(IsSetFinish.class);
    setManager = new SetManager(gameManager, tieBreakGameManager, incrementSetScore, isSetFinish);

    final Set aSet = aSetWithScoresAndGame(setScores.get1(), setScores.get2(), aGame());
    final Game aGame = aGameWithState(State.ONGOING);
    when(gameManager.playerOneScores(aSet.getCurrentGame())).thenReturn(aGame);

    // When
    final Set result = setManager.playerOneScores(aSet);

    // Then
    final Set expected =
        aSetWithScoresAndGameAndState(
            aSet.getPlayerOneScore(), aSet.getPlayerTwoScore(), aGame, SetState.ONGOING);
    assertThat(result).isEqualTo(expected);
  }

  @Property
  void update_state_to_ongoing_when_player_two_scores(
      @ForAll("setScores") Tuple.Tuple2<SetScore, SetScore> setScores) {
    // Given
    gameManager = mock(GameManager.class);
    tieBreakGameManager = mock(TieBreakGameManager.class);
    incrementSetScore = mock(IncrementSetScore.class);
    isSetFinish = mock(IsSetFinish.class);
    setManager = new SetManager(gameManager, tieBreakGameManager, incrementSetScore, isSetFinish);

    final Set aSet = aSetWithScoresAndGame(setScores.get1(), setScores.get2(), aGame());
    final Game aGame = aGameWithState(State.ONGOING);
    when(gameManager.playerTwoScores(aSet.getCurrentGame())).thenReturn(aGame);

    // When
    final Set result = setManager.playerTwoScores(aSet);

    // Then
    final Set expected =
        aSetWithScoresAndGameAndState(
            aSet.getPlayerOneScore(), aSet.getPlayerTwoScore(), aGame, SetState.ONGOING);
    assertThat(result).isEqualTo(expected);
  }
}
