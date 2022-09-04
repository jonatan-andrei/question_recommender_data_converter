package jonatan.andrei.dto;

import jonatan.andrei.domain.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    @NotNull
    private Long postId;

    @NotNull
    private String integrationPostId;

    @NotNull
    private PostType postType;

    @NotNull
    private Long userId;

    @NotNull
    private LocalDateTime publicationDate;

    @NotNull
    private LocalDateTime updateDate;

    @NotNull
    private LocalDateTime integrationDate;

    @NotNull
    private boolean hidden;

    @NotNull
    private Integer upvotes;

    @NotNull
    private Integer downvotes;

}
