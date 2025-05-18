package vue;

import java.io.File;
import java.util.Scanner;

/**
 * Cette classe permet de sélectionner les options de jeu via la console
 * Elle offre les mêmes fonctionnalités que MenuSelection mais en mode texte
 */
public class MenuSelectionConsole {
    private boolean modeIA = false;
    private boolean echec960 = false;
    private String niveauDifficulte = "Moyenne"; // Par défaut
    private String chemin = null; // Chemin vers le fichier à charger
    private Scanner scanner;

    public MenuSelectionConsole() {
        scanner = new Scanner(System.in);
        afficherMenu();
    }

    private void afficherMenu() {
        System.out.println("╔═════════════════════════════════════════╗");
        System.out.println("║         MENU DE SÉLECTION               ║");
        System.out.println("╠═════════════════════════════════════════╣");
        System.out.println("║ 1. Joueur vs Joueur                     ║");
        System.out.println("║ 2. Joueur vs IA                         ║");
        System.out.println("║ 3. Charger une partie (Joueur vs Joueur)║");
        System.out.println("║ 4. Charger une partie (Joueur vs IA)    ║");
        System.out.println("╚═════════════════════════════════════════╝");

        int choix = lireChoixUtilisateur(1, 4);
        
        switch (choix) {
            case 1:
                configurerPartieJoueurVsJoueur();
                break;
            case 2:
                configurerPartieJoueurVsIA();
                break;
            case 3:
                modeIA = false;
                chargerPartie();
                break;
            case 4:
                modeIA = true;
                configurerDifficulteIA();
                chargerPartie();
                break;
        }
    }

    private int lireChoixUtilisateur(int min, int max) {
        int choix = -1;
        while (choix < min || choix > max) {
            System.out.print("Entrez votre choix (" + min + "-" + max + "): ");
            try {
                choix = Integer.parseInt(scanner.nextLine());
                if (choix < min || choix > max) {
                    System.out.println("Choix invalide. Veuillez réessayer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
        return choix;
    }

    private void configurerPartieJoueurVsJoueur() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║      CONFIGURATION JOUEUR VS JOUEUR    ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Activer Échecs 960 ? (o/n)             ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        String reponse = scanner.nextLine().trim().toLowerCase();
        echec960 = reponse.equals("o") || reponse.equals("oui");
        modeIA = false;
    }

    private void configurerPartieJoueurVsIA() {
        configurerDifficulteIA();
        
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ Activer Échecs 960 ? (o/n)             ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        String reponse = scanner.nextLine().trim().toLowerCase();
        echec960 = reponse.equals("o") || reponse.equals("oui");
        modeIA = true;
    }

    private void configurerDifficulteIA() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║      NIVEAU DE DIFFICULTÉ DE L'IA      ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1. Facile                              ║");
        System.out.println("║ 2. Moyenne                             ║");
        System.out.println("║ 3. Difficile                           ║");
        System.out.println("║ 4. Impossible                          ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        int choix = lireChoixUtilisateur(1, 4);
        
        switch (choix) {
            case 1:
                niveauDifficulte = "Facile";
                break;
            case 2:
                niveauDifficulte = "Moyenne";
                break;
            case 3:
                niveauDifficulte = "Difficile";
                break;
            case 4:
                niveauDifficulte = "Impossible";
                break;
        }
    }

    private void chargerPartie() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         CHARGER UNE PARTIE             ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ Entrez le chemin complet du fichier    ║");
        System.out.println("║ (format .pgn ou .fen)                  ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        while (true) {
            chemin = scanner.nextLine().trim();
            
            if (chemin.isEmpty()) {
                System.out.println("Chemin vide.");
                chemin = null;
                break;
            }
            
            File fichier = new File(chemin);
            if (fichier.exists() && fichier.isFile()) {
                String extension = getFileExtension(fichier);
                if (extension.equals("pgn") || extension.equals("fen")) {
                    System.out.println("Fichier trouvé: " + fichier.getName());
                    break;
                } else {
                    System.out.println("Format de fichier non supporté. Utilisez .pgn ou .fen");
                }
            } else {
                System.out.println("Fichier introuvable. Veuillez réessayer ou laisser vide pour annuler.");
            }
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf + 1).toLowerCase();
    }

    public boolean isModeIA() {
        return modeIA;
    }

    public boolean isEchec960() {
        return echec960;
    }

    public String getNiveauDifficulte() {
        return niveauDifficulte;
    }

    public String getChemin() {
        return chemin;
    }
}