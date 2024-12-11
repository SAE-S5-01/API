package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test d'UtilisateurService.java
 */
class UtilisateurServiceTest {

    @Mock
    private InterractionBdUtilisateur interractionBdUtilisateur;

    @Mock
    private PasswordEncoder encoderMotPasse;

    @InjectMocks
    private UtilisateurService utilisateurService;

    private UtilisateurDTO utilisateurDTO;

    @BeforeEach
    public void setUp() {
        // Initialisation des données pour les tests
        utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setNom("NomTest");
        utilisateurDTO.setPrenom("PrenomTest");
        utilisateurDTO.setMail("test.ju@test.com");
        utilisateurDTO.setMotDePasse("mdpTest1!");
        utilisateurDTO.setAdresse("AdresseTest");

        //encoderMotPasse = Mockito.mock(PasswordEncoder.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void creerUtilisateurTest() {
        String motDePasseEncode = "motDePasseEncode";

        // Simulation encodage du mot de passe
        when(encoderMotPasse.encode(utilisateurDTO.getMotDePasse())).thenReturn(motDePasseEncode);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setPrenom(utilisateurDTO.getPrenom());
        utilisateur.setMail(utilisateurDTO.getMail());
        utilisateur.setMotDePasse(motDePasseEncode);
        utilisateur.setAdresse(utilisateurDTO.getAdresse());

        // Simulation sauvegarde dans la base de données
        when(interractionBdUtilisateur.save(Mockito.any(Utilisateur.class))).thenReturn(utilisateur);

        UtilisateurDTO result = utilisateurService.creerUtilisateur(utilisateurDTO);
        assertNotNull(result);
        assertEquals("NomTest", result.getNom());
        assertEquals("PrenomTest", result.getPrenom());
        assertEquals("test.ju@test.com", result.getMail());
        assertEquals(motDePasseEncode, result.getMotDePasse());
        assertEquals("AdresseTest", result.getAdresse());

        verify(encoderMotPasse, times(1)).encode(utilisateurDTO.getMotDePasse());
        verify(interractionBdUtilisateur, times(1)).save(Mockito.any(Utilisateur.class));
    }

    @Test
    void supprimerUtilisateurTest() {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setPrenom(utilisateurDTO.getPrenom());
        utilisateur.setMail(utilisateurDTO.getMail());
        utilisateur.setMotDePasse(utilisateurDTO.getMotDePasse());
        utilisateur.setAdresse(utilisateurDTO.getAdresse());

        utilisateurService.supprimerUtilisateur(utilisateur);

        verify(interractionBdUtilisateur, times(1)).delete(utilisateur);
    }

}