package com.jmaquin.kata.tennis.application;

import static io.vavr.API.*;

import com.jmaquin.kata.tennis.domain.Match;
import com.jmaquin.kata.tennis.domain.Set;
import com.jmaquin.kata.tennis.domain.enums.SetState;
import com.jmaquin.kata.tennis.domain.enums.State;
import com.jmaquin.kata.tennis.domain.enums.Winner;
import java.util.Objects;

public class MatchManager {
  private final SetManager setManager;

  public MatchManager(SetManager setManager) {
    Objects.requireNonNull(setManager, "setManager must not be null!");
    this.setManager = setManager;
  }

  public Match playerOneScores(Match match) {
    return Match(match.getState())
        .of(
            Case(
                $(State.NOT_STARTED),
                () ->
                    match
                        .toBuilder()
                        .set(setManager.playerOneScores(match.getSet()))
                        .state(State.ONGOING)
                        .build()),
            Case(
                $(State.ONGOING),
                () -> {
                  final Set updatedSet = setManager.playerOneScores(match.getSet());
                  return Match(updatedSet.getState())
                      .of(
                          Case(
                              $(SetState.FINISHED),
                              () ->
                                  match
                                      .toBuilder()
                                      .set(updatedSet)
                                      .state(State.FINISHED)
                                      .winner(Winner.PLAYER_1)
                                      .build()),
                          Case(
                              $(),
                              () ->
                                  match.toBuilder().set(updatedSet).state(State.ONGOING).build()));
                }),
            Case($(State.FINISHED), () -> match));
  }

  public Match playerTwoScores(Match match) {
    return Match(match.getState())
        .of(
            Case(
                $(State.NOT_STARTED),
                () ->
                    match
                        .toBuilder()
                        .set(setManager.playerTwoScores(match.getSet()))
                        .state(State.ONGOING)
                        .build()),
            Case(
                $(State.ONGOING),
                () -> {
                  final Set updatedSet = setManager.playerTwoScores(match.getSet());
                  return Match(updatedSet.getState())
                      .of(
                          Case(
                              $(SetState.FINISHED),
                              () ->
                                  match
                                      .toBuilder()
                                      .set(updatedSet)
                                      .state(State.FINISHED)
                                      .winner(Winner.PLAYER_2)
                                      .build()),
                          Case(
                              $(),
                              () ->
                                  match.toBuilder().set(updatedSet).state(State.ONGOING).build()));
                }),
            Case($(State.FINISHED), () -> match));
  }
}
