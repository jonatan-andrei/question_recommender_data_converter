package jonatan.andrei.resource;

import jonatan.andrei.service.ConvertDataService;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;

@Path("/convert-data")
public class ConvertDataResource {

    @Inject
    ConvertDataService convertDataService;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public void convertData(@QueryParam("endDate") LocalDateTime endDate,
                            @QueryParam("integrateWithQRDatabase") boolean integrateWithQRDatabase,
                            @QueryParam("dumpName") String dumpName) {
        convertDataService.convertData(endDate, integrateWithQRDatabase, dumpName);
    }

    @POST
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public void convertDataTest(@QueryParam("dumpName") String dumpName) {
        convertDataService.convertDataTest(dumpName);
    }

}
