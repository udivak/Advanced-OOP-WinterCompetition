package game.entities.sportsman;

public class ActiveCompetitor implements CompetitorState {

    @Override
    public CompetitorState getState() {
        return this;
    }
}
