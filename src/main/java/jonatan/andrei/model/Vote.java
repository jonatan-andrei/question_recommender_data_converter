package jonatan.andrei.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Vote {

    private String Id;

    private String PostId;

    private String UserId;

    private String VoteTypeId;

    private LocalDateTime CreationDate;

}
