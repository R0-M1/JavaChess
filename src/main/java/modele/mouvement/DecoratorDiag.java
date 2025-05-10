package modele.mouvement;

import modele.plateau.Case;
import modele.pieces.Piece;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class DecoratorDiag extends DecoratorCasesAccessibles {
    public DecoratorDiag(Piece piece, Plateau plateau, DecoratorCasesAccessibles base) {
        super(piece, plateau, base);
    }

    @Override
    public ArrayList<Case> getMesCA() {
        ArrayList<Case> cases = new ArrayList<>();
        Case c = piece.getCase();

        int[][] directions = {
                {1, 1},  // down-right
                {-1, -1}, // up-left
                {-1, 1},  // down-left
                {1, -1}  // up-right
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
                        if (next.getPiece().getCouleur() != piece.getCouleur()) {
                            cases.add(next);
                        }
                        break;
                    }
                }
            } while (next != null && next.getPiece() == null);
        }

        return cases;
    }
}
