package game.competition;
import game.arena.IArena;
import game.arena.WinterArena;
import game.entities.sportsman.WinterSportsman;
import game.enums.Discipline;
import game.enums.Gender;
import game.enums.League;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class WinterCompetition - extends Competition define a winter sport competition
 */
public abstract class WinterCompetition extends Competition implements CompetitionPlan{
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
    @Override
    public void setArena(IArena arena) { super.setArena(arena); }
    public void setMaxCompetitors(int maxCompetitors) { super.setMaxCompetitors(maxCompetitors); }
    public void setDiscipline(Discipline discipline) { this.discipline = discipline; }
    public void setLeague(League league) { this.league = league; }
    public void setGender(Gender gender) {this.gender = gender; }
}
