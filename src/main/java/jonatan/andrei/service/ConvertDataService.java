package jonatan.andrei.service;

import io.quarkus.logging.Log;
import jonatan.andrei.model.Tag;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ConvertDataService {

    @Inject
    UserService userService;

    @Inject
    PostService postService;

    @Inject
    TagService tagService;

    @Inject
    ReadXmlFileService readXmlFileService;

    public void convertData(LocalDateTime endDate, boolean integrateWithQRDatabase, String dumpName) {
        endDate = Optional.ofNullable(endDate).orElse(LocalDateTime.now());
        tagService.save(endDate, integrateWithQRDatabase, dumpName);
        userService.save(endDate, integrateWithQRDatabase, dumpName);
        postService.save(endDate, integrateWithQRDatabase, dumpName);
        postService.registerBestAnswer(endDate, integrateWithQRDatabase, dumpName);
        postService.saveComments(endDate, integrateWithQRDatabase, dumpName);
        postService.saveQuestionFollower(endDate, integrateWithQRDatabase, dumpName);
    }

    public void convertDataTest(String dumpName) {
        List<Map<String, String>> tags = readXmlFileService.readXmlFile(dumpName, "Tags", Tag.class);
        Log.info("Found: " + tags.size());
    }

}
