package game.entities.sportsman;

public abstract class WSDecorator implements Runnable{
    private IndependantWinterSportman iws;
    public WSDecorator(IndependantWinterSportman iws){
        this.iws = iws;
    }

    public IndependantWinterSportman getIWS() { return iws; }

    public void run() {
        this.iws.getCompetitor().initRace();
        while (!iws.getArena().isFinished(this.iws.getCompetitor())) {
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
