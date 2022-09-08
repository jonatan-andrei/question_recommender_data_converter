package jonatan.andrei.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public enum TestInformation {

    //PORTUGUESE_10_PERCENTAGE(Dump.PORTUGUESE, LocalDateTime.parse("2015-08-03T14:17:41.973"), 7, 3, BigDecimal.valueOf(10)),
    //PORTUGUESE_40_PERCENTAGE(Dump.PORTUGUESE, LocalDateTime.parse("2016-10-28T00:43:07.851"), 7, 3, BigDecimal.valueOf(40)),
    //PORTUGUESE_50_PERCENTAGE(Dump.PORTUGUESE, LocalDateTime.parse("2017-05-29T00:36:05.874"), 7, 3, BigDecimal.valueOf(50)),
    //PORTUGUESE_60_PERCENTAGE(Dump.PORTUGUESE, LocalDateTime.parse("2018-04-07T21:42:20.641"), 7, 3, BigDecimal.valueOf(60)),
    //PORTUGUESE_70_PERCENTAGE(Dump.PORTUGUESE, LocalDateTime.parse("2019-06-03T16:45:10.134"), 7, 3, BigDecimal.valueOf(70)),
    //PORTUGUESE_80_PERCENTAGE(Dump.PORTUGUESE, LocalDateTime.parse("2020-05-03T21:16:44.518"), 7, 3, BigDecimal.valueOf(80)),
    //PORTUGUESE_90_PERCENTAGE(Dump.PORTUGUESE, LocalDateTime.parse("2021-03-23T12:39:30.071"), 7, 3, BigDecimal.valueOf(90)),
    PT_STACKOVERLOW_40_PERCENTAGE(Dump.PT_STACKOVERLOW, LocalDateTime.parse("2017-01-31T12:54:03.583"), 7, 3, BigDecimal.valueOf(40));

    private Dump dump;

    private LocalDateTime endDate;

    private Integer daysAfterPartialEndDate;

    private Integer minimumOfPreviousAnswers;

    private BigDecimal percentage;
}
