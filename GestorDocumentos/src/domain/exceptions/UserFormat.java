
package domain.exceptions;

/**
 * Exception class to handle bad format in input
 * @author Alejandro Amat
 */
public class UserFormat extends Exception
{
    public UserFormat()
    {
        super("The username/password can not be empty.");
    }
}


