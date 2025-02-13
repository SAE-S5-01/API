/*
 * ContactService.java                                                                                      04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.contact;

import fr.iutrodez.sae501.apicliandcollect.itineraire.InteractionMongoItineraire;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.InteractionMongoUtilisateur;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    private final static Distance DISTANCE_PROSPECT_PROCHE
    = new Distance(1.0, Metrics.KILOMETERS);

    private final static Distance DISTANCE_CLIENT_PROCHE
    = new Distance(0.2, Metrics.KILOMETERS);

    @Autowired
    private InteractionBdContact interactionBdContact;

    @Autowired
    private InteractionMongoContact interactionMongoContact;

    @Autowired
    private InteractionMongoItineraire interactionMongoItineraire;

    @Autowired
    private InteractionMongoUtilisateur interactionMongoUtilisateur;

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
        Contact resultat = interactionBdContact.save(contact);
        contactMongo.set_id(resultat.getId());
        contactMongo.setLocation(new GeoJsonPoint(contactAajoute.getLongitude(), contactAajoute.getLatitude()));
        ContactMongo localisation = interactionMongoContact.save(contactMongo);
        return contactEnJson(resultat , localisation);
    }

    /**
     * Modifie un contact donné
     * @param contactModifier les nouvelles informations de contact
     * @param u l'utilisateur connecté
     * @param id l'id du contact à modifier
     */
    public void modifierContact(ContactDTO contactModifier, Utilisateur u, Long id) {
        Contact contacAmodifier = interactionBdContact.findByUtilisateurAndId(u, id).getFirst();
        contacAmodifier.setEntreprise(contactModifier.getNomEntreprise());
        contacAmodifier.setDescription(contactModifier.getDescription());
        contacAmodifier.setAdresse(contactModifier.getAdresse());
        contacAmodifier.setTelephone(contactModifier.getTelephone());
        contacAmodifier.setNom(contactModifier.getNomContact());
        contacAmodifier.setPrenom(contactModifier.getPrenomContact());
        contacAmodifier.setProspect(contactModifier.isProspect());
        ContactMongo contactMongoAmodifier = interactionMongoContact.findBy_id(id);
        contactMongoAmodifier.setLocation(new GeoJsonPoint(contactModifier.getLongitude(), contactModifier.getLatitude()));
        interactionMongoContact.save(contactMongoAmodifier);
        interactionBdContact.save(contacAmodifier);
    }

    /**
     * Supprime le contact d'id id
     * @param u l'utilisateur connecté
     * @param id l'id du contact à supprimer
     */
    public void supprimerContact(Utilisateur u, Long id) {
        Contact contact = interactionBdContact.findByUtilisateurAndId(u, id).getFirst();
        interactionBdContact.delete(contact);
        interactionMongoContact.deleteBy_id(id);
        interactionMongoItineraire.deleteByIdContact(id);
    }

    /**
     * Récupère la liste des contacts de l'utilisateur u
     *
     * @param u l'utilisateur connecté
     * @return la liste des contacts de l'utilisateur
     */
    public List<ContactDTO> listeContact(Utilisateur u) {
        List<Contact> contacts = interactionBdContact.findByUtilisateur(u);
        List<ContactMongo> localisations = interactionMongoContact.findAll();
        return contacts.stream().map(contact -> {
            ContactMongo localisation = localisations.stream().filter(loc -> loc.get_id() == contact.getId())
                .findFirst().get();
            return contactEnJson(contact, localisation);
        }).collect(Collectors.toList());
    }

    /**
     * Récupère la liste des prospects à moins de 1000m de l'utilisateur.
     * @param u L'utilisateur connecté
     * @return La liste des prospects proches
     */
    public List<ContactDTO> getProspectsProches(Utilisateur u) {
        GeoJsonPoint localisation = interactionMongoUtilisateur.findBy_id(u.getId()).getLocation();

        List<ContactMongo> prospectsProches
        = interactionMongoContact.findByLocationNear(localisation, DISTANCE_PROSPECT_PROCHE);

        return prospectsProches.stream().map(prospect -> {
            Contact contact = interactionBdContact.findById(prospect.get_id()).get();
            return contactEnJson(contact, prospect);
        }).collect(Collectors.toList());
    }

    /**
     * Vérifie si le client est proche de l'utilisateur
     * @param u L'utilisateur connecté
     * @param idClient L'id du client
     * @return true si le client est proche, false sinon
     */
    public boolean isClientProche(Utilisateur u, long idClient) {
        GeoJsonPoint localisation = interactionMongoUtilisateur.findBy_id(u.getId()).getLocation();

        List<ContactMongo> clientsProches = interactionMongoContact.findByLocationNear(localisation, DISTANCE_CLIENT_PROCHE);

        return clientsProches.stream()
            .anyMatch(client -> client.get_id() == idClient);
    }

    /**
     * Convertit un contact en JSON
     *
     * @param contact Le contact à convertir
     * @param localisation La localisation du contact
     * @return Le contact converti en JSON
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
