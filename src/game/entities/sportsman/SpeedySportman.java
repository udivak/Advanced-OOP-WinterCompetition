package game.entities.sportsman;

import game.entities.MobileEntity;

public class SpeedySportman extends WSDecorator {
    private double acceleration;
    public SpeedySportman (IndependantWinterSportman iws, double acceleration) {
        super(iws);
        ((MobileEntity)iws.getCompetitor()).setAcceleration(acceleration);
        this.acceleration = acceleration;
    }
    public void setAcceleration (double acceleration) {
        ((MobileEntity)this.getIWS().getCompetitor()).setAcceleration(acceleration);
    }
    public double getAcceleration() {
        return ((MobileEntity)this.getIWS().getCompetitor()).getAcceleration();
    }

}
