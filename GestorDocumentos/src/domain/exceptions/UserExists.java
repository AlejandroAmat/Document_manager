
package domain.exceptions;

/**
 * Exception class to handle when user already exists
 * @author Alejandro Amat
 */
public class UserExists extends Exception
{
    public UserExists(String user)
    {
        super("The user " + user + " is already registered.");
    }
}


