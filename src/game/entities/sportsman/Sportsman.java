package game.entities.sportsman;
import game.entities.MobileEntity;
import game.enums.Gender;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class Sportsman - extends MobileEntity to describe a Sportsman
 */
public abstract class Sportsman extends MobileEntity {
    private final String name;
    private final double age;
    private final Gender gender;
    public Sportsman(double maxSpeed, double acceleration, String name, double age, Gender gender) {
        super(maxSpeed, acceleration);
        if (age < 0)
            throw new IllegalArgumentException("age cannot be negative");
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
    public String getName() { return this.name; }
    public double getAge() { return age; }
    public Gender getGender() { return gender; }
    public String toString() {
        return "Sportsman: (name = "+this.name+", age = "+this.age+", gender = "+this.gender.toString()+")" + super.toString();
    }
}

