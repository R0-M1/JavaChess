package modele.plateau;

import modele.jeu.Piece;

import java.awt.*;
import java.util.Objects;

public class Case {
    private Point position;
    private Piece piece;

    public Case(int x, int y) {
        position = new Point(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    // TODO: temporaire
    @Override
    public String toString() {
        return position.x + " " + position.y;
    }
}
