package jonatan.andrei.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum PostType {
    QUESTION("1"),
    ANSWER("2");

    private String postTypeId;

    public static PostType findByPostTypeId(String postTypeId) {
        return Stream.of(values())
                .filter(p -> p.getPostTypeId().equals(postTypeId))
                .findFirst()
                .orElse(null);
    }
}
