package com.jmaquin.kata.tennis;

import com.jmaquin.kata.tennis.domain.Game;
import com.jmaquin.kata.tennis.domain.Match;
import com.jmaquin.kata.tennis.domain.Set;
import com.jmaquin.kata.tennis.domain.TieBreakGame;
import com.jmaquin.kata.tennis.domain.enums.*;

public class DataFactory {
  public static Game aGame() {
    return Game.builder().build();
  }

  public static Game aGameWithState(State aState) {
    return Game.builder().state(aState).build();
  }

  public static Game aGame(GameScore aPlayerOneScore, GameScore aPlayerTwoScore, State aState) {
    return Game.builder()
        .playerOneScore(aPlayerOneScore)
        .playerTwoScore(aPlayerTwoScore)
        .state(aState)
        .build();
  }

  public static Match aMatch() {
    return Match.builder().build();
  }

  public static Match aMatch(Set aSet, State aState, Winner aWinner) {
    return Match.builder().set(aSet).state(aState).winner(aWinner).build();
  }

  public static TieBreakGame aTieBreakGame() {
    return TieBreakGame.builder().build();
  }

  public static TieBreakGame aTieBreakGameWithState(State aState) {
    return TieBreakGame.builder().state(aState).build();
  }

  public static TieBreakGame aTieBreakGame(int aPlayerOneScore, int aPlayerTwoScore, State aState) {
    return TieBreakGame.builder()
        .playerOneScore(aPlayerOneScore)
        .playerTwoScore(aPlayerTwoScore)
        .state(aState)
        .build();
  }

  public static Set aSet() {
    return Set.builder().build();
  }

  public static Set aSetWithScores(SetScore aPlayerOneScore, SetScore aPlayerTwoScore) {
    return Set.builder().playerOneScore(aPlayerOneScore).playerTwoScore(aPlayerTwoScore).build();
  }

  public static Set aSetWithState(SetState aSetState) {
    return Set.builder().state(aSetState).build();
  }

  public static Set aSetWithScoresAndState(
      SetScore aPlayerOneScore, SetScore aPlayerTwoScore, SetState aSetState) {
    return Set.builder()
        .playerOneScore(aPlayerOneScore)
        .playerTwoScore(aPlayerTwoScore)
        .state(aSetState)
        .build();
  }

  public static Set aSetWithScoresAndGame(
      SetScore aPlayerOneScore, SetScore aPlayerTwoScore, Game aGame) {
    return Set.builder()
        .playerOneScore(aPlayerOneScore)
        .playerTwoScore(aPlayerTwoScore)
        .currentGame(aGame)
        .build();
  }

  public static Set aSetWithScoresAndTieBreakGame(
      SetScore aPlayerOneScore, SetScore aPlayerTwoScore, TieBreakGame aTieBreakGame) {
    return Set.builder()
        .playerOneScore(aPlayerOneScore)
        .playerTwoScore(aPlayerTwoScore)
        .tieBreakGame(aTieBreakGame)
        .build();
  }

  public static Set aSetWithScoresAndGameAndState(
      SetScore aPlayerOneScore, SetScore aPlayerTwoScore, Game aGame, SetState aState) {
    return Set.builder()
        .playerOneScore(aPlayerOneScore)
        .playerTwoScore(aPlayerTwoScore)
        .currentGame(aGame)
        .state(aState)
        .build();
  }

  public static Set aSetWithScoresAndTieBreakGameAndState(
      SetScore aPlayerOneScore,
      SetScore aPlayerTwoScore,
      TieBreakGame aTieBreakGame,
      SetState aState) {
    return Set.builder()
        .playerOneScore(aPlayerOneScore)
        .playerTwoScore(aPlayerTwoScore)
        .tieBreakGame(aTieBreakGame)
        .state(aState)
        .build();
  }

  public static Set aSet(
      SetScore aPlayerOneScore,
      SetScore aPlayerTwoScore,
      Game aGame,
      TieBreakGame aTieBreakGame,
      SetState aSetState) {
    return Set.builder()
        .playerOneScore(aPlayerOneScore)
        .playerTwoScore(aPlayerTwoScore)
        .currentGame(aGame)
        .tieBreakGame(aTieBreakGame)
        .state(aSetState)
        .build();
  }
}
