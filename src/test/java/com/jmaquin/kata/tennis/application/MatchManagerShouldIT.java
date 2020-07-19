package com.jmaquin.kata.tennis.application;

import static com.jmaquin.kata.tennis.DataFactory.aMatch;
import static org.assertj.core.api.Assertions.assertThat;

import com.jmaquin.kata.tennis.domain.Match;
import com.jmaquin.kata.tennis.domain.enums.SetScore;
import com.jmaquin.kata.tennis.domain.enums.State;
import com.jmaquin.kata.tennis.domain.enums.Winner;
import com.jmaquin.kata.tennis.domain.predicate.IsSetFinish;
import com.jmaquin.kata.tennis.function.DecrementGameScore;
import com.jmaquin.kata.tennis.function.IncrementGameScore;
import com.jmaquin.kata.tennis.function.IncrementSetScore;
import com.jmaquin.kata.tennis.function.UpdateGameScore;
import com.jmaquin.kata.tennis.validator.GameScoresValidator;
import java.util.List;
import java.util.Random;
import net.jqwik.api.*;

class MatchManagerShouldIT {
  private final GameScoresValidator gameScoresValidator = new GameScoresValidator();
  private final IncrementGameScore incrementGameScore = new IncrementGameScore();
  private final DecrementGameScore decrementGameScore = new DecrementGameScore();
  private final UpdateGameScore updateGameScore =
      new UpdateGameScore(gameScoresValidator, incrementGameScore, decrementGameScore);
  private final GameManager gameManager = new GameManager(updateGameScore);
  private final TieBreakGameManager tieBreakGameManager = new TieBreakGameManager();
  private final IncrementSetScore incrementSetScore = new IncrementSetScore();
  private final IsSetFinish isSetFinish = new IsSetFinish();
  private final SetManager setManager =
      new SetManager(gameManager, tieBreakGameManager, incrementSetScore, isSetFinish);
  private final MatchManager matchManager = new MatchManager(setManager);

  @Provide
  private Arbitrary<Integer> integers() {
    return Arbitraries.integers().between(1, 1000);
  }

  @Property
  void run_a_complete_game(@ForAll("integers") int i) {
    // Given
    final Random rd = new Random();
    Match aMatch = aMatch();

    // When
    while (State.NOT_STARTED.equals(aMatch.getState()) || State.ONGOING.equals(aMatch.getState())) {
      if (rd.nextBoolean()) {
        aMatch = matchManager.playerOneScores(aMatch);
      } else {
        aMatch = matchManager.playerTwoScores(aMatch);
      }
    }

    // Then
    assertThat(aMatch.getState()).isEqualTo(State.FINISHED);
    assertThat(aMatch.getWinner()).isNotEqualTo(Winner.UNKNOWN);
    final List<SetScore> playerScores =
        List.of(aMatch.getSet().getPlayerOneScore(), aMatch.getSet().getPlayerTwoScore());
    assertThat(playerScores).containsAnyOf(SetScore.SIX, SetScore.SEVEN);
    assertThat(
            playerScores.stream()
                .filter(
                    playerScore ->
                        SetScore.SIX.equals(playerScore) || SetScore.SEVEN.equals(playerScore)))
        .hasSizeGreaterThan(0);

    if (SetScore.SIX.equals(aMatch.getSet().getPlayerOneScore())
        && aMatch.getWinner().equals(Winner.PLAYER_1)) {
      assertThat(aMatch.getSet().getPlayerTwoScore()).isNotEqualTo(SetScore.SEVEN);
      assertThat(aMatch.getSet().getPlayerTwoScore()).isNotEqualTo(SetScore.SIX);
      assertThat(aMatch.getSet().getPlayerTwoScore()).isNotEqualTo(SetScore.FIVE);
    }

    if (SetScore.SEVEN.equals(aMatch.getSet().getPlayerOneScore())
        && aMatch.getWinner().equals(Winner.PLAYER_1)) {
      assertThat(aMatch.getSet().getPlayerTwoScore()).isNotEqualTo(SetScore.SEVEN);
      assertThat(aMatch.getSet().getPlayerTwoScore()).isNotEqualTo(SetScore.FOUR);
      assertThat(aMatch.getSet().getPlayerTwoScore()).isNotEqualTo(SetScore.THREE);
      assertThat(aMatch.getSet().getPlayerTwoScore()).isNotEqualTo(SetScore.TWO);
      assertThat(aMatch.getSet().getPlayerTwoScore()).isNotEqualTo(SetScore.ONE);
      assertThat(aMatch.getSet().getPlayerTwoScore()).isNotEqualTo(SetScore.ZERO);
    }

    if (SetScore.SIX.equals(aMatch.getSet().getPlayerTwoScore())
        && aMatch.getWinner().equals(Winner.PLAYER_2)) {
      assertThat(aMatch.getSet().getPlayerOneScore()).isNotEqualTo(SetScore.SEVEN);
      assertThat(aMatch.getSet().getPlayerOneScore()).isNotEqualTo(SetScore.SIX);
      assertThat(aMatch.getSet().getPlayerOneScore()).isNotEqualTo(SetScore.FIVE);
    }

    if (SetScore.SEVEN.equals(aMatch.getSet().getPlayerTwoScore())
        && aMatch.getWinner().equals(Winner.PLAYER_2)) {
      assertThat(aMatch.getSet().getPlayerOneScore()).isNotEqualTo(SetScore.SEVEN);
      assertThat(aMatch.getSet().getPlayerOneScore()).isNotEqualTo(SetScore.FOUR);
      assertThat(aMatch.getSet().getPlayerOneScore()).isNotEqualTo(SetScore.THREE);
      assertThat(aMatch.getSet().getPlayerOneScore()).isNotEqualTo(SetScore.TWO);
      assertThat(aMatch.getSet().getPlayerOneScore()).isNotEqualTo(SetScore.ONE);
      assertThat(aMatch.getSet().getPlayerOneScore()).isNotEqualTo(SetScore.ZERO);
    }
  }
}
