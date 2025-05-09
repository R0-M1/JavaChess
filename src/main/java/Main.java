import modele.jeu.Jeu;
import vue.VueControleur;

public class Main {
    public static void main(String[] args) {
        Jeu jeu = new Jeu(true);
        jeu.start();
        VueControleur vueControleur = new VueControleur(jeu);
        vueControleur.setVisible(true);
    }
}