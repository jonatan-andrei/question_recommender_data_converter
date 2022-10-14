package jonatan.andrei.resource;

import jonatan.andrei.dto.ListRecommendedEmailRequestDto;
import io.quarkus.logging.Log;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/recommended-email")
public class RecommendedEmailResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveRecommendedEmailList(ListRecommendedEmailRequestDto recommendedEmailRequestDtoList) {
        Log.info("Received: " + recommendedEmailRequestDtoList);
    }
}
