package jonatan.andrei.resource;

import jonatan.andrei.dto.ListQuestionNotificationRequestDto;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/question-notification")
@Slf4j
public class QuestionNotificationResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveNotificationList(ListQuestionNotificationRequestDto listQuestionNotificationRequestDto) {
        log.info("Received: " + listQuestionNotificationRequestDto);
    }

}
