package jonatan.andrei.service;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Slf4j
public class ConvertDataService {

    @Inject
    UserService userService;

    @Inject
    PostService postService;

    @Inject
    TagService tagService;

    public void convertData() {
        tagService.save();
        userService.save();
        postService.save();
        postService.registerBestAnswer();
        postService.saveComments();
        postService.saveQuestionFollower();
    }

}
