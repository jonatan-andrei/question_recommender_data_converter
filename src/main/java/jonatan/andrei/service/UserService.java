package jonatan.andrei.service;

import jonatan.andrei.dto.CreateUserRequestDto;
import jonatan.andrei.dto.UserPreferencesRequestDto;
import jonatan.andrei.model.User;
import jonatan.andrei.proxy.QuestionRecommenderProxy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static jonatan.andrei.util.FieldUtil.findValue;

@ApplicationScoped
@Slf4j
public class UserService {

    @Inject
    ReadXmlFileService readXmlFileService;

    @Inject
    @RestClient
    QuestionRecommenderProxy questionRecommenderProxy;

    public void save() {
        List<Map<String, String>> users = readXmlFileService.readXmlFile("Users", User.class);
        for (Map<String, String> user : users) {
            questionRecommenderProxy.saveUser(CreateUserRequestDto.builder()
                    .integrationUserId(findValue("Id", user, User.class))
                    .username(findValue("DisplayName", user, User.class))
                    .registrationDate(LocalDateTime.parse(findValue("CreationDate", user, User.class)))
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
}
