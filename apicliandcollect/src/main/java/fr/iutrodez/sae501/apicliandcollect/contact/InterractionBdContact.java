package fr.iutrodez.sae501.apicliandcollect.contact;

import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterractionBdContact extends JpaRepository<Contact, Long> {
    List<Contact> findByUtilisateur(Utilisateur u);

    List<Contact> findByUtilisateurAndId(Utilisateur u, Long id);
}