package jonatan.andrei.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public enum TestInformation {

    PORTUGUESE_10_PERCENTAGE(LocalDateTime.parse("2015-08-03T14:17:41.973"), 3, 3, BigDecimal.valueOf(10));

    private LocalDateTime endDate;

    private Integer daysAfterPartialEndDate;

    private Integer minimumOfPreviousAnswers;

    private BigDecimal percentage;
}
