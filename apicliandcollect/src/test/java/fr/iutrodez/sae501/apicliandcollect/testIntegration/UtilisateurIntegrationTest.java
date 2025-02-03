package fr.iutrodez.sae501.apicliandcollect.testIntegration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
class UtilisateurIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_API = "/api/utilisateur/";

    @Test
    void inscriptionTest() throws Exception{
        String utilisateur = """
                {
                    "mail": "test@gmail.com",
                    "motDePasse": "Test1234@",
                    "nom": "test",
                    "prenom": "test",
                    "adresse": "adresse test",
                    "latitude": 2.987654,
                    "longitude": 44.321654
                }
                """;

        mockMvc.perform(post(BASE_API + "inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur))
                .andExpect(status().isCreated());
    }

    @Test
    void inscriptionSansNomShouldFail() throws Exception{
        String utilisateur = """
                {
                    "mail": "test@gmail.com",
                    "motDePasse": "Test1234@",
                    "prenom": "test",
                    "adresse": "adresse test",
                    "latitude": 2.987654,
                    "longitude": 44.321654
                }
                """;

    mockMvc.perform(post(BASE_API+"inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void inscriptionSansPrenomShouldFail() throws Exception{
        String utilisateur = """
                {
                    "mail": "test@gmail.com",
                    "motDePasse": "Test1234@",
                    "nom": "test",
                    "adresse": "adresse test",
                    "latitude": 2.987654,
                    "longitude": 44.321654
                }
                """;

    mockMvc.perform(post(BASE_API+"inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur))
                .andExpect(status().is4xxClientError());
    }

    @Test
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

        mockMvc.perform(post(BASE_API + "inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void inscriptionSansAdresseShouldFail() throws Exception{
        String utilisateur = """
                {
                    "mail": "test@gmail.com",
                    "motDePasse": "Test1234@",
                    "nom": "test",
                    "prenom": "test",
                    "latitude": 2.987654,
                    "longitude": 44.321654
                }
                """;

        mockMvc.perform(post(BASE_API+"inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void inscriptionSansMotDePasseShouldFail() throws Exception{
        String utilisateur = """
                {
                    "mail": "test@gmail.com",
                    "nom": "test",
                    "prenom": "test",
                    "adresse": "adresse test",
                    "latitude": 2.987654,
                    "longitude": 44.321654
                }
                """;

        mockMvc.perform(post(BASE_API+"inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(utilisateur))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void connexionShouldFail() throws Exception {
        mockMvc.perform(get(BASE_API+"connexion")
                        .param("mail","JaneDoe@gmail.com")
                        .param("motDePasse","Mdp1234@"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void connexionTest() throws Exception {
        mockMvc.perform(get(BASE_API+"connexion")
                    .param("mail", "test@gmail.com")
                    .param("motDePasse","Test1234@"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("Utilisateur connecté avec succès"));
    }

}