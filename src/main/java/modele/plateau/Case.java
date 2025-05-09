package modele.plateau;

import modele.pieces.Piece;

import java.awt.*;

public class Case {
    private final Point position;
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
}
