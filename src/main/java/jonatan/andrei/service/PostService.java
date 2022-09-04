package jonatan.andrei.service;

import io.netty.util.internal.StringUtil;
import jonatan.andrei.domain.PostLinkType;
import jonatan.andrei.domain.PostType;
import jonatan.andrei.domain.VoteType;
import jonatan.andrei.dto.*;
import jonatan.andrei.model.Comment;
import jonatan.andrei.model.Post;
import jonatan.andrei.model.PostLink;
import jonatan.andrei.model.Vote;
import jonatan.andrei.proxy.QuestionRecommenderProxy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
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
            try {
                PostType postType = PostType.findByPostTypeId(findValue("PostTypeId", post, Post.class));
                String userId = findValue("OwnerUserId", post, Post.class).trim();
                if (nonNull(postType)) {
                    boolean isQuestion = PostType.QUESTION.equals(postType);
                    questionRecommenderProxy.savePost(CreatePostRequestDto.builder()
                            .integrationPostId(findValue("Id", post, Post.class))
                            .integrationParentPostId(isQuestion ? null : findValue("ParentId", post, Post.class))
                            .postType(isQuestion ? "QUESTION" : "ANSWER")
                            .publicationDate(LocalDateTime.parse(findValue("CreationDate", post, Post.class)))
                            .title(findValue("Title", post, Post.class).replaceAll("\\<.*?\\>", ""))
                            .contentOrDescription(findValue("Body", post, Post.class).replaceAll("\\<.*?\\>", ""))
                            .url(null)
                            .integrationCategoriesIds(new ArrayList<>())
                            .tags(splitTags(findValue("Tags", post, Post.class)))
                            .integrationUserId(userId)
                            .integrationAnonymousUserId(isNull(userId) ? null : UUID.randomUUID().toString())
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
            } catch (Exception e) {
                log.error("Error converting post: " + findValue("Id", post, Post.class), e);
            }
        }
    }

    public void registerBestAnswer() {
        List<Map<String, String>> posts = readXmlFileService.readXmlFile("Posts", Post.class);
        for (Map<String, String> post : posts) {
            try {
                boolean isQuestion = findValue("PostTypeId", post, Post.class).equals(PostType.QUESTION.getPostTypeId());
                String bestAnswerId = (findValue("AcceptedAnswerId", post, Post.class));
                if (isQuestion && !StringUtil.isNullOrEmpty(bestAnswerId)) {
                    questionRecommenderProxy.registerBestAnswer(BestAnswerRequestDto.builder()
                            .integrationQuestionId(findValue("Id", post, Post.class))
                            .integrationAnswerId(bestAnswerId)
                            .selected(true)
                            .build());
                }
            } catch (Exception e) {
                log.error("Error converting post: " + findValue("Id", post, Post.class), e);
            }
        }
    }

    public void saveComments() {
        List<Map<String, String>> comments = readXmlFileService.readXmlFile("Comments", Comment.class);
        for (Map<String, String> comment : comments) {
            try {
                String parentPostId = findValue("PostId", comment, Comment.class);
                PostResponseDto postResponseDto = findPost(parentPostId);
                String userId = findValue("UserId", comment, Comment.class);
                if (nonNull(postResponseDto)) {
                    questionRecommenderProxy.savePost(CreatePostRequestDto.builder()
                            .integrationPostId("C" + findValue("Id", comment, Comment.class))
                            .integrationParentPostId(parentPostId)
                            .postType(PostType.QUESTION.equals(postResponseDto.getPostType()) ? "QUESTION_COMMENT" : "ANSWER_COMMENT")
                            .publicationDate(LocalDateTime.parse(findValue("CreationDate", comment, Comment.class)))
                            .title(null)
                            .contentOrDescription(findValue("Text", comment, Comment.class))
                            .url(null)
                            .integrationCategoriesIds(new ArrayList<>())
                            .tags(new ArrayList<>())
                            .integrationUserId(userId)
                            .integrationAnonymousUserId(isNull(userId) ? null : UUID.randomUUID().toString())
                            .build());
                }
            } catch (Exception e) {
                log.error("Error converting comment: " + findValue("Id", comment, Comment.class), e);
            }
        }
    }

    public void saveQuestionFollower() {
        List<Map<String, String>> votes = readXmlFileService.readXmlFile("Votes", Vote.class);
        for (Map<String, String> vote : votes) {
            try {
                String questionId = findValue("PostId", vote, Vote.class);
                PostResponseDto question = findPost(questionId);
                if (nonNull(question)) {
                    VoteType voteType = VoteType.findByVoteTypeId(findValue("VoteTypeId", vote, Vote.class));
                    if (VoteType.Favorite.equals(voteType)) {
                        questionRecommenderProxy.saveFollower(QuestionFollowerRequestDto.builder()
                                .integrationQuestionId(questionId)
                                .integrationUserId(findValue("UserId", vote, Vote.class))
                                .followed(true)
                                .startDate(LocalDateTime.parse(findValue("CreationDate", vote, Vote.class)))
                                .build());
                    }
                }
            } catch (Exception e) {
                log.error("Error converting vote: " + findValue("Id", vote, Vote.class), e);
            }
        }
    }

    public void registerDuplicateQuestion() {
        List<Map<String, String>> links = readXmlFileService.readXmlFile("PostLinks", PostLink.class);
        for (Map<String, String> link : links) {
            try {
                PostLinkType postLinkType = PostLinkType.findByPostLinkId(findValue("LinkTypeId", link, PostLink.class));
                if (PostLinkType.DUPLICATE.equals(postLinkType)) {
                    questionRecommenderProxy.registerDuplicateQuestion(DuplicateQuestionRequestDto.builder()
                            .integrationQuestionId(findValue("PostId", link, PostLink.class))
                            .integrationDuplicateQuestionId((findValue("RelatedPostId", link, PostLink.class)))
                            .build());
                }
            } catch (Exception e) {
                log.error("Error converting link: " + findValue("Id", link, PostLink.class), e);
            }
        }
    }

    private List<String> splitTags(String tags) {
        return Stream.of(tags.split(">"))
                .map(t -> t.replace("<", ""))
                .map(t -> t.replace(">", ""))
                .collect(Collectors.toList());
    }

    private PostResponseDto findPost(String postId) {
        try {
            return questionRecommenderProxy.findPost(postId);
        } catch (Exception e) {
            return null;
        }
    }

}
