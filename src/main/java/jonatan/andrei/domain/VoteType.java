package jonatan.andrei.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum VoteType {

    AcceptedByOriginator("1"),
    UpMod("2"),
    DownMod("3"),
    Offensive("4"),
    Favorite("5"),
    Close("6"),
    Reopen("7"),
    BountyStart("8"),
    BountyClose("9"),
    Deletion("10"),
    Undeletion("11"),
    Spam("12"),
    InformModerator("13");

    private String voteTypeId;

    public static VoteType findByVoteTypeId(String voteTypeId) {
        return Stream.of(values())
                .filter(v -> v.getVoteTypeId().equals(voteTypeId))
                .findFirst()
                .orElse(null);
    }
}
