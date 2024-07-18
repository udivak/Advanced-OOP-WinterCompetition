package game.entities;
import utilities.Point;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Interface IMobileEntity - an interface with functionality of moving Entity
 */
public interface IMobileEntity {
    Point getLocation();
    void move(double friction);
}
