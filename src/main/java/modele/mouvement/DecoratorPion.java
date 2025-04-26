package modele.mouvement;

import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.jeu.Couleur;
import modele.pieces.Piece;

import java.util.ArrayList;

public class DecoratorPion extends DecoratorCasesAccessibles {
    public DecoratorPion(Piece piece, Plateau plateau, DecoratorCasesAccessibles base) {
        super(piece, plateau, base);
    }

    @Override
    public ArrayList<Case> getMesCA() {
        ArrayList<Case> cases = new ArrayList<>();
        Case c = piece.getCase();

        int dir = (piece.getCouleur() == Couleur.BLANC) ? -1 : 1;

        // Avancer d'une case
        Case avant = plateau.getCaseRelative(c, 0, dir);
        if (avant!=null && avant.getPiece() == null) {
            cases.add(avant);

            // Si en position initiale, avancer de 2 cases
            Case deuxCases = plateau.getCaseRelative(avant, 0, dir);
            if (!piece.aDejaBouge && deuxCases.getPiece() == null) {
                cases.add(deuxCases);
            }
        }

        // Captures diagonales
        for (int dx = -1; dx <= 1; dx += 2) {
            Case diagonale = plateau.getCaseRelative(c, dx, dir);
            if (diagonale!=null && diagonale.getPiece() != null && diagonale.getPiece().getCouleur() != piece.getCouleur()) {
                cases.add(diagonale);
            }
        }

        return cases;
    }
}
