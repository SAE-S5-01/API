package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ControleurUtilisateur {

    @Autowired
    private InterractionBdUtilisateur InterractionBdUtilisateur;

    @PostMapping("/inscription")
    public void inscrireUtilisateur(@RequestBody UtilisateurDTO utilisateurDTO) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setPrenom(utilisateurDTO.getPrenom());
        utilisateur.setMail(utilisateurDTO.getMail());
        utilisateur.setToken("nouveau token");
        InterractionBdUtilisateur.save(utilisateur);
    }
}
