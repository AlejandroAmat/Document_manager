package domain.exceptions;

public class InvalidExpression extends Exception
{
    public InvalidExpression() {
        super("The expression introduced is not valid");
    }

    public InvalidExpression(String s) {
        super("The expression introduced is not valid: " + s);
    }
}
