import modele.jeu.Jeu;
import vue.MenuSelection;
import vue.VueControleur;
import vue.VueControleurConsole;

public class Main {
    public static void main(String[] args) {
        boolean modeConsole = false;

        // Lire les arguments
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--mode=console")) {
                modeConsole = true;
            }
        }

        if(modeConsole) {
            Jeu jeu = new Jeu(false);
            jeu.start();
            VueControleurConsole controleur = new VueControleurConsole(jeu);
            controleur.jouer();
        } else {
            MenuSelection ms = new MenuSelection();
            Jeu jeu = new Jeu(ms.isModeIA());
            jeu.start();
            VueControleur vueControleur = new VueControleur(jeu);
            vueControleur.setVisible(true);
        }
    }
}