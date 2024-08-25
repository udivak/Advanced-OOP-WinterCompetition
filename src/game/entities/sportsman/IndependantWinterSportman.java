package game.entities.sportsman;
import game.arena.IArena;
import game.competition.Competitor;
import utilities.Point;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Vector;
/**
 * @authors Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class IndependantWinterSportman - a class that contains competitor and arena for code-reusability and running in thread
 */
public class IndependantWinterSportman extends Observable implements Runnable, IWinterSportman {
    private final IArena arena;
    private WinterSportsman competitor;
    private final Vector<Observer> observers;
    private final int id;
    private Color color;
    private CompetitorState state;
    private int status;

    public IndependantWinterSportman(WinterSportsman competitor, IArena arena, int id) {
        this.competitor = competitor;
        this.arena = arena;
        this.observers = new Vector<>();
        this.id = id;
        this.color = null;
        this.status = 0;
        this.state = new ActiveCompetitor();

    }
    public IndependantWinterSportman(WinterSportsman competitor, IArena arena, int id, Color color) {
        this.competitor = competitor;
        this.arena = arena;
        this.observers = new Vector<>();
        this.id = id;
        this.color = color;
    }
    public void run() {
        double injuryLocation = 0;
        if (status == 1) {
            //state = new InjuredCompetitor();
            Random random = new Random();
            injuryLocation = random.nextDouble(1, arena.getLength());
        }
        else if (status ==2)
            state = new DisabledCompetitor();

        if (state instanceof DisabledCompetitor)
            return;

        this.competitor.initRace();

        while (!arena.isFinished(this.competitor)) {
            this.competitor.move(arena.getFriction());
            if (injuryLocation != 0) {
                if (competitor.getLocation().getX() >= injuryLocation) {
                    state = new InjuredCompetitor();
                    notifyObservers(state);
                    break;
                }
            }
            if (arena.isFinished(competitor))
                state = new FinishedCompetitor();

            notifyObservers(this.competitor.getLocation());
            try {
                Thread.sleep(100);
            } catch (InterruptedException err) {
                System.out.println(err.getMessage());
            }
        }
    }
    public void setState(CompetitorState newState) { state = newState; }
    public void setStatus(int status){ this.status = status; }
    public IndependantWinterSportman getIWS() {
        return this;
    }
//    @Override
//    public void run() {
////        this.competitor.initRace();
////        System.out.println("id: "+id + " moves");
//        if (!arena.isFinished(this.competitor)) {
//            this.competitor.move(arena.getFriction());
//            notifyObservers(this.competitor.getLocation());
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException err) {
//                System.out.println(err.getMessage());
//            }
//        }
//    }
    public synchronized void notifyObservers(CompetitorState state) {
        for (Observer observer : observers) {
            observer.update(this, state);
        }
        try {
            Thread.sleep(30);
        } catch (InterruptedException err) {
            System.out.println(err.getMessage());
        }
    }
    public synchronized void notifyObservers(Point location) {
        for (Observer observer : observers) {
            observer.update(this ,location);
        }
       try {
           Thread.sleep(30);
       } catch (InterruptedException err) {
           System.out.println(err.getMessage());
       }
    }
    public void setObserver (Observer observer) { observers.add(observer); }

    public Vector<Observer> getObservers() { return this.observers; }

    public int getID() { return id; }

    public void setColor(Color color) { this.color = color; }

    public Color getColor() { return color; }

    public String toString(){
        return "Name : " + competitor.getName() + ",  Age : " + competitor.getAge() + ",  Acceleration : " +
                competitor.getAcceleration() + ",  MaxSpeed : " + competitor.getMaxSpeed();
    }
    public Competitor getCompetitor() { return this.competitor; }

    public IArena getArena() { return arena; }

    public boolean isFinished() {
        return this.arena.isFinished(this.competitor);
    }

    public CompetitorState getState() {
        return state;
    }

}
