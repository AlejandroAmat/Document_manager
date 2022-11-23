package domain.exceptions;

public class InvalidWordFormat extends Exception
{
    public InvalidWordFormat(String word)
    {
        super("The word " + word + " has an invalid format. Use only a-z characters.");
    }
}
