package jonatan.andrei.service;

import jonatan.andrei.dto.TagRequestDto;
import jonatan.andrei.model.Tag;
import io.quarkus.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static jonatan.andrei.util.FieldUtil.findValue;

@ApplicationScoped
public class TagService {

    @Inject
    ReadXmlFileService readXmlFileService;

    @Inject
    QuestionRecommenderProxyService questionRecommenderProxyService;

    public void save(LocalDateTime endDate, boolean integrateWithQRDatabase, String dumpName) {
        List<Map<String, String>> tags = readXmlFileService.readXmlFile(dumpName, "Tags", Tag.class);
        for (Map<String, String> tag : tags) {
            try {
                questionRecommenderProxyService.saveTag(TagRequestDto.builder()
                        .name(findValue("TagName", tag, Tag.class))
                        .active(true)
                        .build(), integrateWithQRDatabase);
            } catch (Exception e) {
                Log.error("Error converting tag: " + findValue("Id", tag, Tag.class), e);
            }
        }
    }

}
