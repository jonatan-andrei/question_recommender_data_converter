package jonatan.andrei.service;

import io.netty.util.internal.StringUtil;
import jonatan.andrei.domain.PostType;
import jonatan.andrei.dto.BestAnswerRequestDto;
import jonatan.andrei.dto.CreatePostRequestDto;
import jonatan.andrei.dto.ViewsRequestDto;
import jonatan.andrei.model.Comment;
import jonatan.andrei.model.Post;
import jonatan.andrei.proxy.QuestionRecommenderProxy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jonatan.andrei.util.FieldUtil.findValue;

@ApplicationScoped
@Slf4j
public class PostService {

    @Inject
    ReadXmlFileService readXmlFileService;

    @Inject
    @RestClient
    QuestionRecommenderProxy questionRecommenderProxy;

    public void save() {
        List<Map<String, String>> posts = readXmlFileService.readXmlFile("Posts", Post.class);
        for (Map<String, String> post : posts) {
            boolean isQuestion = findValue("PostTypeId", post, Post.class).equals(PostType.QUESTION.getPostTypeId());
            questionRecommenderProxy.savePost(CreatePostRequestDto.builder()
                    .integrationPostId(findValue("Id", post, Post.class))
                    .integrationParentPostId(isQuestion ? null : findValue("ParentId", post, Post.class))
                    .postType(isQuestion ? "QUESTION" : "ANSWER")
                    .publicationDate(LocalDateTime.parse(findValue("CreationDate", post, Post.class)))
                    .title(findValue("Title", post, Post.class))
                    .contentOrDescription(findValue("Body", post, Post.class))
                    .url(null)
                    .integrationCategoriesIds(new ArrayList<>())
                    .tags(splitTags(findValue("Tags", post, Post.class)))
                    .integrationUserId(findValue("OwnerUserId", post, Post.class))
                    .integrationAnonymousUserId(null)
                    .build());

            if (isQuestion) {
                Integer views = Optional.ofNullable(findValue("ViewCount", post, Post.class))
                        .map(v -> Integer.valueOf(v))
                        .orElse(0);
                questionRecommenderProxy.registerViews(ViewsRequestDto.builder()
                        .integrationUsersId(new ArrayList<>())
                        .totalViews(views)
                        .integrationQuestionId(findValue("Id", post, Post.class))
                        .build());
            }
        }
    }

    public void registerBestAnswer() {
        List<Map<String, String>> posts = readXmlFileService.readXmlFile("Posts", Post.class);
        for (Map<String, String> post : posts) {
            boolean isQuestion = findValue("PostTypeId", post, Post.class).equals(PostType.QUESTION.getPostTypeId());
            String bestAnswerId = (findValue("AcceptedAnswerId", post, Post.class));
            if (isQuestion && !StringUtil.isNullOrEmpty(bestAnswerId)) {
                questionRecommenderProxy.registerBestAnswer(BestAnswerRequestDto.builder()
                        .integrationQuestionId(findValue("Id", post, Post.class))
                        .integrationAnswerId(bestAnswerId)
                        .selected(true)
                        .build());
            }
        }
    }

    public void saveComments() {
        // TODO consultar post para saber o tipo
        List<Map<String, String>> comments = readXmlFileService.readXmlFile("Comments", Comment.class);
        for (Map<String, String> comment : comments) {
            questionRecommenderProxy.savePost(CreatePostRequestDto.builder()
                    .integrationPostId("C" + findValue("Id", comment, Comment.class))
                    .integrationParentPostId(findValue("PostId", comment, Comment.class))
                    .postType("QUESTION_COMMENT")
                    .publicationDate(LocalDateTime.parse(findValue("CreationDate", comment, Comment.class)))
                    .title(null)
                    .contentOrDescription(findValue("Text", comment, Comment.class))
                    .url(null)
                    .integrationCategoriesIds(new ArrayList<>())
                    .tags(new ArrayList<>())
                    .integrationUserId(findValue("UserId", comment, Comment.class))
                    .integrationAnonymousUserId(null)
                    .build());
        }
    }

    private List<String> splitTags(String tags) {
        return Stream.of(tags.split(">"))
                .map(t -> t.replace("<", ""))
                .map(t -> t.replace(">", ""))
                .collect(Collectors.toList());
    }

}
