package com.jmaquin.kata.tennis.function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.jmaquin.kata.tennis.domain.enums.GameScore;
import com.jmaquin.kata.tennis.validator.GameScoresValidator;
import io.vavr.Tuple2;
import io.vavr.control.Validation;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateGameScoreShould {
  @Mock private GameScoresValidator gameScoresValidator;
  @Mock private IncrementGameScore incrementGameScore;
  @Mock private DecrementGameScore decrementGameScore;
  @InjectMocks private UpdateGameScore updateGameScore;

  private static Stream<Arguments> provideArgumentsIncrementDeuceRuleEnabled() {
    return Stream.of(
        Arguments.of(GameScore.THIRTY, GameScore.FORTY),
        Arguments.of(GameScore.DEUCE, GameScore.DEUCE),
        Arguments.of(GameScore.FORTY, GameScore.ADVANTAGE));
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsIncrementDeuceRuleEnabled")
  void activate_deuce_rule(GameScore aScorerGameScore, GameScore anOpponentGameScore) {
    // Given
    final boolean isDeuce = true;
    final GameScore aResultScorerGameScore = GameScore.getRandomScore();
    final GameScore aResultOpponentGameScore = GameScore.getRandomScore();
    final Tuple2<GameScore, GameScore> gameScores =
        io.vavr.Tuple.of(aScorerGameScore, anOpponentGameScore);
    final Validation<String, Tuple2<GameScore, GameScore>> aValidation =
        Validation.valid(gameScores);
    when(gameScoresValidator.apply(aScorerGameScore, anOpponentGameScore)).thenReturn(aValidation);
    when(incrementGameScore.apply(aScorerGameScore, isDeuce)).thenReturn(aResultScorerGameScore);
    when(decrementGameScore.apply(anOpponentGameScore, isDeuce))
        .thenReturn(aResultOpponentGameScore);

    // When
    final Tuple2<GameScore, GameScore> result =
        updateGameScore.apply(aScorerGameScore, anOpponentGameScore);

    // Then
    verify(gameScoresValidator, times(1)).apply(aScorerGameScore, anOpponentGameScore);
    verify(incrementGameScore, times(1)).apply(aScorerGameScore, isDeuce);
    verify(decrementGameScore, times(1)).apply(anOpponentGameScore, isDeuce);
    final Tuple2<GameScore, GameScore> expected =
        io.vavr.Tuple.of(aResultScorerGameScore, aResultOpponentGameScore);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void activate_deuce_rule_and_not_decrement_opponent_score() {
    // Given
    final GameScore aScorerGameScore = GameScore.ADVANTAGE;
    final GameScore anOpponentGameScore = GameScore.FORTY;
    final boolean isDeuce = true;
    final GameScore aResultScorerGameScore = GameScore.getRandomScore();
    final Tuple2<GameScore, GameScore> gameScores =
        io.vavr.Tuple.of(aScorerGameScore, anOpponentGameScore);
    final Validation<String, Tuple2<GameScore, GameScore>> aValidation =
        Validation.valid(gameScores);
    when(gameScoresValidator.apply(aScorerGameScore, anOpponentGameScore)).thenReturn(aValidation);
    when(incrementGameScore.apply(aScorerGameScore, isDeuce)).thenReturn(aResultScorerGameScore);

    // When
    final Tuple2<GameScore, GameScore> result =
        updateGameScore.apply(aScorerGameScore, anOpponentGameScore);

    // Then
    verify(gameScoresValidator, times(1)).apply(aScorerGameScore, anOpponentGameScore);
    verify(incrementGameScore, times(1)).apply(aScorerGameScore, isDeuce);
    final Tuple2<GameScore, GameScore> expected =
        io.vavr.Tuple.of(aResultScorerGameScore, anOpponentGameScore);
    assertThat(result).isEqualTo(expected);
  }

  @Provide
  private Arbitrary<Tuple.Tuple2<GameScore, GameScore>> notDeuceGameScores() {
    final List<GameScore> scorerGameScores = List.of(GameScore.values());
    final List<GameScore> opponentGameScores = List.of(GameScore.values());
    final Collection<Tuple.Tuple2<GameScore, GameScore>> scores =
        scorerGameScores.stream()
            .flatMap(
                scorerGameScore ->
                    opponentGameScores.stream()
                        .map(opponentGameScore -> Tuple.of(scorerGameScore, opponentGameScore)))
            .filter(tuple -> !Tuple.of(GameScore.THIRTY, GameScore.FORTY).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.DEUCE, GameScore.DEUCE).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.FORTY, GameScore.ADVANTAGE).equals(tuple))
            .filter(tuple -> !Tuple.of(GameScore.ADVANTAGE, GameScore.FORTY).equals(tuple))
            .collect(Collectors.toList());
    return Arbitraries.of(scores);
  }

  @Property
  void not_activate_deuce_rule(
      @ForAll("notDeuceGameScores") Tuple.Tuple2<GameScore, GameScore> gameScores) {
    // Given
    gameScoresValidator = mock(GameScoresValidator.class);
    incrementGameScore = mock(IncrementGameScore.class);
    decrementGameScore = mock(DecrementGameScore.class);
    updateGameScore =
        new UpdateGameScore(gameScoresValidator, incrementGameScore, decrementGameScore);

    final boolean isDeuce = false;
    final GameScore aResultScorerGameScore = GameScore.getRandomScore();
    final Tuple2<GameScore, GameScore> scores =
        io.vavr.Tuple.of(gameScores.get1(), gameScores.get2());
    final Validation<String, Tuple2<GameScore, GameScore>> aValidation = Validation.valid(scores);
    when(gameScoresValidator.apply(scores._1(), scores._2())).thenReturn(aValidation);
    when(incrementGameScore.apply(gameScores.get1(), isDeuce)).thenReturn(aResultScorerGameScore);

    // When
    final Tuple2<GameScore, GameScore> result =
        updateGameScore.apply(gameScores.get1(), gameScores.get2());

    // Then
    final Tuple2<GameScore, GameScore> expected =
        io.vavr.Tuple.of(aResultScorerGameScore, gameScores.get2());
    verify(gameScoresValidator, times(1)).apply(gameScores.get1(), gameScores.get2());
    verify(incrementGameScore, times(1)).apply(gameScores.get1(), isDeuce);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void throw_illegal_argument_exception() {
    // Given
    final GameScore aScorerGameScore = GameScore.getRandomScore();
    final GameScore anOpponentGameScore = GameScore.getRandomScore();
    final String anError = "anError";
    final Validation<String, Tuple2<GameScore, GameScore>> aValidation =
        Validation.invalid(anError);
    when(gameScoresValidator.apply(aScorerGameScore, anOpponentGameScore)).thenReturn(aValidation);

    // When
    assertThatThrownBy(() -> updateGameScore.apply(aScorerGameScore, anOpponentGameScore))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(anError);

    // Then
    verify(gameScoresValidator, times(1)).apply(aScorerGameScore, anOpponentGameScore);
  }
}
