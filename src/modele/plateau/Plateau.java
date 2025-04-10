package modele.plateau;

import modele.jeu.*;

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
        tab[0][0].setPiece(new Tour(false));
        tab[1][0].setPiece(new Cavalier(false));
        tab[2][0].setPiece(new Fou(false));
        tab[3][0].setPiece(new Dame(false));
        tab[4][0].setPiece(new Roi(false));
        tab[5][0].setPiece(new Fou(false));
        tab[6][0].setPiece(new Cavalier(false));
        tab[7][0].setPiece(new Tour(false));
        for (int i = 0; i < 8; i++) {
            tab[i][1].setPiece(new Pion(false));
        }

        tab[0][7].setPiece(new Tour(true));
        tab[1][7].setPiece(new Cavalier(true));
        tab[2][7].setPiece(new Fou(true));
        tab[3][7].setPiece(new Dame(true));
        tab[4][7].setPiece(new Roi(true));
        tab[5][7].setPiece(new Fou(true));
        tab[6][7].setPiece(new Cavalier(true));
        tab[7][7].setPiece(new Tour(true));
        for (int i = 0; i < 8; i++) {
            tab[i][6].setPiece(new Pion(true));
        }


    }


    public Case[][] getCases() {
        return tab;
    }
}
