package modele.plateau;

import modele.jeu.*;
import modele.jeu.pieces.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Observable;

public class Plateau extends Observable {
    public static final int SIZE_X = 8;
    public static final int SIZE_Y = 8;
    private Case[][] tab;
    private HashMap<Piece, Point> map;

    public Plateau() {
        initPlateauVide();
        placerPieces();
    }

    private void initPlateauVide() {
        tab = new Case[SIZE_X][SIZE_Y];
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                tab[x][y] = new Case(x, y);
            }
        }
    }

    private void placerPieces() {
        tab[0][0].setPiece(new Tour(Couleur.NOIR));
        tab[1][0].setPiece(new Cavalier(Couleur.NOIR));
        tab[2][0].setPiece(new Fou(Couleur.NOIR));
        tab[3][0].setPiece(new Dame(Couleur.NOIR));
        tab[4][0].setPiece(new Roi(Couleur.NOIR));
        tab[5][0].setPiece(new Fou(Couleur.NOIR));
        tab[6][0].setPiece(new Cavalier(Couleur.NOIR));
        tab[7][0].setPiece(new Tour(Couleur.NOIR));
        for (int i = 0; i < 8; i++) {
            tab[i][1].setPiece(new Pion(Couleur.NOIR));
        }

        tab[0][7].setPiece(new Tour(Couleur.BLANC));
        tab[1][7].setPiece(new Cavalier(Couleur.BLANC));
        tab[2][7].setPiece(new Fou(Couleur.BLANC));
        tab[3][7].setPiece(new Dame(Couleur.BLANC));
        tab[4][7].setPiece(new Roi(Couleur.BLANC));
        tab[5][7].setPiece(new Fou(Couleur.BLANC));
        tab[6][7].setPiece(new Cavalier(Couleur.BLANC));
        tab[7][7].setPiece(new Tour(Couleur.BLANC));
        for (int i = 0; i < 8; i++) {
            tab[i][6].setPiece(new Pion(Couleur.BLANC));
        }
    }

    public void notifierChangement() {
        setChanged();
        notifyObservers();
    }

    public Case[][] getCases() {
        return tab;
    }
}
