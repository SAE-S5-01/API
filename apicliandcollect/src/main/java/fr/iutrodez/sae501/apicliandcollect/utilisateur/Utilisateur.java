package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import fr.iutrodez.sae501.apicliandcollect.contact.Contact;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "utilisateur")
public class Utilisateur implements UserDetails {
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

    @Column(name = "motDePasse")
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return mail;
    }
}