package domain.testing;
import domain.classes.Content;
import domain.classes.Document;
import domain.exceptions.InvalidDocumentFormat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


public class DocumentTest {


    private static Content content = Mockito.mock(Content.class);

    private static LinkedList<String> sentences;
    private static Document doc;

    @BeforeClass
    public static void init() throws InvalidDocumentFormat
    {
        sentences = new LinkedList<String>();
        sentences.add("Hola,   qué    tal estas");
        sentences.add("a mi me gusta el azucar");
        sentences.add("Hoy hace un dia soleado");
        sentences.add("A mi me gustaria aprobar prop");
        sentences.add("Hace calor hoy.");

        doc = new Document("Arnau", "Hello", sentences, "xml");
    }

    @Test
    public void constructorTest() throws InvalidDocumentFormat
    {
        Document d1 = new Document("kbk", "kjkj", sentences, "xml");
        Document d2 = new Document("kbk", "kjkj", sentences, "prop");
        Document d3 = new Document("kbk", "kjkj", sentences, "txt");
    }

    @Test (expected = InvalidDocumentFormat.class)
    public void constructorExceptionTest() throws InvalidDocumentFormat
    {
        Document d = new Document("kbk", "kjkj", sentences, "lol");
    }

    @Test
    public void equalsTest() throws InvalidDocumentFormat
    {
        LinkedList<String> l = new LinkedList<>();
        l.add("Hello, how are you?");
        Document d = new Document("Arnau", "Hello", l, "prop");

        assertTrue(doc.equals(doc));
        assertFalse(doc.equals(l));
        assertTrue(doc.equals(d));
    }

    @Test
    public void isFormatValidTest()
    {
        assertTrue(Document.isFormatValid("xml"));
        assertTrue(Document.isFormatValid("txt"));
        assertTrue(Document.isFormatValid("prop"));
        assertFalse(Document.isFormatValid("jkdhcd"));
        assertFalse(Document.isFormatValid("Xml"));
    }

}
