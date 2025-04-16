package modele.jeu.mouvement;

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
            Case next = c;

            while (true) {
                next = plateau.getCaseRelative(next, dir[0], dir[1]);
                if (next == null) break;

                Piece pieceSurCase = next.getPiece();

                if (pieceSurCase == null) {
                    cases.add(next);
                } else {
                    if (pieceSurCase.getCouleur() != piece.getCouleur()) {
                        cases.add(next);
                    }
                    break; // Une pièce bloque le passage
                }
            }
        }

        return cases;
    }
}
