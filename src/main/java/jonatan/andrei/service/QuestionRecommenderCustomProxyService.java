package jonatan.andrei.service;

import jonatan.andrei.dto.RecommendationSettingsRequestDto;
import jonatan.andrei.dto.RecommendedListResponseDto;
import jonatan.andrei.proxy.QuestionRecommenderCustomProxy;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class QuestionRecommenderCustomProxyService {

    private final QuestionRecommenderCustomProxy questionRecommenderCustomProxy;

    public QuestionRecommenderCustomProxyService() {
        questionRecommenderCustomProxy = RestClientBuilder.newBuilder()
                .baseUri(URI.create("http://localhost:8079"))
                .connectTimeout(20L, TimeUnit.MINUTES)
                .build(QuestionRecommenderCustomProxy.class);
    }

    public RecommendedListResponseDto findRecommendedList(Integer lengthQuestionListPage,
                                                          String integrationUserId,
                                                          LocalDateTime dateOfRecommendations,
                                                          Integer pageNumber) {
        return questionRecommenderCustomProxy.findRecommendedList(lengthQuestionListPage, integrationUserId, dateOfRecommendations, pageNumber);
    }

    public void saveRecommendationSettings(List<RecommendationSettingsRequestDto> recommendationSettings) {
        questionRecommenderCustomProxy.saveRecommendationSettings(recommendationSettings);
    }

}
