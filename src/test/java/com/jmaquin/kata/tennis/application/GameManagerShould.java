package com.jmaquin.kata.tennis.application;

import static com.jmaquin.kata.tennis.DataFactory.aGame;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.jmaquin.kata.tennis.domain.Game;
import com.jmaquin.kata.tennis.domain.GameScore;
import com.jmaquin.kata.tennis.function.UpdateGameScore;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameManagerShould {
  @Mock private UpdateGameScore updateGameScore;
  @InjectMocks private GameManager gameManager;

  @Test
  void score_for_player_one() {
    // Given
    final GameScore aPlayerOneScore = GameScore.getRandomScore();
    final GameScore aPlayerTwoScore = GameScore.getRandomScore();
    final Game aGame = aGame(aPlayerOneScore, aPlayerTwoScore);
    final GameScore anUpdatedPlayerOneGameScore = GameScore.getRandomScore();
    final GameScore anUpdatedPlayerTwoGameScore = GameScore.getRandomScore();
    final Tuple2<GameScore, GameScore> anUpdatedGameScores =
        Tuple.of(anUpdatedPlayerOneGameScore, anUpdatedPlayerTwoGameScore);
    when(updateGameScore.apply(aPlayerOneScore, aPlayerTwoScore)).thenReturn(anUpdatedGameScores);

    // When
    final Game result = gameManager.playerOneScores(aGame);

    // Then
    final Game expected = aGame(anUpdatedPlayerOneGameScore, anUpdatedPlayerTwoGameScore);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void score_for_player_two() {
    // Given
    final GameScore aPlayerOneScore = GameScore.getRandomScore();
    final GameScore aPlayerTwoScore = GameScore.getRandomScore();
    final Game aGame = aGame(aPlayerOneScore, aPlayerTwoScore);
    final GameScore anUpdatedPlayerOneGameScore = GameScore.getRandomScore();
    final GameScore anUpdatedPlayerTwoGameScore = GameScore.getRandomScore();
    final Tuple2<GameScore, GameScore> anUpdatedGameScores =
        Tuple.of(anUpdatedPlayerTwoGameScore, anUpdatedPlayerOneGameScore);
    when(updateGameScore.apply(aPlayerTwoScore, aPlayerOneScore)).thenReturn(anUpdatedGameScores);

    // When
    final Game result = gameManager.playerTwoScores(aGame);

    // Then
    final Game expected = aGame(anUpdatedPlayerOneGameScore, anUpdatedPlayerTwoGameScore);
    assertThat(result).isEqualTo(expected);
  }
}
