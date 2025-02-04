/*
 * ReponseTextuelle.java                                  3 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */
package fr.iutrodez.sae501.apicliandcollect;

/**
 * Représente une réponse textuelle qui est convertie
 * au format JSON avant d'être renvoyée au client.
 * @param message le message à renvoyer
 */
public record ReponseTextuelle(String message) {}