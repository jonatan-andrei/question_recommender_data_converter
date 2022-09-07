package jonatan.andrei.service;

import jonatan.andrei.dto.CreateUserRequestDto;
import jonatan.andrei.dto.UserPreferencesRequestDto;
import jonatan.andrei.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
    QuestionRecommenderProxyService questionRecommenderProxyService;

    public void save(LocalDateTime endDate, boolean integrateWithQRDatabase, String dumpName) {
        List<Map<String, String>> users = readXmlFileService.readXmlFile(dumpName, "Users", User.class);
        for (Map<String, String> user : users) {
            try {
                LocalDateTime creationDate = LocalDateTime.parse(findValue("CreationDate", user, User.class));
                if (creationDate.isBefore(endDate)) {
                    questionRecommenderProxyService.saveUser(CreateUserRequestDto.builder()
                            .integrationUserId(findValue("Id", user, User.class))
                            .username(findValue("DisplayName", user, User.class))
                            .registrationDate(creationDate)
                            .userPreferences(UserPreferencesRequestDto.builder()
                                    .emailNotificationEnable(false)
                                    .notificationEnable(false)
                                    .recommendationEnable(false)
                                    .explicitIntegrationCategoriesIds(new ArrayList<>())
                                    .ignoredIntegrationCategoriesIds(new ArrayList<>())
                                    .explicitTags(new ArrayList<>())
                                    .ignoredTags(new ArrayList<>())
                                    .build())
                            .build(), integrateWithQRDatabase);
                }
            } catch (Exception e) {
                log.error("Error converting user: " + findValue("Id", user, User.class), e);
            }
        }
    }
}
