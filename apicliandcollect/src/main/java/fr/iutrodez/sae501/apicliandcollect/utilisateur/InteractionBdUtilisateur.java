package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InteractionBdUtilisateur extends JpaRepository<Utilisateur, Long> {
    boolean existsByMail(String mail);
    Optional<Utilisateur> findByMail(String mail);
}