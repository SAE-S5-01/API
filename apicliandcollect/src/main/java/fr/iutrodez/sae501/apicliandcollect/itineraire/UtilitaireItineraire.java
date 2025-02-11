/*
 * UtilitaireItineraire.java                                                                                04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import org.springframework.data.geo.Point;

import java.util.*;

/**
 * Calcul d'itineraire
 * @author Descriaud Lucas
 */
public class UtilitaireItineraire {

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

    public static double distanceEntrePoint(Point point1, Point point2 ){
        return Math.sqrt(Math.pow(point2.getX() - point1.getX(),2) + Math.pow(point2.getY()-point1.getY(),2));
    }
}
