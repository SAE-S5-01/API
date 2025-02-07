/*
 * GroupValidationDTO.java                               04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.utilisateur;

/**
 * Différenciation des usages des annotations
 *
 * @author Lucas DESCRIAUD
 * @author Loïc FAUGIERES
 * @author Simon GUIRAUD
 */
public class GroupValidationDTO {

    /**
     * Appel d'une annotation dans le cadre de la création d'un utilisateur
     */
    public interface CreationUtilisateur {}

    /**
     * Appel d'une annotation dans le cadre de la modification d'un utilisateur
     */
    public interface ModificationUtilisateur {}
}
