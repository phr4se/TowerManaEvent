package phrase.towerManaEvent.action;

import phrase.towerManaEvent.action.context.Context;
import phrase.towerManaEvent.action.context.impl.StringContext;
import phrase.towerManaEvent.action.impl.BroadcastAction;
import phrase.towerManaEvent.action.impl.ConsoleAction;
import phrase.towerManaEvent.action.impl.MessageAction;
import phrase.towerManaEvent.action.impl.SoundAction;

public enum ActionType {
    CONSOLE(new ConsoleAction(), StringContext.class),
    MESSAGE(new MessageAction(), StringContext.class),
    BROADCAST(new BroadcastAction(), StringContext.class),
    SOUND(new SoundAction(), SoundAction.class);
    private final Action<?> action;
    private final Class<?> clazz;

    ActionType(Action<?> action, Class<?> clazz) {
        this.action = action;
        this.clazz = clazz;
    }

    public <C extends Context> Action<C> getAction() {
        return (Action<C>) action;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
