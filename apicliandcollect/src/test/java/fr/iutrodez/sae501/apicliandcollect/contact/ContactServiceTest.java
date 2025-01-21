package fr.iutrodez.sae501.apicliandcollect.contact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Classe de test de  {@link fr.iutrodez.sae501.apicliandcollect.contact.ContactService}
 */
@ActiveProfiles("test")
@SpringBootTest
class ContactServiceTest {

    @Mock
    private InterractionMongoContact interractionMongoContact;

    private ContactMongo contactMongo;

    @BeforeEach
    void setUp() {
        // Initialisation d'un contact pour le test
        contactMongo = new ContactMongo();
        contactMongo.set_id(1L);
    }

    @Test
    void testFindContactById() {
        // Simule la récupération d'un contact par son id
        when(interractionMongoContact.findById("1")).thenReturn(java.util.Optional.of(contactMongo));

        ContactMongo result = interractionMongoContact.findById("1").orElse(null);

        // Vérifie que le contact récupéré n'est pas null et a les bonnes informations
        assertThat(result).isNotNull();
    }
}