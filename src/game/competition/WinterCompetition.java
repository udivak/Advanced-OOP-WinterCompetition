package game.competition;
import game.arena.WinterArena;
import game.enums.Discipline;
import game.enums.Gender;
import game.enums.League;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class WinterCompetition - extends Competition define a winter sport competition
 */
public abstract class WinterCompetition extends Competition {
    private Discipline discipline;
    private League league;
    private Gender gender;
    public WinterCompetition(WinterArena arena, int maxCompetitors, Discipline discipline, League league, Gender gender) {
        super(arena, maxCompetitors);
        this.discipline = discipline;
        this.league = league;
        this.gender = gender;
    }
    public Discipline getDiscipline() { return discipline; }
    public League getLeague() { return this.league; }
    public Gender getGender() { return gender; }
    public String toString() {
        return "WinterCompetition: (discipline="+this.discipline+", league="+this.league+", gender="+this.gender+")";
    }
}
