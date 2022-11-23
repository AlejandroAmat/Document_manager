
package domain.exceptions;

/**
 * Excepció per a avisar que no hi ha cap usuari loguejat al sistema.
 * @author Eloi Merino
 */
public class UserNotLogged extends Exception
{
    public UserNotLogged()
    {
        super("There is no logged user. Register and LogIn first to manage Documents.");
    }
}