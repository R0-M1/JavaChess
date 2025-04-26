package modele.mouvement;

import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.pieces.Piece;

import java.util.ArrayList;

public abstract class DecoratorCasesAccessibles {
    DecoratorCasesAccessibles base;
    Piece piece;
    Plateau plateau;

    public DecoratorCasesAccessibles(Piece piece, Plateau plateau, DecoratorCasesAccessibles base) {
        this.piece = piece;
        this.plateau = plateau;
        this.base = base;
    }

    public abstract ArrayList<Case> getMesCA();

    public ArrayList<Case> getCA() {
        ArrayList<Case> casesPossibles = getMesCA();

        if (base != null) {
            casesPossibles.addAll(base.getCA());
        }

        return casesPossibles;
    }

    public ArrayList<Case> getCasesValides() {
        ArrayList<Case> casesValides = new ArrayList<>();

        Case caseDepart = piece.getCase();

        for (Case caseDestination : getCA()) {
            Piece pieceCapturee = caseDestination.getPiece();

            // Simule le déplacement
            caseDepart.setPiece(null);
            caseDestination.setPiece(piece);
            piece.setCase(caseDestination);

            boolean estEchec = plateau.getJeu().estEnEchec(piece.getCouleur());

            // Annule le coup
            caseDepart.setPiece(piece);
            caseDestination.setPiece(pieceCapturee);
            piece.setCase(caseDepart);

            // Garde uniquement les coups qui ne mettent pas en échec
            if (!estEchec) {
                casesValides.add(caseDestination);
            }
        }

        return casesValides;
    }
}
