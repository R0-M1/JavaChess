package modele.jeu;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class JoueurIA extends Joueur {
    private static final String API_URL = "https://stockfish.online/api/s/v2.php";

    private Integer depth;

    public JoueurIA(Jeu jeu, Couleur couleur, Integer depth) {
        super(jeu, couleur);
        this.depth = depth;
    }

    @Override
    public Coup getCoup() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String fen = jeu.getPlateau().getFEN();

            URI uri = new URIBuilder(API_URL)
                    .addParameter("fen", fen)
                    .addParameter("depth", Integer.toString(depth))
                    .build();

            // Créer la requête GET
            HttpGet request = new HttpGet(uri);

            // Envoyer la requête et obtenir la réponse
            HttpResponse response = client.execute(request);
            String responseString = EntityUtils.toString(response.getEntity());

            JSONObject responseJSON = new JSONObject(responseString);

            String bestMove = responseJSON.getString("bestmove").split(" ")[1];

            String from = bestMove.substring(0, 2);
            String to = bestMove.substring(2, 4);

            int fromX = from.charAt(0) - 'a';
            int fromY = 8 - (from.charAt(1) - '0');
            int toX = to.charAt(0) - 'a';
            int toY = 8 - (to.charAt(1) - '0');

            return new Coup(jeu.getPlateau().getCases()[fromX][fromY], jeu.getPlateau().getCases()[toX][toY]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
