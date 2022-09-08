package jonatan.andrei.service;

import jonatan.andrei.domain.RecommendationSettingsType;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Slf4j
public class SettingsModelService {

    public List<Map<RecommendationSettingsType, Integer>> createSettingsModel() {
        List<Map<RecommendationSettingsType, Integer>> settingsModel = new ArrayList<>();
        settingsModel.add(settingsModel1());
        return settingsModel;
    }

    public Map<RecommendationSettingsType, Integer> settingsModel1() {
        Map<RecommendationSettingsType, Integer> settingsModel = new HashMap<>();
        return settingsModel;
    }

}
