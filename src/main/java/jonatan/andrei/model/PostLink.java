package jonatan.andrei.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLink {

    private String Id;

    private String CreationDate;

    private String PostId;

    private String RelatedPostId;

    private String LinkTypeId;
}
