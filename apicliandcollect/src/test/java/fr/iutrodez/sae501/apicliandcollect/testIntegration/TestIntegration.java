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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    // base Api itinéraire
    private final static String ROUTE_ITINERAIRE = "/api/itineraire";

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
                    "prospect": false,
                    "longitude": 2.7227829999999997,
                    "latitude": 44.4712353
                }
                """;

    private static final String CLIENT_ITINERAIRE_1 = """
                {
                    "ID": 1,
                    "nomEntreprise": "entreprise1",
                    "adresse": "Adresse 1",
                    "nomContact": "nom contact",
                    "prenomContact": "prenom contact",
                    "telephone": "+33612345678",
                    "description": "client pour itineraire",
                    "prospect": false,
                    "longitude": 2.5726834999999997,
                    "latitude": 44.35595800000001
                }
                """;

    private static final String CLIENT_ITINERAIRE_2 = """
                {
                    "ID": 2,
                    "nomEntreprise": "entreprise2",
                    "adresse": "Adresse 2",
                    "nomContact": "nom contact",
                    "prenomContact": "prenom contact",
                    "telephone": "+33612345678",
                    "description": "client pour itineraire",
                    "prospect": false,
                    "longitude": 2.5758355,
                    "latitude": 44.358561
                }
                """;

    private static final String CLIENT_ITINERAIRE_3 = """
                {
                    "ID": 3,
                    "nomEntreprise": "entreprise3",
                    "adresse": "Adresse 3",
                    "nomContact": "nom contact",
                    "prenomContact": "prenom contact",
                    "telephone": "+33612345678",
                    "description": "client pour itineraire",
                    "prospect": false,
                    "longitude": 2.5737413,
                    "latitude": 44.3539443
                }
                """;

    private static final String CLIENT_ITINERAIRE_4 = """
                {
                    "ID": 4,
                    "nomEntreprise": "entreprise4",
                    "adresse": "Adresse 4",
                    "nomContact": "nom contact",
                    "prenomContact": "prenom contact",
                    "telephone": "+33612345678",
                    "description": "client pour itineraire",
                    "prospect": false,
                    "longitude": 2.7227829999999997,
                    "latitude": 44.4712353
                }
                """;

    private static final String ITINERAIRE =
                """
                {
                    "nomItineraire": "Itineraire test",
                    "domicile": {"x": 2.575986 , "y": 44.349388999999995},
                    "listePoint": {
                        "1": {"x": 2.5726834999999997 , "y": 44.35595800000001},
                        "2": {"x": 2.5758355 , "y": 44.358561},
                        "3": {"x": 2.5737413 , "y": 44.3539443},
                        "4": {"x": 2.7227829999999997 , "y": 44.4712353}
                    }
                }
                """;

    private Long id;

    private HashMap<String,String> con  = new HashMap<>();

    @Test
    @Order(1)
    void inscriptionTest() throws Exception{
        String utilisateur = """
                {
                    "mail": "testetestetest@gmail.com",
                    "motDePasse": "Test1234@",
                    "nom": "test",
                    "prenom": "test",
                    "adresse": "adresse test",
                    "latitude": 44.321654,
                    "longitude": 2.987654
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
                        .param("mail", "testetestetest@gmail.com")
                        .param("motDePasse","Test1234@"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("Utilisateur connecté avec succès"));
    }

    /**
     * Méthode qui vient initialiser une connexion à l'API afin de tester toute la partie contact :
     * C'est-à-dire la création, la modification et la récupération des contacts.
     * @throws Exception
     */
    @BeforeEach
    void setUp() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/utilisateur/connexion")
                        .param("mail","testetestetest@gmail.com")
                        .param("motDePasse","Test1234@"))
                .andReturn();

        String reponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        con = objectMapper.readValue(reponse, new TypeReference<>() {});
    }

    @Test
    @Order(5)
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
    @Order(6)
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
    @Order(7)
    void obtenirContactSansConnexion() throws Exception {
        mockMvc.perform(get(ROUTE_API))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(8)
    void modifierContact() throws Exception {
        creationClientAModifier();
        String clientApresModif = """
               {
                    "nomEntreprise": "entreprise modifier",
                    "adresse": "Rue Carnus, L'Usine à Gaz, Camonil, Rodez, Aveyron, Occitanie, France métropolitaine, 12000, France",
                    "nomContact": "nom contact",
                    "prenomContact": "prenom contact",
                    "telephone": "+33612345678",
                    "description": "Client après modification",
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
                .andExpect(jsonPath("[1].description").value("Client après modification"));
    }

    @Test
    @Order(9)
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
    @Order(10)
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
    @Order(11)
    void modifierContactSansNomEntrepriseShouldFail() throws Exception {
        creationClientAModifier();
        String clientApresModif = """
               {
                    "adresse": "Rue Carnus, L'Usine à Gaz, Camonil, Rodez, Aveyron, Occitanie, France métropolitaine, 12000, France",
                    "nomContact": "nom contact",
                    "prenomContact": "prenom contact",
                    "telephone": "+33612345678",
                    "description": "Client après modification",
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
    @Order(12)
    void modifierContactSansAdresseShouldFail() throws Exception {
        creationClientAModifier();
        String clientApresModif = """
               {
                    "nomEntreprise": "entreprise test",
                    "nomContact": "nom contact",
                    "prenomContact": "prenom contact",
                    "telephone": "+33612345678",
                    "description": "Client après modification",
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
    void supprimerClientTest() throws Exception{
        creationClientAModifier();
        mockMvc.perform(delete(ROUTE_API+"/"+id)
                .header("Authorization", "Bearer "+con.get("token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("Client supprimé avec succès"));
    }

    @Test
    @Order(14)
    void supprimerClientAvecIdInvalideShouldFail() throws Exception {
        mockMvc.perform(delete(ROUTE_API+"/-1")
                .header("Authorization", "Bearer "+con.get("token")))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @Order(15)
    void creerItineraireTest() throws Exception{
        ajouterClient();
        mockMvc.perform(post(ROUTE_ITINERAIRE)
                        .header("Authorization", "Bearer "+con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ITINERAIRE))
                .andExpect(status().isOk());
    }

    @Test
    @Order(16)
    void creerItineraireInvalideshouldFail() throws Exception{
        ajouterClient();
        String itineraire =
                """
                {
                    "nomItineraire": "Itineraire test",
                    "domicile": {"x": 2.575986 , "y": 44.349388999999995},
                    "listePoint": {
                        {"x": 2.5726834999999997 , "y": 44.35595800000001},
                        "2": {"x": 2.5758355 , "y": 44.358561},
                        "3": {"x": 2.5737413 , "y": 44.3539443},
                        "4": {"x": 2.7227829999999997 , "y": 44.4712353}
                    }
                }
                """;
        mockMvc.perform(post(ROUTE_ITINERAIRE)
                        .header("Authorization", "Bearer "+con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itineraire))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @Order(17)
    void creerItineraireSansNomShouldFail() throws Exception{
        ajouterClient();
        String itineraire =
                """
                {
                    "domicile": {"x": 2.575986 , "y": 44.349388999999995},
                    "listePoint": {
                        "1": {"x": 2.5726834999999997 , "y": 44.35595800000001},
                        "2": {"x": 2.5758355 , "y": 44.358561},
                        "3": {"x": 2.5737413 , "y": 44.3539443},
                        "4": {"x": 2.7227829999999997 , "y": 44.4712353}
                    }
                }
                """;
        mockMvc.perform(post(ROUTE_ITINERAIRE)
                        .header("Authorization", "Bearer "+con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itineraire))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("erreur.nomItineraire").value("L'itinéraire doit avoir un nom"));
    }

    @Test
    @Order(18)
    void creerItineraireSansDomicileShouldFail() throws Exception{
        ajouterClient();
        String itineraire =
                """
                {
                    "nomItineraire": "test",
                    "listePoint": {
                        "1": {"x": 2.5726834999999997 , "y": 44.35595800000001},
                        "2": {"x": 2.5758355 , "y": 44.358561},
                        "3": {"x": 2.5737413 , "y": 44.3539443},
                        "4": {"x": 2.7227829999999997 , "y": 44.4712353}
                    }
                }
                """;
        mockMvc.perform(post(ROUTE_ITINERAIRE)
                        .header("Authorization", "Bearer "+con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itineraire))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("erreur.domicile").value("Le domicile doit être renseigné"));
    }

    @Test
    @Order(19)
    void creerItineraireSansListePointShouldFail() throws Exception {
        ajouterClient();
        String itineraire =
                """
                        {
                            "nomItineraire": "test",
                            "domicile": {"x": 2.575986 , "y": 44.349388999999995}
                        }
                        """;
        mockMvc.perform(post(ROUTE_ITINERAIRE)
                        .header("Authorization", "Bearer " + con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itineraire))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @Order(20)
    void calculerItineraire() throws Exception{
        mockMvc.perform(post(ROUTE_ITINERAIRE+"/calculer")
                    .header("Authorization", "Bearer "+con.get("token"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ITINERAIRE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("itineraire.[0].id").value("-1"))
                .andExpect(jsonPath("itineraire.[1].id").value("3"))
                .andExpect(jsonPath("itineraire.[2].id").value("1"))
                .andExpect(jsonPath("itineraire.[3].id").value("2"))
                .andExpect(jsonPath("itineraire.[4].id").value("4"))
                .andExpect(jsonPath("itineraire.[5].id").value("-2"));
    }

    @Test
    @Order(60)
    void SupprimerUtilisateurTest() throws Exception{
        MvcResult result = mockMvc.perform(get(BASE_API_UTILISATEUR +"connexion")
                        .param("mail", "testetestetest@gmail.com")
                        .param("motDePasse","Test1234@"))
                .andExpect(status().isOk()).andReturn();

        String reponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        con = objectMapper.readValue(reponse, new TypeReference<>() {});

        mockMvc.perform(delete("/api/utilisateur")
                        .header("Authorization","Bearer " + con.get("token")))
                .andExpect(status().isOk());
    }

    private void creationClientAModifier() throws Exception {
        MvcResult result = mockMvc.perform(post(ROUTE_API).header("Authorization", "Bearer " + con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CLIENT_AVANT_MODIF))
                .andExpect(status().isCreated())
                .andReturn();

        JSONObject client = new JSONObject(result.getResponse().getContentAsString());

        id = client.optLong("id");
    }

    private void ajouterClient() throws Exception {
        mockMvc.perform(post(ROUTE_API).header("Authorization", "Bearer " + con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CLIENT_ITINERAIRE_1))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(post(ROUTE_API).header("Authorization", "Bearer " + con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CLIENT_ITINERAIRE_2))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(post(ROUTE_API).header("Authorization", "Bearer " + con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CLIENT_ITINERAIRE_3))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(post(ROUTE_API).header("Authorization", "Bearer " + con.get("token"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CLIENT_ITINERAIRE_4))
                .andExpect(status().isCreated())
                .andReturn();
    }
}