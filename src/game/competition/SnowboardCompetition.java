package game.competition;
import game.arena.WinterArena;
import game.entities.sportsman.IndependantWinterSportman;
import game.entities.sportsman.Snowboarder;
import game.entities.sportsman.WinterSportsman;
import game.enums.Discipline;
import game.enums.Gender;
import game.enums.League;
import java.util.Observable;

/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class SkiCompetition - extends WinterCompetition to define a Snowboard competition
 */
public class SnowboardCompetition extends WinterCompetition {
    public SnowboardCompetition(WinterArena arena, int maxCompetitors, Discipline discipline, League league, Gender gender){
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
   @Override
    public synchronized void update(Observable competitor, Object arg) {
        try {
            WinterSportsman comp = (WinterSportsman) ((IndependantWinterSportman) competitor).getCompetitor();
            if (this.getArena().isFinished(comp)) {
                this.getFinishedCompetitors().add(comp);
                this.getActiveCompetitors().remove(comp);
            }
        } catch (ClassCastException e) { //////////////////////////
            System.out.println(e.getMessage());
        }
    }
    public String toString() {
        return "SnowboardCompetition: " + super.toString();
    }
}



