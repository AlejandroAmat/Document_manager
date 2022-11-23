
package domain.exceptions;

/**
 * Exception class to handle when User is already connected
 * @author Alejandro Amat
 */
public class UserConnected extends Exception
{
    public UserConnected()
    {
        super("There is already a User connected");
    }
}


