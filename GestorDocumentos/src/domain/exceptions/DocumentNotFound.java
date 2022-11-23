package domain.exceptions;

public class DocumentNotFound extends Exception
{
    public DocumentNotFound(String author, String title)
    {
        super("The document with author '" + author + "' and title '" + title + "' does not exist");
    }
}
