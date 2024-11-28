package fr.iutrodez.sae501.apicliandcollect.utilisateur;

/**
 * Exception levée lorsqu'une erreur d'authentification survient
 */
public class ErreurAuthentification extends RuntimeException {
    public ErreurAuthentification(String message) {
        super(message);
    }
}