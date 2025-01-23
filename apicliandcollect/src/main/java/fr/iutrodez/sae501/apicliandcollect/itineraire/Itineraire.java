package fr.iutrodez.sae501.apicliandcollect.itineraire;

import fr.iutrodez.sae501.apicliandcollect.contact.Contact;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "itineraire")
public class Itineraire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur") // Colonne FK dans la table Utilisateur
    private Utilisateur utilisateur;

    @Column(name = "nom" , length = 50)
    private String nom;

}