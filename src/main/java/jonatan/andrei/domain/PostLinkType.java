package jonatan.andrei.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum PostLinkType {

    LINKED("1"),
    DUPLICATE("3");

    private String postLinkTypeId;


    public static PostLinkType findByPostLinkId(String postLinkTypeId) {
        return Stream.of(values())
                .filter(l -> l.getPostLinkTypeId().equals(postLinkTypeId))
                .findFirst()
                .orElse(null);
    }
}
