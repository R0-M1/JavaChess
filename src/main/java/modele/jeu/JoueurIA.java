package modele.jeu;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.*;

public class JoueurIA extends Joueur {
    public JoueurIA(Jeu jeu, Couleur couleur) {
        super(jeu, couleur);
    }

    @Override
    public Coup getCoup() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String fen = jeu.getPlateau().getFEN();

            // Créer la requête POST
            HttpPost request = new HttpPost("https://chess-api.com/v1");
            request.setHeader("Content-Type", "application/json");

            // Corps de la requête JSON
            String jsonInputString = "{\"fen\": \"" + fen + "\"}";
            StringEntity entity = new StringEntity(jsonInputString);
            request.setEntity(entity);

            // Envoyer la requête et obtenir la réponse
            HttpResponse response = client.execute(request);
            String responseString = EntityUtils.toString(response.getEntity());

            JSONObject responseJSON = new JSONObject(responseString);

            int fromNumeric = responseJSON.getInt("fromNumeric");
            int toNumeric = responseJSON.getInt("toNumeric");
            System.out.println("fromNumeric: " + fromNumeric);
            System.out.println("toNumeric: " + toNumeric);

            int fromY = 8 - fromNumeric % 10;
            int fromX = fromNumeric / 10 - 1;
            int toY = 8 - toNumeric % 10;
            int toX = toNumeric / 10 - 1;

            System.out.println(responseJSON.get("text"));
            System.out.println(fromX + " " + fromY + " -> " + toX + " " + toY);

            return new Coup(jeu.getPlateau().getCases()[fromX][fromY], jeu.getPlateau().getCases()[toX][toY]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
