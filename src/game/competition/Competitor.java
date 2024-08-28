package game.competition;
import game.entities.IMobileEntity;
import utilities.Point;

/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Interface Competitor - interface with functionality of a competitive object
 */
public interface Competitor extends IMobileEntity {
    void initRace();
    String getName();
    Point getLocation();
}
