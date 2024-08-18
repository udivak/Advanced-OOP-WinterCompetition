package utilities;
import game.arena.IArena;
import game.arena.WinterArena;
import game.entities.sportsman.IndependantWinterSportman;
import game.entities.sportsman.Skier;
import game.entities.sportsman.WinterSportsman;
import game.enums.Discipline;
import game.enums.Gender;
import game.enums.SnowSurface;
import game.enums.WeatherCondition;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.Executors;

/**
 * @author Itzhak Eretz Kdosha
 * Main class(run demo)
 */
public class Program {
    public static void main(String[] args) {
        CompetitionGUI gui = new CompetitionGUI();
        try {
            gui.setVisible(true);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

//		ExecutorService executer = Executors.newFixedThreadPool(5);
//		IArena arena = new WinterArena(700, SnowSurface.POWDER, WeatherCondition.SUNNY);
//		Queue<IndependantWinterSportman> queue = new LinkedList<>();
//		for (int i=0; i<10; i++) {
//			WinterSportsman competitor = new Skier("udi", 12, Gender.MALE, 5, 60, Discipline.DOWNHILL, i);
//
//			IndependantWinterSportman iws = new IndependantWinterSportman(competitor, arena, i, null);
//			queue.add(iws);
//
//		}
//		System.out.println(queue.size());
//		while (!queue.isEmpty()){
//			IndependantWinterSportman temp = queue.poll();
//			System.out.println("size : "+queue.size());
//			executer.execute(temp::run);
//			if (!temp.isFinished())
//				queue.add(temp);
//			else{
//				System.out.println("id "+temp.getID()+" finished.");
//			}
//		}
//		executer.shutdown();
    }
}

