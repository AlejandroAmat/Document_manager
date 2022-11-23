package domain.testing;

import domain.classes.Content;
import domain.exceptions.InvalidWordFormat;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static java.lang.Math.*;
import static org.junit.Assert.*;


public class ContentTest {

    private static LinkedList<String> sentences;
    private static Content content;

    @BeforeClass
    public static void init()
    {
        //Words that aren't stop words:
        //hola, gusta, azucar, dia, soleado, gustaria, aprobar, prop, calor
        sentences = new LinkedList<String>();
        sentences.add("Hola,   qué    tal estas");
        sentences.add("a mi me gusta el azucar");
        sentences.add("Hoy hace un dia soleado");
        sentences.add("A mi me gustaria aprobar prop");
        sentences.add("Hace calor hoy hola.");

        content = new Content(sentences);
    }

    @Test
    public void isWordTest()
    {
        assertFalse(content.isWord("que"));
        assertFalse(content.isWord("hoy"));
        assertTrue(content.isWord("calor"));
        assertFalse(content.isWord("frio"));
        assertTrue(content.isWord("hola"));
    }

    @Test
    public void getTfTest() throws InvalidWordFormat
    {
        assertTrue(content.getTf("que") == 0f);
        assertTrue(content.getTf("hola") == (float)2/10);
        assertTrue(content.getTf("noexiste") == 0f);
        assertTrue(content.getTf("hace") == 0f);
        assertTrue(content.getTf("gustaria") == (float)1/10);
        assertTrue(content.getTf("prop") == (float)1/10);
    }

    @Test (expected = InvalidWordFormat.class)
    public void getTfExceptionTest() throws InvalidWordFormat
    {
        content.getTf("hola juan");
    }

    @Test
    public void getSentencesTest()
    {
        assertSame(sentences, content.getSentences());
    }

    @Test
    public void weightsTest() throws InvalidWordFormat
    {
        double tolerance = 0.0000001;

        content.updateWeight("hola", 0.5f);
        assertTrue(abs(content.getWeight1("hola") - ( (2/10f)*0.5 )) < tolerance);
        assertTrue(abs(content.getWeight2("hola") - ((float)1+log10(2))*0.5f ) < tolerance);

        content.updateWeight("que", 0.2f);
        assertTrue(content.getWeight1("que") == 0f);
        assertTrue(content.getWeight2("que") == 0f);

        content.updateWeight("soleado", 0.3f);
        assertTrue(abs(content.getWeight1("soleado") - (1/10f)*0.3) < tolerance);
        assertTrue(abs(content.getWeight2("soleado") - ((float)1+log10(1))*0.3f ) < tolerance);

    }

    @Test (expected = InvalidWordFormat.class)
    public void getWeightExceptionTest() throws InvalidWordFormat
    {
        content.getTf("hola juan");
    }

    @Test
    public void getWordsTest()
    {
        String[] words = content.getWords();

        assertEquals(9, words.length);

        LinkedList<String> l = new LinkedList<>(Arrays.asList(words));
        assertTrue(l.contains("hola"));
        assertTrue(l.contains("gusta"));
        assertTrue(l.contains("azucar"));
        assertTrue(l.contains("dia"));
        assertTrue(l.contains("soleado"));
        assertTrue(l.contains("gustaria"));
        assertTrue(l.contains("aprobar"));
        assertTrue(l.contains("prop"));
        assertTrue(l.contains("calor"));

    }

}