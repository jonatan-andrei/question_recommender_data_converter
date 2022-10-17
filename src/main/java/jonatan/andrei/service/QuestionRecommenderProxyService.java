package jonatan.andrei.service;

import jonatan.andrei.dto.*;
import jonatan.andrei.proxy.QuestionRecommenderDatabaseProxy;
import jonatan.andrei.proxy.QuestionRecommenderProxy;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
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

    void clear(boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            questionRecommenderDatabaseProxy.clear();
        } else {
            questionRecommenderProxy.clear();
        }
    }

    void clearRecommendations(boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            questionRecommenderDatabaseProxy.clearRecommendations();
        } else {
            questionRecommenderProxy.clearRecommendations();
        }
    }

    void saveRecommendationSettings(List<RecommendationSettingsRequestDto> recommendationSettings, boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            questionRecommenderDatabaseProxy.saveRecommendationSettings(recommendationSettings);
        } else {
            questionRecommenderProxy.saveRecommendationSettings(recommendationSettings);
        }
    }

    List<QuestionsAnsweredByUserResponseDto> findQuestionsAnsweredInPeriod(LocalDateTime startDate,
                                                                           LocalDateTime endDate,
                                                                           Integer minimumOfPreviousAnswers,
                                                                           boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            return questionRecommenderDatabaseProxy.findQuestionsAnsweredInPeriod(startDate, endDate, minimumOfPreviousAnswers);
        } else {
            return questionRecommenderProxy.findQuestionsAnsweredInPeriod(startDate, endDate, minimumOfPreviousAnswers);
        }
    }

    RecommendedListResponseDto findRecommendedList(Integer lengthQuestionListPage, String integrationUserId,
                                                   LocalDateTime dateOfRecommendations, boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            return questionRecommenderDatabaseProxy.findRecommendedList(lengthQuestionListPage, integrationUserId, dateOfRecommendations, 1);
        } else {
            return questionRecommenderProxy.findRecommendedList(lengthQuestionListPage, integrationUserId, dateOfRecommendations, 1);
        }
    }

    List<UserTagDto> findUserTags(String integrationUserId, boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            return questionRecommenderDatabaseProxy.findUserTags(integrationUserId);
        } else {
            return questionRecommenderProxy.findUserTags(integrationUserId);
        }
    }

    TotalActivitySystemResponseDto findByPostClassificationType(boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            return questionRecommenderDatabaseProxy.findByPostClassificationType("TAG");
        } else {
            return questionRecommenderProxy.findByPostClassificationType("TAG");
        }
    }

    void saveTestResult(TestResultRequestDto testResultRequestDto, boolean integrateWithQRDatabase) {
        if (integrateWithQRDatabase) {
            questionRecommenderDatabaseProxy.saveTestResult(testResultRequestDto);
        } else {
            questionRecommenderProxy.saveTestResult(testResultRequestDto);
        }
    }

    TestInformationResponseDto findTestInformation(String testInformation, Integer settingsModel) {
        return questionRecommenderProxy.findTestInformation(testInformation, settingsModel);
    }


}
