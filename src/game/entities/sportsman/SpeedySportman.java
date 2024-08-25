package game.entities.sportsman;

import game.entities.MobileEntity;

public class SpeedySportman extends WSDecorator {
    private double acceleration;

    public SpeedySportman (IWinterSportman iws, double acceleration) {
        super(iws);
        IndependantWinterSportman IWS1 = (IndependantWinterSportman) iws;
        ((MobileEntity)IWS1.getCompetitor()).setAcceleration(acceleration);
        this.acceleration = acceleration;
    }
    public SpeedySportman (WSDecorator iws, double acceleration) {
        super(iws.getIWS());
        IndependantWinterSportman IWS1 = iws.getIWS();
        ((MobileEntity) IWS1.getCompetitor()).setAcceleration(acceleration);
        this.acceleration = acceleration;
    }

        public void setAcceleration (double acceleration) {
        ((MobileEntity)this.getIWS().getCompetitor()).setAcceleration(acceleration);
    }
    public double getAcceleration() {
        return ((MobileEntity)this.getIWS().getCompetitor()).getAcceleration();
    }

}
