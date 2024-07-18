package game.arena;
import game.entities.IMobileEntity;
import game.enums.SnowSurface;
import game.enums.WeatherCondition;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class WinterArena - implements IArena to define a winter competition arena
 */
public class WinterArena implements IArena{
    private double length;
    private SnowSurface surface;
    private WeatherCondition condition;
    public WinterArena(double length, SnowSurface surface, WeatherCondition condition) {
        if (length < 0) {
            throw new IllegalArgumentException("Invalid length value");
        }
        this.length = length;
        this.surface = surface;
        this.condition = condition;
    }
    public double getLength() { return this.length; }
    public SnowSurface getSurface() { return this.surface; }
    public WeatherCondition getCondition() { return this.condition; }
    public void setLength(double len){
        if (len < 0) {
            throw new IllegalArgumentException("Invalid length value");
        }
        this.length = len;
    }
    public void setSurface(SnowSurface sur){
        this.surface = sur;
    }
    public void setCondition(WeatherCondition condition) {
        this.condition = condition;
    }
    @Override
    public double getFriction() { return this.surface.getFriction(); }
    @Override
    public boolean isFinished(IMobileEntity me) {
        return me.getLocation().getX() >= this.getLength();
    }
    public boolean equals(Object other) {
        if (other instanceof WinterArena){
            return this.getLength()==((WinterArena) other).getLength() && this.surface == ((WinterArena) other).getSurface()
                    && this.condition == ((WinterArena) other).getCondition();
        }
        return false;
    }
    @Override
    public String toString() {
        return "WinterArena: (length = " +getLength() + "SnowSurface = "+getSurface() + "WeatherCondition = "
                +getCondition() +")";
    }
}
