package game.competition;
import game.arena.IArena;
import game.arena.WinterArena;
import game.entities.sportsman.IndependantWinterSportman;
import game.entities.sportsman.Skier;
import game.entities.sportsman.WinterSportsman;
import game.enums.Discipline;
import game.enums.Gender;
import game.enums.League;
import java.util.Observable;

/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class SkiCompetition - extends WinterCompetition to define a Ski competition
 */
public class SkiCompetition extends WinterCompetition {
    public SkiCompetition(WinterArena arena, int maxCompetitors, Discipline discipline, League league, Gender gender) {
        super(arena, maxCompetitors, discipline, league, gender);
    }
    @Override
    public boolean isValidCompetitor(Competitor competitor) {
        if (competitor instanceof Skier) {
            return this.getLeague().isInLeague(((Skier) competitor).getAge())
                    && this.getGender() == ((Skier) competitor).getGender()
                    && this.getDiscipline() == ((Skier) competitor).getDiscipline();
        }
        return false;
    }
    public boolean equals(Object other) {
        if (other instanceof SkiCompetition) {
            return this.getArena() == ((SkiCompetition) other).getArena() &&
                    this.getMaxCompetitors() == ((SkiCompetition) other).getMaxCompetitors() &&
                    this.getDiscipline() == ((SkiCompetition) other).getDiscipline() &&
                    this.getLeague() == ((SkiCompetition) other).getLeague() &&
                    this.getGender() == ((SkiCompetition) other).getGender();
        }
        return false;
    }
    public String toString() {
        return "SkiCompetition: " + super.toString();
    }
    @Override
    public synchronized void update(Observable competitor, Object arg) {
        try {
            WinterSportsman comp = (WinterSportsman) ((IndependantWinterSportman) competitor).getCompetitor();
            if (this.getArena().isFinished(comp)) {
                this.getFinishedCompetitors().add(comp);
                this.getActiveCompetitors().remove(comp);
            }
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
        }
    }
}

