package domain.drivers;

import domain.exceptions.InvalidExpression;
import domain.controllers.DomainCtrl;
import domain.exceptions.*;
import domain.utils.Pair;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

/**
 * Classe de Driver del Controlador de la capa de domini.
 * Inclou un runner (main) amb el que es poden executar tots els casos d'ús.
 */
public class DomainDriver {

    /**
     * Instància de controlador del domini per a fer les operacions.
     */
    DomainCtrl dc = new DomainCtrl();

    /**
     * Instància de Scanner per a rebre input de l'usuari.
     */
    Scanner sc = new Scanner(System.in);

    public static void main (String[] args) {
        DomainDriver dd = new DomainDriver();
        Scanner scanner = new Scanner(System.in);
        String comandes =
        "----USERS----\nregisterUser\nlogInUser\nlogOutUser\nmodifyUser\ndeleteUser\naddDoc\n" +
        "----DOCUMENTS----\nmodifyDoc\neraseDoc\ngetContentDoc\ngetDocsAuthor\ngetDocsSimilar\ngetDocsBoolean\ngetDocsPrefix\ngetDocsQuery\n" +
        "----HISTORY----\ncheckHistory\neraseHistory\n" +
        "----SORTIR----\nexit";

        System.out.println("Driver de la capa de Domini\nLes comandes possibles estan especificades al fitxer comandes del directori DOCS d'aquest projecte.");
        System.out.println(comandes);
        System.out.println("Introdueix la següent comanda:");
        String cmd = scanner.nextLine().toLowerCase(Locale.ENGLISH);
        while(!cmd.equals("exit")) {
            switch (cmd) {
                // ----USERS----
                case "registeruser": dd.registerNewUser(); break;
                case "loginuser": dd.loginUser(); break;
                case "logoutuser": dd.logOutUser(); break;
                case "modifyuser": dd.modifyAccountDetails(); break;
                case "deleteuser": dd.deleteUser(); break;
                // ----DOCUMENTS----
                case "adddoc": dd.createDocument(); break;
                case "modifydoc": dd.modifyDocument(); break;
                case "erasedoc": dd.eraseDocument(); break;
                case "getcontentdoc": dd.getDocumentContent(); break;
                case "getdocsauthor": dd.getDocumentsAuthor(); break;
                case "getdocssimilar": dd.getDocumentsSimilar(); break;
                case "getdocsboolean": dd.getDocumentsBoolean(); break;
                case "getauthorsprefix": dd.getAuthorsPrefix(); break;
                case "getdocsquery" : dd.getDocumentsQuery(); break;
                // ----HISTORY----
                case "checkhistory": dd.checkHistory(); break;
                case "erasehistory": dd.eraseHistory(); break;
                // ----INVALID-COMMAND----
                default: System.out.println("Comanda no vàlida"); break;
            }
            System.out.println("Introdueix la següent comanda:");
            cmd = scanner.nextLine().toLowerCase(Locale.ENGLISH);
        }
        System.out.println("Tancant Domain driver.\n");
    }

    //----------------USUARIOS----------------
    /**
     * Funció de driver per a registrar un nou usuari.
     * Arguments de consola:
     * arg[0]: Nom d'usuari
     * arg[1]: Contrasenya
     */
    public void registerNewUser() {
        System.out.println("Introdueix a continuació els detalls del nou usuari a registrar.");
        System.out.println("Nom d'usuari: ");
        String username = sc.nextLine();
        System.out.println("Contrasenya: ");
        String password = sc.nextLine();

        try {
            dc.signUp(username, password);
        } catch (UserFormat | UserExists e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Ususari registrat correctament.");
    }
    /**
     * Funció de driver per a fer log-in d'un nou usuari.
     * Arguments:
     * arg[0]: Nom d'usuari
     * arg[1]: Contrasenya
     */
    public void loginUser() {
        System.out.println("Introdueix a continuació els detalls del nou usuari a loguinejar.");
        System.out.println("Nom d'usuari: ");
        String username = sc.nextLine();
        System.out.println("Contrasenya: ");
        String password = sc.nextLine();

        try {
            dc.logIn(username, password);
        } catch (UserNotFound | PasswordIncorrect | UserConnected e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Usuari i Contrasenya vàlides");
    }

    /**
     * Funció de driver per a fer log-out d'un nou usuari.
     * Sense arguments.
     */
    public void logOutUser() {
        try {
            dc.logOut();
        } catch (UserNotFound | UserConnected e) {
           e.printStackTrace();
           return;
        }
    }

    /**
     * Funció de driver per a fer modificar els detalls d'un usuari.
     * Podem modifcar tant el nom d'usuari com la contrasenya, especificant-ho quan ens ho demanen.
     * Arguments:
     * arg[0]: paràmetre a modificar de l'usuari (username o password).
     * arg[1]: Username anterior.
     * arg[2]: Password anterior.
     * arg[3]: Username o Password nou, en funció d'allò especificat al paràmetre arg[0].
     */
    public void modifyAccountDetails() {
        System.out.println("Que es vol modificar: username o password?");
        String par = sc.nextLine().toLowerCase(Locale.ENGLISH);;
        if (!(par.equals("username") || par.equals("password"))){
            System.out.println("Paràmetre invàlid");
            return;
        }
        System.out.println("Introdueix els detalls del compte a modificar: ");
        System.out.println("Old Username:");
        String oldUsername = sc.nextLine();
        System.out.println("Old Password:");
        String oldPassword = sc.nextLine();

        if (par.equals("username")) {
            System.out.println("New Username:");
            String newUsername = sc.nextLine();
            try {
                dc.changeUsername(oldUsername, newUsername, oldPassword);
            } catch (UserFormat | UserNotFound | PasswordIncorrect | UserExists e) {
                e.printStackTrace();
                return;
            }
        }
        else {
            System.out.println("New Password:");
            String newPassword = sc.nextLine();
            try {
                dc.changePassword(oldUsername, oldPassword, newPassword);
            } catch (UserFormat | UserNotFound | PasswordIncorrect e) {
                e.printStackTrace();
                return;
            }
        }
        System.out.println(par + " actualitzat correctament.");
    }

    /**
     * Funció de driver per a esborrar un usuari del sistema.
     * Arguments:
     * arg[0]: Nom d'usuari a esborrar
     */
    public void deleteUser() {
        System.out.println("Introdueix el username de l'usuari a esborrar:");
        String username = sc.nextLine();

        try {
            dc.delete(username);
        } catch (UserNotFound e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Usuari " + username + " esborrat correctament.");
    }

    //----------------DOCUMENTOS----------------

    /**
     * Funció de driver per a crear un nou document.
     * Arguments:
     * arg[0]: Autor
     * arg[1]: Títol
     * arg[2]: Contingut
     * arg[3]: Format del document (pot ser "txt", "xml", "prop")
     */
    public void createDocument () {
        System.out.println("Introdueix els detalls del document a crear:");
        System.out.println("Autor:");
        String author = sc.nextLine();
        System.out.println("Títol:");
        String title = sc.nextLine();
        System.out.println("Contingut:");
        String content = sc.nextLine();
        System.out.println("Format:");
        String format = sc.nextLine();

        try{
            dc.addDocument(author, title, content, format);
        } catch (InvalidDocumentFormat | UserNotLogged e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Document afegit correctament.");
    }

    /**
     * Funció de driver per a modificar un document existent.
     * Arguments:
     * arg[0]: Autor
     * arg[1]: Títol
     * arg[2]: Paràmetre a modificar del document (autor, titol, contingut o format)
     * arg[3]: Valor a canviar en funció d'allò especificat al arg[2].
     */
    public void modifyDocument () {
        System.out.println("Introdueix els detalls del document a modifcar:");
        System.out.println("Autor del document a modificar:");
        String oldAuthor = sc.nextLine();
        System.out.println("Títol del document a modificar:");
        String oldTitle = sc.nextLine();
        System.out.println("Paràmetre del document a modificar (autor, titol, contingut, format):");
        String par = sc.nextLine().toLowerCase(Locale.ENGLISH);
        if (!(par.equals("autor") || par.equals("titol") || par.equals("contingut") || par.equals("format"))) {
            System.out.println("Paràmetre invàlid.");
            return;
        }
        System.out.println("Introdueix el nou " + par + ":");
        switch (par) {
            case "autor":
                String newAuthor = sc.nextLine();
                try {
                    dc.changeDocAuthor(oldAuthor, oldTitle, newAuthor);
                }
                catch (DocumentNotFound | UserNotLogged e) {
                    e.printStackTrace();
                    return;
                }
                break;
            case "titol":
                String newTitle = sc.nextLine();
                try {
                    dc.changeDocTitle(oldAuthor, oldTitle, newTitle);
                }
                catch (DocumentNotFound | UserNotLogged e) {
                    e.printStackTrace();
                    return;
                }
                break;
            case "contingut":
                String newContent = sc.nextLine();
                try {
                    dc.changeDocContent(oldAuthor, oldTitle, newContent);
                }
                catch (InvalidDocumentFormat | DocumentNotFound | UserNotLogged e) {
                    e.printStackTrace();
                    return;
                }
                break;
            case "format":
                String newFormat = sc.nextLine();
                try {
                    dc.changeDocFormat(oldAuthor, oldTitle, newFormat);
                }
                catch (InvalidDocumentFormat | UserNotLogged | DocumentNotFound e) {
                    e.printStackTrace();
                    return;
                }
                break;
        }
        System.out.println(par + " correctament modifcat");

    }

    /**
     * Funció de driver per a esborrar un document existent.
     * Arguments:
     * arg[0]: Autor
     * arg[1]: Títol
     */
    public void eraseDocument () {
        System.out.println("Introdueix els detalls del document a esborrar:");
        System.out.println("Autor:");
        String author = sc.nextLine();
        System.out.println("Títol:");
        String title = sc.nextLine();
        try {
            dc.removeDocument(author, title);
        }
        catch (DocumentNotFound | UserNotLogged e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Document esborrat amb éxit.");
    }

    /**
     * Funció de driver per a mostrar per terminal el contingut d'un document.
     * Arguments:
     * arg[0]: Autor
     * arg[1]: Títol
     */
    public void getDocumentContent() {
        System.out.println("Introdueix els detalls del document a consultar el contingut:");
        System.out.println("Autor:");
        String author = sc.nextLine();
        System.out.println("Títol:");
        String title = sc.nextLine();
        String result = "";
        try {
            result = dc.getDocumentContent(author, title);
        }
        catch (DocumentNotFound | UserNotLogged e) {
            e.printStackTrace();
            return;
        }
        System.out.println(result);
    }

    /**
     * Funció de driver per a mostrar per terminal els documents d'un autor.
     * Arguments:
     * arg[0]: Autor
     */
    public void getDocumentsAuthor () {
        System.out.println("Introdueix l'autor/a de qui busquem els Documents:");
        String author = sc.nextLine();
        LinkedList<String> result;
        try{
            result = dc.getAuthorDocuments(author);
        }
        catch (AuthorNotFound | UserNotLogged e) {
            e.printStackTrace();
            return;
        }
        int i = 1;
        for (String a : result)
            System.out.println("Document " + i++ + ": " + a);
    }

    /**
     * Funció de driver per a mostrar per terminal un llistat dels k documents més similars a un referent.
     * Arguments:
     * arg[0]: Autor del referent
     * arg[1]: Títol del referent
     * arg[2]: Nombre de documents més similars a buscar (llarg màxim del llistat).
     * arg[3]: Xifra (1 o 2) en funció del tipus de criteri de cerca es vulgui aplicar. Modifica la estratégia d'assignació de pessos.
     */
    public void getDocumentsSimilar () {
        System.out.println("Introdueix els detalls del document a trobar similis:");
        System.out.println("Autor");
        String author = sc.nextLine();
        System.out.println("Títol:");
        String title = sc.nextLine();
        System.out.println("Número de documents més semblants:");
        int k = Integer.parseInt(sc.nextLine());
        System.out.println("Criteri d'assignació de pesos (1 o 2, en altre cas 2 és default):");
        int type = Integer.parseInt(sc.nextLine());
        int i = 0;
        LinkedList<Pair<String,String>> result = new LinkedList<>();
        try{
            result = dc.searchDocumentsBySimilarity(author, title, k, type);
        }
        catch (InvalidWordFormat | InvalidExpression | DocumentNotFound | UserNotLogged | InvalidWeightType e) {
            e.printStackTrace();
            return;
        }
        for (Pair<String,String> a : result)
            System.out.println("Document " + i++ + ": " + a.getFirst() + " " + a.getSecond());
    }

    /**
     * Funció de driver per a mostrar per terminal un llistat de documents del sistema que compleixen un cert statement Booleà.
     * Arguments:
     * arg[0]: Boolean statement a evaluar.
     */
    public void getDocumentsBoolean () {
        System.out.println("Introdueix el boolean statement a evaluar");
        String statement = sc.nextLine();
        LinkedList<Pair<String,String>> result = new LinkedList<>();
        try {
            result = dc.searchDocumentsByBooleanStatement(statement);
        } catch (InvalidWordFormat | InvalidExpression | UserNotLogged e) {
            e.printStackTrace();
            return;
        }
        int i = 1;
        for (Pair<String,String> a : result)
            System.out.println("Document " + i++ + ": " + a.getFirst() + " " + a.getSecond());
    }

    /**
     * Funció de driver per a mostrar per terminal un llistat de tots els autors del sistema que continguin un cert prefix.
     * Arguments:
     * arg[0]: Prefix a evaluar.
     */
    public void getAuthorsPrefix ()  {
        System.out.println("Introdueix el prefix de l'autor a buscar:");
        String prefix = sc.nextLine();
        LinkedList<String> result = new LinkedList<>();
        try {
            result = dc.searchDocumentByAuthorPrefix(prefix);
        }
        catch (AuthorNotFound | InvalidWordFormat | InvalidExpression | UserNotLogged e) {
            e.printStackTrace();
            return;
        }
        for (String a : result)
            System.out.println(a);
    }

    /**
     * Funció de driver per a mostrar per terminal un llistat dels k documents més similars a un query referent.
     * Arguments:
     * arg[0]: Query de referència per buscar similituds.
     * arg[1]: Nombre de documents més similars a buscar (llarg màxim del llistat).
     * arg[2]: Xifra (1 o 2) en funció del tipus de criteri de cerca es vulgui aplicar. Modifica la estratégia d'assignació de pessos.
     */
    public void getDocumentsQuery () {
        System.out.println("Introdueix la Query a evaluar:");
        String query = sc.nextLine();
        System.out.println("Introdueix el numero de documents a evaluar:");
        int k = Integer.parseInt(sc.nextLine());
        System.out.println("Criteri d'assignació de pesos (1 o 2, en altre cas 2 és default): ");
        int type = Integer.parseInt(sc.nextLine());

        LinkedList<Pair<String,String>> result = new LinkedList<>();
        try{
            result = dc.searchDocumentsByQuery(query, k, type);
        }
        catch (InvalidWordFormat | InvalidExpression | UserNotLogged | InvalidWeightType e) {
            e.printStackTrace();
            return;
        }
        int i = 1;
        for (Pair<String,String> a : result)
            System.out.println("Document " + i++ + ": " + a.getFirst() + " " + a.getSecond());
    }

    //----------------HISTORIAL----------------
    /**
     * Funció de driver per a mostrar per terminal els contingus de l'historial de cerques booleanes.
     * Sense arguments.
     */
    public void checkHistory () {
        LinkedList<String> result;
        try {
            result = dc.checkHistory();
        }
        catch (UserNotLogged e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Els continguts de l'Historial son els següents:");
        for (String a : result) {
            System.out.println(a);
        }
    }

    /**
     * Funció de driver per a esborrar els contingus de l'historial de cerques booleanes.
     * Sense arguments.
     */
    public void eraseHistory () {
        try {
            dc.clearHistory();
        }
        catch (UserNotLogged e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Historial de cerques esborrat!");
    }
}