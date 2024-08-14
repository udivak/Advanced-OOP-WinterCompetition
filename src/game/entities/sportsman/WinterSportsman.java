package game.entities.sportsman;
import game.enums.League;
import game.competition.Competitor;
import game.enums.Discipline;
import game.enums.Gender;
/**
 * @authors Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class WinterSportsman - extends Sportsman to describe a Winter-Sportsman
 */
public abstract class WinterSportsman extends Sportsman implements Competitor {
    Discipline discipline;
    public WinterSportsman(double maxSpeed, double acceleration, String name, double age, Gender
                           gender, Discipline discipline) {
        super(maxSpeed, acceleration, name, age, gender);
        this.setAcceleration(this.getAcceleration() + League.calcAccelerationBonus(this.getAge()));
        this.discipline = discipline;
    }
    public abstract WinterSportsman clone();
    public void setDiscipline(Discipline discipline) { this.discipline = discipline; }
    public Discipline getDiscipline() { return discipline; }
    @Override
    public void initRace() {
        this.getLocation().setX(0);
    }
    @Override
    public void move(double friction) {
        double new_speed = this.getSpeed() + (this.getAcceleration() * friction);
        new_speed = Math.min(new_speed, this.getMaxSpeed());
        this.setSpeed(new_speed);
        this.getLocation().setX(this.getLocation().getX() + new_speed);
    }
    @Override
    public String toString() {
        return "WinterSportsman: (discipline = " + discipline + ") " + super.toString();
    }
}
