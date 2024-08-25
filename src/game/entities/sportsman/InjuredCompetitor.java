package game.entities.sportsman;

public class InjuredCompetitor implements  CompetitorState {

    @Override
    public CompetitorState getState() {
        return this;
    }

    public String toString() {
        return "Injured";
    }
}
