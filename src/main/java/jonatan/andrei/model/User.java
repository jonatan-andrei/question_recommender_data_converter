package jonatan.andrei.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class User {

    private String Id;

    private Integer Reputation;

    private LocalDateTime CreationDate;

    private String DisplayName;

    private LocalDateTime LastAccessDate;

    private String Location;

    private String AboutMe;

    private String WebsiteUrl;

    private Integer Views;

    private Integer UpVotes;

    private Integer DownVotes;

    private String ProfileImageUrl;

    private Integer AccountId;

}
