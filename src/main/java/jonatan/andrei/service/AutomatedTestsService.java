package jonatan.andrei.service;

import jonatan.andrei.domain.Dump;
import jonatan.andrei.domain.RecommendationSettingsType;
import jonatan.andrei.domain.TestInformation;
import jonatan.andrei.dto.QuestionsAnsweredByUserResponseDto;
import jonatan.andrei.dto.RecommendedListResponseDto;
import jonatan.andrei.dto.TestResultRequestDto;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class AutomatedTestsService {

    @Inject
    ConvertDataService convertDataService;

    @Inject
    SettingsModelService settingsModelService;

    @Inject
    QuestionRecommenderProxyService questionRecommenderProxyService;

    @Transactional
    public void startAutomatedTests() {
        List<Dump> dumps = Arrays.asList(Dump.values());
        for (Dump dump : dumps) {
            //questionRecommenderProxyService.clear(true);
            convertDataService.convertData(dump.getEndDate(), true, dump.getDumpName());
            List<TestInformation> tests = Arrays.asList(TestInformation.values())
                    .stream().filter(t -> dump.equals(t.getDump())).collect(Collectors.toList());
            for (TestInformation testInformation : tests) {
                startTest(testInformation, dump);
            }
        }
    }

    private void startTest(TestInformation testInformation, Dump dump) {
        questionRecommenderProxyService.clear(false);
        convertDataService.convertData(testInformation.getEndDate(), false, dump.getDumpName());
        var settingsModels = settingsModelService.createSettingsModel();
        for (var settings : settingsModels) {
            questionRecommenderProxyService.clearRecommendations(false);
            questionRecommenderProxyService.saveRecommendationSettings(settings, false);
            startTestBySettings(settings, testInformation, dump);
        }
    }

    private void startTestBySettings(Map<RecommendationSettingsType, Integer> settings, TestInformation testInformation, Dump dump) {
        List<TestResultRequestDto.TestResultUserRequestDto> resultUsers = new ArrayList<>();
        List<QuestionsAnsweredByUserResponseDto> questionsAnsweredByUserResponseDtoList = questionRecommenderProxyService.findQuestionsAnsweredInPeriod(
                testInformation.getEndDate(), testInformation.getEndDate().plusDays(testInformation.getDaysAfterPartialEndDate()), testInformation.getMinimumOfPreviousAnswers(), true);
        for (QuestionsAnsweredByUserResponseDto questionsAnsweredByUserResponseDto : questionsAnsweredByUserResponseDtoList) {
            resultUsers.add(startTestByUser(questionsAnsweredByUserResponseDto));
        }
        Integer numberOfQuestions = resultUsers.stream()
                .map(TestResultRequestDto.TestResultUserRequestDto::getNumberOfQuestions)
                .mapToInt(Integer::intValue).sum();
        Integer numberOfRecommendedQuestions = resultUsers.stream()
                .map(TestResultRequestDto.TestResultUserRequestDto::getNumberOfRecommendedQuestions)
                .mapToInt(Integer::intValue).sum();

        TestResultRequestDto testResultRequestDto = TestResultRequestDto.builder()
                .dumpName(dump.getDumpName())
                .integratedDumpPercentage(testInformation.getPercentage())
                .daysAfterDumpConsidered(testInformation.getDaysAfterPartialEndDate())
                .settings(settings.toString())
                .totalActivitySystem(questionRecommenderProxyService.findByPostClassificationType(false).toString())
                .numberOfUsers(questionsAnsweredByUserResponseDtoList.size())
                .numberOfQuestions(numberOfQuestions)
                .numberOfRecommendedQuestions(numberOfRecommendedQuestions)
                .percentageOfCorrectRecommendations(calculatePercentageRecommendations(numberOfRecommendedQuestions, numberOfQuestions))
                .testDate(LocalDateTime.now())
                .users(resultUsers)
                .build();

        questionRecommenderProxyService.saveTestResult(testResultRequestDto, false);
    }

    private TestResultRequestDto.TestResultUserRequestDto startTestByUser(QuestionsAnsweredByUserResponseDto questionsAnsweredByUserResponseDto) {
        Integer lengthQuestionListPage = questionsAnsweredByUserResponseDto.getQuestions().size() > 20
                ? questionsAnsweredByUserResponseDto.getQuestions().size() : 20;
        RecommendedListResponseDto recommendedListResponseDto = questionRecommenderProxyService.findRecommendedList(lengthQuestionListPage,
                questionsAnsweredByUserResponseDto.getIntegrationUserId(), questionsAnsweredByUserResponseDto.getDateFirstAnswer(), false);

        List<String> questionsAnsweredByUser = questionsAnsweredByUserResponseDto.getQuestions()
                .stream().map(q -> q.getIntegrationQuestionId())
                .collect(Collectors.toList());

        Integer numberOfRecommendedQuestions = recommendedListResponseDto.getQuestions().stream()
                .map(q -> q.getIntegrationQuestionId())
                .filter(q -> questionsAnsweredByUser.contains(q))
                .collect(Collectors.toList())
                .size();

        return TestResultRequestDto.TestResultUserRequestDto.builder()
                .integrationUserId(questionsAnsweredByUserResponseDto.getIntegrationUserId())
                .numberOfQuestions(questionsAnsweredByUserResponseDto.getQuestions().size())
                .numberOfRecommendedQuestions(numberOfRecommendedQuestions)
                .percentageOfCorrectRecommendations(calculatePercentageRecommendations(numberOfRecommendedQuestions, questionsAnsweredByUser.size()))
                .error(false)
                .userTags(questionRecommenderProxyService.findUserTags(questionsAnsweredByUserResponseDto.getIntegrationUserId(), false).toString())
                .questions(questionsAnsweredByUserResponseDto.getQuestions().toString())
                .recommendedQuestions(recommendedListResponseDto.getQuestions().toString())
                .build();
    }

    private BigDecimal calculatePercentageRecommendations(Integer numberOfRecommendedQuestions, Integer numberOfQuestions) {
        if (numberOfRecommendedQuestions == 0 || numberOfQuestions == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(numberOfRecommendedQuestions).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(numberOfQuestions), 2, RoundingMode.HALF_UP);
    }
}
