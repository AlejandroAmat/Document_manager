package domain.testing;

import domain.classes.PrefixComparator;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Clase de Test para la funcionalidad de prefix comparator. Se definen los autores y los prefijos y se realiza el test de comparación.
 * @author Alejandro Amat
 */
public class PrefixComparatorTest {

    private static ArrayList<String> authors = new ArrayList<String>();
    private static ArrayList<PrefixComparator> prefixes = new ArrayList<PrefixComparator>();

    /**
     * Se definen las cadenas de caracteres a evaluar. Tanto los autores como los prefijos.
     */
    @BeforeClass
    public static void init(){

        authors.add("Juan Luis Díaz");
        authors.add("GerAlexManolo ");
        authors.add("Gerardo");
        authors.add("juanlucas");
        authors.add("");
        authors.add("pe");

        prefixes.add(new PrefixComparator("Ju"));
        prefixes.add(new PrefixComparator("juanLuis"));
        prefixes.add(new PrefixComparator("juan lu"));
        prefixes.add(new PrefixComparator("gerA"));
        prefixes.add(new PrefixComparator("Manol"));
        prefixes.add(new PrefixComparator("Ju"));
        prefixes.add(new PrefixComparator("gerar"));
        prefixes.add(new PrefixComparator(""));
        prefixes.add(new PrefixComparator("j"));
        prefixes.add(new PrefixComparator("Pepe"));

    }

    /**
     * El Test comprueba que la detección y comparación de prefijos sea correcta.
     */
    @Test
    public void compare(){

        assertTrue(prefixes.get(0).compare(authors.get(0)));
        assertFalse(prefixes.get(0).compare(authors.get(1)));
        assertFalse(prefixes.get(1).compare(authors.get(0)));
        assertTrue(prefixes.get(2).compare(authors.get(0)));
        assertFalse(prefixes.get(2).compare(authors.get(3)));
        assertTrue(prefixes.get(3).compare(authors.get(1)));
        assertTrue(prefixes.get(3).compare(authors.get(2)));
        assertFalse(prefixes.get(3).compare(authors.get(0)));
        assertFalse(prefixes.get(4).compare(authors.get(1)));
        assertTrue(prefixes.get(5).compare(authors.get(0)));
        assertTrue(prefixes.get(3).compare(authors.get(2)));
        assertTrue(prefixes.get(6).compare(authors.get(2)));
        assertFalse(prefixes.get(6).compare(authors.get(1)));

        //No Prefix
        assertTrue(prefixes.get(7).compare(authors.get(0)));
        assertTrue(prefixes.get(7).compare(authors.get(1)));
        assertTrue(prefixes.get(7).compare(authors.get(2)));
        assertTrue(prefixes.get(7).compare(authors.get(3)));

        //No Author->It returns true but in reality no Author can be empty
        assertTrue(prefixes.get(7).compare(authors.get(4)));

        assertTrue(prefixes.get(8).compare(authors.get(0)));
        assertFalse(prefixes.get(8).compare(authors.get(1)));
        assertFalse(prefixes.get(9).compare(authors.get(4)));

    }
}