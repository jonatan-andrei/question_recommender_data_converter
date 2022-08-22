package jonatan.andrei;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/convert-data")
public class ConvertDataResource {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String convertData() {
        return "Success!";
    }


}
