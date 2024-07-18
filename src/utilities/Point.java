package utilities;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class Point - (x,y)
 */
public class Point {
    private double x;
    private double y;
    public Point() {            //default ctor
        setX(0);
        setY(0);
    }
    public Point(double x, double y) {
        setX(x);
        setY(y);
    }
    public Point(Point other) {         //copy ctor
        setX(other.getX());
        setY(other.getY());
    }
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public void setX(double x) {
        if (x<0 || x>1000000) {
            throw new IllegalArgumentException("Invalid x value");
        }
        this.x = x;
    }
    public void setY(double y) {
        if (y<0 || y>800) {
            throw new IllegalArgumentException("Invalid y value");
        }
        this.y = y;
    }
    public String toString() {
        return "Point: (x=" + getX() + ", y=" + getY() + ")";
    }
    public boolean equals(Object other) {
        if (other instanceof Point) { return this.x == ((Point)other).getX() && this.y == ((Point)other).getY(); }
        return false;
    }
}
