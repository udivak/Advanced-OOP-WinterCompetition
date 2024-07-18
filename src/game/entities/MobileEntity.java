package game.entities;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class MobileEntity - extends Entity to add mobility functionality
 */
public abstract class MobileEntity extends Entity implements IMobileEntity {
    private double maxSpeed;
    private double acceleration;
    private double speed;
    public MobileEntity(double maxSpeed, double acceleration) {
        super();
        if (maxSpeed < 0)
            throw new IllegalArgumentException("maxSpeed cannot be smaller than 0");
        this.speed = 0;
        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;
    }
    public double getMaxSpeed() { return this.maxSpeed; }
    public double getAcceleration() { return this.acceleration; }
    public double getSpeed() { return this.speed; }
    public void setMaxSpeed(double maxSpeed) {
        if (maxSpeed < 0)
            throw new IllegalArgumentException("maxSpeed cannot be smaller than 0");
        this.maxSpeed = maxSpeed;
    }
    public void setAcceleration(double acceleration) { this.acceleration = acceleration; }
    public void setSpeed(double speed) { this.speed = speed; }
    public String toString() {
        return "MobileEntity: (speed = "+this.speed+", maxSpeed = "+this.maxSpeed+", acceleration = "+this.acceleration+
                ")" + "\n" + super.toString();
    }
}
