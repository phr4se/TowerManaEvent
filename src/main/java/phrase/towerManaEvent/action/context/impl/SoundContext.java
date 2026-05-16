package phrase.towerManaEvent.action.context.impl;

import org.bukkit.Sound;
import phrase.towerManaEvent.action.context.Context;

public record SoundContext(Sound sound, float volume, float pitch) implements Context {
    public static SoundContext validate(String data) {
        String[] strings = data.split(";");
        Sound sound = Sound.valueOf(strings[0]);
        float volume = Float.parseFloat(strings[1]);
        float pitch = Float.parseFloat(strings[2]);
        return new SoundContext(sound, volume, pitch);
    }
}
