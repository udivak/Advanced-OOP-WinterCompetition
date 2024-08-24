package game.entities.sportsman;
/**
 * @author Ehud Vaknin 209479088, Moshe Bercovich 206676850
 * Class WSDecorator - Decorator DP for WinterSportman using IWS
 */
public abstract class WSDecorator implements IWinterSportman, Runnable {
    protected IWinterSportman iws;

    public WSDecorator(IWinterSportman iws){
        this.iws = iws;
    }

    public IndependantWinterSportman getIWS() { return (IndependantWinterSportman) iws; }

    public void run() {
        IndependantWinterSportman iws = (IndependantWinterSportman) this.iws;
        iws.getCompetitor().initRace();
        while (!iws.getArena().isFinished(iws.getCompetitor())) {
            iws.getCompetitor().move(iws.getArena().getFriction());
            iws.notifyObservers(iws.getCompetitor().getLocation());
            try {
                Thread.sleep(100);
            } catch (InterruptedException err) {
                System.out.println(err.getMessage());
            }
        }
    }
}
