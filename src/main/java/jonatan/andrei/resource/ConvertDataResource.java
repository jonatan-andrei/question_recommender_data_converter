package jonatan.andrei.resource;

import jonatan.andrei.model.User;
import jonatan.andrei.service.ReadXmlFileService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Path("/convert-data")
@Slf4j
public class ConvertDataResource {

    @Inject
    ReadXmlFileService readXmlFileService;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String convertData() {
        List<Map<Field, String>> users = readXmlFileService.readXmlFile("Users", User.class);
        log.info("Users: " + users);
        log.info("Tamanho: " + users.size());
        return "teste";
    }


}
