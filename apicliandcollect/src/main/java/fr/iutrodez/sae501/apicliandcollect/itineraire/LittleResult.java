package fr.iutrodez.sae501.apicliandcollect.itineraire;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class LittleResult {

    Double[][] matrice;

    double coutMin;

    private List<Pair<Long,Long>> itineraire;

    public LittleResult(Double[][] matrice, double coutMin) {
        this.matrice = matrice;
        this.coutMin = coutMin;
        itineraire = new ArrayList<>();
    }

    public LittleResult(LittleResult chemin) {
        this.matrice = chemin.matrice;
        this.coutMin = chemin.coutMin;
        setItineraire(chemin.getItineraire());
    }

    public void addCouple(Pair<Long,Long> couple){
        itineraire.add(couple);
    }

}
