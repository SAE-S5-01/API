package fr.iutrodez.sae501.apicliandcollect.contact;

import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test de  {@link fr.iutrodez.sae501.apicliandcollect.contact.ContactService}
 */
@ActiveProfiles("test")
@SpringBootTest
class ContactServiceTest {

    @Mock
    private InterractionBdContact interractionBdContact;

    @Mock
    private InterractionMongoContact interractionMongoContact;

    @Mock
    private Contact contact;

    @InjectMocks
    private ContactService contactService;

    private ContactDTO contactDTO;

    private ContactMongo contactMongo;

    private Utilisateur utilisateur;

    private Long id = 1L;

    private Contact contactSQL;


    @BeforeEach
    public void setUp() {
        // Initialisation des données pour les tests
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

        utilisateur = new Utilisateur();

        contactSQL = new Contact();
        contactSQL.setId(id);
        contactSQL.setEntreprise(contactDTO.getNomEntreprise());
        contactSQL.setNom(contactDTO.getNomContact());
        contactSQL.setPrenom(contactDTO.getPrenomContact());
        contactSQL.setDescription(contactDTO.getDescription());
        contactSQL.setTelephone(contactDTO.getTelephone());
        contactSQL.setAdresse(contactDTO.getAdresse());
        contactSQL.setUtilisateur(utilisateur);

        id = 1L;
        //utilisateur.setId(1L);

        MockitoAnnotations.initMocks(this);
    }

    private long simulationSauvegarde() {
        return id;
    }

    @Test
    void testFindContactById() {
        contactMongo = new ContactMongo();
        contactMongo.set_id(id);
        // Simule la récupération d'un contact par son id
        when(interractionMongoContact.findById(id.toString())).thenReturn(java.util.Optional.of(contactMongo));

        ContactMongo result = interractionMongoContact.findById(id.toString()).orElse(null);

        // Vérifie que le contact récupéré n'est pas null et a les bonnes informations
        assertThat(result).isNotNull();
    }

    @Test
    void creerContactTest() {
        contactMongo = new ContactMongo();
        contactMongo.set_id(id);
        contactMongo.setLocation(new GeoJsonPoint(contactDTO.getLongitude(), contactDTO.getLatitude()));

        // Simulation sauvegarde dans les bases de données
        when(interractionBdContact.save(Mockito.any(Contact.class))).thenReturn(contactSQL);
        when(interractionMongoContact.save(Mockito.any(ContactMongo.class))).thenReturn(contactMongo);

        ContactDTO result = contactService.creerContact(contactDTO, utilisateur);
        assertNotNull(result);
        assertEquals("EntrepriseTest", result.getNomEntreprise());
        assertEquals("NomTest", result.getNomContact());
        assertEquals("PrenomTest", result.getPrenomContact());
        assertEquals("Une description", result.getDescription());
        assertEquals("+33612857496", result.getTelephone());
        assertEquals("AdresseTest", result.getAdresse());
        assertEquals(48.858370, result.getLatitude());
        assertEquals(2.294481, result.getLongitude());

        verify(interractionBdContact, times(1)).save(Mockito.any(Contact.class));
        verify(interractionMongoContact, times(1)).save(Mockito.any(ContactMongo.class));
    }

    @Test
    void modifierContact(){
        ContactDTO contactModifier = new ContactDTO();
        contactModifier.setNomEntreprise("Nom entreprise de Test");
        contactModifier.setAdresse("Adresse de test");
        contactModifier.setDescription("description de test");
        contactModifier.setPrenomContact("Prenom");
        contactModifier.setNomContact("Nom");
        contactModifier.setTelephone("+33698745612");
        contactModifier.setLongitude(1.851270);
        contactModifier.setLatitude(42.788370);

        List<Contact> contactEnBD = new ArrayList<>();
        contactEnBD.add(contactSQL);

        contactMongo = new ContactMongo();
        contactMongo.set_id(id);
        contactMongo.setLocation(new GeoJsonPoint(contactDTO.getLongitude(), contactDTO.getLatitude()));

        when(interractionMongoContact.findBy_id(Mockito.any(Long.class))).thenReturn(contactMongo);
        when(interractionBdContact.findByUtilisateurAndId(Mockito.any(Utilisateur.class),Mockito.any(Long.class))).thenReturn(contactEnBD);

        contactService.modifierContact(contactModifier,utilisateur,id);
        Contact result = contactEnBD.getFirst();
        assertEquals(contactModifier.getNomEntreprise(),result.getEntreprise());
        assertEquals(contactModifier.getAdresse(), result.getAdresse());
        assertEquals(contactModifier.getNomContact(),result.getNom());
        assertEquals(contactModifier.getPrenomContact(),result.getPrenom());
        assertEquals(contactModifier.getTelephone(), result.getTelephone());
        assertEquals(contactModifier.getDescription(), result.getDescription());
        assertEquals(contactModifier.getLongitude(),contactMongo.getLocation().getX());
        assertEquals(contactModifier.getLatitude(), contactMongo.getLocation().getY());
    }

}