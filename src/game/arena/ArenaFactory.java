package game.arena;
import game.enums.SnowSurface;
import game.enums.WeatherCondition;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class ArenaFactory - Factory for instantinating arenas according to a string (arena type).
 */
public class ArenaFactory {
    public ArenaFactory() {}
    public IArena createArena(String arenaType, double length, SnowSurface surface, WeatherCondition condition) {
        if (arenaType.equals("Winter")) {
            return new WinterArena(length, surface, condition);
        }
        else
            return null;
    }
}
