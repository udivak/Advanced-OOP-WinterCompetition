package game.arena;
import game.entities.IMobileEntity;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Interface IArena - interface with functionality of a competitive arena
 */
public interface IArena {
    public double getFriction();
    boolean isFinished(IMobileEntity me);
    double getLength();
}
