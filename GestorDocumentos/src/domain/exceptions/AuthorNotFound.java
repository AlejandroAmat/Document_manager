
    package domain.exceptions;

    public class AuthorNotFound extends Exception
    {
        public AuthorNotFound(String author)
        {
            super("The author " + author + " does not exists.");
        }
    }


