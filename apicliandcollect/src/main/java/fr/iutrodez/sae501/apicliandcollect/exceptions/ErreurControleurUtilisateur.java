package fr.iutrodez.sae501.apicliandcollect.exceptions;

/**
 * Exception levée lorsqu'une erreur d'authentification survient
 */
public class ErreurControleurUtilisateur extends RuntimeException {
    public ErreurControleurUtilisateur(String message) {
        super(message);
    }
}