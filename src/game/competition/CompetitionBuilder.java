package game.competition;
import game.arena.ArenaFactory;
import game.arena.WinterArena;
import game.entities.sportsman.Skier;
import game.entities.sportsman.Snowboarder;
import game.enums.*;
import java.util.ArrayList;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class CompetitionBuilder - Builder DP for Competition
 */
public class CompetitionBuilder implements ICompetitionBuilder{
    private Competition competition;
    private int maxCompetitors;
    private WinterArena arena;
    private ArrayList<Competitor> activeCompetitors;
    private Competitor competitor;
    private ArenaFactory arenaFactory;
    public CompetitionBuilder(int maxCompetitors) {
        this.maxCompetitors = maxCompetitors;
        this.arenaFactory = new ArenaFactory();
//        this.arena = (WinterArena) arenaFactory.createArena("Winter", 700, SnowSurface.CRUD, WeatherCondition.SUNNY);
        this.activeCompetitors = new ArrayList<>();
//        this.competitor = new Skier("default-name", 12, Gender.MALE, 12, 12, Discipline.DOWNHILL, 1);
//        activeCompetitors.add(this.competitor);
//        for (int i=0; i<maxCompetitors - 1; i++)
//            activeCompetitors.add(((Skier) competitor).clone());
//
//        this.competition.setActiveCompetitors(this.activeCompetitors);
    }
    public void buildArena(String arenaType, double length, SnowSurface surface, WeatherCondition condition) {
        this.arena = (WinterArena) arenaFactory.createArena(arenaType, length, surface, condition);
//        this.competition.setArena(this.arena);
    }
    public void buildDiscipline(Discipline discipline) {
        ((WinterCompetition) competition).setDiscipline(discipline);
    }
    public void buildLeague(League league) {
        ((WinterCompetition) competition).setLeague(league);
    }
    public void buildGender(Gender gender) {
        ((WinterCompetition) competition).setGender(gender);
    }
    public void changeCompetitionType(String type) {
        if (type.equals("Snowboard")) {
            this.competition = new SnowboardCompetition(arena, maxCompetitors, Discipline.DOWNHILL, League.JUNIOR, Gender.MALE);
        }
        else if (type.equals("Ski")) {
            this.competition = new SkiCompetition(arena, maxCompetitors, null, null, null);
        }
    }
    public Competition getCompetition() { return competition; }
    public void buildDefaultCompetitors() {
        this.activeCompetitors.clear();
        if (competition.getClass().getSimpleName().equals("SkiCompetition")) {
            int id = 1;
            this.competitor = new Skier("default-name", 12, Gender.MALE, 12, 12, Discipline.DOWNHILL, id);
            this.activeCompetitors.add(this.competitor);
            for (int i=0; i < maxCompetitors - 1; i++)
                activeCompetitors.add(((Skier) competitor).clone());
        }
//        else if (competition.getClass().getSimpleName().equals("SnowboardCompetition")) {
//            this.competitor = new Snowboarder("default-name", 12, Gender.MALE, 12, 12, Discipline.DOWNHILL, 1);
//            for (int i = 0; i < maxCompetitors; i++)
//                activeCompetitors.add(((Snowboarder) competitor).clone());
//        }
        this.competition.setActiveCompetitors(this.activeCompetitors);
    }
}
