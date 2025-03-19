package fr.iutrodez.sae501.apicliandcollect.itineraire;

import org.springframework.data.geo.Point;

import java.util.*;

import static fr.iutrodez.sae501.apicliandcollect.itineraire.UtilitaireItineraire.*;

public class TestPerformance {


    public static void main(String[] args){
        Point Domicile = new Point(2.566667, 44.333333);
        LinkedHashMap<Long, Point> listeClient = new LinkedHashMap<>();
        listeClient.put(145L, new Point(2.3522, 48.8566));
        listeClient.put(218L, new Point(5.3698, 43.2965));
        listeClient.put(354L, new Point(4.8357, 45.7640));
        listeClient.put(465L, new Point(-1.5536, 47.2184));
        listeClient.put(53L, new Point(3.0586, 50.6333));
        listeClient.put(6L, new Point(7.7521, 48.5734));
        listeClient.put(778L, new Point(-0.5792, 44.8378));
        listeClient.put(88888L, new Point(1.4442, 43.6047));
        List<List<Long>> permutations = genererPermutations(new ArrayList<>(listeClient.keySet()));
        listeClient.put(-1L,Domicile);

        LinkedHashMap<Long, Point> listeClientCopy = new LinkedHashMap<>(listeClient);

        Map<Long, Integer> indexMap = new HashMap<>();
        int index = 0;
        for (Long key : listeClient.keySet()) {
            indexMap.put(key, index++);
        }
        Double[][] distance = genererDistance(new ArrayList<>(listeClient.values()));

        Runtime memoire = Runtime.getRuntime();
        memoire.gc();

        long memoireAvant = memoire.totalMemory() - memoire.freeMemory();
        calculeItineraireLittle(listeClient);
        long memoireApres = memoire.totalMemory() - memoire.freeMemory();
        Long memoireLittle = memoireApres - memoireAvant;

        memoire.gc();
        memoireAvant = memoire.totalMemory() - memoire.freeMemory();
        forceBrut(indexMap,permutations,distance);
        memoireApres = memoire.totalMemory() - memoire.freeMemory();
        Long memoireBrutForce = memoireApres - memoireAvant;

        memoire.gc();
        memoireAvant = memoire.totalMemory() - memoire.freeMemory();
        CalculeItineraireGlouton(listeClient);
        memoireApres = memoire.totalMemory() - memoire.freeMemory();
        Long memoireGlouton = memoireApres - memoireAvant;

        System.out.println("=========Test de memoire avec 8 clients============\n");
        System.out.printf("memoire utilisé par algo force brut : %s \n",memoireBrutForce);
        System.out.printf("memoire utilisé par algo glouton : %s \n",memoireGlouton);
        System.out.printf("memoire utilisé par algo de Little : %s \n",memoireLittle);

        System.out.println("\n=========Test de temps avec 8 clients==============\n");
        long debut = System.currentTimeMillis();
        forceBrut(indexMap,permutations,distance);
        long fin = System.currentTimeMillis();
        long tempsForceBrut = fin - debut;

        debut = System.currentTimeMillis();
        calculeItineraireLittle(listeClientCopy);
        fin = System.currentTimeMillis();
        long tempsLittle = fin -debut;

        debut = System.currentTimeMillis();
        CalculeItineraireGlouton(listeClientCopy);
        fin = System.currentTimeMillis();
        long tempsGlouton = fin -debut;

        System.out.printf("temps d'execution de l'algo force brut : %s \n",tempsForceBrut);
        System.out.printf("temps d'execution de l'algo glouton : %s \n",tempsGlouton);
        System.out.printf("temps d'execution de l'algo de Little : %s \n",tempsLittle);

    }
}
