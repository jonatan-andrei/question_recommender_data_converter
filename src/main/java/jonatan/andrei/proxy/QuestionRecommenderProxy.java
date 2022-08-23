package jonatan.andrei.proxy;

import jonatan.andrei.dto.CreateUserRequestDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@RegisterRestClient(configKey = "qr-api")
public interface QuestionRecommenderProxy {

    @POST
    @Path("/user")
    void saveUser(CreateUserRequestDto createUserRequestDto);

}
