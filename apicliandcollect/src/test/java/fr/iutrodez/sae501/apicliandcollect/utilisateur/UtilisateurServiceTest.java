package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import fr.iutrodez.sae501.apicliandcollect.contact.InterractionBdContact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test de  {@link fr.iutrodez.sae501.apicliandcollect.utilisateur.UtilisateurService}
 */
@ActiveProfiles("test")
@SpringBootTest
class UtilisateurServiceTest {

    @Mock
    private InterractionBdUtilisateur interractionBdUtilisateur;

    @Mock
    private InterractionMongoUtilisateur interractionMongoUtilisateur;

    @Mock
    private PasswordEncoder encoderMotPasse;

    @InjectMocks
    private UtilisateurService utilisateurService;

    private UtilisateurDTO utilisateurDTO;

    long id = 1L;

    double longitude = 54.123;

    double latitude = 76.1245;

    @BeforeEach
    public void setUp() {
        // Initialisation des données pour les tests
        utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setNom("NomTest");
        utilisateurDTO.setPrenom("PrenomTest");
        utilisateurDTO.setMail("test.ju@test.com");
        utilisateurDTO.setMotDePasse("mdpTest1!");
        utilisateurDTO.setAdresse("AdresseTest");

        utilisateurService = new UtilisateurService();

        MockitoAnnotations.initMocks(this);
    }

    @Test
    void creerUtilisateurTest() {
        String motDePasseEncode = "motDePasseEncode";

        // Simulation encodage du mot de passe
        when(encoderMotPasse.encode(utilisateurDTO.getMotDePasse())).thenReturn(motDePasseEncode);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(id);
        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setPrenom(utilisateurDTO.getPrenom());
        utilisateur.setMail(utilisateurDTO.getMail());
        utilisateur.setMotDePasse(motDePasseEncode);
        utilisateur.setAdresse(utilisateurDTO.getAdresse());

        UtilisateurMongo utilisateurMongo = new UtilisateurMongo();
        utilisateurMongo.set_id(utilisateur.getId());
        utilisateurMongo.setLocation(new GeoJsonPoint(longitude, latitude));

        // Simulation sauvegarde dans la base de données
        when(interractionBdUtilisateur.save(Mockito.any(Utilisateur.class))).thenReturn(utilisateur);
        when(interractionMongoUtilisateur.save(Mockito.any(UtilisateurMongo.class))).thenReturn(utilisateurMongo);

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

        doNothing().when(interractionBdUtilisateur).delete(Mockito.any(Utilisateur.class));

        utilisateurService.supprimerUtilisateur(utilisateur);

        verify(interractionBdUtilisateur, times(1)).delete(utilisateur);
    }

}