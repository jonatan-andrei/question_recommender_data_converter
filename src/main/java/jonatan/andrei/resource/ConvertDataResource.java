package jonatan.andrei.resource;

import jonatan.andrei.model.User;
import jonatan.andrei.service.ReadXmlFileService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/convert-data")
public class ConvertDataResource {

    @Inject
    ReadXmlFileService readXmlFileService;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String convertData() {
        User user = readXmlFileService.readXmlFile();
        return user.getDisplayName();
    }


}
