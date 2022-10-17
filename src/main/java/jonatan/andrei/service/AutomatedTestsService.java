package jonatan.andrei.service;

import io.quarkus.logging.Log;
import jonatan.andrei.domain.Dump;
import jonatan.andrei.domain.TestInformation;
import jonatan.andrei.dto.QuestionsAnsweredByUserResponseDto;
import jonatan.andrei.dto.RecommendedListResponseDto;
import jonatan.andrei.dto.TestInformationResponseDto;
import jonatan.andrei.dto.TestResultRequestDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
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
            questionRecommenderProxyService.clear(true);
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
            TestInformationResponseDto testInformationResponseDto = TestInformationResponseDto.builder()
                    .dumpName(dump.name())
                    .dumpEndDate(dump.getEndDate())
                    .endDateTestInformation(testInformation.getEndDate())
                    .daysAfterPartialEndDate(testInformation.getDaysAfterPartialEndDate())
                    .minimumOfPreviousAnswers(testInformation.getMinimumOfPreviousAnswers())
                    .percentage(testInformation.getPercentage())
                    .settings(settings)
                    .build();
            startTestBySettings(testInformationResponseDto);
        }
    }

    @Transactional
    public void startTestByTestInformation(String testInformation, Integer settings, boolean clearQR, boolean clearQRDatabase) {
        TestInformationResponseDto testInformationResponseDto = questionRecommenderProxyService.findTestInformation(testInformation, settings);

        if (clearQRDatabase) {
            questionRecommenderProxyService.clear(true);
            convertDataService.convertData(testInformationResponseDto.getDumpEndDate(), true, testInformationResponseDto.getDumpName());
        }

        if (clearQR) {
            questionRecommenderProxyService.clear(false);
            convertDataService.convertData(testInformationResponseDto.getEndDateTestInformation(), false, testInformationResponseDto.getDumpName());
        }

        startTestBySettings(testInformationResponseDto);
    }

    public void startTestBySettings(TestInformationResponseDto testInformationResponseDto) {
        questionRecommenderProxyService.clearRecommendations(false);
        questionRecommenderProxyService.saveRecommendationSettings(testInformationResponseDto.getSettings(), false);
        List<TestResultRequestDto.TestResultUserRequestDto> resultUsers = new ArrayList<>();
        List<QuestionsAnsweredByUserResponseDto> questionsAnsweredByUserResponseDtoList = questionRecommenderProxyService.findQuestionsAnsweredInPeriod(
                testInformationResponseDto.getEndDateTestInformation(), testInformationResponseDto.getEndDateTestInformation().plusDays(testInformationResponseDto.getDaysAfterPartialEndDate()), testInformationResponseDto.getMinimumOfPreviousAnswers(), true);
        for (QuestionsAnsweredByUserResponseDto questionsAnsweredByUserResponseDto : questionsAnsweredByUserResponseDtoList) {
            resultUsers.add(startTestByUser(questionsAnsweredByUserResponseDto));
        }
        Integer numberOfQuestions = resultUsers.stream()
                .map(TestResultRequestDto.TestResultUserRequestDto::getNumberOfQuestions)
                .mapToInt(Integer::intValue).sum();
        Integer numberOfRecommendedQuestions = resultUsers.stream()
                .map(TestResultRequestDto.TestResultUserRequestDto::getNumberOfRecommendedQuestions)
                .mapToInt(Integer::intValue).sum();

        String totalActivitySystem = questionRecommenderProxyService.findByPostClassificationType(false).toString();

        TestResultRequestDto testResultRequestDto = TestResultRequestDto.builder()
                .dumpName(testInformationResponseDto.getDumpName())
                .integratedDumpPercentage(testInformationResponseDto.getPercentage())
                .daysAfterDumpConsidered(testInformationResponseDto.getDaysAfterPartialEndDate())
                .settings(testInformationResponseDto.getSettings().toString().substring(0, Math.min(79999, testInformationResponseDto.getSettings().toString().length())))
                .totalActivitySystem(totalActivitySystem.substring(0, Math.min(79999, totalActivitySystem.length())))
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

        String userTags = questionRecommenderProxyService.findUserTags(questionsAnsweredByUserResponseDto.getIntegrationUserId(), false).toString();

        return TestResultRequestDto.TestResultUserRequestDto.builder()
                .integrationUserId(questionsAnsweredByUserResponseDto.getIntegrationUserId())
                .numberOfQuestions(questionsAnsweredByUserResponseDto.getQuestions().size())
                .numberOfRecommendedQuestions(numberOfRecommendedQuestions)
                .percentageOfCorrectRecommendations(calculatePercentageRecommendations(numberOfRecommendedQuestions, questionsAnsweredByUser.size()))
                .error(false)
                .userTags(userTags.substring(0, Math.min(79999, userTags.length())))
                .questions(questionsAnsweredByUserResponseDto.getQuestions().toString().substring(0, Math.min(79999, questionsAnsweredByUserResponseDto.getQuestions().toString().length())))
                .recommendedQuestions(recommendedListResponseDto.getQuestions().toString().substring(0, Math.min(79999, recommendedListResponseDto.getQuestions().toString().length())))
                .build();
    }

    private BigDecimal calculatePercentageRecommendations(Integer numberOfRecommendedQuestions, Integer numberOfQuestions) {
        if (numberOfRecommendedQuestions == 0 || numberOfQuestions == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(numberOfRecommendedQuestions).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(numberOfQuestions), 2, RoundingMode.HALF_UP);
    }
}
