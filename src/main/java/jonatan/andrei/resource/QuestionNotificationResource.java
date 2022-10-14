package jonatan.andrei.resource;

import jonatan.andrei.dto.ListQuestionNotificationRequestDto;
import io.quarkus.logging.Log;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/question-notification")
public class QuestionNotificationResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveNotificationList(ListQuestionNotificationRequestDto listQuestionNotificationRequestDto) {
        Log.info("Received: " + listQuestionNotificationRequestDto);
    }

}
