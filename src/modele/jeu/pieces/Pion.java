package modele.jeu.pieces;

import modele.jeu.Piece;
import modele.jeu.Coup;
import modele.jeu.Couleur;
import modele.plateau.Case;
import modele.plateau.Plateau;

public class Pion extends Piece {
    public Pion(Plateau plateau, Couleur couleur) {
        super(plateau, couleur);
    }

    @Override
    public boolean coupValide(Coup c) {
        int dx = c.arr.x - c.dep.x;  // Déplacement sur les colonnes (x)
        int dy = c.arr.y - c.dep.y;  // Déplacement sur les lignes (y)

        // Pion blanc se déplace vers le bas (vers les lignes plus grandes) et le pion noir vers le haut (vers les lignes plus petites)
        int direction = (this.getCouleur() == Couleur.BLANC) ? -1 : 1;

        // 1. Si le pion avance d'une case en ligne droite (et non en diagonale)
        if (dx == 0 && dy == direction) {
            System.out.println("test");
            // Le pion peut avancer d'une case vers l'avant si la case est vide
            Case caseArr = p.getCases()[c.arr.x][c.arr.y];
            if (caseArr.getPiece() == null) {
                return true;
            }
        }

        // 2. Si le pion capture en diagonale
        if (Math.abs(dx) == 1 && Math.abs(dy) == direction) {
            // Vérifier si une pièce adverse est présente sur la case
            Case caseArr = p.getCases()[c.arr.x][c.arr.y];
            Piece pieceArr = caseArr.getPiece();
            if (pieceArr != null && pieceArr.getCouleur() != this.getCouleur()) {
                return true;
            }
        }

        // 3. Vérification du premier coup (le pion peut avancer de deux cases)
        if (dx == 0 && dy == 2 * direction && (this.getCouleur() == Couleur.BLANC && c.dep.y == 1) || (this.getCouleur() == Couleur.NOIR && c.dep.y == 6)) {
            // Vérifier que les deux cases devant lui sont vides
            Case caseArr = p.getCases()[c.arr.x][c.arr.y];
            Case caseMiddle = p.getCases()[c.dep.x][c.dep.y + direction];
            if (caseArr.getPiece() == null && caseMiddle.getPiece() == null) {
                return true;
            }
        }

        return false;
    }
}
