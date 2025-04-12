package modele.jeu;

import modele.plateau.Case;

import java.awt.*;

public class Coup {
    public Point dep;
    public Point arr;

    public Coup(Case caseDep, Case caseArr) {
        Point pos = caseDep.getPosition();
        dep = new Point(pos.x, pos.y);
        pos = caseArr.getPosition();
        arr = new Point(pos.x, pos.y);
    }

    // TODO faire une méthode estCoupValide() pour vérifier si le coup est valide. Elle sera appelé dans envoyerCoup() de Jeu. [peut etre à mettre dans Piece]

}
