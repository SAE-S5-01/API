package fr.iutrodez.sae501.apicliandcollect.utilisateur;

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
    private InteractionBdUtilisateur interactionBdUtilisateur;

    @Mock
    private InteractionMongoUtilisateur interactionMongoUtilisateur;

    @Mock
    private PasswordEncoder encoderMotPasse;

    @InjectMocks
    private UtilisateurService utilisateurService;

    private UtilisateurDTO utilisateurDTO;

    Long id = 1L;

    @BeforeEach
    public void setUp() {
        // Initialisation des données pour les tests
        utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setNom("NomTest");
        utilisateurDTO.setPrenom("PrenomTest");
        utilisateurDTO.setMail("test.ju@test.com");
        utilisateurDTO.setMotDePasse("mdpTest1!");
        utilisateurDTO.setAdresse("AdresseTest");
        utilisateurDTO.setLongitude(54.123);
        utilisateurDTO.setLatitude(76.1245);

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
        utilisateurMongo.setLocation(new GeoJsonPoint(utilisateurDTO.getLongitude(), utilisateurDTO.getLatitude()));

        // Simulation sauvegarde dans les bases de données
        when(interactionBdUtilisateur.save(Mockito.any(Utilisateur.class))).thenReturn(utilisateur);
        when(interactionMongoUtilisateur.save(Mockito.any(UtilisateurMongo.class))).thenReturn(utilisateurMongo);

        UtilisateurDTO result = utilisateurService.creerUtilisateur(utilisateurDTO);
        assertNotNull(result);
        assertEquals("NomTest", result.getNom());
        assertEquals("PrenomTest", result.getPrenom());
        assertEquals("test.ju@test.com", result.getMail());
        assertEquals(motDePasseEncode, result.getMotDePasse());
        assertNotEquals("mdpTest1!", result.getMotDePasse());
        assertEquals("AdresseTest", result.getAdresse());
        assertEquals(54.123, result.getLongitude());
        assertEquals(76.1245, result.getLatitude());

        verify(encoderMotPasse, times(1)).encode(utilisateurDTO.getMotDePasse());
        verify(interactionBdUtilisateur, times(1)).save(Mockito.any(Utilisateur.class));
        verify(interactionMongoUtilisateur, times(1)).save(Mockito.any(UtilisateurMongo.class));
    }

    @Test
    void supprimerUtilisateurTest() {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setPrenom(utilisateurDTO.getPrenom());
        utilisateur.setMail(utilisateurDTO.getMail());
        utilisateur.setMotDePasse(utilisateurDTO.getMotDePasse());
        utilisateur.setAdresse(utilisateurDTO.getAdresse());

        doNothing().when(interactionBdUtilisateur).delete(Mockito.any(Utilisateur.class));

        utilisateurService.supprimerUtilisateur(utilisateur);

        verify(interactionBdUtilisateur, times(1)).delete(utilisateur);
    }

}