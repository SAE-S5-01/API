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

    public static LinkedHashMap<Long, Point> CalculeItineraireGlouton(LinkedHashMap<Long, Point> listeClient){

        double min;
        int nbClient = listeClient.size()-1;
        Long id;
        Point domicile = listeClient.get(-1L);
        Point localisation = listeClient.get(-1L);
        listeClient.remove(-1L);
        min = distanceEntrePoint(localisation,listeClient.firstEntry().getValue());
        LinkedHashMap<Long,Point> clientOrdonnees = new LinkedHashMap<>();


        while(clientOrdonnees.size() < nbClient){
            Set<Long> ids = listeClient.keySet();
            id = listeClient.firstEntry().getKey();
                for (Long i : ids) {
                    if (!clientOrdonnees.containsKey(i)) {
                        double distance1 = distanceEntrePoint(localisation, listeClient.get(i));
                        if (distance1 < min) {
                            id = i;
                            min = distance1;
                        }
                    }
                }

            if(!clientOrdonnees.containsValue(listeClient.get(id))){
                clientOrdonnees.put(id,listeClient.get(id));
                localisation = listeClient.get(id);
                listeClient.remove(id);
                if(listeClient.size()>= 1){
                    min = distanceEntrePoint(localisation, listeClient.sequencedValues().getFirst());
                }
            }
        }
        clientOrdonnees.putFirst(-1L, domicile);

        return clientOrdonnees;
    }


    public static double distanceEntrePoint(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point2.getX() - point1.getX(), 2) + Math.pow(point2.getY() - point1.getY(), 2));
    }

    /**
     * Génère toutes les permutations possibles d'une liste de points.
     * @param Points Liste des points
     * @return Liste des permutations
     */
    private static List<List<Long>> generatePermutations(List<Long> Points) {
        List<List<Long>> result = new ArrayList<>();
        permute(Points, 0, result);
        return result;
    }

    /**
     * Génère toutes les permutations possibles d'une liste de villes.
     * @param points Liste des villes
     * @param debut Indice de départ
     * @param resultat Liste des permutations
     */
    private static void permute(List<Long> points, int debut, List<List<Long>> resultat) {
        if (debut == points.size() - 1) {
            resultat.add(new ArrayList<>(points));
            return;
        }
        for (int i = debut; i < points.size(); i++) {
            Collections.swap(points, debut, i);
            permute(points, debut + 1, resultat);
            Collections.swap(points, debut, i);
        }
    }
    /**
     * Génère la matrice des distances entre les points.
     * @param points Liste des points
     * @return Matrice des distances
     */
    public static Double[][] genererDistance(List<Point> points) {
        int n = points.size();
        Double[][] distances = new Double[n][n];

        try {
            // Construire le JSON
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

            // Création des headers HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", API_KEY);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody.toString(), headers);

            // Exécuter la requête
            ResponseEntity<String> response = restTemplate.exchange(
                    MATRIX_URL, HttpMethod.POST, requestEntity, String.class
            );

            // Vérifier la réponse
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

    /**
     * Calcule le chemin optimal par force brute.
     * @param indexClient Map des id des points et leur index dans la liste des distances
     * @param permutations liste de toutes les permutations possibles
     * @param distances Matrice des distances entre les points
     * @return le chemin optimal
     */
    public static List<Long> forceBrut(Map<Long, Integer> indexClient , List<List<Long>> permutations , Double[][] distances) {
        double minimum = Double.MAX_VALUE;
        List<Long> cheminOpti = List.of();
        for(List<Long> p : permutations) {
            double longueurP = longueurListe(p , indexClient , distances);
            if (longueurP < minimum) {
                minimum = longueurP;
                cheminOpti = p;
            }
        }
        return  cheminOpti;
    }

    /**
     * Calcule la longueur d'un chemin.
     * @param chemin Liste des id des points dans l'ordre du chemin
     * @param indexClient Map des id des points et leur index dans la liste des distances
     * @param distance Matrice des distances entre les points
     * @return la longueur du chemin
     */
    public static double longueurListe(List<Long> chemin , Map<Long, Integer> indexClient, Double[][] distance) {
        /*
         * Le domicile sera toujours la dernière position de la liste des distances.
         * On initialise la distance totale à Domile -> point 1 + dernier point -> domicile
         */
        double distanceTotale = distance[distance.length-1][indexClient.get(chemin.get(0))]; ;
        distanceTotale += distance[distance.length-2][distance.length-1];
        // Parcourir chaque paire consécutive de points dans le chemin
        for (int i = 0; i < chemin.size() - 1; i++) {
            /*
                * get(i) et get(i+1) sont les id des points de la liste des distances.
                * On récupère les index de ces points dans indexClient
                * On additionne a la distance totale la distance entre ces deux points donné par distance[a][b].
                */
            int indexA = indexClient.get(chemin.get(i));
            int indexB = indexClient.get(chemin.get(i + 1));
            distanceTotale += distance[indexA][indexB];
        }
        return distanceTotale;
    }

    /**
     * Main pour test
     * @param args
     */
    public static void main(String[] args) {
        Point Domicile = new Point(2.566667, 44.333333); // Rodez
        LinkedHashMap<Long, Point> listeClient = new LinkedHashMap<>();
        listeClient.put(145L, new Point(2.3522, 48.8566));  // Paris
        listeClient.put(218L, new Point(5.3698, 43.2965));  // Marseille
        listeClient.put(354L, new Point(4.8357, 45.7640));// Lyon
        listeClient.put(465L, new Point(-1.5536, 47.2184)); // Nantes
        listeClient.put(53L, new Point(3.0586, 50.6333));  // Lille
        listeClient.put(6L, new Point(7.7521, 48.5734));  // Strasbourg
        listeClient.put(778L, new Point(-0.5792, 44.8378)); // Bordeaux
        listeClient.put(88888L, new Point(1.4442, 43.6047));  // Toulouse

        List<List<Long>> permutations = generatePermutations(new ArrayList<>(listeClient.keySet()));
        listeClient.put(-1L, Domicile);
        Double[][] distances = genererDistance(new ArrayList<>(listeClient.values()));
        Map<Long, Integer> indexMap = new HashMap<>();
        int index = 0;
        for (Long key : listeClient.keySet()) {  // listeClient est la LinkedHashMap
            indexMap.put(key, index++);
        }
        indexMap.put(-1L, listeClient.size()); // Domicile à la dernière position
        List<Long> distanceOptiBrut = forceBrut(indexMap , permutations , distances);

    }
}

