package domain.exceptions;

public class InvalidWeightType extends Exception
{
    public InvalidWeightType(int actual)
    {
        super("The weight type " + actual + "is not valid. It must be 1 or 2.");
    }
}
