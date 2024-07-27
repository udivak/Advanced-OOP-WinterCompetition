package game.entities.sportsman;
import game.enums.Discipline;
import game.enums.Gender;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class Snowboarder - extends Winter-Sportsman to define its base class as a Snowboarder
 */
public class Snowboarder extends WinterSportsman {
    public Snowboarder(String name, double age, Gender gender, double acceleration, double maxSpeed, Discipline discipline) {
        super(maxSpeed, acceleration, name, age, gender, discipline);
    }
    public boolean equals(Object other) {
        if (other instanceof Snowboarder) {
            return this.getName().equals(((Snowboarder) other).getName()) &&
                    this.getAge() == ((Snowboarder) other).getAge() &&
                    this.getGender() == ((Snowboarder) other).getGender() &&
                    this.getAcceleration() == ((Snowboarder) other).getAcceleration() &&
                    this.getMaxSpeed() == ((Snowboarder) other).getMaxSpeed() &&
                    this.getDiscipline() == ((Snowboarder) other).getDiscipline();
        }
        return false;
    }

    public String toString() {
        return "Snowboarder: " + super.toString();
    }
}