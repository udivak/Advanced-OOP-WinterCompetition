package game.competition;
import game.arena.IArena;
import game.entities.sportsman.WinterSportsman;
import java.util.ArrayList;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class Competition - abstract class to define a sport competition
 */
public abstract class Competition {
    private IArena arena;
    private int maxCompetitors;
    private ArrayList<Competitor> activeCompetitors;
    private ArrayList<Competitor> finishedCompetitors;
    public Competition(IArena arena, int maxCompetitors){
        this.arena = arena;
        this.maxCompetitors = maxCompetitors;
        this.activeCompetitors = new ArrayList<>();
        this.finishedCompetitors = new ArrayList<>();
    }
    public IArena getArena() { return this.arena; }
    public int getMaxCompetitors() { return this.maxCompetitors; }
    public ArrayList<Competitor> getActiveCompetitors() { return this.activeCompetitors; }
    public ArrayList<Competitor> getFinishedCompetitors() { return this.finishedCompetitors; }
    public abstract boolean isValidCompetitor(Competitor competitor);
    public void addCompetitor(Competitor competitor) {
        if (this.activeCompetitors.size() < this.maxCompetitors) {
            if (isValidCompetitor(competitor)) {
                this.activeCompetitors.add(competitor);
                return;
            }
            else {
                throw new IllegalArgumentException("Invalid competitor " +
                        ((WinterSportsman) competitor).getClass().getSimpleName()
                        + " " + ((WinterSportsman) competitor).getName());
            }
        }
        throw new IllegalStateException(this.arena.getClass().getSimpleName() + " is full, max = " + getMaxCompetitors());
    }
    public void playTurn() {
        ArrayList<Competitor> temp_active = new ArrayList<>(activeCompetitors);
        for (Competitor comp: temp_active) {
            comp.move(arena.getFriction());
            if (arena.isFinished(comp)) {
                this.finishedCompetitors.add(comp);
                this.activeCompetitors.remove(comp);
            }
        }
    }
    public boolean hasActiveCompetitor(){
        return !this.activeCompetitors.isEmpty();
    }
    public String toString() {
        StringBuilder output = new StringBuilder("Competition: (arena=" + this.arena.toString() +
                ", maxCompetitor=" + this.maxCompetitors + "\nactiveCompetitors="
                + this.activeCompetitors.size() + ", finishedCompetitors=" + this.finishedCompetitors.size() + ")\n");
        output.append("activeCompetitors:\n");
        for (Competitor comp: this.activeCompetitors)
            output.append(comp.toString()).append("\n");
        for (Competitor comp: this.finishedCompetitors)
            output.append(comp.toString()).append("\n");
        return output.toString();
    }
}

