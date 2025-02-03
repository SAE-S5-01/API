package fr.iutrodez.sae501.apicliandcollect.contact;


import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private InterractionBdContact interractionBdContact;

    @Autowired
    private InterractionMongoContact interractionMongoContact;

    /**
     * Crée un nouveau contact pour l'utilisateur u
     *
     * @param contactAajoute le contact à ajouter
     * @param u l'utilisateur connecté
     * @return le contact ajouté
     */
    @Transactional
    public ContactDTO creerContact(ContactDTO contactAajoute, Utilisateur u) {
        Contact contact = new Contact();
        ContactMongo contactMongo = new ContactMongo();
        contact.setEntreprise(contactAajoute.getNomEntreprise());
        contact.setNom(contactAajoute.getNomContact());
        contact.setPrenom(contactAajoute.getPrenomContact());
        contact.setAdresse(contactAajoute.getAdresse());
        contact.setDescription(contactAajoute.getDescription());
        contact.setTelephone(contactAajoute.getTelephone());
        contact.setProspect(contactAajoute.isProspect());
        contact.setUtilisateur(u);
        Contact resultat = interractionBdContact.save(contact);
        contactMongo.set_id(resultat.getId());
        contactMongo.setLocation(new GeoJsonPoint(contactAajoute.getLongitude(), contactAajoute.getLatitude()));
        ContactMongo localisation = interractionMongoContact.save(contactMongo);
        return contactEnJson(resultat , localisation);
    }

    public void modifierContact(ContactDTO contactModifier, Utilisateur u, Long id) {
        Contact contacAmodifier = interractionBdContact.findByUtilisateurAndId(u, id).getFirst();
        contacAmodifier.setEntreprise(contactModifier.getNomEntreprise());
        contacAmodifier.setDescription(contactModifier.getDescription());
        contacAmodifier.setAdresse(contactModifier.getAdresse());
        contacAmodifier.setTelephone(contactModifier.getTelephone());
        contacAmodifier.setNom(contactModifier.getNomContact());
        contacAmodifier.setPrenom(contactModifier.getPrenomContact());
        contacAmodifier.setProspect(contactModifier.isProspect());
        ContactMongo contactMongoAmodifier = interractionMongoContact.findBy_id(id);
        contactMongoAmodifier.setLocation(new GeoJsonPoint(contactModifier.getLongitude(), contactModifier.getLatitude()));
        interractionMongoContact.save(contactMongoAmodifier);
        interractionBdContact.save(contacAmodifier);
    }

    /**
     * Supprime le contact d'id id
     * @param id l'id du contact à supprimer
     */
    public void supprimerContact(Utilisateur u, Long id) {
        Contact contact = interractionBdContact.findByUtilisateurAndId(u, id).getFirst();
        interractionBdContact.delete(contact);
        interractionMongoContact.delete(interractionMongoContact.findBy_id(id));
        // TODO : vérifier si besoin suppression autre...
    }

    /**
     * Récupère la liste des contacts de l'utilisateur u
     *
     * @param u l'utilisateur connecté
     * @return la liste des contacts de l'utilisateur
     */
    public List<ContactDTO> listeContact(Utilisateur u) {
        List<Contact> contacts = interractionBdContact.findByUtilisateur(u);
        List<ContactMongo> localisations = interractionMongoContact.findAll();
        return contacts.stream().map(contact -> {
            ContactMongo localisation = localisations.stream().filter(loc -> loc.get_id() == contact.getId())
                .findFirst().get();
            return contactEnJson(contact, localisation);
        }).collect(Collectors.toList());
    }

    /**
     * Convertit un contact en JSON
     *
     * @param contact      le contact à convertir
     * @param localisation
     * @return le contact converti en JSON
     */
    public ContactDTO contactEnJson(Contact contact, ContactMongo localisation) {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setID(contact.getId());
        contactDTO.setNomEntreprise(contact.getEntreprise());
        contactDTO.setNomContact(contact.getNom());
        contactDTO.setPrenomContact(contact.getPrenom());
        contactDTO.setAdresse(contact.getAdresse());
        contactDTO.setDescription(contact.getDescription());
        contactDTO.setTelephone(contact.getTelephone());
        contactDTO.setProspect(contact.isProspect());
        contactDTO.setLatitude(localisation.getLocation().getY());
        contactDTO.setLongitude(localisation.getLocation().getX());
        return contactDTO;
    }

}
