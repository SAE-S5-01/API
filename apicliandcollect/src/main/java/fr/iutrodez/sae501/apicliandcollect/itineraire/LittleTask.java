package fr.iutrodez.sae501.apicliandcollect.itineraire;

import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;

import java.util.concurrent.Callable;

import static fr.iutrodez.sae501.apicliandcollect.itineraire.AlgoLittleUtil.reductionMatriceDistance;

public class LittleTask implements Callable<LittleResult> {

    private final int ligne;

    private final int colonne;

    private final Double regret;

    private final boolean prendreBranche;

    private final LittleResult chemin;

    public LittleTask(LittleResult chemin, int ligne, int colonne, boolean prendreBranche, Double regret) {
        this.ligne = ligne;
        this.regret = regret;
        this.colonne = colonne;
        this.prendreBranche = prendreBranche;
        this.chemin = chemin;
    }

    @Override
    public LittleResult call() {
        if (prendreBranche) {
            calculPrendreBranche(chemin, ligne, colonne);
        } else {
            calculPasPrendreBranche(chemin, regret, ligne, colonne);
        }

        return chemin;
    }

    static void calculPrendreBranche(LittleResult chemin, int ligne, int colonne){
        int taille = chemin.matrice.length-1;
        Double[][] nvMatrice = new Double[taille][taille];
        for(int i = 0; i<chemin.matrice.length; i++){
            for(int j = 0; j<chemin.matrice.length;j++){
                if(i != ligne && j != colonne ){
                    nvMatrice[i > ligne ||i>taille-1? i-1:i][j> colonne || j>taille-1? j-1:j] = chemin.matrice[i][j];
                }
            }
        }
        chemin.coutMin += reductionMatriceDistance(nvMatrice);
        chemin.setMatrice(nvMatrice);
    }

    static void calculPasPrendreBranche(LittleResult chemin, Double regret, int ligne, int colonne){
        chemin.matrice[ligne][colonne] = null;
        chemin.coutMin += regret;
    }
}
