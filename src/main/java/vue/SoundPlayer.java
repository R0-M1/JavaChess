package vue;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
    public static void play(String filename) {
        try {
            URL soundURL = SoundPlayer.class.getClassLoader().getResource(filename);
            if (soundURL == null) {
                System.err.println("Fichier audio non trouvé : " + filename);
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundURL);
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
