package jonatan.andrei.proxy;

import jonatan.andrei.dto.BestAnswerRequestDto;
import jonatan.andrei.dto.CreatePostRequestDto;
import jonatan.andrei.dto.CreateUserRequestDto;
import jonatan.andrei.dto.ViewsRequestDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@RegisterRestClient(configKey = "qr-api")
public interface QuestionRecommenderProxy {

    @POST
    @Path("/user")
    void saveUser(CreateUserRequestDto createUserRequestDto);

    @POST
    @Path("/post")
    void savePost(CreatePostRequestDto createPostRequestDto);

    @POST
    @Path("/post/register-views")
    void registerViews(ViewsRequestDto viewsRequestDto);

    @PUT
    @Path("/post//register-best-answer")
    void registerBestAnswer(BestAnswerRequestDto bestAnswerRequestDto);

}
