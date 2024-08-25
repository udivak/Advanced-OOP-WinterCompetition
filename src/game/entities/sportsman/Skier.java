package game.entities.sportsman;
import game.enums.Discipline;
import game.enums.Gender;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class Skier - extends Winter-Sportsman to define its base class as a Skier
 */
public class Skier extends WinterSportsman {
    public Skier(String name, double age, Gender gender, double acceleration, double maxSpeed, Discipline discipline, int id) {
        super(maxSpeed, acceleration, name, age, gender, discipline, id);
    }
    public boolean equals(Object other) {
        if (other instanceof Skier) {
            return this.getName().equals(((Skier) other).getName()) &&
                    this.getAge() == ((Skier) other).getAge() &&
                    this.getGender() == ((Skier) other).getGender() &&
                    this.getAcceleration() == ((Skier) other).getAcceleration() &&
                    this.getMaxSpeed() == ((Skier) other).getMaxSpeed() &&
                    this.getDiscipline() == ((Skier) other).getDiscipline();
        }
        return false;
    }
    @Override
    public Skier clone() {
        return new Skier(this.getName(), this.getAge(), this.getGender(), this.getAcceleration(), this.getMaxSpeed(),
                        this.getDiscipline(), this.id);
    }
    public String toString() {
        return "Skier: " + super.toString();
    }
}
