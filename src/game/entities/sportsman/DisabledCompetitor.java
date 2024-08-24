package game.entities.sportsman;

public class DisabledCompetitor implements CompetitorState {

    @Override
    public CompetitorState getState() {
        return this;
    }
}
