package jonatan.andrei.service;

import jonatan.andrei.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Slf4j
public class ConvertDataService {

    @Inject
    UserService userService;

    public void convertData(){
        userService.save();
    }

}
