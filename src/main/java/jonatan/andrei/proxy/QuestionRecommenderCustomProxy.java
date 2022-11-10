package jonatan.andrei.proxy;

import jonatan.andrei.dto.RecommendationSettingsRequestDto;
import jonatan.andrei.dto.RecommendedListResponseDto;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.time.LocalDateTime;
import java.util.List;

public interface QuestionRecommenderCustomProxy {

    @GET
    @Path("/recommended-list")
    RecommendedListResponseDto findRecommendedList(@QueryParam("lengthQuestionListPage") Integer lengthQuestionListPage,
                                                   @QueryParam("integrationUserId") String integrationUserId,
                                                   @QueryParam("dateOfRecommendations") LocalDateTime dateOfRecommendations,
                                                   @QueryParam("pageNumber") Integer pageNumber);

    @PUT
    @Path("/recommendation-settings")
    void saveRecommendationSettings(List<RecommendationSettingsRequestDto> recommendationSettings);

}
