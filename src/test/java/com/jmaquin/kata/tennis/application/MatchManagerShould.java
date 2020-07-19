package com.jmaquin.kata.tennis.application;

import static com.jmaquin.kata.tennis.DataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.jmaquin.kata.tennis.domain.Match;
import com.jmaquin.kata.tennis.domain.Set;
import com.jmaquin.kata.tennis.domain.enums.SetScore;
import com.jmaquin.kata.tennis.domain.enums.SetState;
import com.jmaquin.kata.tennis.domain.enums.State;
import com.jmaquin.kata.tennis.domain.enums.Winner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchManagerShould {
  @Mock private SetManager setManager;
  @InjectMocks private MatchManager matchManager;

  @Test
  void start_game_when_player_one_scores_and_match_not_started() {
    // Given
    final Match aMatch = aMatch();
    final Set aSetWithState = aSetWithState(SetState.ONGOING);
    when(setManager.playerOneScores(aMatch.getSet())).thenReturn(aSetWithState);

    // When
    final Match result = matchManager.playerOneScores(aMatch);

    // Then
    final Match expected = aMatch(aSetWithState, State.ONGOING, Winner.UNKNOWN);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void start_game_when_player_two_scores_and_match_not_started() {
    // Given
    final Match aMatch = aMatch();
    final Set aSetWithState = aSetWithState(SetState.ONGOING);
    when(setManager.playerTwoScores(aMatch.getSet())).thenReturn(aSetWithState);

    // When
    final Match result = matchManager.playerTwoScores(aMatch);

    // Then
    final Match expected = aMatch(aSetWithState, State.ONGOING, Winner.UNKNOWN);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void not_change_match_state_when_player_one_scores_and_match_and_set_ongoing() {
    // Given
    final Match aMatch = aMatch(aSetWithState(SetState.ONGOING), State.ONGOING, Winner.UNKNOWN);
    final Set aSet = aSetWithStateAndScores(SetScore.ONE, SetScore.ZERO, SetState.ONGOING);
    when(setManager.playerOneScores(aMatch.getSet())).thenReturn(aSet);

    // When
    final Match result = matchManager.playerOneScores(aMatch);

    // Then
    final Match expected = aMatch(aSet, State.ONGOING, Winner.UNKNOWN);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void not_change_match_state_when_player_two_scores_and_match_and_set_ongoing() {
    // Given
    final Match aMatch = aMatch(aSetWithState(SetState.ONGOING), State.ONGOING, Winner.UNKNOWN);
    final Set aSet = aSetWithStateAndScores(SetScore.ONE, SetScore.ZERO, SetState.ONGOING);
    when(setManager.playerTwoScores(aMatch.getSet())).thenReturn(aSet);

    // When
    final Match result = matchManager.playerTwoScores(aMatch);

    // Then
    final Match expected = aMatch(aSet, State.ONGOING, Winner.UNKNOWN);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void finish_match_when_player_one_scores_and_set_finished() {
    // Given
    final Match aMatch = aMatch(aSetWithState(SetState.ONGOING), State.ONGOING, Winner.UNKNOWN);
    final Set aSet = aSetWithStateAndScores(SetScore.ONE, SetScore.ZERO, SetState.FINISHED);
    when(setManager.playerOneScores(aMatch.getSet())).thenReturn(aSet);

    // When
    final Match result = matchManager.playerOneScores(aMatch);

    // Then
    final Match expected = aMatch(aSet, State.FINISHED, Winner.PLAYER_1);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void finish_match_when_player_two_scores_and_set_finished() {
    // Given
    final Match aMatch = aMatch(aSetWithState(SetState.ONGOING), State.ONGOING, Winner.UNKNOWN);
    final Set aSet = aSetWithStateAndScores(SetScore.ONE, SetScore.ZERO, SetState.FINISHED);
    when(setManager.playerTwoScores(aMatch.getSet())).thenReturn(aSet);

    // When
    final Match result = matchManager.playerTwoScores(aMatch);

    // Then
    final Match expected = aMatch(aSet, State.FINISHED, Winner.PLAYER_2);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void return_same_match_when_player_one_score_and_match_already_finished() {
    // Given
    final Match aMatch = aMatch(aSetWithState(SetState.FINISHED), State.FINISHED, Winner.PLAYER_1);

    // When
    final Match result = matchManager.playerOneScores(aMatch);

    // Then
    final Match expected = aMatch(aMatch.getSet(), State.FINISHED, Winner.PLAYER_1);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void return_same_match_when_player_two_score_and_match_already_finished() {
    // Given
    final Match aMatch = aMatch(aSetWithState(SetState.FINISHED), State.FINISHED, Winner.PLAYER_2);

    // When
    final Match result = matchManager.playerTwoScores(aMatch);

    // Then
    final Match expected = aMatch(aMatch.getSet(), State.FINISHED, Winner.PLAYER_2);
    assertThat(result).isEqualTo(expected);
  }
}
