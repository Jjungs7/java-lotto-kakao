package lotto.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lotto.domain.dto.LottoNumber;
import lotto.domain.dto.WinningNumbers;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MatchResultsTest {
    @Test
    void matchesAreCorrect() {
        LottoNumbers luckyNumbers = new LottoNumbers(Stream.of(1, 2, 3, 4, 5, 6)
                .map(LottoNumber::valueOf)
                .collect(Collectors.toList()));
        LottoNumber bonusNumber = LottoNumber.valueOf(7);
        WinningNumbers winningNumbers = new WinningNumbers(luckyNumbers, bonusNumber);
        LottoMatcher lottoMatcher = new LottoMatcher(winningNumbers);

        LottoTickets lottoTickets = new LottoTickets(Arrays.asList(
                new LottoNumbers(LottoNumberArray.asList(1, 2, 3, 4, 5, 6)),
                new LottoNumbers(LottoNumberArray.asList(1, 2, 3, 4, 5, 7)),
                new LottoNumbers(LottoNumberArray.asList(1, 2, 3, 4, 5, 8)),
                new LottoNumbers(LottoNumberArray.asList(1, 2, 3, 4, 8, 9)),
                new LottoNumbers(LottoNumberArray.asList(1, 2, 3, 8, 9, 10)),
                new LottoNumbers(LottoNumberArray.asList(1, 2, 3, 7, 8, 9)),
                new LottoNumbers(LottoNumberArray.asList(1, 2, 7, 8, 9, 10)),
                new LottoNumbers(LottoNumberArray.asList(1, 2, 8, 9, 10, 11))));

        assertThat(lottoMatcher.match(lottoTickets))
                .isEqualTo(new MatchResults(Arrays.asList(
                        MatchResult.FIRST,
                        MatchResult.SECOND,
                        MatchResult.THIRD,
                        MatchResult.FOURTH,
                        MatchResult.FIFTH,
                        MatchResult.FIFTH,
                        MatchResult.NULL,
                        MatchResult.NULL
                )));
    }

    @Test
    void testHashCode() {
        List<MatchResult> results = Arrays.asList(
                MatchResult.FIRST,
                MatchResult.SECOND
        );
        List<MatchResult> differentResult = Arrays.asList(
                MatchResult.FIRST,
                MatchResult.THIRD
        );
        MatchResults matchResults = new MatchResults(results);

        assertThat(matchResults.hashCode()).isEqualTo(new MatchResults(results).hashCode());
        assertThat(matchResults.hashCode()).isNotEqualTo(new MatchResults(differentResult).hashCode());
    }

    @Test
    void testDelegate() {
        MatchResults matchResults = new MatchResults(Arrays.asList(
                MatchResult.FIRST,
                MatchResult.THIRD
        ));

        matchResults.delegate(consumer -> assertThat(consumer.containsKey(MatchResult.FIRST)).isTrue());
        matchResults.delegate(consumer -> assertThat(consumer.containsKey(MatchResult.SECOND)).isFalse());
    }
}
