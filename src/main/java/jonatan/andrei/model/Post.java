package jonatan.andrei.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Post {

    private String Id;

    private String PostTypeId;

    private String ParentId;

    private String AcceptedAnswerId;

    private LocalDateTime CreationDate;

    private Integer Score;

    private Integer ViewCount;

    private String Body;

    private String OwnerUserId;

    private LocalDateTime LastActivityDate;

    private String Title;

    private String Tags;

    private Integer AnswerCount;

    private Integer CommentCount;

    private String ContentLicense;

    private String LastEditorUserId;

    private LocalDateTime LastEditDate;

}
