/*
 * InteractionBdParcours.java                            13 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.parcours;

import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Interface pour les interactions avec la base de données
 * relationnelle pour les parcours.
 */
public interface InteractionBdParcours extends JpaRepository<Parcours, Long> {
    List<Parcours> findByUtilisateur(Utilisateur u);

    List<Parcours> findByUtilisateurAndStatut(Utilisateur u, StatutParcours statut);

    List<Parcours> findByUtilisateurAndId(Utilisateur u, Long id);

    @Modifying
    @Query("UPDATE Parcours p SET p.statut = :nouveauStatut WHERE p.utilisateur = :utilisateur AND p.statut = :ancienStatut")
    void updateStatutByUtilisateurAndStatut(Utilisateur utilisateur, StatutParcours ancienStatut, StatutParcours nouveauStatut);

    void deleteByUtilisateur(Utilisateur u);
}