package com.jmaquin.kata.tennis.application;

import static io.vavr.API.*;

import com.jmaquin.kata.tennis.domain.Game;
import com.jmaquin.kata.tennis.domain.Set;
import com.jmaquin.kata.tennis.domain.TieBreakGame;
import com.jmaquin.kata.tennis.domain.enums.SetScore;
import com.jmaquin.kata.tennis.domain.enums.SetState;
import com.jmaquin.kata.tennis.domain.enums.State;
import com.jmaquin.kata.tennis.domain.predicate.IsSetFinish;
import com.jmaquin.kata.tennis.function.IncrementSetScore;
import io.vavr.Tuple;
import java.util.Objects;

public class SetManager {
  private final GameManager gameManager;
  private final TieBreakGameManager tieBreakGameManager;
  private final IncrementSetScore incrementSetScore;
  private final IsSetFinish isSetFinish;

  public SetManager(
      GameManager gameManager,
      TieBreakGameManager tieBreakGameManager,
      IncrementSetScore incrementSetScore,
      IsSetFinish isSetFinish) {
    Objects.requireNonNull(gameManager, "gameManager must not be null!");
    Objects.requireNonNull(tieBreakGameManager, "tieBreakGameManager must not be null!");
    Objects.requireNonNull(incrementSetScore, "incrementSetScore must not be null!");
    Objects.requireNonNull(isSetFinish, "isSetFinishable must not be null!");
    this.gameManager = gameManager;
    this.tieBreakGameManager = tieBreakGameManager;
    this.incrementSetScore = incrementSetScore;
    this.isSetFinish = isSetFinish;
  }

  public Set playerOneScores(Set set) {
    return Match(Tuple.of(set.getPlayerOneScore(), set.getPlayerTwoScore()))
        .of(
            Case(
                $(Tuple.of(SetScore.SIX, SetScore.SIX)),
                () -> {
                  final TieBreakGame updatedTieBreakGame =
                      tieBreakGameManager.playerOneScores(set.getTieBreakGame());
                  if (State.FINISHED.equals(updatedTieBreakGame.getState())) {
                    return set.toBuilder()
                        .playerOneScore(
                            incrementSetScore.apply(
                                set.getPlayerOneScore(), set.getPlayerTwoScore()))
                        .tieBreakGame(updatedTieBreakGame)
                        .state(SetState.FINISHED)
                        .build();
                  } else {
                    return set.toBuilder()
                        .tieBreakGame(updatedTieBreakGame)
                        .state(SetState.TIEBREAK)
                        .build();
                  }
                }),
            Case(
                $(),
                () -> {
                  final Game updatedGame = gameManager.playerOneScores(set.getCurrentGame());
                  if (State.FINISHED.equals(updatedGame.getState())) {
                    final Set updatedSet =
                        set.toBuilder()
                            .playerOneScore(
                                incrementSetScore.apply(
                                    set.getPlayerOneScore(), set.getPlayerTwoScore()))
                            .currentGame(updatedGame)
                            .build();
                    if (isSetFinish.test(set)) {
                      return updatedSet.toBuilder().state(SetState.FINISHED).build();
                    } else {
                      return updatedSet.toBuilder().state(SetState.ONGOING).build();
                    }

                  } else {
                    return set.toBuilder().currentGame(updatedGame).state(SetState.ONGOING).build();
                  }
                }));
  }

  public Set playerTwoScores(Set set) {
    return Match(Tuple.of(set.getPlayerOneScore(), set.getPlayerTwoScore()))
        .of(
            Case(
                $(Tuple.of(SetScore.SIX, SetScore.SIX)),
                () -> {
                  final TieBreakGame updatedTieBreakGame =
                      tieBreakGameManager.playerTwoScores(set.getTieBreakGame());
                  if (State.FINISHED.equals(updatedTieBreakGame.getState())) {
                    return set.toBuilder()
                        .playerTwoScore(
                            incrementSetScore.apply(
                                set.getPlayerTwoScore(), set.getPlayerOneScore()))
                        .tieBreakGame(updatedTieBreakGame)
                        .state(SetState.FINISHED)
                        .build();
                  } else {
                    return set.toBuilder()
                        .tieBreakGame(updatedTieBreakGame)
                        .state(SetState.TIEBREAK)
                        .build();
                  }
                }),
            Case(
                $(),
                () -> {
                  final Game updatedGame = gameManager.playerTwoScores(set.getCurrentGame());
                  if (State.FINISHED.equals(updatedGame.getState())) {
                    final Set updatedSet =
                        set.toBuilder()
                            .playerTwoScore(
                                incrementSetScore.apply(
                                    set.getPlayerTwoScore(), set.getPlayerOneScore()))
                            .currentGame(updatedGame)
                            .build();
                    if (isSetFinish.test(updatedSet)) {
                      return updatedSet.toBuilder().state(SetState.FINISHED).build();
                    } else {
                      return updatedSet.toBuilder().state(SetState.ONGOING).build();
                    }
                  } else {
                    return set.toBuilder().currentGame(updatedGame).state(SetState.ONGOING).build();
                  }
                }));
  }
}
