
package domain.exceptions;

/**
 * Exception class to handle when password does not match user
 * @author Alejandro Amat
 */
public class PasswordIncorrect extends Exception
{
    public PasswordIncorrect(String password)
    {
        super("The password " + password + " does not correspond with the user.");
    }
}


