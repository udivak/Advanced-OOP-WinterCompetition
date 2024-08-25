package game.entities.sportsman;

public class FinishedCompetitor implements CompetitorState {

    @Override
    public CompetitorState getState() {
        return this;
    }

    public String toString() {
        return "Finished";
    }
}
