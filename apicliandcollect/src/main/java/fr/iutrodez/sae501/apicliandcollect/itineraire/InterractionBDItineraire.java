package fr.iutrodez.sae501.apicliandcollect.itineraire;

import fr.iutrodez.sae501.apicliandcollect.contact.Contact;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;

import java.util.List;

public interface InterractionBDItineraire {
    List<Contact> findByUtilisateur(Utilisateur u);
}
