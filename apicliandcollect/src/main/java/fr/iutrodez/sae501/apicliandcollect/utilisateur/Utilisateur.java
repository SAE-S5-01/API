package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import fr.iutrodez.sae501.apicliandcollect.contact.Contact;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


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

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> clients = new ArrayList<>();

    public void ajouterClient(Contact contact) {
        clients.add(contact);
        contact.setUtilisateur(this);
    }

    public void supprimerClient(Contact contact) {
        clients.remove(contact);
        contact.setUtilisateur(null);
    }
}