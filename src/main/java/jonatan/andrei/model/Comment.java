package jonatan.andrei.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Comment {

    private String Id;

    private String PostId;

    private String Text;

    private LocalDateTime CreationDate;

    private String UserId;
}
