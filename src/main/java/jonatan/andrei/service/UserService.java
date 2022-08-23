package jonatan.andrei.service;

import jonatan.andrei.dto.CreateUserRequestDto;
import jonatan.andrei.dto.UserPreferencesRequestDto;
import jonatan.andrei.model.User;
import jonatan.andrei.proxy.QuestionRecommenderProxy;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UserService {

    @Inject
    ReadXmlFileService readXmlFileService;

    @Inject
    @RestClient
    QuestionRecommenderProxy questionRecommenderProxy;

    public void save() {
        List<Map<Field, String>> users = readXmlFileService.readXmlFile("Users", User.class);
        questionRecommenderProxy.saveUser(CreateUserRequestDto.builder()
                .integrationUserId("999999")
                .registrationDate(LocalDateTime.now())
                .userPreferences(UserPreferencesRequestDto.builder()
                        .emailNotificationEnable(false)
                        .notificationEnable(false)
                        .recommendationEnable(false)
                        .explicitIntegrationCategoriesIds(new ArrayList<>())
                        .ignoredIntegrationCategoriesIds(new ArrayList<>())
                        .explicitTags(new ArrayList<>())
                        .ignoredTags(new ArrayList<>())
                        .build())
                .build());
    }
}
