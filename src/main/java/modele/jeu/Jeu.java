package modele.jeu;

import modele.pieces.*;
import modele.plateau.Plateau;
import modele.plateau.Case;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jeu extends Thread {
    private Plateau plateau;
    private Joueur joueurB;
    private Joueur joueurN;
    public Coup coup;

    private Couleur tourActuel;
    private GameEvent currentEvent;

    private List<String> mouvementsPGN = new ArrayList<>();
    private List<Coup> historiqueCoups = new ArrayList<>();
    public int numeroMouvement = 1;

    // Constructeur pour lancer une nouvelle une partie
    public Jeu(boolean modeIA, Integer depth, boolean chess960) {
        this.plateau = new Plateau(this, chess960);
        this.tourActuel = Couleur.BLANC; // Le tour commence avec les Blancs

        if (modeIA) {
            this.joueurB = new Joueur(this, Couleur.BLANC);
            this.joueurN = new JoueurIA(this, Couleur.NOIR, depth);
        } else {
            this.joueurB = new Joueur(this, Couleur.BLANC);
            this.joueurN = new Joueur(this, Couleur.NOIR);
        }
    }

    // Constructeur pour charger une partie depuis un fichier
    public Jeu(String chemin, boolean modeIA, Integer depth) {
        this(modeIA, depth, false);
        importerPartie(chemin);
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
            Joueur j = getJoueurCourant();
            Coup c = j.getCoup();
            while (!coupValide(c)) {
                currentEvent = GameEvent.INVALID_MOVE;
                plateau.notifierChangement(currentEvent);
                c = j.getCoup();
            }
            appliquerCoup(c);
            plateau.notifierChangement(currentEvent);
            changerTour();
        }
        plateau.notifierChangement(currentEvent);
    }

    private void appliquerCoup(Coup c) {
        Case dep = plateau.getCases()[c.dep.x][c.dep.y];
        Case arr = plateau.getCases()[c.arr.x][c.arr.y];
        Piece piece = dep.getPiece();

        piece.aDejaBouge = true;

        if (arr.getPiece() != null) {
            currentEvent = GameEvent.CAPTURE;
        } else {
            currentEvent = GameEvent.MOVE;
        }

        // Vérifie si c'est une prise en passant
        if (piece instanceof Pion && arr.getPiece() == null && c.dep.x != c.arr.x) {
            int dir = (piece.getCouleur() == Couleur.BLANC) ? -1 : 1;
            Case caseCapture = plateau.getCases()[c.arr.x][c.arr.y - dir]; // pion capturé en arrière
            caseCapture.setPiece(null);
            currentEvent = GameEvent.CAPTURE;
        }

        // Déplacement simple
        arr.setPiece(piece);
        piece.setCase(arr);
        dep.setPiece(null);

        // Roque
        if (piece instanceof Roi && Math.abs(c.arr.x - c.dep.x) == 2) {
            int y = c.dep.y;
            if (c.arr.x == 6) { // petit roque
                Case tourDep = plateau.getCases()[7][y];
                Case tourArr = plateau.getCases()[5][y];
                Piece tour = tourDep.getPiece();
                tourDep.setPiece(null);
                tourArr.setPiece(tour);
                tour.setCase(tourArr);
                currentEvent = GameEvent.CASTLE;
            } else if (c.arr.x == 2) { // grand roque
                Case tourDep = plateau.getCases()[0][y];
                Case tourArr = plateau.getCases()[3][y];
                Piece tour = tourDep.getPiece();
                tourDep.setPiece(null);
                tourArr.setPiece(tour);
                tour.setCase(tourArr);
                currentEvent = GameEvent.CASTLE;
            }
        }

        if (estEnEchec(getTourActuel()==Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC)) {
            currentEvent = GameEvent.CHECK;
        }

        // Détection promotion
        if (arr.getPiece() instanceof Pion) {
            Pion pion = (Pion) arr.getPiece();
            if ((pion.getCouleur() == Couleur.NOIR && arr.getPosition().y == Plateau.SIZE_Y-1) ||
                    (pion.getCouleur() == Couleur.BLANC && arr.getPosition().y == 0)) {
                if(joueurN instanceof JoueurIA && pion.getCouleur() == Couleur.NOIR) {
                    Dame nouvelleDame = new Dame(plateau, getTourActuel());
                    plateau.promouvoirPion(arr, nouvelleDame);
                } else {
                    currentEvent = GameEvent.PROMOTION;
                }
            }
        }

        // Ajouter le coup au format PGN
        historiqueCoups.add(c);

        String notationPGN = convertirCoupEnPGN(c, piece);
        if (piece.getCouleur() == Couleur.BLANC) {
            mouvementsPGN.add(numeroMouvement + ". " + notationPGN);
        } else {
            if (mouvementsPGN.isEmpty()) {
                // Après chargement d'un FEN la liste est vide donc on ajoute un nouvel élément
                mouvementsPGN.add(numeroMouvement + ". ... " + notationPGN);
            } else {
                mouvementsPGN.set(mouvementsPGN.size() - 1, mouvementsPGN.get(mouvementsPGN.size() - 1) + " " + notationPGN);
            }
            numeroMouvement++;
        }
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

        if (count >= 3) {
            currentEvent = GameEvent.DRAW;
            return true;
        }

        if (estEchecEtMat(tourActuel)) {
            currentEvent = GameEvent.CHECKMATE;
            return true;
        }
        if (estPat(tourActuel)) {
            currentEvent = GameEvent.STALEMATE;
            return true;
        }

        return false;
    }

    public boolean estEchecEtMat(Couleur couleur) {
        return estEnEchec(couleur) && !aUnCoupLegal(couleur);
    }

    private boolean estPat(Couleur couleur) {
        return !estEnEchec(couleur) && !aUnCoupLegal(couleur);
    }

    private boolean aUnCoupLegal(Couleur couleur) {
        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Piece piece = plateau.getCases()[x][y].getPiece();
                if (piece != null && piece.getCouleur() == couleur) {
                    if (!piece.getDCA().getCasesValides().isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void envoyerCoup(Coup c) {
        coup = c;
        synchronized (this) {
            notify();
        }
    }

    // Méthode pour alterner les tours entre les joueurs
    public void changerTour() {
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

    public Coup getDernierCoup() {
        if(historiqueCoups.isEmpty()) return null;
        return historiqueCoups.get(historiqueCoups.size()-1);
    }

    private String convertirCoupEnPGN(Coup c, Piece piece) {
        StringBuilder notation = new StringBuilder();
        if (currentEvent == GameEvent.CASTLE) {
            if (c.arr.x == 6) { // petit roque
                return "O-O";
            } else { // grand roque
                return "O-O-O";
            }
        }

        if (!(piece instanceof Pion)) {
            if (piece instanceof Roi) notation.append("K");
            else if (piece instanceof Dame) notation.append("Q");
            else if (piece instanceof Tour) notation.append("R");
            else if (piece instanceof Fou) notation.append("B");
            else if (piece instanceof Cavalier) notation.append("N");
        } else if (currentEvent == GameEvent.CAPTURE) {
            notation.append((char)('a' + c.dep.x));
        }

        // Indication de capture
        if (currentEvent == GameEvent.CAPTURE) {
            notation.append("x");
        }

        // Case d'arrivée
        notation.append((char)('a' + c.arr.x));
        notation.append(8 - c.arr.y); // Les rangs sont inversés en PGN (8 en haut, 1 en bas)

        // Indication d'échec ou d'échec et mat
        if (currentEvent == GameEvent.CHECK) {
            notation.append("+");
        }

        return notation.toString();
    }

    // Méthode pour mettre à jour la notation PGN après une promotion
    public void mettreAJourPromotionPGN(Piece piecePromotion) {
        if (piecePromotion == null || mouvementsPGN.isEmpty()) return;

        String pieceSymbole = "";
        if (piecePromotion instanceof Dame) pieceSymbole = "Q";
        else if (piecePromotion instanceof Tour) pieceSymbole = "R";
        else if (piecePromotion instanceof Fou) pieceSymbole = "B";
        else if (piecePromotion instanceof Cavalier) pieceSymbole = "N";

        if (!pieceSymbole.isEmpty()) {
            int dernierIndex = mouvementsPGN.size() - 1;
            String dernierCoup = mouvementsPGN.get(dernierIndex);
            mouvementsPGN.set(dernierIndex, dernierCoup + "=" + pieceSymbole);
        }
    }

    public void exporterPartie(String cheminFichier) {
        if(cheminFichier.endsWith(".pgn")) {
            exporterPGN(cheminFichier);
        } else if (cheminFichier.endsWith(".fen")) {
            exporterFEN(cheminFichier);
        }
    }

    private void exporterFEN(String cheminFichier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
            String fen = plateau.getFEN();
            writer.write(fen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void exporterPGN(String cheminFichier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
            // En-têtes PGN
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            String date = dateFormat.format(new Date());

            writer.write("[Event \"Partie locale\"]");
            writer.newLine();
            writer.write("[Site \"??\"]");
            writer.newLine();
            writer.write("[Date \"" + date + "\"]");
            writer.newLine();
            writer.write("[Round \"?\"]");
            writer.newLine();
            writer.write("[White \"Joueur Blanc\"]");
            writer.newLine();
            writer.write("[Black \"Joueur Noir\"]");
            writer.newLine();
            writer.write("[Result \"*\"]");
            writer.newLine();

            // Position initiale en FEN si c'est une partie Chess960
            if (plateau.isChess960()) {
                writer.write("[SetUp \"1\"]");
                writer.newLine();
                writer.write("[FEN \"" + plateau.getFEN() + "\"]");
                writer.newLine();
            }

            writer.newLine();

            // Écriture des coups
            StringBuilder mouvements = new StringBuilder();
            for (String coup : mouvementsPGN) {
                if (mouvements.length() + coup.length() + 1 > 80) {
                    // Nouvelle ligne si on dépasse 80 caractères
                    writer.write(mouvements.toString());
                    writer.newLine();
                    mouvements = new StringBuilder();
                }
                if (mouvements.length() > 0) {
                    mouvements.append(" ");
                }
                mouvements.append(coup);
            }

            if (mouvements.length() > 0) {
                writer.write(mouvements.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void importerPartie(String cheminFichier) {
        if(cheminFichier.endsWith(".pgn")) {
            importerPGN(cheminFichier);
        } else if (cheminFichier.endsWith(".fen")) {
            String fen;
            try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
                fen = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            plateau.importerFEN(fen);
        }
    }

    public void importerPGN(String cheminFichier) {
        StringBuilder contenu = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                contenu.append(ligne).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String pgnText = contenu.toString();

        String enTetes = "";
        String coups;

        // Séparer les en-têtes des coups
        Pattern pattern = Pattern.compile("(\\[.*?\\].*?)\\s*\\n\\s*\\n(.*)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(pgnText);

        if (matcher.find()) {
            enTetes = matcher.group(1);
            coups = matcher.group(2);
        } else {
            // Pas d'en-têtes, tout est considéré comme des coups
            coups = pgnText;
        }

        // Vérifier s'il y a une position FEN initiale
        Pattern fenPattern = Pattern.compile("\\[FEN \"(.*?)\"\\]");
        Matcher fenMatcher = fenPattern.matcher(enTetes);

        if (fenMatcher.find()) {
            String fen = fenMatcher.group(1);
            // Initialiser le plateau avec la position FEN
            plateau.importerFEN(fen);
            plateau.setChess960(true);
        }

        coups = coups.replaceAll("\\{.*?\\}", ""); // Supprimer les commentaires pour etre compatible avec chess.com
        coups = coups.replaceAll("\\$\\d+", ""); // Supprimer les annotations
        coups = coups.replaceAll("\\s+", " ").trim(); // Normaliser les espaces

        // Extraire les coups avec leur numéro
        Pattern coupPattern = Pattern.compile("(\\d+\\.\\s*)(\\S+)(\\s+(\\S+))?");
        Matcher coupMatcher = coupPattern.matcher(coups);

        boolean dernierCoupNoir = false;

        while (coupMatcher.find()) {
            String coupBlanc = coupMatcher.group(2);
            appliquerCoupPGN(coupBlanc, Couleur.BLANC);
            dernierCoupNoir = false;

            if (coupMatcher.group(4) != null) {
                String coupNoir = coupMatcher.group(4);
                appliquerCoupPGN(coupNoir, Couleur.NOIR);
                dernierCoupNoir = true;
            }
        }

        tourActuel = dernierCoupNoir ? Couleur.BLANC : Couleur.NOIR;
    }

    private void appliquerCoupPGN(String coupPGN, Couleur couleur) {
        if (coupPGN.equals("O-O") || coupPGN.equals("0-0")) {
            // Petit roque
            int y = (couleur == Couleur.BLANC) ? 7 : 0;
            Coup coup = new Coup(plateau.getCases()[4][y], plateau.getCases()[6][y]);
            appliquerCoup(coup);
            return;
        } else if (coupPGN.equals("O-O-O") || coupPGN.equals("0-0-0")) {
            // Grand roque
            int y = (couleur == Couleur.BLANC) ? 7 : 0;
            Coup coup = new Coup(plateau.getCases()[4][y], plateau.getCases()[2][y]);
            appliquerCoup(coup);
            return;
        }

        // Supprimer les symboles d'échec et d'échec et mat
        coupPGN = coupPGN.replaceAll("[+#]", "");

        String promotion = null;
        if (coupPGN.contains("=")) {
            promotion = coupPGN.substring(coupPGN.indexOf("=") + 1);
            coupPGN = coupPGN.substring(0, coupPGN.indexOf("="));
        }

        // Extraire la case d'arrivée
        String caseArrivee = coupPGN.substring(coupPGN.length() - 2);
        int xArr = caseArrivee.charAt(0) - 'a';
        int yArr = 8 - (caseArrivee.charAt(1) - '0');

        // Déterminer la pièce et la case de départ
        char typePiece = coupPGN.length() > 2 ? coupPGN.charAt(0) : 'P';
        if (typePiece >= 'a' && typePiece <= 'h') {
            typePiece = 'P';
        }

        // Trouver la pièce qui peut faire ce mouvement
        Piece pieceADeplacer = null;
        Case caseDep = null;

        for (int x = 0; x < Plateau.SIZE_X; x++) {
            for (int y = 0; y < Plateau.SIZE_Y; y++) {
                Piece piece = plateau.getCases()[x][y].getPiece();
                if (piece != null && piece.getCouleur() == couleur) {
                    // Vérifie si c'est le bon type de pièce
                    boolean bonType = false;
                    if (typePiece == 'P' && piece instanceof Pion) bonType = true;
                    else if (typePiece == 'K' && piece instanceof Roi) bonType = true;
                    else if (typePiece == 'Q' && piece instanceof Dame) bonType = true;
                    else if (typePiece == 'R' && piece instanceof Tour) bonType = true;
                    else if (typePiece == 'B' && piece instanceof Fou) bonType = true;
                    else if (typePiece == 'N' && piece instanceof Cavalier) bonType = true;

                    if (bonType) {
                        // Vérifier si la pièce peut atteindre la case d'arrivée
                        Case caseArr = plateau.getCases()[xArr][yArr];
                        if (piece.getDCA().getCasesValides().contains(caseArr)) {
                            pieceADeplacer = piece;
                            caseDep = plateau.getCases()[x][y];
                            break;
                        }
                    }
                }
            }
            if (pieceADeplacer != null) break;
        }

        if (pieceADeplacer != null && caseDep != null) {
            Coup coup = new Coup(caseDep, plateau.getCases()[xArr][yArr]);
            appliquerCoup(coup);

            // Gérer la promotion
            if (promotion != null && pieceADeplacer instanceof Pion) {
                Piece nouvellePiece = null;
                switch (promotion) {
                    case "Q":
                        nouvellePiece = new Dame(plateau, couleur);
                        break;
                    case "R":
                        nouvellePiece = new Tour(plateau, couleur);
                        break;
                    case "B":
                        nouvellePiece = new Fou(plateau, couleur);
                        break;
                    case "N":
                        nouvellePiece = new Cavalier(plateau, couleur);
                        break;
                }
                if (nouvellePiece != null) {
                    plateau.promouvoirPion(plateau.getCases()[xArr][yArr], nouvellePiece);
                }
            }
        }
    }
}
