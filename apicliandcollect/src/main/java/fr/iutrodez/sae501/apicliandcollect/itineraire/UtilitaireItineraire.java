/*
 * UtilitaireItineraire.java                                                                                04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import com.google.gson.JsonParser;
import org.springframework.data.geo.Point;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Calcul d'itineraire
 * @author Descriaud Lucas
 */
public class UtilitaireItineraire {

    //TODO a deplacer dans properties
    private static final String API_KEY = "5b3ce3597851110001cf6248e319034bfeca4b95ba9290a1113f74db";
    // TODO : idem au dessus ?
    private static final String MATRIX_URL = "https://api.openrouteservice.org/v2/matrix/driving-car";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static LinkedHashMap<Long, Point> CalculeItineraireGlouton(LinkedHashMap<Long, Point> listeClient) {

        double min;
        Long id = -1L;
        Point domicile = listeClient.get(-1L);
        Point localisation = listeClient.get(-1L);
        listeClient.remove(-1L);
        min = distanceEntrePoint(localisation, listeClient.firstEntry().getValue());
        LinkedHashMap<Long, Point> clientOrdonnees = new LinkedHashMap<>();
        Set<Long> ids = listeClient.keySet();

        while (clientOrdonnees.size() < listeClient.size()) {
            for (Long i : ids) {
                if (!clientOrdonnees.containsKey(i)) {
                    double distance1 = distanceEntrePoint(localisation, listeClient.get(i));
                    if (distance1 < min) {
                        id = i;
                        min = distance1;
                    }
                }
            }

            if (!clientOrdonnees.containsValue(listeClient.get(id))) {
                clientOrdonnees.put(id, listeClient.get(id));
                localisation = listeClient.get(id);
                min = 100L;
            }
        }
        clientOrdonnees.putFirst(-1L, domicile);

        return clientOrdonnees;
    }

    public static double distanceEntrePoint(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point2.getX() - point1.getX(), 2) + Math.pow(point2.getY() - point1.getY(), 2));
    }

    private static List<List<Long>> generatePermutations(List<Long> cities) {
        List<List<Long>> result = new ArrayList<>();
        permute(cities, 0, result);
        return result;
    }

    private static void permute(List<Long> cities, int start, List<List<Long>> result) {
        if (start == cities.size() - 1) {
            result.add(new ArrayList<>(cities));
            return;
        }
        for (int i = start; i < cities.size(); i++) {
            Collections.swap(cities, start, i);
            permute(cities, start + 1, result);
            Collections.swap(cities, start, i);
        }
    }



        public static Double[][] genererDistance(List<Point> points) {
            int n = points.size();
            Double[][] distances = new Double[n][n];

            try {
                // 1️⃣ Construire le JSON
                JsonObject jsonBody = new JsonObject();  // Crée un JsonObject vide
                JsonArray locations = new JsonArray();   // Crée un JsonArray pour les coordonnées
                JsonArray metricsValue = new JsonArray();   // Crée un JsonArray pour les coordonnées

                for (Point p : points) {
                    JsonArray coord = new JsonArray();
                    coord.add(p.getX());  // Ajoute la longitude (ou X)
                    coord.add(p.getY());  // Ajoute la latitude (ou Y)
                    locations.add(coord); // Ajoute les coordonnées au tableau "locations"
                }
                metricsValue.add("distance");
                jsonBody.add("locations", locations);
                jsonBody.add("metrics", metricsValue);

                // 2️⃣ Création des headers HTTP
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", API_KEY);

                HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody.toString(), headers);

                // 3️⃣ Exécuter la requête
                ResponseEntity<String> response = restTemplate.exchange(
                        MATRIX_URL, HttpMethod.POST, requestEntity, String.class
                );

                // 4️⃣ Vérifier la réponse
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Erreur HTTP : " + response.getStatusCodeValue());
                }

                JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
                JsonArray distancesArray = jsonResponse.getAsJsonArray("distances");

                for (int i = 0; i < n; i++) {
                    JsonArray row = distancesArray.get(i).getAsJsonArray();
                    for (int j = 0; j < n; j++) {
                        distances[i][j] = row.get(j).getAsDouble();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return distances;
        }

    public static void main(String[] args) {
        Point Domicile = new Point(2.566667, 44.333333);
        LinkedHashMap<Long, Point> listeClient = new LinkedHashMap<>();
        listeClient.put(1L, new Point(2.3522, 48.8566));  // Paris
        listeClient.put(2L, new Point(5.3698, 43.2965));  // Marseille
        listeClient.put(3L, new Point(4.8357, 45.7640));  // Lyon
        listeClient.put(4L, new Point(-1.5536, 47.2184)); // Nantes
        listeClient.put(5L, new Point(3.0586, 50.6333));  // Lille
        listeClient.put(6L, new Point(7.7521, 48.5734));  // Strasbourg
        listeClient.put(7L, new Point(-0.5792, 44.8378)); // Bordeaux
        listeClient.put(8L, new Point(1.4442, 43.6047));  // Toulouse

        List<List<Long>> permutations = generatePermutations(new ArrayList<>(listeClient.keySet()));
        Double[][] distances = genererDistance(new ArrayList<>(listeClient.values()));


        // Affichage des distances ligne par ligne
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[i].length; j++) {
                System.out.printf("%8.2f", distances[i][j]);
            }
            System.out.println();
        }
    }
}

