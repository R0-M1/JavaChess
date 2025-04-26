package modele.jeu;

import modele.pieces.Piece;
import modele.pieces.Roi;
import modele.plateau.Plateau;
import modele.plateau.Case;

public class Jeu extends Thread {
    private Plateau plateau;
    private Joueur joueurB;
    private Joueur joueurN;
    public Coup coup;

    private Couleur tourActuel;
    private GameEvent currentEvent;

    public Jeu() {
        this.plateau = new Plateau(this);
        this.joueurB = new Joueur(this, Couleur.BLANC);
        this.joueurN = new Joueur(this, Couleur.NOIR);
        this.tourActuel = Couleur.BLANC; // Le tour commence avec les Blancs
    }

    @Override
    public void run() {
        try {
            jouerPartie();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void jouerPartie() throws InterruptedException {
        while (!partieTermine()) {
            plateau.notifierChangement(currentEvent); // Notifier un GameEvent
            Joueur j = getJoueurCourant();
            Coup c = j.getCoup();
            while (!coupValide(c)) {
                System.out.println("coup non valide");
                currentEvent = GameEvent.INVALID_MOVE;
                plateau.notifierChangement(currentEvent);
                c = j.getCoup();
            }
            appliquerCoup(c);
            changerTour();

            //NOTE: quand je fais plateau.notifierChangement(currentEvent); ICI ça fait le problème, car vu qu'il y a 2 threads, la vue est en train d'afficher les pieces pendant que le modèle est en train de calculer les coups possibles avec partieTermine()
        }
    }

    private void appliquerCoup(Coup c) {
        // NOTE: Faire la vérification du type de coup ici puis appliquer le coup en fonction, et changer currentEvent

        Case dep = plateau.getCases()[c.dep.x][c.dep.y];
        Case arr = plateau.getCases()[c.arr.x][c.arr.y];

        dep.getPiece().aDejaBouge = true;

        if(arr.getPiece() != null) {
            currentEvent = GameEvent.CAPTURE;
        } else {
            currentEvent = GameEvent.MOVE;
        }

        arr.setPiece(dep.getPiece());
        dep.getPiece().setCase(arr);
        dep.setPiece(null);

        System.out.println(c.dep.x + " " + c.dep.y + " -> " + c.arr.x + " " + c.arr.y);

        //currentEvent = GameEvent.MOVE; // A changer de place, soit dans un decorator, soit dans Coup

        // TODO: gérer la capture, la promotion, le roque, etc.
    }

    private boolean coupValide(Coup c) {
        Case dep = plateau.getCases()[c.dep.x][c.dep.y];
        Case arr = plateau.getCases()[c.arr.x][c.arr.y];

        Piece piece = dep.getPiece();
        if (piece == null) return false;

        // Vérifie que la pièce appartient au joueur courant
        if (piece.getCouleur() != getJoueurCourant().getCouleur()) return false;

        // Vérifie que la case d’arrivée est dans les cases accessibles
        return piece.getDCA().getCasesValides().contains(arr);
    }

    public boolean estEnEchec(Couleur couleur) {
        Case caseRoi = null;

        // Trouver la case du roi de la couleur concernée
        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Piece p = plateau.getCases()[x][y].getPiece();
                if (p instanceof Roi && p.getCouleur() == couleur) {
                    caseRoi = plateau.getCases()[x][y];
                    break;
                }
            }
        }

        // Parcourir les pièces adverses
        Couleur couleurAdverse = (couleur == Couleur.BLANC) ? Couleur.NOIR : Couleur.BLANC;

        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Piece p = plateau.getCases()[x][y].getPiece();
                if (p != null && p.getCouleur() == couleurAdverse) {
                    if (p.getDCA().getCA().contains(caseRoi)) {
                        currentEvent = GameEvent.CHECK;
                        return true; // Le roi est attaqué
                    }
                }
            }
        }

        return false;
    }

    private boolean partieTermine() {
        int cle = plateau.hashPlateau();
        int count = plateau.historiquePositions.getOrDefault(cle, 0) + 1;
        plateau.historiquePositions.put(cle, count);
        
        if(count >= 3) {
            currentEvent = GameEvent.DRAW;
            plateau.notifierChangement(currentEvent); // TODO: Faire quelque chose de plus regroupé pour les notifierChangement()
            return true;
        }
        if(estEchecEtMat(tourActuel)) {
            System.out.println("Échec et mat !");
            currentEvent = GameEvent.CHECKMATE;
            plateau.notifierChangement(currentEvent);
            return true;
        }
        
        return false;
        // TODO: Vérification nulle par pat, (et peut etre nulle par manque de matériel)
    }

    public boolean estEchecEtMat(Couleur couleur) {
        if (!estEnEchec(couleur)) return false;

        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Piece piece = plateau.getCases()[x][y].getPiece();
                if (piece != null && piece.getCouleur() == couleur) {
                    if (!piece.getDCA().getCasesValides().isEmpty()) {
                        return false; // Il reste au moins un coup légal
                    }
                }
            }
        }

        return true; // Aucune pièce ne peut jouer => échec et mat
    }

    public void envoyerCoup(Coup c) {
        coup = c;
        synchronized (this) {
            notify();
        }
    }

    // Méthode pour alterner les tours entre les joueurs
    private void changerTour() {
        // Alterne entre les couleurs de joueur
        tourActuel = (tourActuel == Couleur.BLANC) ? Couleur.NOIR : Couleur.BLANC;
    }

    // Renvoie le joueur dont c'est le tour
    private Joueur getJoueurCourant() {
        if (tourActuel == Couleur.BLANC) {
            return joueurB;
        } else {
            return joueurN;
        }
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Couleur getTourActuel() {
        return tourActuel;
    }
}
