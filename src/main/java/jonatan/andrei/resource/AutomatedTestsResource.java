package jonatan.andrei.resource;

import jonatan.andrei.service.AutomatedTestsService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/automated-tests")
@Slf4j
public class AutomatedTestsResource {

    @Inject
    AutomatedTestsService automatedTestsService;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public void startAutomatedTests() {
        automatedTestsService.startAutomatedTests();
    }

}
