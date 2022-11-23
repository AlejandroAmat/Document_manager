package domain.controllers;

import domain.classes.Document;
import domain.exceptions.InvalidExpression;
import domain.exceptions.*;
import domain.utils.Pair;

import java.util.LinkedList;

import static domain.utils.Phraser.getPhrases;
import static domain.utils.Phraser.mergePhrases;


public class DomainCtrl {
    private final UserCtrl UserCtrl;
    private final HistoryCtrl HistoryCtrl;

    // Creadora

    /**
     * Constructora default del driver de domini. Inicializa les estructures necesaries.
     */
    public DomainCtrl() {
        this.UserCtrl = new UserCtrl();
        this.HistoryCtrl = new HistoryCtrl();
    }

    // ----------USUARIOS----------
    /**
     * Registra un nou usuari al sistema
     * @param username Nom de l'usuari a registrar al sistema.
     * @param password Contrasenya per a validar l'usuari al sistema.
     * @throws UserFormat, UserExists
     */
    public void signUp (String username, String password) throws UserFormat, UserExists {
        UserCtrl.signUp(username, password);
    }

    /**
     * Fa login de l'usuari especificat
     * @param username Nom de l'usuari a entrar al sistema.
     * @param password Contrasenya associada amb l'usuari proporcionat.
     * @throws UserNotFound, UserConnected, PasswordIncorrect
     */
    public void logIn (String username, String password) throws UserNotFound, UserConnected, PasswordIncorrect {
        UserCtrl.logIn(username, password);
    }

    /**
     * Fa logOut de l'usuari actualment dins del sistema
     * @throws UserNotFound, UserConnected
     */
    public void logOut () throws UserNotFound, UserConnected {
        UserCtrl.logOut();
    }

    /**
     * Esborra l'usuari especificat del sistema.
     * @param username Nom de l'usuari a esborrar.
     * @throws UserNotFound
     */
    public void delete (String username) throws UserNotFound {
        UserCtrl.delete(username);
    }

    /**
     * Canvia el username de l'usuari especificat
     * @param oldUsername Nom d'usuari abans del canvi.
     * @param newUsername Nou nom d'usuari que s'aplicarà després del canvi.
     * @param password Contrasenya de l'usuari a canviar.
     * @throws UserFormat, UserNotFound, PasswordIncorrect, UserExists
     */
    public void changeUsername (String oldUsername, String newUsername, String password) throws UserFormat, UserNotFound, PasswordIncorrect, UserExists {
        UserCtrl.changeUsername(oldUsername, newUsername, password);
    }

    /**
     * Canvia la contrasenya de l'usuari especificat.
     * @param username Nom d'usuari sobre el qué realitzar el canvi.
     * @param oldPassword Contrasenya prèvia al canvi.
     * @param newPassword Nova contrasenya posterior al canvi.
     * @throws UserFormat, UserNotFound, PasswordIncorrect
     */
    public void changePassword (String username, String oldPassword, String newPassword) throws UserFormat, UserNotFound, PasswordIncorrect {
        UserCtrl.changePassword(username, oldPassword, newPassword);
    }


    // ----------DOCUMENTOS----------
    /**
     * Afegim un nou document al sistema
     * @param author Autor del nou document.
     * @param title Títol del nou document.
     * @param content Contingut del nou document.
     * @param format Format del nou document. Pot ser "txt", "xml", o "prop"
     * @return True o false, segons s'hagi pogut afegir amb exit o no.
     * @throws InvalidDocumentFormat
     * @throws UserNotLogged
     */
    public boolean addDocument (String author, String title, String content, String format) throws InvalidDocumentFormat, UserNotLogged {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        return DocumentCtrl.add(author, title, getPhrases(content), format);
    }

    /**
     * Esborra el document especificat del sistema.
     * @param author Autor del document a esborrar.
     * @param title Títol del document a esborrar.
     * @throws DocumentNotFound, UserNotLogged
     */
    public void removeDocument (String author, String title) throws DocumentNotFound, UserNotLogged {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        DocumentCtrl.remove(author,title);
    }

    /**
     * Canvia l'autor d'un document.
     * @param author Autor anterior al canvi.
     * @param title Títol del document a canviar.
     * @param newAuthor Nou autor de qui serà aquest document.
     * @return True o false, segons si la operació ha sigut exitosa o no.
     * @throws DocumentNotFound, UserNotLogged
     */
    public boolean changeDocAuthor(String author, String title, String newAuthor) throws DocumentNotFound, UserNotLogged {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        return DocumentCtrl.changeAuthor(author, title, newAuthor);
    }

    /**
     * Modifica el títol del document especificat.
     * @param author Autor del document a modificar.
     * @param title Títol antic previ al canvi.
     * @param newTitle Nou títol a aplicar després del canvi.
     * @return True o false, segons si l'operació ha sigut exitosa o no.
     * @throws DocumentNotFound, UserNotLogged
     */
    public boolean changeDocTitle(String author, String title, String newTitle) throws DocumentNotFound, UserNotLogged {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        return DocumentCtrl.changeTitle(author, title, newTitle);
    }

    /**
     * Sobreescriu el contingut del document especificat.
     * @param author Autor del document a modificar.
     * @param title Títol del document a modificar.
     * @param newContent Nou contingut del document especificay.
     * @throws InvalidDocumentFormat, DocumentNotFound, UserNotLogged
     */
    public void changeDocContent(String author, String title, String newContent) throws InvalidDocumentFormat, DocumentNotFound, UserNotLogged {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        DocumentCtrl.changeContent(author, title, getPhrases(newContent));
    }

    /**
     * Canvia el format intern del document.
     * @param author Autor del document a modificar.
     * @param title Títol del document a modificar.
     * @param newFormat Nou format a aplicar al document especificar. Pot ser: "txt", "xml", o "prop".
     * @throws InvalidDocumentFormat, UserNotLogged, DocumentNotFound
     */
    public void changeDocFormat (String author, String title, String newFormat) throws InvalidDocumentFormat, UserNotLogged, DocumentNotFound {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        DocumentCtrl.changeFormat(author, title, newFormat);
    }

    /**
     * Retorna el contingut del document especificat.
     * @param author Autor del document a veure.
     * @param title Títol del document a veure.
     * @return String amb el contingut del document especificat.
     * @throws DocumentNotFound, UserNotLogged
     */
    public String getDocumentContent (String author, String title) throws DocumentNotFound, UserNotLogged {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        return mergePhrases(DocumentCtrl.getDocument(author, title).getSentences());
    }

    /**
     * Buscar tots els títols d'un autor.
     * @param author Autor de qui es busquen els títols.
     * @return Llistat amb els títols de l'autor.
     * @throws AuthorNotFound, UserNotLogged
     */
    public LinkedList<String> getAuthorDocuments (String author) throws AuthorNotFound, UserNotLogged {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        return DocumentCtrl.getTitlesByAuthor(author);
    }

    /**
     * Buscar autors que compleixin amb un cert prefix.
     * @param prefix Prefix sobre el qual realitzar la cerca.
     * @return Llistat de noms d'autors que compleixen amb el criteri del prefix.
     * @throws AuthorNotFound, InvalidWordFormat, InvalidExpression, UserNotLogged
     */
    public LinkedList<String> searchDocumentByAuthorPrefix (String prefix) throws AuthorNotFound, InvalidWordFormat, InvalidExpression, UserNotLogged {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        return DocumentCtrl.searchByAuthorPrefix(prefix);
    }

    /**
     * Buscar k documents ordenats per similitud a un de referència.
     * @param title Títol del document de referència.
     * @param author Autor del document de referència.
     * @param k Nombre de documents més similars a trobar.
     * @param type Tipus d'estratègia de cerca. Pot prendre valors 1 o 2.
     * @return Llistat de Pairs de {Autor, Títol} dels documents ordenats de més similar a menys similar.
     * @throws InvalidWordFormat, DocumentNotFound, InvalidExpression, UserNotLogged, InvalidWeightType
     */
    public LinkedList<Pair<String,String>> searchDocumentsBySimilarity (String title, String author, int k, int type) throws InvalidWordFormat, DocumentNotFound, InvalidExpression, UserNotLogged, InvalidWeightType {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        LinkedList<Document> search = DocumentCtrl.searchBySimilarity(title, author, k, type);
        LinkedList<Pair<String, String>> result = new LinkedList<>();
        for (Document a : search) {
            result.add(new Pair<>(a.getAuthor(), a.getTitle()));
        }
        return result;
    }

    /**
     * Buscar documents per expressió booleana
     * @param statement Expressió a evaluar.
     * @return Llistat de Pairs de {Autor, Títol} dels documents que compleixen statement.
     * @throws InvalidWordFormat, InvalidExpression, UserNotLogged
     */
    public LinkedList<Pair<String, String>> searchDocumentsByBooleanStatement (String statement) throws InvalidWordFormat, InvalidExpression, UserNotLogged {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        HistoryCtrl.addStatement(statement);
        LinkedList<Document> search = DocumentCtrl.searchByBooleanStatement(statement);
        LinkedList<Pair<String, String>> result = new LinkedList<>();
        for (Document a : search) {
            result.add(new Pair<>(a.getAuthor(), a.getTitle()));
        }
        return result;
    }

    /**
     * Buscar documents més semblants a una creta query.
     * @param query Expressió a evaluar.
     * @param k Nombre de documents més similars a retornar.
     * @param type Tipus d'estratègia de cerca. Pot prendre valors 1 o 2.
     * @return Llistat de Pairs de {Autor, Títol} dels documents ordenats de més similar a menys similar.
     * @throws InvalidWordFormat, InvalidExpression, UserNotLogged, InvalidWeightType
     */
    public LinkedList<Pair<String, String>> searchDocumentsByQuery (String query, int k, int type) throws InvalidWordFormat, InvalidExpression, UserNotLogged, InvalidWeightType {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        LinkedList<Document> search = DocumentCtrl.searchByQuery(query, k, type);
        LinkedList<Pair<String, String>> result = new LinkedList<>();
        for (Document a : search) {
            result.add(new Pair<>(a.getAuthor(), a.getTitle()));
        }
        return result;
    }

    // ----------HISTORY----------
    /**
     * Consultar l'historial de cerques booleanes.
     * @return Llistat amb les cerques booleanes ordenades en el temps.
     * @throws UserNotLogged
     */
    public LinkedList<String> checkHistory () throws UserNotLogged {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        return HistoryCtrl.getHistory();
    }

    /**
     * Esborrar l'historial de cerques booleanes.
     * @throws UserNotLogged
     */
    public void clearHistory () throws UserNotLogged {
        if (UserCtrl.getActual_user() == null)
            throw new UserNotLogged();
        HistoryCtrl.clearHistory();
    }


}
