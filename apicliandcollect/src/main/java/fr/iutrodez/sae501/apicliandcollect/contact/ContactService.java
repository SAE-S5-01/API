package fr.iutrodez.sae501.apicliandcollect.contact;

import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private InterractionBdContact interractionBdContact;

    @Transactional
    public Contact creerContact(ContactDTO contactAajoute, Utilisateur u) {
        Contact contact = new Contact();
        contact.setNom(contactAajoute.getNom());
        contact.setPrenom(contactAajoute.getPrenom());
        contact.setAdresse(contactAajoute.getAdresse());
        contact.setDescription(contactAajoute.getDescription());
        contact.setTelephone(contactAajoute.getTelephone());
        contact.setUtilisateur(u);
        return interractionBdContact.save(contact);
    }
}
