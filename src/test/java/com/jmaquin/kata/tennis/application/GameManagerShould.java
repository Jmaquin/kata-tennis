package com.jmaquin.kata.tennis.application;

import static com.jmaquin.kata.tennis.DataFactory.aGame;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jmaquin.kata.tennis.domain.Game;
import com.jmaquin.kata.tennis.domain.enums.GameScore;
import com.jmaquin.kata.tennis.domain.enums.State;
import com.jmaquin.kata.tennis.function.UpdateGameScore;
import io.vavr.Tuple;
import io.vavr.Tuple2;
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
class GameManagerShould {
  @Mock private UpdateGameScore updateGameScore;
  @InjectMocks private GameManager gameManager;

  @Provide
  private Arbitrary<net.jqwik.api.Tuple.Tuple2<GameScore, GameScore>> gameScores() {
    final List<GameScore> scorerGameScores = List.of(GameScore.values());
    final List<GameScore> opponentGameScores = List.of(GameScore.values());
    final Collection<net.jqwik.api.Tuple.Tuple2<GameScore, GameScore>> scores =
        scorerGameScores.stream()
            .flatMap(
                scorerGameScore ->
                    opponentGameScores.stream()
                        .map(
                            opponentGameScore ->
                                net.jqwik.api.Tuple.of(scorerGameScore, opponentGameScore)))
            .filter(
                tuple ->
                    !net.jqwik.api.Tuple.of(GameScore.ADVANTAGE, GameScore.FORTY).equals(tuple))
            .filter(
                tuple -> !net.jqwik.api.Tuple.of(GameScore.FORTY, GameScore.THIRTY).equals(tuple))
            .filter(
                tuple -> !net.jqwik.api.Tuple.of(GameScore.FORTY, GameScore.FIFTEEN).equals(tuple))
            .filter(tuple -> !net.jqwik.api.Tuple.of(GameScore.FORTY, GameScore.ZERO).equals(tuple))
            .filter(tuple -> !GameScore.WIN_GAME.equals(tuple.get1()))
            .collect(Collectors.toList());
    return Arbitraries.of(scores);
  }

  @Property
  void score_point_for_player_one(
      @ForAll("gameScores") net.jqwik.api.Tuple.Tuple2<GameScore, GameScore> gameScores) {
    // Given
    updateGameScore = mock(UpdateGameScore.class);
    gameManager = new GameManager(updateGameScore);

    final Game aGame = aGame(gameScores.get1(), gameScores.get2(), State.NOT_STARTED);
    final GameScore anUpdatedPlayerOneGameScore = GameScore.FIFTEEN;
    final GameScore anUpdatedPlayerTwoGameScore = GameScore.getRandomScore();
    final Tuple2<GameScore, GameScore> anUpdatedGameScores =
        Tuple.of(anUpdatedPlayerOneGameScore, anUpdatedPlayerTwoGameScore);
    when(updateGameScore.apply(gameScores.get1(), gameScores.get2()))
        .thenReturn(anUpdatedGameScores);

    // When
    final Game result = gameManager.playerOneScores(aGame);

    // Then
    final Game expected =
        aGame(anUpdatedPlayerOneGameScore, anUpdatedPlayerTwoGameScore, State.ONGOING);
    assertThat(result).isEqualTo(expected);
  }

  @Property
  void score_point_for_player_two(
      @ForAll("gameScores") net.jqwik.api.Tuple.Tuple2<GameScore, GameScore> gameScores) {
    // Given
    updateGameScore = mock(UpdateGameScore.class);
    gameManager = new GameManager(updateGameScore);

    final Game aGame = aGame(gameScores.get2(), gameScores.get1(), State.NOT_STARTED);
    final GameScore anUpdatedPlayerOneGameScore = GameScore.getRandomScore();
    final GameScore anUpdatedPlayerTwoGameScore = GameScore.FIFTEEN;
    final Tuple2<GameScore, GameScore> anUpdatedGameScores =
        Tuple.of(anUpdatedPlayerTwoGameScore, anUpdatedPlayerOneGameScore);
    when(updateGameScore.apply(gameScores.get1(), gameScores.get2()))
        .thenReturn(anUpdatedGameScores);

    // When
    final Game result = gameManager.playerTwoScores(aGame);

    // Then
    final Game expected =
        aGame(anUpdatedPlayerOneGameScore, anUpdatedPlayerTwoGameScore, State.ONGOING);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void score_point_and_end_game_for_player_one() {
    // Given
    final GameScore aPlayerOneScore = GameScore.getRandomScore();
    final GameScore aPlayerTwoScore = GameScore.getRandomScore();
    final Game aGame = aGame(aPlayerOneScore, aPlayerTwoScore, State.ONGOING);
    final GameScore anUpdatedPlayerOneGameScore = GameScore.WIN_GAME;
    final GameScore anUpdatedPlayerTwoGameScore = GameScore.getRandomScore();
    final Tuple2<GameScore, GameScore> anUpdatedGameScores =
        Tuple.of(anUpdatedPlayerOneGameScore, anUpdatedPlayerTwoGameScore);
    when(updateGameScore.apply(aPlayerOneScore, aPlayerTwoScore)).thenReturn(anUpdatedGameScores);

    // When
    final Game result = gameManager.playerOneScores(aGame);

    // Then
    final Game expected =
        aGame(anUpdatedPlayerOneGameScore, anUpdatedPlayerTwoGameScore, State.FINISHED);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void score_point_and_end_game_for_player_two() {
    // Given
    final GameScore aPlayerOneScore = GameScore.getRandomScore();
    final GameScore aPlayerTwoScore = GameScore.getRandomScore();
    final Game aGame = aGame(aPlayerOneScore, aPlayerTwoScore, State.ONGOING);
    final GameScore anUpdatedPlayerOneGameScore = GameScore.getRandomScore();
    final GameScore anUpdatedPlayerTwoGameScore = GameScore.WIN_GAME;
    final Tuple2<GameScore, GameScore> anUpdatedGameScores =
        Tuple.of(anUpdatedPlayerTwoGameScore, anUpdatedPlayerOneGameScore);
    when(updateGameScore.apply(aPlayerTwoScore, aPlayerOneScore)).thenReturn(anUpdatedGameScores);

    // When
    final Game result = gameManager.playerTwoScores(aGame);

    // Then
    final Game expected =
        aGame(anUpdatedPlayerOneGameScore, anUpdatedPlayerTwoGameScore, State.FINISHED);
    assertThat(result).isEqualTo(expected);
  }
}
