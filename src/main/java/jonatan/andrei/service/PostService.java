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
import io.quarkus.logging.Log;

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
public class PostService {

    @Inject
    ReadXmlFileService readXmlFileService;

    @Inject
    QuestionRecommenderProxyService questionRecommenderProxyService;

    public void save(LocalDateTime endDate, boolean integrateWithQRDatabase, String dumpName) {
        List<Map<String, String>> posts = readXmlFileService.readXmlFile(dumpName, "Posts", Post.class);
        for (Map<String, String> post : posts) {
            try {
                String id = findValue("Id", post, Post.class);
                PostType postType = PostType.findByPostTypeId(findValue("PostTypeId", post, Post.class));
                String userId = findValue("OwnerUserId", post, Post.class).trim();
                LocalDateTime creationDate = LocalDateTime.parse(findValue("CreationDate", post, Post.class));
                if (nonNull(postType) && creationDate.isBefore(endDate)) {
                    boolean isQuestion = PostType.QUESTION.equals(postType);
                    questionRecommenderProxyService.savePost(CreatePostRequestDto.builder()
                            .integrationPostId(id)
                            .integrationParentPostId(isQuestion ? null : findValue("ParentId", post, Post.class))
                            .postType(isQuestion ? "QUESTION" : "ANSWER")
                            .publicationDate(creationDate)
                            .title(findValue("Title", post, Post.class).replaceAll("\\<.*?\\>", ""))
                            .contentOrDescription(findValue("Body", post, Post.class).replaceAll("\\<.*?\\>", ""))
                            .url(null)
                            .integrationCategoriesIds(new ArrayList<>())
                            .tags(splitTags(findValue("Tags", post, Post.class)))
                            .integrationUserId(userId)
                            .integrationAnonymousUserId(isNull(userId) ? null : UUID.randomUUID().toString())
                            .build(), integrateWithQRDatabase);

                    if (isQuestion) {
                        Integer views = Optional.ofNullable(findValue("ViewCount", post, Post.class))
                                .map(v -> Integer.valueOf(v))
                                .orElse(0);
                        questionRecommenderProxyService.registerViews(ViewsRequestDto.builder()
                                .integrationUsersId(new ArrayList<>())
                                .totalViews(views)
                                .integrationQuestionId(findValue("Id", post, Post.class))
                                .build(), integrateWithQRDatabase);
                    }
                }
            } catch (Exception e) {
                Log.error("Error converting post: " + findValue("Id", post, Post.class), e);
            }
        }
    }

    public void registerBestAnswer(LocalDateTime endDate, boolean integrateWithQRDatabase, String dumpName) {
        List<Map<String, String>> posts = readXmlFileService.readXmlFile(dumpName, "Posts", Post.class);
        for (Map<String, String> post : posts) {
            try {
                LocalDateTime creationDate = LocalDateTime.parse(findValue("CreationDate", post, Post.class));
                boolean isQuestion = findValue("PostTypeId", post, Post.class).equals(PostType.QUESTION.getPostTypeId());
                String bestAnswerId = (findValue("AcceptedAnswerId", post, Post.class));
                PostResponseDto postResponseDto = findPost(bestAnswerId, integrateWithQRDatabase);
                if (isQuestion && !StringUtil.isNullOrEmpty(bestAnswerId) && creationDate.isBefore(endDate) && nonNull(postResponseDto)) {
                    questionRecommenderProxyService.registerBestAnswer(BestAnswerRequestDto.builder()
                            .integrationQuestionId(findValue("Id", post, Post.class))
                            .integrationAnswerId(bestAnswerId)
                            .selected(true)
                            .build(), integrateWithQRDatabase);
                }
            } catch (Exception e) {
                Log.error("Error converting post: " + findValue("Id", post, Post.class), e);
            }
        }
    }

    public void saveComments(LocalDateTime endDate, boolean integrateWithQRDatabase, String dumpName) {
        List<Map<String, String>> comments = readXmlFileService.readXmlFile(dumpName, "Comments", Comment.class);
        for (Map<String, String> comment : comments) {
            try {
                LocalDateTime creationDate = LocalDateTime.parse(findValue("CreationDate", comment, Comment.class));
                if (creationDate.isBefore(endDate)) {
                    String parentPostId = findValue("PostId", comment, Comment.class);
                    PostResponseDto postResponseDto = findPost(parentPostId, integrateWithQRDatabase);
                    String userId = findValue("UserId", comment, Comment.class);
                    if (nonNull(postResponseDto)) {
                        questionRecommenderProxyService.savePost(CreatePostRequestDto.builder()
                                .integrationPostId("C" + findValue("Id", comment, Comment.class))
                                .integrationParentPostId(parentPostId)
                                .postType(PostType.QUESTION.equals(postResponseDto.getPostType()) ? "QUESTION_COMMENT" : "ANSWER_COMMENT")
                                .publicationDate(creationDate)
                                .title(null)
                                .contentOrDescription(findValue("Text", comment, Comment.class))
                                .url(null)
                                .integrationCategoriesIds(new ArrayList<>())
                                .tags(new ArrayList<>())
                                .integrationUserId(userId)
                                .integrationAnonymousUserId(isNull(userId) ? null : UUID.randomUUID().toString())
                                .build(), integrateWithQRDatabase);
                    }
                }
            } catch (Exception e) {
                Log.error("Error converting comment: " + findValue("Id", comment, Comment.class), e);
            }
        }
    }

    public void saveQuestionFollower(LocalDateTime endDate, boolean integrateWithQRDatabase, String dumpName) {
        List<Map<String, String>> votes = readXmlFileService.readXmlFile(dumpName, "Votes", Vote.class);
        for (Map<String, String> vote : votes) {
            try {
                LocalDateTime creationDate = LocalDateTime.parse(findValue("CreationDate", vote, Vote.class));
                if (creationDate.isBefore(endDate)) {
                    String questionId = findValue("PostId", vote, Vote.class);
                    PostResponseDto question = findPost(questionId, integrateWithQRDatabase);
                    if (nonNull(question)) {
                        VoteType voteType = VoteType.findByVoteTypeId(findValue("VoteTypeId", vote, Vote.class));
                        if (VoteType.Favorite.equals(voteType)) {
                            questionRecommenderProxyService.saveFollower(QuestionFollowerRequestDto.builder()
                                    .integrationQuestionId(questionId)
                                    .integrationUserId(findValue("UserId", vote, Vote.class))
                                    .followed(true)
                                    .startDate(creationDate)
                                    .build(), integrateWithQRDatabase);
                        }
                    }
                }
            } catch (Exception e) {
                Log.error("Error converting vote: " + findValue("Id", vote, Vote.class), e);
            }
        }
    }

    public void registerDuplicateQuestion(LocalDateTime endDate, boolean integrateWithQRDatabase, String dumpName) {
        List<Map<String, String>> links = readXmlFileService.readXmlFile(dumpName, "PostLinks", PostLink.class);
        for (Map<String, String> link : links) {
            try {
                PostLinkType postLinkType = PostLinkType.findByPostLinkId(findValue("LinkTypeId", link, PostLink.class));
                if (PostLinkType.DUPLICATE.equals(postLinkType)) {
                    questionRecommenderProxyService.registerDuplicateQuestion(DuplicateQuestionRequestDto.builder()
                            .integrationQuestionId(findValue("PostId", link, PostLink.class))
                            .integrationDuplicateQuestionId((findValue("RelatedPostId", link, PostLink.class)))
                            .build(), integrateWithQRDatabase);
                }
            } catch (Exception e) {
                Log.error("Error converting link: " + findValue("Id", link, PostLink.class), e);
            }
        }
    }

    private List<String> splitTags(String tags) {
        return Stream.of(tags.split(">"))
                .map(t -> t.replace("<", ""))
                .map(t -> t.replace(">", ""))
                .collect(Collectors.toList());
    }

    private PostResponseDto findPost(String postId, boolean integrateWithQRDatabase) {
        try {
            return questionRecommenderProxyService.findPost(postId, integrateWithQRDatabase);
        } catch (Exception e) {
            return null;
        }
    }

}
