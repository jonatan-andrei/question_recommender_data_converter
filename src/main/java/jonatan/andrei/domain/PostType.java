package jonatan.andrei.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PostType {
    QUESTION("1"),
    ANSWER("2");

    private String postTypeId;
}
