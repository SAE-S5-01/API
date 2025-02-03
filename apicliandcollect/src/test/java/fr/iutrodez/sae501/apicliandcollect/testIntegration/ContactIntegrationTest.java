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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContactIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final static String ROUTE_API = "/api/contact";

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

    @BeforeEach
    void setUp() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/utilisateur/connexion")
                        .param("mail","test@gmail.com")
                        .param("motDePasse","Test1234@"))
                .andReturn();

        String reponse = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        con = objectMapper.readValue(reponse, new TypeReference<>() {});
    }

    @Test
    @Order(1)
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
    @Order(2)
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
    @Order(3)
    void obtenirContactSansConnexion() throws Exception {
        mockMvc.perform(get(ROUTE_API))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(3)
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
    @Order(4)
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
    @Order(5)
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
    @Order(7)
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
    @Order(8)
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
    @Order(9)
    void supprimerUtilisateurTest() throws Exception{
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