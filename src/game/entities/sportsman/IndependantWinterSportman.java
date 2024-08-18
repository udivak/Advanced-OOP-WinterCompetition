package game.entities.sportsman;
import game.arena.IArena;
import game.competition.Competitor;
import utilities.Point;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
/**
 * @authors Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class IndependantWinterSportman - a class that contains competitor and arena for code-reusability and running in thread
 */
public class IndependantWinterSportman extends Observable implements Runnable {
    private final IArena arena;
    private final WinterSportsman competitor;
    private final Vector<Observer> observers;
    private final int id;
    private Color color;
    private int idDisplay;
    public IndependantWinterSportman(WinterSportsman competitor, IArena arena, int id) {
        this.competitor = competitor;
        this.arena = arena;
        this.observers = new Vector<>();
        this.id = id;
        this.color = null;
    }
    public IndependantWinterSportman(WinterSportsman competitor, IArena arena, int id, Color color) {
        this.competitor = competitor;
        this.arena = arena;
        this.observers = new Vector<>();
        this.id = id;
        this.color = color;
    }
    public void run() {
        this.competitor.initRace();
        while (!arena.isFinished(this.competitor)) {
            this.competitor.move(arena.getFriction());
            notifyObservers(this.competitor.getLocation());
            try {
                Thread.sleep(100);
            } catch (InterruptedException err) {
                System.out.println(err.getMessage());
            }
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
    public String toString(){
        return "IWS - " + this.competitor.getName();
    }
    public Competitor getCompetitor() { return this.competitor; }
}
