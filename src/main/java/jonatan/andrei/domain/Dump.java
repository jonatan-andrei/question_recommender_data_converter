package jonatan.andrei.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public enum Dump {

    PORTUGUESE("portuguese", LocalDateTime.parse("2022-06-05T03:02:17.153"));

    private String dumpName;

    private LocalDateTime endDate;
}
