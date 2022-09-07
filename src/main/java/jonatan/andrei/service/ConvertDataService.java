package jonatan.andrei.service;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
@Slf4j
public class ConvertDataService {

    @Inject
    UserService userService;

    @Inject
    PostService postService;

    @Inject
    TagService tagService;

    public void convertData(LocalDateTime endDate, boolean integrateWithQRDatabase, String dumpName) {
        endDate = Optional.ofNullable(endDate).orElse(LocalDateTime.now());
        tagService.save(endDate, integrateWithQRDatabase, dumpName);
        userService.save(endDate, integrateWithQRDatabase, dumpName);
        postService.save(endDate, integrateWithQRDatabase, dumpName);
        postService.registerBestAnswer(endDate, integrateWithQRDatabase, dumpName);
        postService.saveComments(endDate, integrateWithQRDatabase, dumpName);
        postService.saveQuestionFollower(endDate, integrateWithQRDatabase, dumpName);
    }

}
