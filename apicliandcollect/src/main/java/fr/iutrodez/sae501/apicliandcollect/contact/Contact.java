package fr.iutrodez.sae501.apicliandcollect.contact;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_token") // Colonne FK dans la table
    private Utilisateur utilisateur;

    @Column(name = "nom" , length = 50)
    private String nom;

    @Column(name = "prenom" , length = 50)
    private String prenom;

    @Column(name = "adresse" , length = 100)
    private String adresse;

    @Column(name = "description" , length = 500)
    private String description;

    @Column(name = "telephone" , length = 10)
    private String telephone;
}
