package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InterractionBdUtilisateur extends JpaRepository<Utilisateur, Long> {
    boolean existsByMail(String mail);
}