package com.jmaquin.kata.tennis.predicate;

import static com.jmaquin.kata.tennis.DataFactory.aSetWithScores;
import static org.assertj.core.api.Assertions.assertThat;

import com.jmaquin.kata.tennis.domain.Set;
import com.jmaquin.kata.tennis.domain.enums.SetScore;
import com.jmaquin.kata.tennis.domain.predicate.IsSetFinish;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.jqwik.api.*;

class IsSetFinishShould {
  private final IsSetFinish isSetFinish = new IsSetFinish();

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
            .filter(
                tuple ->
                    (SetScore.SIX.equals(tuple.get1()) && tuple.get2().compareTo(SetScore.FIVE) < 0)
                        || (SetScore.SEVEN.equals(tuple.get1())
                            && tuple.get2().compareTo(SetScore.FOUR) > 0)
                        || (SetScore.SIX.equals(tuple.get2())
                            && tuple.get1().compareTo(SetScore.FIVE) < 0)
                        || (SetScore.SEVEN.equals(tuple.get2())
                            && tuple.get1().compareTo(SetScore.FOUR) > 0))
            .collect(Collectors.toList());
    return Arbitraries.of(scores);
  }

  @Property
  void return_true(@ForAll("setScores") Tuple.Tuple2<SetScore, SetScore> setScores) {
    // Given
    final Set aSet = aSetWithScores(setScores.get1(), setScores.get2());

    // When
    final boolean result = isSetFinish.test(aSet);

    // Then
    assertThat(result).isTrue();
  }

  @Provide
  private Arbitrary<Tuple.Tuple2<SetScore, SetScore>> setScoresNotFinished() {
    final List<SetScore> scorerSetScores = List.of(SetScore.values());
    final List<SetScore> opponentSetScores = List.of(SetScore.values());
    final Collection<Tuple.Tuple2<SetScore, SetScore>> scores =
        scorerSetScores.stream()
            .flatMap(
                scorerSetScore ->
                    opponentSetScores.stream()
                        .map(opponentSetScore -> Tuple.of(scorerSetScore, opponentSetScore)))
            .filter(
                tuple ->
                    !((SetScore.SIX.equals(tuple.get1())
                            && tuple.get2().compareTo(SetScore.FIVE) < 0)
                        || (SetScore.SEVEN.equals(tuple.get1())
                            && tuple.get2().compareTo(SetScore.FOUR) > 0)
                        || (SetScore.SIX.equals(tuple.get2())
                            && tuple.get1().compareTo(SetScore.FIVE) < 0)
                        || (SetScore.SEVEN.equals(tuple.get2())
                            && tuple.get1().compareTo(SetScore.FOUR) > 0)))
            .collect(Collectors.toList());
    return Arbitraries.of(scores);
  }

  @Property
  void return_false(@ForAll("setScoresNotFinished") Tuple.Tuple2<SetScore, SetScore> setScores) {
    // Given
    final Set aSet = aSetWithScores(setScores.get1(), setScores.get2());

    // When
    final boolean result = isSetFinish.test(aSet);

    // Then
    assertThat(result).isFalse();
  }
}
