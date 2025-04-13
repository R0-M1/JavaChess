package modele.jeu.decorators;

import modele.jeu.Piece;
import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class DecoratorLigne extends DecoratorCasesAccessibles {
    public DecoratorLigne(Piece piece, Plateau plateau, DecoratorCasesAccessibles base) {
        super(piece, plateau, base);
    }

    @Override
    public ArrayList<Case> getMesCA() {
        ArrayList<Case> cases = new ArrayList<>();
        Case c = piece.getCase();

        int[][] directions = {
                {1, 0},  // right
                {-1, 0}, // left
                {0, 1},  // down
                {0, -1}  // up
        };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];
            Case next = c;

            do {
                next = plateau.getCaseRelative(next, dx, dy);
                if (next != null) {
                    if (next.getPiece() == null) {
                        cases.add(next);
                    } else {
                         if (next.getPiece().getCouleur()!=piece.getCouleur()) {
                             cases.add(next);
                         }
                        break; // stop if any piece is there
                    }
                }
            } while (next != null && next.getPiece() == null);
        }

        return cases;
    }
    // TODO A Finir
}
