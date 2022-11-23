
package domain.exceptions;

/**
 * Exception class to handle User Not Found
 * @author Alejandro Amat
 */
public class UserNotFound extends Exception
{
    public UserNotFound(String user)
    {
        super("The user " + user + " does not exist.");
    }
}


