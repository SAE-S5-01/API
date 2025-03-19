package fr.iutrodez.sae501.apicliandcollect.itineraire;

import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class AlgoLittleUtil {

    private static LittleResult  meilleurChemin = new LittleResult(null, 0.0);

    private static ArrayList<Long> clientLigne;

    private static ArrayList<Long> clientColonne;

    public static List<Pair<Long, Long>> lancerAlgorithme(Double[][] matrice, Set<Long> clientId){
        meilleurChemin.coutMin = reductionMatriceDistance(matrice);
        meilleurChemin.matrice  = matrice;
        clientColonne = new ArrayList<>(clientId.stream().toList());
        clientLigne = new ArrayList<>(clientId.stream().toList());
        return lancerAlgorithme(meilleurChemin);
    }

    private static List<Pair<Long, Long>> lancerAlgorithme(LittleResult chemin) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Double[][] matriceRegret = matriceRegret(chemin.matrice);
        double regretMax = getRegretMax(matriceRegret);

        // Trouver la case avec le regret max
        int ligne = 0, colonne = 0;
        for (int i = 0; i < matriceRegret.length; i++) {
            for (int j = 0; j < matriceRegret[i].length; j++) {
                if (matriceRegret[i][j] != null && matriceRegret[i][j] == regretMax) {
                    ligne = i;
                    colonne = j;
                    break;
                }
            }
        }
        LittleResult prendreBranche = new LittleResult(meilleurChemin);
        LittleResult pasPrendreBranche = new LittleResult(meilleurChemin);

        Future<LittleResult> future1 = executor.submit(new LittleTask(prendreBranche, ligne, colonne, true, regretMax));
        Future<LittleResult> future2 = executor.submit(new LittleTask(pasPrendreBranche, ligne, colonne, false, regretMax));
        try {
            LittleResult result1 = future1.get();
            LittleResult result2 = future2.get();

            // Comparer les coûts et choisir le meilleur chemin
            if(result1.coutMin < result2.coutMin ){
                meilleurChemin = result1;
                meilleurChemin.addCouple(Pair.of(clientLigne.remove(ligne),clientColonne.remove(colonne)));
            } else {
                meilleurChemin = result2;
            }

            // Continuer l'algorithme avec la meilleure solution
            if (meilleurChemin.matrice.length > 2 && regretMax != Double.POSITIVE_INFINITY) {
                lancerAlgorithme(meilleurChemin);
            } else {
                terminerItineraire(meilleurChemin);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        return meilleurChemin.getItineraire();
    }

    static void terminerItineraire(LittleResult chemin){
        if( !clientLigne.get(0).equals(clientColonne.get(0)) && !clientLigne.get(1).equals(clientColonne.get(1))){
            chemin.addCouple(Pair.of(clientLigne.removeFirst(),clientColonne.removeFirst()));
            chemin.addCouple(Pair.of(clientLigne.removeFirst(),clientColonne.removeFirst()));
            chemin.coutMin += chemin.matrice[0][0] + chemin.matrice[1][1];
        } else {
            chemin.addCouple(Pair.of(clientLigne.removeFirst(),clientColonne.remove(1)));
            chemin.addCouple(Pair.of(clientLigne.removeFirst(),clientColonne.removeFirst()));
            chemin.coutMin += chemin.matrice[0][1] + chemin.matrice[1][0];
        }
    }

    private static double getRegretMax(Double[][] matriceRegret){
        double regretMax = 0.0;
        for (Double[] doubles : matriceRegret) {
            for (int j = 0; j < matriceRegret.length; j++) {
                regretMax = doubles[j] != null ? Math.max(doubles[j], regretMax) : regretMax;
            }
        }
        return regretMax;
    }

    private static Double[][] matriceRegret(Double[][] distance){
        int taille =  distance.length;
        int idLigne =0 ,idColonne = 0;
        Double[][] matriceDesRegret = new Double[taille][taille];
        for(int i = 0; i<taille;i++){
            for(int j = 0; j<taille; j++){
                double minCollone = Double.MAX_VALUE;
                double minLigne = Double.MAX_VALUE;
                if(distance[i][j] != null && distance[i][j].equals(0.0) && i!=j){
                    idLigne = i;
                    idColonne = j;
                    for(int y = 0; y < taille; y++){
                        if(y != i && y != idColonne){
                            minLigne = distance[i][y] != null ?
                                    Math.min(distance[i][y],minLigne):
                                    minLigne;
                        }
                    }
                    for(int w = 0; w < taille; w++){
                        if(w != idColonne && w != idLigne){
                            minCollone = distance[w][idColonne] != null ?
                                    Math.min(distance[w][idColonne],minCollone):
                                    minCollone;
                        }
                    }
                    matriceDesRegret[idLigne][idColonne] = minLigne + minCollone ;
                }
            }
        }
        return matriceDesRegret;
    }

    public static Double reductionMatriceDistance(Double[][] matriceDistance){
        double borneLittle = 0.0;

        // reduction des lignes
        for(int i = 0; i< matriceDistance.length; i++){
            double minLigne = Double.MAX_VALUE;
            for(int j = 0; j < matriceDistance[i].length; j++){
                if(j != i){
                    minLigne = matriceDistance[i][j]!= null ? Math.min(matriceDistance[i][j],minLigne) : minLigne;
                }
            }
            borneLittle += minLigne;
            if(minLigne != 0.0){
                for(int j = 0; j < matriceDistance[i].length; j++){
                    if(matriceDistance[i][j] != null && matriceDistance[i][j] != 0){
                        matriceDistance[i][j] -= minLigne;
                    }
                }
            }
        }

        //reduction Colonne
        for(int j = 0; j< matriceDistance.length; j++){
            double minCollone = Double.MAX_VALUE;
            for(int i = 0; i < matriceDistance[j].length; i++){
                if(j != i){
                    minCollone = matriceDistance[i][j]!= null ? Math.min(matriceDistance[i][j],minCollone): minCollone;
                }
            }
            borneLittle += minCollone;
            if(minCollone != 0.0){
                for(int i = 0; i < matriceDistance[j].length; i++){
                    if(matriceDistance[i][j] != null && matriceDistance[i][j] != 0){
                        matriceDistance[i][j] -= minCollone;
                    }
                }
            }
        }
        return borneLittle;
    }
}
