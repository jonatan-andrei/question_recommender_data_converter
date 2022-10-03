package jonatan.andrei.resource;

import jonatan.andrei.dto.ListRecommendedEmailRequestDto;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/recommended-email")
@Slf4j
public class RecommendedEmailResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveRecommendedEmailList(ListRecommendedEmailRequestDto recommendedEmailRequestDtoList) {
        log.info("Received: " + recommendedEmailRequestDtoList);
    }
}
