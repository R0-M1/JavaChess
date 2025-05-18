package modele.mouvement;

import modele.jeu.Couleur;
import modele.pieces.Piece;
import modele.pieces.Tour;
import modele.plateau.Case;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class DecoratorRoi extends DecoratorCasesAccessibles {
    private boolean enSimulation = false;

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

        // Désactivation du roque pour le mode échec960
        if (!piece.aDejaBouge && !plateau.isChess960()) {
            int y = (piece.getCouleur() == Couleur.BLANC) ? Plateau.SIZE_Y - 1 : 0;

            // Petit roque
            if (verifierRoque(4, y, 7, y, true)) {
                cases.add(plateau.getCases()[6][y]);
            }

            // Grand roque
            if (verifierRoque(4, y, 0, y, false)) {
                cases.add(plateau.getCases()[2][y]);
            }
        }

        return cases;
    }

    private boolean verifierRoque(int roiX, int roiY, int tourX, int tourY, boolean petitRoque) {
        Piece tour = plateau.getCases()[tourX][tourY].getPiece();
        if (!(tour instanceof Tour) || tour.aDejaBouge) return false;

        if(!enSimulation) {
            enSimulation = true;
            boolean echec = plateau.getJeu().estEnEchec(piece.getCouleur());
            enSimulation = false;
            if (echec) {
                return false;
            }
        }

        int direction = (petitRoque ? 1 : -1);
        int start = roiX + direction;
        int end = petitRoque ? 6 : 2;

        for (int x = start; petitRoque ? x <= end : x >= end; x += direction) {
            if (plateau.getCases()[x][roiY].getPiece() != null) return false;

            // Simule le Roi passant par cette case
            Case caseRoi = plateau.getCases()[roiX][roiY];
            Case caseInter = plateau.getCases()[x][roiY];

            caseRoi.setPiece(null);
            caseInter.setPiece(piece);
            piece.setCase(caseInter);

            boolean echec = plateau.getJeu().estEnEchec(piece.getCouleur());

            // Reset
            caseInter.setPiece(null);
            caseRoi.setPiece(piece);
            piece.setCase(caseRoi);

            if (echec) return false;
        }

        return true;
    }
}

