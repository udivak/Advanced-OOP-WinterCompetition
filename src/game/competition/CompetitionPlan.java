package game.competition;
import game.arena.IArena;
import game.enums.Discipline;
import game.enums.Gender;
import game.enums.League;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Interface CompetitionPlan - interface with set methods of all competition's attrs -> for Competition Builder
 */
public interface CompetitionPlan {
    void setArena(IArena arena);
    void setMaxCompetitors(int maxCompetitors);
    void setDiscipline(Discipline discipline);
    void setLeague(League league);
    void setGender(Gender gender);
}
