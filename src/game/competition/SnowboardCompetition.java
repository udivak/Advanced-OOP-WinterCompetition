package game.competition;
import game.arena.WinterArena;
import game.entities.sportsman.Snowboarder;
import game.enums.Discipline;
import game.enums.Gender;
import game.enums.League;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class SkiCompetition - extends WinterCompetition to define a Snowboard competition
 */
public class SnowboardCompetition extends WinterCompetition {
    public SnowboardCompetition(WinterArena arena, int maxCompetitors, Discipline discipline,
                          League league, Gender gender){
        super(arena, maxCompetitors, discipline, league, gender);
    }
    @Override
    public boolean isValidCompetitor(Competitor competitor) {
        if (competitor instanceof Snowboarder) {
            return this.getLeague().isInLeague(((Snowboarder) competitor).getAge())
                    && this.getGender() == ((Snowboarder) competitor).getGender()
                    && this.getDiscipline() == ((Snowboarder) competitor).getDiscipline();
        }
        return false;
    }
    public boolean equals(Object other) {
       if (other instanceof SnowboardCompetition) {
            return this.getArena() == ((SnowboardCompetition) other).getArena() &&
                    this.getMaxCompetitors() == ((SnowboardCompetition) other).getMaxCompetitors() &&
                    this.getDiscipline() == ((SnowboardCompetition) other).getDiscipline() &&
                    this.getLeague() == ((SnowboardCompetition) other).getLeague() &&
                    this.getGender() == ((SnowboardCompetition) other).getGender();
        }
        return false;
    }
    public String toString() {
        return "SnowboardCompetition: " + super.toString();
    }

}



