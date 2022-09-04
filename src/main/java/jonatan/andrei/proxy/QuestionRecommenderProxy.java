package jonatan.andrei.proxy;

import jonatan.andrei.dto.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;

@RegisterRestClient(configKey = "qr-api")
public interface QuestionRecommenderProxy {

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

}
