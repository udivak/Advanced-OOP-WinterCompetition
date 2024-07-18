package game;
import game.competition.Competition;
import game.competition.Competitor;
import game.entities.sportsman.WinterSportsman;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class GameEngine - Singleton class to create a single object that runs the game(competition)
 */
public class GameEngine {
    private static GameEngine instance = null;
    private GameEngine() {}
    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }
    public void startRace(Competition comp) {
        for (Competitor competitor: comp.getActiveCompetitors()) {
            competitor.initRace();
        }
        int steps = 1;
        while (comp.hasActiveCompetitor()) {
            comp.playTurn();
            steps++;
        }
        System.out.println("Race finished in "+steps+" steps");
        this.printResults(comp);
    }
    public void printResults(Competition comp) {
        System.out.println("Race results :");
        int i = 1;
        for (Competitor competitor: comp.getFinishedCompetitors()) {
            System.out.print((i++)+". "+((WinterSportsman) competitor).getName());
            System.out.println();
        }
    }
}
