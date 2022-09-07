package jonatan.andrei.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum SettingsModel {

    ONE(settingsModel1());

    private Map<RecommendationSettingsType, Integer> modelSettings;

    public static Map<RecommendationSettingsType, Integer> settingsModel1() {
        Map<RecommendationSettingsType, Integer> settingsModel = new HashMap<>();
        return settingsModel;
    }

}
