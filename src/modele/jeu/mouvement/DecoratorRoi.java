package modele.jeu.mouvement;

import modele.jeu.Piece;
import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class DecoratorRoi extends DecoratorCasesAccessibles {
    public DecoratorRoi(Piece piece, Plateau plateau, DecoratorCasesAccessibles base) {
        super(piece, plateau, base);
    }

    @Override
    public ArrayList<Case> getMesCA() {
        ArrayList<Case> cases = new ArrayList<>();
        Case c = piece.getCase();

        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] dir : directions) {
            Case dest = plateau.getCaseRelative(c, dir[0], dir[1]);
            if (dest != null && (dest.getPiece() == null || dest.getPiece().getCouleur() != piece.getCouleur())) {
                cases.add(dest);
            }
        }

        return cases;
    }
}
