package fr.iutrodez.sae501.apicliandcollect.testIntegration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Cette classe permet de réaliser tous les tests d'intégration de l'API.
 * Tous les tests d'intégration sont regroupés dans la même classe et sont ordonnés
 * grâce à l'annotation @Order(), en attendant de trouver une meilleure solution.
 *
 * Pour effectuer les tests unitaires, on utilise MockMvc pour simuler les appels à l'API,
 * et on a défini une base de données de test dans le fichier properties.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestIntegration {

    @Autowired
    private MockMvc mockMvc;

    // Route api pour la partit contact
    private final static String ROUTE_API = "/api/contact";

    // Base de la route api utilisateur
    private static final String BASE_API_UTILISATEUR = "/api/utilisateur/";

    private static final String CLIENT_AVANT_MODIF = """
                {
                    "nomEntreprise": "entrepriseAModifier",
                    "adresse": "Rue Carnus, L'Usine à Gaz, Camonil, Rodez, Aveyron, Occitanie, France métropolitaine, 12000, France",
                    "nomContact": "nom contact",
                    "prenomContact": "prenom contact",
                    "telephone": "+33612345678",
                    "description": "Client avant modif",
                    "prospect": true,
                    "longitude": 44.145678,
                    "latitude": 2.123456
                }
                """;

    private Long id;

    private HashMap<String,String> con  = new HashMap<>();

    @Test
    @Order(1)
    void inscriptionTest() throws Exception{
        String utilisateur = """
                {
                    "mail": "testtest@gmail.com",
                    "motDePasse": "Test1234@",
                    "nom": "test",
                    "prenom": "test",
                    "adresse": "adresse test",
                    "latitude": 2.987654,
                    "longitude": 44.321654
                }
                """;

        mockMvc.perform(post(BASE_API_UTILISATEUR + "inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void inscriptionSansNomShouldFail() throws Exception{
        String utilisateur = """
                {
                    "mail": "testSansNom@gmail.com",
                    "motDePasse": "Test1234@",
                    "prenom": "test",
                    "adresse": "adresse test",
                    "latitude": 2.987654,
                    "longitude": 44.321654
                }
                """;

        mockMvc.perform(post(BASE_API_UTILISATEUR +"inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(2)
    void inscriptionSansPrenomShouldFail() throws Exception{
        String utilisateur = """
                {
                    "mail": "testSansPrenom@gmail.com",
                    "motDePasse": "Test1234@",
                    "nom": "test",
                    "adresse": "adresse test",
                    "latitude": 2.987654,
                    "longitude": 44.321654
                }
                """;

        mockMvc.perform(post(BASE_API_UTILISATEUR +"inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(2)
    void inscriptionSansMailShouldFail() throws Exception{
        String utilisateur = """
                {
                    "motDePasse": "Test1234@",
                    "nom": "test",
                    "prenom": "test",
                    "adresse": "adresse test",
                    "latitude": 2.987654,
                    "longitude": 44.321654
                }
                """;

        mockMvc.perform(post(BASE_API_UTILISATEUR + "inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(2)
    void inscriptionSansAdresseShouldFail() throws Exception{
        String utilisateur = """
                {
                    "mail": "testSansAdresse@gmail.com",
                    "motDePasse": "Test1234@",
                    "nom": "test",
                    "prenom": "test",
                    "latitude": 2.987654,
                    "longitude": 44.321654
                }
                """;

        mockMvc.perform(post(BASE_API_UTILISATEUR +"inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(2)
    void inscriptionSansMotDePasseShouldFail() throws Exception{
        String utilisateur = """
                {
                    "mail": "SansMdp@gmail.com",
                    "nom": "test",
                    "prenom": "test",
                    "adresse": "adresse test",
                    "latitude": 2.987654,
                    "longitude": 44.321654
                }
                """;

        mockMvc.perform(post(BASE_API_UTILISATEUR +"inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @Order(3)
    void connexionShouldFail() throws Exception {
        mockMvc.perform(get(BASE_API_UTILISATEUR +"connexion")
                        .param("mail","JaneDoe@gmail.com")
                        .param("motDePasse","Mdp1234@"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(4)
    void connexionTest() throws Exception {
        mockMvc.perform(get(BASE_API_UTILISATEUR +"connexion")
                        .param("mail", "testtest@gmail.com")
                        .param("motDePasse","Test1234@"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("Utilisateur connecté avec succès"));
    }

    @Test
    @Order(5)
    void SupprimerUtilisateurTest() throws Exception{
        MvcResult result = mockMvc.perform(get(BASE_API_UTILISATEUR +"connexion")
                        .param("mail", "testtest@gmail.com")
                        .param("motDePasse","Test1234@"))
                .andExpect(status().isOk()).andReturn();

        String reponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        con = objectMapper.readValue(reponse, new TypeReference<>() {});

        mockMvc.perform(put("/api/utilisateur/suppresionCompte")
                        .header("Authorization","Bearer " + con.get("token")))
                .andExpect(status().isOk());
    }

    /**
     * Méthode qui vient initialiser une connexion à l'API afin de tester toute la partie contact :
     * C'est-à-dire la création, la modification et la récupération des contacts.
     * @throws Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        String utilisateur = """
                {
                    "mail": "testInte@gmail.com",
                    "motDePasse": "Test1234@",
                    "nom": "test",
                    "prenom": "test",
                    "adresse": "adresse test",
                    "latitude": 2.987654,
                    "longitude": 44.321654
                }
                """;

        mockMvc.perform(post(BASE_API_UTILISATEUR + "inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur));

        MvcResult result = mockMvc.perform(get("/api/utilisateur/connexion")
                        .param("mail","testInte@gmail.com")
                        .param("motDePasse","Test1234@"))
                .andReturn();

        String reponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        con = objectMapper.readValue(reponse, new TypeReference<>() {});
    }

    @Test
    @Order(6)
    void nouveauContact() throws Exception {
        String client =  """
                                {
                                    "nomEntreprise": "entreprise",
                                    "adresse": "Rue Carnus, L'Usine à Gaz, Camonil, Rodez, Aveyron, Occitanie, France métropolitaine, 12000, France",
                                    "nomContact": "nom",
                                    "prenomContact": "prenom",
                                    "telephone": "+33612345678",
                                    "description": "Blabla",
                                    "prospect": true,
                                    "longitude": 44.145678,
                                    "latitude": 2.123456
                                }
                                """;
        mockMvc.perform(post(ROUTE_API).header("Authorization","Bearer " + con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(client))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(7)
    void obtenirContact() throws Exception {
    mockMvc.perform(get(ROUTE_API)
                        .header("Authorization","Bearer " + con.get("token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("[0].nomEntreprise").value("entreprise"))
                .andExpect(jsonPath("[0].adresse").value("Rue Carnus, L'Usine à Gaz, Camonil, Rodez, Aveyron, Occitanie, France métropolitaine, 12000, France"))
                .andExpect(jsonPath("[0].nomContact").value("nom"))
                .andExpect(jsonPath("[0].prenomContact").value("prenom"))
                .andExpect(jsonPath("[0].telephone").value("+33612345678"))
                .andExpect(jsonPath("[0].description").value("Blabla"))
                .andExpect(jsonPath("[0].prospect").value(true))
                .andExpect(jsonPath("[0].longitude").value("44.145678"))
                .andExpect(jsonPath("[0].latitude").value("2.123456"));
    }

    @Test
    @Order(8)
    void obtenirContactSansConnexion() throws Exception {
        mockMvc.perform(get(ROUTE_API))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(9)
    void modifierContact() throws Exception {
        creationClientAModifier();
        String clientApresModif = """
               {
                    "nomEntreprise": "entreprise modifier",
                    "adresse": "Rue Carnus, L'Usine à Gaz, Camonil, Rodez, Aveyron, Occitanie, France métropolitaine, 12000, France",
                    "nomContact": "nom contact",
                    "prenomContact": "prenom contact",
                    "telephone": "+33612345678",
                    "description": "Client aprés modification",
                    "prospect": true,
                    "longitude": 44.145678,
                    "latitude": 2.123456
               }
               """;
        mockMvc.perform(put(ROUTE_API)
                        .header("Authorization", "Bearer "+con.get("token"))
                        .param("id",id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientApresModif))
                .andExpect(status().isOk());

        mockMvc.perform(get(ROUTE_API)
                        .header("Authorization","Bearer " + con.get("token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[1].nomEntreprise").value("entreprise modifier"))
                .andExpect(jsonPath("[1].description").value("Client aprés modification"));
    }

    @Test
    @Order(10)
    void CreationContactSansNomEntrepriseShouldFail() throws Exception {
        String client =  """
                                {
                                    "adresse": "Rue Carnus, L'Usine à Gaz, Camonil, Rodez, Aveyron, Occitanie, France métropolitaine, 12000, France",
                                    "nomContact": "nom",
                                    "prenomContact": "prenom",
                                    "telephone": "+33612345678",
                                    "description": "Blabla",
                                    "prospect": true,
                                    "longitude": 44.145678,
                                    "latitude": 2.123456
                                }
                                """;
        mockMvc.perform(post(ROUTE_API).header("Authorization","Bearer " + con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(client))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(11)
    void CreationContactSansAdresseShouldFail() throws Exception {
        String client =  """
                                {
                                    "nomEntreprise": "entreprise",
                                    "nomContact": "nom",
                                    "prenomContact": "prenom",
                                    "telephone": "+33612345678",
                                    "description": "Blabla",
                                    "prospect": true,
                                    "longitude": 44.145678,
                                    "latitude": 2.123456
                                }
                                """;
        mockMvc.perform(post(ROUTE_API).header("Authorization","Bearer " + con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(client))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(12)
    void modifierContactSansNomEntrepriseShouldFail() throws Exception {
        creationClientAModifier();
        String clientApresModif = """
               {
                    "adresse": "Rue Carnus, L'Usine à Gaz, Camonil, Rodez, Aveyron, Occitanie, France métropolitaine, 12000, France",
                    "nomContact": "nom contact",
                    "prenomContact": "prenom contact",
                    "telephone": "+33612345678",
                    "description": "Client aprés modification",
                    "prospect": true,
                    "longitude": 44.145678,
                    "latitude": 2.123456
               }
               """;
        mockMvc.perform(put(ROUTE_API)
                        .header("Authorization", "Bearer "+con.get("token"))
                        .param("id",id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientApresModif))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(13)
    void modifierContactSansAdresseShouldFail() throws Exception {
        creationClientAModifier();
        String clientApresModif = """
               {
                    "nomEntreprise": "entreprise test",
                    "nomContact": "nom contact",
                    "prenomContact": "prenom contact",
                    "telephone": "+33612345678",
                    "description": "Client aprés modification",
                    "prospect": true,
                    "longitude": 44.145678,
                    "latitude": 2.123456
               }
               """;
    mockMvc.perform(put(ROUTE_API)
                        .header("Authorization", "Bearer "+con.get("token"))
                        .param("id",id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientApresModif))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(14)
    void clean() throws Exception{
        mockMvc.perform(put("/api/utilisateur/suppresionCompte")
                        .header("Authorization","Bearer " + con.get("token")))
                .andExpect(status().isOk());
    }

    public void creationClientAModifier() throws Exception {
        MvcResult result = mockMvc.perform(post(ROUTE_API).header("Authorization", "Bearer " + con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CLIENT_AVANT_MODIF))
                .andExpect(status().isCreated())
                .andReturn();

        JSONObject client = new JSONObject(result.getResponse().getContentAsString());

        id = client.optLong("id");
    }
}