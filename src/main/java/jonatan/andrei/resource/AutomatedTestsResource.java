package jonatan.andrei.resource;

import jonatan.andrei.domain.SettingsModelType;
import jonatan.andrei.service.AutomatedTestsService;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.Arrays.asList;

@Path("/automated-tests")
public class AutomatedTestsResource {

    @Inject
    AutomatedTestsService automatedTestsService;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public void startAutomatedTests() {
        automatedTestsService.startAutomatedTests();
    }

    @POST
    @Path("/test-information")
    @Produces(MediaType.TEXT_PLAIN)
    public void startAutomatedTests(@QueryParam("testInformation") String testInformation, @QueryParam("settings") SettingsModelType settings, @QueryParam("clearQR") boolean clearQR, @QueryParam("clearQRDatabase") boolean clearQRDatabase) {
        automatedTestsService.startTestByTestInformation(testInformation, settings, clearQR, clearQRDatabase);
    }

    @POST
    @Path("/test-information/all")
    @Produces(MediaType.TEXT_PLAIN)
    public void startAllAutomatedTests(@QueryParam("testInformation") String testInformation, @QueryParam("clearQR") boolean clearQR, @QueryParam("clearQRDatabase") boolean clearQRDatabase) {
        List<SettingsModelType> settingsList = asList(SettingsModelType.values());
        for (SettingsModelType settings : settingsList) {
            automatedTestsService.startTestByTestInformation(testInformation, settings, clearQR, clearQRDatabase);
        }
    }

}
