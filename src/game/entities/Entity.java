package game.entities;
import utilities.Point;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class Entity - an Entity contains Point-(x,y)
 */
public abstract class Entity {
    private Point location;
    public Entity() {
        this.location = new Point();
    }
    public Entity(double x, double y) {
        this.location = new Point(x, y);
    }
    public Entity(Point p) {
        this.location = new Point(p.getX(), p.getY());
    }
    public Point getLocation() { return this.location; }
    public void setLocation(double x, double y) {
        this.location.setX(x);
        this.location.setY(y);
    }
    public void setLocation(Point p) {
        this.location = new Point(p);
    }
    public String toString() {
        return "Entity: (" + this.location.toString() + ")";
    }
}
