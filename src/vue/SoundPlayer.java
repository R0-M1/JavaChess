package vue;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    public static void play(String filename) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(filename));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Le format du fichier audio n'est pas supporté : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier audio : " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("La ligne audio n'est pas disponible : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Une erreur inattendue est survenue : " + e.getMessage());
        }

    }
}