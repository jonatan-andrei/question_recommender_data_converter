package jonatan.andrei.resource;

import jonatan.andrei.service.ConvertDataService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/convert-data")
@Slf4j
public class ConvertDataResource {

    @Inject
    ConvertDataService convertDataService;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public void convertData() {
        convertDataService.convertData();
    }


}
