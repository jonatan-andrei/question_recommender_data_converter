package jonatan.andrei.proxy;

import jonatan.andrei.dto.*;

import javax.ws.rs.*;
import java.time.LocalDateTime;
import java.util.List;

public interface QuestionRecommenderAbstractProxy {

    @POST
    @Path("/user")
    void saveUser(CreateUserRequestDto createUserRequestDto);

    @POST
    @Path("/post")
    void savePost(CreatePostRequestDto createPostRequestDto);

    @GET
    @Path("/post/{integrationPostId}")
    PostResponseDto findPost(@PathParam(value = "integrationPostId") String integrationPostId);

    @POST
    @Path("/post/register-views")
    void registerViews(ViewsRequestDto viewsRequestDto);

    @PUT
    @Path("/post/register-best-answer")
    void registerBestAnswer(BestAnswerRequestDto bestAnswerRequestDto);

    @POST
    @Path("/tag")
    void saveTag(TagRequestDto tagRequestDto);

    @POST
    @Path("/post/register-question-follower")
    void saveFollower(QuestionFollowerRequestDto questionFollowerRequestDto);

    @PUT
    @Path("/post/register-duplicate_question")
    void registerDuplicateQuestion(DuplicateQuestionRequestDto duplicateQuestionRequestDto);

    @DELETE
    @Path("/question-recommender/clear")
    void clear();

    @DELETE
    @Path("/question-recommender/clear-recommendations")
    void clearRecommendations();

    @PUT
    @Path("/recommendation-settings")
    void saveRecommendationSettings(List<RecommendationSettingsRequestDto> recommendationSettings);

    @GET
    @Path("/recommended-list")
    RecommendedListResponseDto findRecommendedList(@QueryParam("lengthQuestionListPage") Integer lengthQuestionListPage,
                                                   @QueryParam("integrationUserId") String integrationUserId,
                                                   @QueryParam("dateOfRecommendations") LocalDateTime dateOfRecommendations,
                                                   @QueryParam("pageNumber") Integer pageNumber);


    @GET
    @Path("/user/find-questions-answered-in-period")
    List<QuestionsAnsweredByUserResponseDto> findQuestionsAnsweredInPeriod(@QueryParam("startDate") LocalDateTime startDate,
                                                                           @QueryParam("endDate") LocalDateTime endDate,
                                                                           @QueryParam("minimumOfPreviousAnswers") Integer minimumOfPreviousAnswers);

    @GET
    @Path("/user/find-user-tags")
    List<UserTagDto> findUserTags(@QueryParam("integrationUserId") String integrationUserId);

    @GET
    @Path("/total-activity-system")
    TotalActivitySystemResponseDto findByPostClassificationType(@QueryParam("postClassificationType") String postClassificationType);

    @POST
    @Path("/test-result")
    void saveTestResult(TestResultRequestDto testResultRequestDto);

    @GET
    @Path("/test-information")
    TestInformationResponseDto findTestInformation(@QueryParam("testInformation") String testInformation, @QueryParam("settingsModel") Integer settingsModel);
}
