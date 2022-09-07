package jonatan.andrei.service;

import jonatan.andrei.dto.*;
import jonatan.andrei.proxy.QuestionRecommenderDatabaseProxy;
import jonatan.andrei.proxy.QuestionRecommenderProxy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Slf4j
public class QuestionRecommenderProxyService {

    @Inject
    @RestClient
    QuestionRecommenderProxy questionRecommenderProxy;

    @Inject
    @RestClient
    QuestionRecommenderDatabaseProxy questionRecommenderDatabaseProxy;

    public void saveUser(CreateUserRequestDto createUserRequestDto, boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            questionRecommenderDatabaseProxy.saveUser(createUserRequestDto);
        } else {
            questionRecommenderProxy.saveUser(createUserRequestDto);
        }
    }

    void savePost(CreatePostRequestDto createPostRequestDto, boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            questionRecommenderDatabaseProxy.savePost(createPostRequestDto);
        } else {
            questionRecommenderProxy.savePost(createPostRequestDto);
        }
    }

    PostResponseDto findPost(String integrationPostId, boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            return questionRecommenderDatabaseProxy.findPost(integrationPostId);
        } else {
            return questionRecommenderProxy.findPost(integrationPostId);
        }
    }

    void registerViews(ViewsRequestDto viewsRequestDto, boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            questionRecommenderDatabaseProxy.registerViews(viewsRequestDto);
        } else {
            questionRecommenderProxy.registerViews(viewsRequestDto);
        }
    }

    void registerBestAnswer(BestAnswerRequestDto bestAnswerRequestDto, boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            questionRecommenderDatabaseProxy.registerBestAnswer(bestAnswerRequestDto);
        } else {
            questionRecommenderProxy.registerBestAnswer(bestAnswerRequestDto);
        }
    }

    void saveTag(TagRequestDto tagRequestDto, boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            questionRecommenderDatabaseProxy.saveTag(tagRequestDto);
        } else {
            questionRecommenderProxy.saveTag(tagRequestDto);
        }
    }

    void saveFollower(QuestionFollowerRequestDto questionFollowerRequestDto, boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            questionRecommenderDatabaseProxy.saveFollower(questionFollowerRequestDto);
        } else {
            questionRecommenderProxy.saveFollower(questionFollowerRequestDto);
        }
    }

    void registerDuplicateQuestion(DuplicateQuestionRequestDto duplicateQuestionRequestDto, boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            questionRecommenderDatabaseProxy.registerDuplicateQuestion(duplicateQuestionRequestDto);
        } else {
            questionRecommenderProxy.registerDuplicateQuestion(duplicateQuestionRequestDto);
        }
    }
}
