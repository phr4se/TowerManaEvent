package phrase.towerManaEvent.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionTransformer {

    private static final Pattern PATTERN = Pattern.compile("\\[(\\S+)] ?(.*)");

    public static Map<ActionType, List<String>> transform(List<String> settings) {

        Map<ActionType, List<String>> map = new HashMap<>();

        for(String setting : settings) {

            Matcher matcher = PATTERN.matcher(setting);

            ActionType actionType = null;
            String string = null;

            while (matcher.find()) {

                actionType = ActionType.valueOf(matcher.group(1).toUpperCase());
                string = matcher.group(2);

            }

            if(actionType != null && string != null) {
                String finalString = string;
                map.compute(actionType, (k, v) -> {
                    if(v == null || v.isEmpty()) {
                        List<String> strings = new ArrayList<>();
                        strings.add(finalString);
                        return strings;
                    }
                    v.add(finalString);
                    return v;
                });
            }

        }

        return map;

    }

}
