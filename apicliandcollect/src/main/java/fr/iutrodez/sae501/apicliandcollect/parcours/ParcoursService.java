/*
 * ParcoursService.java                                  13 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.parcours;

import fr.iutrodez.sae501.apicliandcollect.contact.Contact;
import fr.iutrodez.sae501.apicliandcollect.contact.InteractionBdContact;
import fr.iutrodez.sae501.apicliandcollect.itineraire.InteractionMongoItineraire;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import jakarta.transaction.Transactional;
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
    @Autowired
    private InterractionMongoParcours interractionMongoParcours;

    /**
     * Récupère la liste des parcours de l'utilisateur u
     * @param u L'utilisateur connecté
     * @return La liste des parcours de l'utilisateur
     */
    public List<ParcoursDTO> listeParcours(Utilisateur u) {
        List<Parcours> parcours = interactionBdParcours.findByUtilisateur(u);

        List<ParcoursDTO> result = parcours.stream().map(p -> {
            ParcoursDTO dto = new ParcoursDTO();
            dto.setId(p.getId());
            dto.setStatut(p.getStatut());
            dto.setDateCreation(p.getDateCreation());
            dto.setIdItineraire(p.getIdItineraire());
            dto.setIdDernierContactVisite(p.getDernierContactVisite() != null
                    ? p.getDernierContactVisite().getId()
                    : null);

            // Vérifier si une ligne MongoDB existe pour ce parcours
            ParcoursMongo parcoursMongo = interractionMongoParcours.findByIdParcours(p.getId());

            // Ajouter les infos Mongo si elles existent
            if (parcoursMongo != null && parcoursMongo.getPrecedentesPositionsGps() != null) {
                dto.setPositionsGpsPrecedentes(parcoursMongo.getPrecedentesPositionsGps());
            }

            return dto;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * Récupère la liste des parcours de l'utilisateur u ayant un statut donné
     * @param u L'utilisateur connecté
     * @param statut Le statut des parcours à récupérer
     * @return La liste des parcours de l'utilisateur
     */
    public List<ParcoursDTO> listeParcours(Utilisateur u, StatutParcours statut) {
        List<Parcours> parcours = interactionBdParcours.findByUtilisateurAndStatut(u, statut);
        List<ParcoursDTO> result = parcours.stream().map(p -> {
            ParcoursDTO dto = new ParcoursDTO();
            dto.setId(p.getId());
            dto.setStatut(p.getStatut());
            dto.setDateCreation(p.getDateCreation());
            dto.setIdItineraire(p.getIdItineraire());
            dto.setIdDernierContactVisite(p.getDernierContactVisite() != null
                    ? p.getDernierContactVisite().getId()
                    : null);

            // Vérifier si une ligne MongoDB existe pour ce parcours
            ParcoursMongo parcoursMongo = interractionMongoParcours.findByIdParcours(p.getId());
            // Ajouter les infos Mongo si elles existent
            if (parcoursMongo != null && parcoursMongo.getPrecedentesPositionsGps() != null) {
                dto.setPositionsGpsPrecedentes(parcoursMongo.getPrecedentesPositionsGps());
            }

            return dto;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * Crée un nouveau parcours pour l'utilisateur u
     *
     * @param parcoursACreer Le parcours à créer
     * @param u L'utilisateur connecté
     * @throws IllegalArgumentException Si le contact ou l'itinéraire n'existe pas
     * @return Le parcours créé
     */
    @Transactional
    public ParcoursDTO creerParcours(ParcoursDTO parcoursACreer, Utilisateur u)
        throws IllegalArgumentException {

        if (interactionMongoItineraire.findBy_idAndIdCreateur(parcoursACreer.getIdItineraire(), u.getId()) == null) {
            throw new IllegalArgumentException("L'itinéraire n'existe pas");
        }

        // Si le nouveau statut est EN_COURS, passer tous les autres à EN_PAUSE en une requête
        if (parcoursACreer.getStatut() == StatutParcours.EN_COURS) {
            interactionBdParcours.updateStatutByUtilisateurAndStatut(u, StatutParcours.EN_COURS, StatutParcours.EN_PAUSE);
        }

        Parcours parcours = new Parcours();
        parcours.setStatut(StatutParcours.EN_COURS);
        parcours.setDateCreation(parcoursACreer.getDateCreation());
        parcours.setIdItineraire(parcoursACreer.getIdItineraire());

        if (parcoursACreer.getIdDernierContactVisite() != null) {
            Contact dernierContactVisite = interactionBdContact.findById(parcoursACreer.getIdDernierContactVisite()).get();
            parcours.setDernierContactVisite(dernierContactVisite);
        }

        parcours.setUtilisateur(u);
        Parcours resultat = null;
        try {
            resultat = interactionBdParcours.save(parcours);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parcoursEnJson(resultat);
    }

    /**
     * Modifie un parcours donné
     * @param parcoursModifie Les nouvelles informations de parcours
     * @param u L'utilisateur connecté
     * @param id L'id du parcours à modifier
     */
    @Transactional
    public void modifierParcours(ParcoursDTO parcoursModifie, Utilisateur u, Long id) {
        // Si le nouveau statut est EN_COURS, passer tous les autres à EN_PAUSE en une requête
        if (parcoursModifie.getStatut() == StatutParcours.EN_COURS) {
            interactionBdParcours.updateStatutByUtilisateurAndStatut(u, StatutParcours.EN_COURS, StatutParcours.EN_PAUSE);
        }

        Parcours parcours = interactionBdParcours.findByUtilisateurAndId(u, id).getFirst();
        parcours.setStatut(parcoursModifie.getStatut());

        if (parcoursModifie.getIdDernierContactVisite() != null) {
            parcours.setDernierContactVisite(interactionBdContact.findById(parcoursModifie.getIdDernierContactVisite()).get());
        }
        if (parcoursModifie.getPositionsGpsPrecedentes() != null ) {

            ParcoursMongo parcoursModifieMongo = interractionMongoParcours.findByIdParcours(parcours.getId());
            if (parcoursModifieMongo == null) {
                parcoursModifieMongo = new ParcoursMongo();
            }
            parcoursModifieMongo.setIdParcours(parcours.getId());
            parcoursModifieMongo.setPrecedentesPositionsGps(parcoursModifie.getPositionsGpsPrecedentes());
            interractionMongoParcours.save(parcoursModifieMongo);
        }
        interactionBdParcours.save(parcours);
    }

    /**
     * Supprime un parcours donné
     * @param u L'utilisateur connecté
     * @param id L'id du parcours à supprimer
     */
    @Transactional
    public void supprimerParcours(Utilisateur u, Long id) {
        interactionBdParcours.deleteByUtilisateurAndId(u, id);
        interractionMongoParcours.deleteByIdParcours(id);
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
        parcoursDTO.setIdDernierContactVisite(parcours.getDernierContactVisite() != null
                                              ? parcours.getDernierContactVisite().getId()
                                              : null);
        return parcoursDTO;
    }

}
