/*
 * Parcours.java                                         13 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.parcours;

import fr.iutrodez.sae501.apicliandcollect.contact.Contact;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Parcours caractérisé par un statut, une date de création, un itinéraire et
 * le dernier client / prospect visité.
 *
 * @author Loïc FAUGIERES
 */
@Setter
@Getter
@Entity
@Table(name = "parcours")
public class Parcours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur")
    private Utilisateur utilisateur;

    @Column(name = "statut")
    @Enumerated(EnumType.ORDINAL)
    private StatutParcours statut;

    @Column(name = "date_creation")
    private Date dateCreation;

    @Column(name = "itineraire")
    private String idItineraire;

    @ManyToOne
    @JoinColumn(name = "contact", nullable = true)
    private Contact dernierContactVisite;
}
