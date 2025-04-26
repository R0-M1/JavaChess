package modele.mouvement;

import modele.pieces.Piece;
import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class DecoratorCavalier extends DecoratorCasesAccessibles {
    public DecoratorCavalier(Piece piece, Plateau plateau, DecoratorCasesAccessibles base) {
        super(piece, plateau, base);
    }

    @Override
    public ArrayList<Case> getMesCA() {
        ArrayList<Case> cases = new ArrayList<>();
        Case c = piece.getCase();

        int[][] offsets = {
                {1, 2}, {2, 1}, {-1, 2}, {-2, 1},
                {1, -2}, {2, -1}, {-1, -2}, {-2, -1}
        };

        for (int[] offset : offsets) {
            Case dest = plateau.getCaseRelative(c, offset[0], offset[1]);
            if (dest != null && (dest.getPiece() == null || dest.getPiece().getCouleur() != piece.getCouleur())) {
                cases.add(dest);
            }
        }

        return cases;
    }
}
