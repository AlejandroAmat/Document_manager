package domain.exceptions;

public class InvalidDocumentFormat extends Exception
{
    public InvalidDocumentFormat(String invalidFormat)
    {
        super("'" + invalidFormat + "' is an invalid document format. Accepted formats are 'xml', 'txt' and 'prop'");
    }
}
