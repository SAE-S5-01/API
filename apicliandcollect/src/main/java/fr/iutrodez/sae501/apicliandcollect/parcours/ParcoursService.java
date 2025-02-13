/*
 * ParcoursService.java                                  13 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.parcours;

import fr.iutrodez.sae501.apicliandcollect.contact.Contact;
import fr.iutrodez.sae501.apicliandcollect.contact.InteractionBdContact;
import fr.iutrodez.sae501.apicliandcollect.itineraire.InteractionMongoItineraire;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParcoursService {

    @Autowired
    private InteractionBdParcours interactionBdParcours;

    @Autowired
    private InteractionBdContact interactionBdContact;

    @Autowired
    private InteractionMongoItineraire interactionMongoItineraire;

    /**
     * Crée un nouveau parcours pour l'utilisateur u
     *
     * @param parcoursACreer Le parcours à créer
     * @param u L'utilisateur connecté
     * @throws IllegalArgumentException Si le contact ou l'itinéraire n'existe pas
     * @return Le parcours créé
     */
    public ParcoursDTO creerParcours(ParcoursDTO parcoursACreer, Utilisateur u)
        throws IllegalArgumentException {
        Contact dernierContactVisite = interactionBdContact.findById(parcoursACreer.getIdDernierContactVisite()).get();

        if (interactionMongoItineraire.findBy_idAndIdCreateur(parcoursACreer.getIdItineraire(), u.getId()) == null) {
            throw new IllegalArgumentException("L'itinéraire n'existe pas");
        }

        Parcours parcours = new Parcours();
        parcours.setStatut(StatutParcours.EN_COURS);
        parcours.setDateCreation(parcoursACreer.getDateCreation());
        parcours.setIdItineraire(parcoursACreer.getIdItineraire());
        parcours.setDernierContactVisite(dernierContactVisite);
        parcours.setUtilisateur(u);
        Parcours resultat = interactionBdParcours.save(parcours);

        return parcoursEnJson(resultat);
    }

    /**
     * Modifie un parcours donné
     * @param parcoursModifie Les nouvelles informations de parcours
     * @param u L'utilisateur connecté
     * @param id L'id du parcours à modifier
     */
    public void modifierParcours(ParcoursDTO parcoursModifie, Utilisateur u, Long id) {
        Parcours parcours = interactionBdParcours.findByUtilisateurAndId(u, id).getFirst();

        parcours.setStatut(parcoursModifie.getStatut());
        parcours.setDernierContactVisite(interactionBdContact.findById(parcoursModifie.getIdDernierContactVisite()).get());
        interactionBdParcours.save(parcours);
    }

    /**
     * Récupère la liste des parcours de l'utilisateur u
     *
     * @param u L'utilisateur connecté
     * @param statut Le statut des parcours à récupérer
     * @return La liste des parcours de l'utilisateur
     */
    public List<ParcoursDTO> listeParcours(Utilisateur u, StatutParcours statut) {
        List<Parcours> parcours = interactionBdParcours.findByUtilisateurAndStatut(u, statut);
        return parcours.stream().map(this::parcoursEnJson).collect(Collectors.toList());
    }

    /**
     * Convertit un parcours en JSON
     *
     * @param parcours Le parcours à convertir
     * @return Le parcours converti en JSON
     */
    public ParcoursDTO parcoursEnJson(Parcours parcours) {
        ParcoursDTO parcoursDTO = new ParcoursDTO();
        parcoursDTO.setId(parcours.getId());
        parcoursDTO.setStatut(parcours.getStatut());
        parcoursDTO.setDateCreation(parcours.getDateCreation());
        parcoursDTO.setIdItineraire(parcours.getIdItineraire());
        parcoursDTO.setIdDernierContactVisite(parcours.getDernierContactVisite().getId());
        return parcoursDTO;
    }

}
