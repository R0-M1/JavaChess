import modele.jeu.Jeu;
import vue.MenuSelection;
import vue.VueControleur;

public class Main {
    public static void main(String[] args) {
        MenuSelection ms = new MenuSelection();
        Jeu jeu = new Jeu(ms.isModeIA());
        jeu.start();
        VueControleur vueControleur = new VueControleur(jeu);
        vueControleur.setVisible(true);
    }
}