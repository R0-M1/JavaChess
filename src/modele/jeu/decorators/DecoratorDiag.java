package modele.jeu.decorators;

import modele.plateau.Case;
import modele.jeu.Piece;
import modele.plateau.Plateau;

import java.util.ArrayList;

public class DecoratorDiag extends DecoratorCasesAccessibles {
    public DecoratorDiag(Piece piece, Plateau plateau, DecoratorCasesAccessibles base) {
        super(piece, plateau, base);
    }

    @Override
    public ArrayList<Case> getMesCA() {
        ArrayList<Case> casesAccessibles = new ArrayList<>();
        Case positionActuelle = piece.getCase();

        // Définir les directions diagonales à parcourir (haut-gauche, haut-droite, bas-gauche, bas-droite)
        int[] directions = {-1, 1};  // haut/bas et gauche/droite

        // Vérifier les diagonales
        for (int dx : directions) {
            for (int dy : directions) {
                Case caseTemp = plateau.getCaseRelative(positionActuelle, dx, dy);
                while (caseTemp != null && caseTemp.getPiece() == null) {
                    casesAccessibles.add(caseTemp);
                    caseTemp = plateau.getCaseRelative(caseTemp, dx, dy);
                }
                // Si la case est occupée par une pièce adverse, on ajoute aussi cette case
                if (caseTemp != null && caseTemp.getPiece() != null && caseTemp.getPiece().getCouleur() != piece.getCouleur()) {
                    casesAccessibles.add(caseTemp);
                }
            }
        }

        return casesAccessibles;
    }
}
