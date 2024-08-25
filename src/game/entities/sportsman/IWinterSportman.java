package game.entities.sportsman;

import game.competition.Competitor;

public interface IWinterSportman {
    void run();
    IndependantWinterSportman getIWS();
    Competitor getCompetitor();
}
