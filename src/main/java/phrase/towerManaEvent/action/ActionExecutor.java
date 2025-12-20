package phrase.towerManaEvent.action;

import org.bukkit.entity.Player;
import phrase.towerManaEvent.action.context.Context;
import phrase.towerManaEvent.action.context.impl.SoundContext;
import phrase.towerManaEvent.action.context.impl.StringContext;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ActionExecutor {

    private static final Map<Class<?>, Function<String, Context>> VALIDATORS = Map.of(
            StringContext.class, StringContext::validate,
            SoundContext.class, SoundContext::validate
    );


    public static void execute(Player player, Map<ActionType, List<String>> map) {

        map.entrySet().forEach(entry -> {

            ActionType actionType = entry.getKey();
            List<String> messages = entry.getValue();

            Action<Context> action = actionType.getAction();

            Function<String, ? extends Context> function = VALIDATORS.get(actionType.getClazz());
            messages.forEach(message -> action.execute(player, function.apply(message)));

        });

    }

}
