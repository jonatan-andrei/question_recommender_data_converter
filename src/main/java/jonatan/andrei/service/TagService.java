package jonatan.andrei.service;

import jonatan.andrei.dto.TagRequestDto;
import jonatan.andrei.model.Tag;
import jonatan.andrei.proxy.QuestionRecommenderProxy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static jonatan.andrei.util.FieldUtil.findValue;

@ApplicationScoped
@Slf4j
public class TagService {

    @Inject
    ReadXmlFileService readXmlFileService;

    @Inject
    @RestClient
    QuestionRecommenderProxy questionRecommenderProxy;

    public void save() {
        List<Map<String, String>> tags = readXmlFileService.readXmlFile("Tags", Tag.class);
        for (Map<String, String> tag : tags) {
            questionRecommenderProxy.saveTag(TagRequestDto.builder()
                    .name(findValue("TagName", tag, Tag.class))
                    .active(true)
                    .build());
        }
    }

}
