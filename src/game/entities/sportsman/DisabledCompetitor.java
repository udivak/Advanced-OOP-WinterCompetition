package game.entities.sportsman;

import java.util.Random;

public class DisabledCompetitor implements CompetitorState {

    @Override
    public CompetitorState getState() {
        return this;
    }

    public String toString() {
        return "Disabled";
    }

}
