package game.competition;
import game.enums.*;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Interface ICompetitionBuilder - defines methods for CompetitionBuilder
 */
public interface ICompetitionBuilder {
    void changeCompetitionType(String type);
    void buildArena(String arenaType, double length, SnowSurface surface, WeatherCondition condition);
    void buildDiscipline(Discipline discipline);
    void buildLeague(League league);
    void buildGender(Gender gender);
    void buildDefaultCompetitors();
    Competition getCompetition();
}
