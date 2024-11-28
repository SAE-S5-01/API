package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "utilisateur")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom" , length = 50)
    private String nom;

    @Column(name = "prenom" , length = 50)
    private String prenom;

    @Column(name = "mail" , length = 50)
    private String mail;

    @Column(name = "token" , length = 50)
    private String token;

    @Column(name = "motDePasse" , length = 20)
    private String motDePasse;


    @Column (name = "adresse" , length = 100)
    private String adresse;

}