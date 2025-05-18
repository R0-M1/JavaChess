import modele.jeu.Jeu;
import vue.MenuSelection;
import vue.MenuSelectionConsole;
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
            MenuSelectionConsole msc = new MenuSelectionConsole();

            Jeu jeu;
            if (msc.getChemin() != null) {
                // Charger une partie depuis un fichier
                jeu = new Jeu(msc.getChemin(), msc.isModeIA(), mapDiffToDepth(msc.getNiveauDifficulte()));
            } else {
                // Nouvelle partie
                jeu = new Jeu(msc.isModeIA(), mapDiffToDepth(msc.getNiveauDifficulte()), msc.isEchec960());
            }

            jeu.start();
            VueControleurConsole controller = new VueControleurConsole(jeu, msc.isModeIA());
            controller.jouer();
        } else {
            MenuSelection ms = new MenuSelection();

            Jeu jeu;
            if (ms.getChemin() != null) {
                // Charger une partie depuis un fichier
                jeu = new Jeu(ms.getChemin(), ms.isModeIA(), mapDiffToDepth(ms.getNiveauDifficulte()));
            } else {
                // Nouvelle partie
                jeu = new Jeu(ms.isModeIA(), mapDiffToDepth(ms.getNiveauDifficulte()), ms.isEchec960());
            }

            jeu.start();
            VueControleur vueControleur = new VueControleur(jeu);
            vueControleur.setVisible(true);
        }
    }

    private static int mapDiffToDepth(String level) {
        if (level == null) return 6;
        switch (level) {
            case "Facile":
                return 3;
            case "Moyenne":
                return 6;
            case "Difficile":
                return 10;
            case "Impossible":
                return 15;
            default:
                return 6;
        }
    }
}
