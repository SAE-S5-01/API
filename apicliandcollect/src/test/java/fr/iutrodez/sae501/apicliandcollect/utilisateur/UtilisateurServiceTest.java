package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import fr.iutrodez.sae501.apicliandcollect.contact.*;
import fr.iutrodez.sae501.apicliandcollect.itineraire.InteractionMongoItineraire;
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

import java.util.ArrayList;
import java.util.List;

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
    private InteractionBdContact interactionBdContact;

    @Mock
    private InteractionMongoUtilisateur interactionMongoUtilisateur;

    @Mock
    private InteractionMongoContact interactionMongoContact;

    @Mock
    private InteractionMongoItineraire interactionMongoItineraire;

    @Mock
    private PasswordEncoder encoderMotPasse;

    @InjectMocks
    private UtilisateurService utilisateurService;

    @InjectMocks
    private ContactService contactService;

    private UtilisateurDTO utilisateurDTO;

    private ContactDTO contactDTO;

    Long idUtilisateur = 1L;

    Long idContact = 1L;

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

        contactDTO = new ContactDTO();
        contactDTO.setNomEntreprise("EntrepriseTest");
        contactDTO.setNomContact("NomTest");
        contactDTO.setPrenomContact("PrenomTest");
        contactDTO.setDescription("Une description");
        contactDTO.setTelephone("+33612857496");
        contactDTO.setAdresse("AdresseTest");
        contactDTO.setLatitude(48.858370);
        contactDTO.setLongitude(2.294481);

        contactService = new ContactService();

        MockitoAnnotations.initMocks(this);
    }

    @Test
    void creerUtilisateurTest() {
        String motDePasseEncode = "motDePasseEncode";

        // Simulation encodage du mot de passe
        when(encoderMotPasse.encode(utilisateurDTO.getMotDePasse())).thenReturn(motDePasseEncode);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(idUtilisateur);
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
        utilisateur.setId(idUtilisateur);
        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setPrenom(utilisateurDTO.getPrenom());
        utilisateur.setMail(utilisateurDTO.getMail());
        utilisateur.setMotDePasse(utilisateurDTO.getMotDePasse());
        utilisateur.setAdresse(utilisateurDTO.getAdresse());

        UtilisateurMongo utilisateurMongo = new UtilisateurMongo();
        utilisateurMongo.set_id(utilisateur.getId());
        utilisateurMongo.setLocation(new GeoJsonPoint(utilisateurDTO.getLongitude(), utilisateurDTO.getLatitude()));

        Contact contactSQL = new Contact();
        contactSQL.setId(idContact);
        contactSQL.setEntreprise(contactDTO.getNomEntreprise());
        contactSQL.setNom(contactDTO.getNomContact());
        contactSQL.setPrenom(contactDTO.getPrenomContact());
        contactSQL.setDescription(contactDTO.getDescription());
        contactSQL.setTelephone(contactDTO.getTelephone());
        contactSQL.setAdresse(contactDTO.getAdresse());
        contactSQL.setUtilisateur(utilisateur);

        ContactMongo contactMongo = new ContactMongo();
        contactMongo.set_id(idContact);
        contactMongo.setLocation(new GeoJsonPoint(contactDTO.getLongitude(), contactDTO.getLatitude()));

        List<Contact> contacts = new ArrayList<>();
        contacts.add(contactSQL);

        when(interactionBdContact.findByUtilisateur(Mockito.any(Utilisateur.class))).thenReturn(contacts);

        doNothing().when(interactionBdUtilisateur).delete(Mockito.any(Utilisateur.class));
        doNothing().when(interactionMongoUtilisateur).deleteBy_id(Mockito.any(Long.class));
        doNothing().when(interactionBdContact).deleteByUtilisateur(Mockito.any(Utilisateur.class));
        doNothing().when(interactionMongoContact).deleteBy_id(Mockito.any(Long.class));

        doNothing().when(interactionMongoItineraire).deleteByIdCreateur(Mockito.any(Long.class));

        utilisateurService.supprimerUtilisateur(utilisateur);

        verify(interactionBdUtilisateur, times(1)).delete(utilisateur);
    }

}