/*
 * InteractionBdParcours.java                            13 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.parcours;

import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface pour les interactions avec la base de données
 * relationnelle pour les parcours.
 */
public interface InteractionBdParcours extends JpaRepository<Parcours, Long> {
    List<Parcours> findByUtilisateurAndStatut(Utilisateur u, StatutParcours statut);

    List<Parcours> findByUtilisateurAndId(Utilisateur u, Long id);

    void deleteByUtilisateur(Utilisateur u);
}