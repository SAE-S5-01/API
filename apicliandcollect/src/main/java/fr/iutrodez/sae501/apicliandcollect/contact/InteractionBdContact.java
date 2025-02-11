/*
 * InteractionBdContact.java                                                                                04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.contact;

import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InteractionBdContact extends JpaRepository<Contact, Long> {
    List<Contact> findByUtilisateur(Utilisateur u);

    @Query("SELECT concat(c.nom, ' ',  c.prenom) FROM Contact c WHERE c.id = :id")
    String findNameById(@Param("id") Long id);
    List<Contact> findByUtilisateurAndId(Utilisateur u, Long id);

    @Query("SELECT c.entreprise FROM Contact c WHERE c.id = :id")
    String findEntrepriseById(@Param("id") Long id);

    void deleteByUtilisateur(Utilisateur u);
}