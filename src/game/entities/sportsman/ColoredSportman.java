package game.entities.sportsman;

import game.entities.MobileEntity;

import java.awt.*;

public class ColoredSportman extends WSDecorator {
    private Color color;

    public ColoredSportman(IWinterSportman iws, Color color) {
        super(iws);
        getIWS().setColor(color);
        this.color = color;
    }

    public ColoredSportman (WSDecorator iws, Color color) {
        super(iws.getIWS());
        getIWS().setColor(color);
        this.color = color;
    }

    public void setColor(Color color) {
        getIWS().setColor(color);
    }

    public Color getColor() { return getIWS().getColor(); }
}
