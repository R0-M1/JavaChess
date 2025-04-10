package modele.jeu;

public class Joueur {
    private final Jeu jeu;

    public Joueur(Jeu jeu) {
        this.jeu = jeu;
    }

    public Coup getCoup() throws InterruptedException {
        synchronized (jeu) {
            jeu.wait();
        }
        return jeu.coup;
    }
}
