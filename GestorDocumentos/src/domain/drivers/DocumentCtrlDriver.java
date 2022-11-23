package domain.drivers;

import domain.classes.Document;
import domain.exceptions.*;
import domain.controllers.DocumentCtrl;
import domain.utils.Phraser;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Clase Driver para ejecutar las funcionalidades del controlador de Documento. Menú con distintas opciones para añadir, modificar, conectar y desconectar usuarios. Indica
 * por pantalla el resultado de las operaciones. Se ejecuta constantemente hasta que se escoge la función salir.
 * @author Alejandro Amat
 */
public class DocumentCtrlDriver{


    final String ANSI_RED = "\u001B[31m";
    final String ANSI_RESET = "\u001B[0m";
    final String ANSI_GREEN = "\u001B[32m";
    String options;
    String author;
    String title;
    String new_name;
    Scanner scanner;
    String format;
    boolean success_error;




    /**
     * Método constructor de la clase. Se inicializan las variables importantes de cara al flujo
     */
    public DocumentCtrlDriver(){
        this.scanner = new Scanner(System.in);
        this.success_error = true;
        this.options = "";
        this.author = "";
        this.title= "";
        this.new_name = "";
        this.format = "";


    }

    /**
     * método main. El programa se ejecuta desde aquí. Se tiene un bucle con las opciones de:
     * -AñadirDocumento
     * -EliminarDocumento
     * -CambiarAutor
     * -CambiarTitulo
     * -CambiarContenido
     * -CambiarFormato
     * -BuscarPorSimilitud
     * -Similitud Query
     * -Search Boolean
     * -Documentos por Autor
     * -Obtener Contenido Documento
     * -Salir del Distema
     * Se analizan los distintos errores retornados por el controlador y hay un prompt que indica constatemente qué información incluir en cada momento.
     * El driver utiliza al controlador de Documento para la gestión y cálculo de los diferentes métodos.
     * @param args
     */
    public static void main(String[] args){

        DocumentCtrlDriver control_driver = new DocumentCtrlDriver();


        while (!control_driver.options.equals("salir") ){
            System.out.println("\n  Menú Principal Driver Documento \n ------------------------------- \n " +
                    "(i)    Añadir Documento : 'addDoc' \n (ii)   Eliminar Documento : 'removeDoc' \n (iii)  Cambiar Autor : 'changeAuthor' \n (iv)   Cambiar Título : " +
                    "changeTitle'' \n " +
                    "(v)    Cambiar Contenido : 'changeContent' \n (vi)   Cambiar Formato : 'changeFormat' \n (vii)  Buscar Por Similitud : 'searchSimilarity' \n" +
                    " (viii) Buscar mediante Similitud con Query : 'searchQuery' \n (ix)   Búsqueda Booleana : 'searchBoolean' \n (x)    Obtener Documentos por Autor : '" +
                    "titlesAuthor' \n (xi)   Obtener Documento : 'getdocument'  \n (xii)  Obtener Autor por prefijo : 'authorPrefix'  \n (xiii) Salir del Sistema : 'salir'   ");
            control_driver.options = control_driver.scanner.nextLine();

            switch (control_driver.options.toLowerCase()){
                case "adddoc":
                    control_driver.AddDoc();
                    break;
                case "removedoc":
                    control_driver.DeleteDoc();
                    break;
                case "changeauthor" :
                    control_driver.ChangeAuthor();
                    break;
                case "changetitle":
                    control_driver.ChangeTitle();
                    break;
                case "changecontent":
                    control_driver.ChangeContent();
                    break;
                case "changeformat":
                    control_driver.ChangeFormat();
                    break;
                case "searchsimilarity" :
                    control_driver.SearchBySimilarity();
                    break;
                case "searchquery" :
                    control_driver.SearchByQuery();
                    break;
                case "searchboolean" :
                    control_driver.SearchByBoolean();
                    break;
                case "titlesauthor" :
                    control_driver.TitlesByAuthor();
                    break;
                case "getdocument" :
                    control_driver.GetDocument();
                    break;
                case "authorprefix":
                    control_driver.AuthorByPrefix();
                    break;
                case "salir":
                    System.out.println("Exit");
                    break;
                default : System.out.println("No existe esa opción");
            }

        }

    }
    /**
     * AddDoc (Añade un nuevo documento regido por Autor, Título, contenido y Formato
     * @throws InvalidWordFormat
     */
    public void AddDoc()  {
        try{
            System.out.println(" \n Añadir Documento \n -----------------");
            System.out.println(" \n Nombre del Autor:");
            this.author = this.scanner.nextLine();
            System.out.println(" Título del documento:");
            this.title = this.scanner.nextLine();

            LinkedList<String> sentences = new LinkedList<String>();

            System.out.println("Contenido del documento:");
            sentences = Phraser.getPhrases(this.scanner.nextLine());

            System.out.println(" Formato del documento:");
            this.format = this.scanner.nextLine();

            this.success_error = DocumentCtrl.add(this.author, this.title, sentences, this.format);
            if(this.success_error == true) System.out.println(ANSI_GREEN + "Succes" + ANSI_RESET);
            else System.out.println(ANSI_RED + "Error" + ANSI_RESET);
        }
        catch (InvalidDocumentFormat ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * Delete (elimina documento)
     */
    public void DeleteDoc(){
        try {
            System.out.println(" \n Eliminar Documento \n -----------------");
            System.out.println(" \n Nombre del Autor:");
            this.author = this.scanner.nextLine();
            System.out.println(" Título del documento a eliminar:");
            this.title = this.scanner.nextLine();

            DocumentCtrl.remove(this.author, this.title);
            System.out.println(ANSI_GREEN + "Succes" + ANSI_RESET);
        } catch (DocumentNotFound ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Cambia el Autor de un documento manteniendo su contenido, titulo y formato
     * @throws DocumentNotFound
     */
    public void ChangeAuthor()  {
        try {
            System.out.println(" \n Modificar Autor Documento \n -----------------");
            System.out.println(" \n Nombre del Autor:");
            this.author = this.scanner.nextLine();
            System.out.println(" Título del documento:");
            this.title = this.scanner.nextLine();
            System.out.println(" Nuevo nombre de Autor");
            String newAuthor = this.scanner.nextLine();

            this.success_error = DocumentCtrl.changeAuthor(this.author, this.title, newAuthor);

            if(this.success_error == true) System.out.println(ANSI_GREEN + "Succes" + ANSI_RESET);
            else System.out.println(ANSI_RED + "Error" + ANSI_RESET);
        }
        catch(DocumentNotFound ex){
            ex.printStackTrace();
        }
    }

    /**
     * Change Title- Modifica el título de un Autor
     * @throws DocumentNotFound
     */
    public void ChangeTitle()  {
        try {
            System.out.println(" \n Modificar Título Documento \n -----------------");
            System.out.println(" \n Nombre del Autor:");
            this.author = this.scanner.nextLine();
            System.out.println(" Título del documento:");
            this.title = this.scanner.nextLine();
            System.out.println(" Nuevo Título");
            String newTitle = this.scanner.nextLine();

            this.success_error = DocumentCtrl.changeTitle(this.author, this.title, newTitle);
            if(this.success_error == true) System.out.println(ANSI_GREEN + "Succes" + ANSI_RESET);
            else System.out.println(ANSI_RED + "Error" + ANSI_RESET);
        }
        catch(DocumentNotFound ex){
            ex.printStackTrace();
        }
    }

    /**
     * ChangeContent - cambia el contenido de un documento
     * @throws InvalidDocumentFormat
     */
    public void ChangeContent(){
        try {
            System.out.println(" \n Modificar Contenido Documento \n -----------------");
            System.out.println(" \n Nombre del Autor:");
            this.author = this.scanner.nextLine();
            System.out.println(" Título del documento:");
            this.title = this.scanner.nextLine();
            System.out.println(" Nuevo Contenido:");
            LinkedList<String> content = Phraser.getPhrases(this.scanner.nextLine());

            DocumentCtrl.changeContent(this.author, this.title, content);
            System.out.println(ANSI_GREEN + "Succes" + ANSI_RESET);
        }
        catch(InvalidDocumentFormat | DocumentNotFound ex){
            ex.printStackTrace();
        }
    }

    /**
     * Modifica el formato del documento dado
     * @throws InvalidDocumentFormat
     */
    public void ChangeFormat(){
        try {
            System.out.println(" \n Modificar Formato Documento \n -----------------");
            System.out.println(" \n Nombre del Autor:");
            this.author = this.scanner.nextLine();
            System.out.println(" Título del documento:");
            this.title = this.scanner.nextLine();
            System.out.println(" Nuevo Fromato:");
            String format = this.scanner.nextLine();

            DocumentCtrl.changeFormat(this.author, this.title, format);
            System.out.println(ANSI_GREEN + "Succes" + ANSI_RESET);
        }
        catch(InvalidDocumentFormat | DocumentNotFound ex){
            ex.printStackTrace();
        }
    }

    /**
     * Realiza una búsqueda por Similarididad de los k dodcumentos más parecidos (en función del contenido). Si k>numDocs, internamente se hace para que devuelva
     * numDocs. Muestra por pantalla Autor y Título
     * @throws InvalidWordFormat,DocumentNotFound, InvalidExpression
     */
    public void SearchBySimilarity(){
        try {
            System.out.println(" \n Búsqueda Por Similitud \n -----------------");
            System.out.println(" \n Nombre del Autor:");
            this.author = this.scanner.nextLine();
            System.out.println(" Título del documento:");
            this.title = this.scanner.nextLine();
            System.out.println("número de documentos más parecidos a devolver: (INT)");
            String k = this.scanner.nextLine();
            System.out.println("Criterio de comparación (1 o 2, cualquier otro devulve excepcion):");
            String type = scanner.nextLine();
            LinkedList<Document> docs=  DocumentCtrl.searchBySimilarity(this.author, this.title, Integer.parseInt(k), Integer.parseInt(type));
            for(Document doc : docs){
                System.out.println(ANSI_GREEN + "Author:" + doc.getAuthor()+ ", Title: " + doc.getTitle()+ ANSI_RESET);
            }
        }
        catch(InvalidWordFormat | DocumentNotFound | InvalidExpression | InvalidWeightType ex){
            ex.printStackTrace();
        }
    }


    /**
     * Realiza una búsqueda por Similarididad de los k dodcumentos más parecidos a la query dada. Muestra por pantalla Autor y título de cada documento.
     * @throws InvalidWordFormat,InvalidExpression
     */
    public void SearchByQuery() {
        try {
            System.out.println(" \n Búsqueda Por Similitud mediante Query \n -----------------");
            System.out.println(" \n Query:");
            String query = this.scanner.nextLine();
            System.out.println("número de documentos más parecidos a devolver: (INT)");
            String k = this.scanner.nextLine();
            System.out.println("Criterio de comparación (1 o 2, cualquier otro devulve excepcion):");
            String type = scanner.nextLine();

            LinkedList<Document> docs = DocumentCtrl.searchByQuery(query, Integer.parseInt(k), Integer.parseInt(type));
            for (Document doc : docs) {
                System.out.println(ANSI_GREEN + "Author:" + doc.getAuthor() + ", Title: " + doc.getTitle() + ANSI_RESET);
            }
        } catch (InvalidWordFormat | InvalidExpression | InvalidWeightType ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Realiza una búsqueda mediante una declaración booleana. Muestra por pantalla el título y autor de los documentos que cumplan con la expresión
     * @throws InvalidWordFormat, InvalidExpression
     */
    public void SearchByBoolean() {
        try {
            System.out.println(" \n Búsqueda con declaración Booleana \n -----------------");
            System.out.println(" \n Declaración:");
            String query = this.scanner.nextLine();

            LinkedList<Document> docs = DocumentCtrl.searchByBooleanStatement(query);
            for (Document doc : docs) {
                System.out.println(ANSI_GREEN + "Author:" + doc.getAuthor() + ", Title: " + doc.getTitle() + ANSI_RESET);
            }
        } catch (InvalidWordFormat | InvalidExpression ex) {
            ex.printStackTrace();
        }
    }

    /**
     * muestra todos los documentos (Títulos) de un autor
     * @throws AuthorNotFound
     */
    public void TitlesByAuthor() {
        try {
            System.out.println(" \n Búsqueda de Títulos de Autor \n -----------------");
            System.out.println(" \n Autor:");
            String author = this.scanner.nextLine();

            LinkedList<String> docs = DocumentCtrl.getTitlesByAuthor(author);
            System.out.println(ANSI_GREEN + "Author:" + author);
            for (String doc : docs) {
                System.out.println(" Title: " + doc);
            }
            System.out.println(ANSI_RESET);
        } catch (AuthorNotFound ex) {
            ex.printStackTrace();
            return;
        }
    }

    /**
     * Permite obtener el contenido de un documento dado un Autor y Título
     * @throws DocumentNotFound
     */
    public void GetDocument() {
        try {
            System.out.println(" \n Obtención de Documento \n -----------------");
            System.out.println(" \n Autor:");
            String author = this.scanner.nextLine();
            System.out.println(" Título del documento:");
            this.title = this.scanner.nextLine();
            String content = Phraser.mergePhrases(DocumentCtrl.getDocument(this.author, this.title).getSentences());
            System.out.println(ANSI_GREEN + "Contenido del Documento : " + content + ANSI_RESET);

        } catch (DocumentNotFound ex) {
            ex.printStackTrace();
            return;
        }
    }

    /**
     * Muestra por pantalla todos los autores que contengan un prefijo
     * @throws InvalidWordFormat,InvalidExpression
     */
    public void AuthorByPrefix() {
        try {
            System.out.println(" \n Obtención de Autores por prefijo \n -----------------");
            System.out.println(" \n Prefijo:");
            String prefix = this.scanner.nextLine();

            LinkedList<String> content = DocumentCtrl.searchByAuthorPrefix(prefix);
            for (String author : content) {
                System.out.println(ANSI_GREEN + "Author:" + author + ANSI_RESET);
            }

        } catch (InvalidWordFormat | InvalidExpression e) {
            e.printStackTrace();
        }
    }

}