package domain.testing;

import domain.utils.SHA_256;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

import java.util.ArrayList;


/**
 * Clase de Test para la función estática del Hash. Simplemente se verifica que haga la conversión a Hash mediante SHA_256 correctamente. Se calcula el
 * Hash peviamente desde una web online y se mira que coincidan.
 * @author Alejandro Amat
 */
public class SHA_256Test {

    private static ArrayList<String> sentences = new ArrayList<String>();

    /**
     * Se definen las cadenas de caracteres a evaluar
     */
    @BeforeClass
    public static void init(){

        sentences.add("FTnh@907fn1x");
        sentences.add("zcuK919k5Z*P");
        sentences.add("kVPX3!^8s9$$");
        sentences.add("sdugh3ded33e3");
        sentences.add("i3d97623683egb3e308dh38dh3d63drt");
        sentences.add("2'e*___-d3d0oj263e5faa//(%");

    }

    /**
     * El Test comprueba que las cadenas de caracteres coincidan con el valor del SHA_256 calculado.
     */
    @Test
    public void passwordToHashText() throws RuntimeException{

        assertEquals(SHA_256.passwordToHash(sentences.get(0)),"6418eaa216d6fbffa471e47d7eaee50f6fbaad5aaf2955312cffdaf0dbd5a211");
        assertEquals(SHA_256.passwordToHash(sentences.get(1)),"ecca7bf24b93e764fab9059ada5b5c65f144c4f338aab38631c355301e17e85a");
        assertEquals(SHA_256.passwordToHash(sentences.get(2)),"984b775548a5d692656d5dec1600e8f421c3dbc4d0d6cc54b754e87b9d09e9e8");
        assertEquals(SHA_256.passwordToHash(sentences.get(3)),"171fa345ad34c4e3887d2ed124a7d8d111256c7de6bb30f2850edc8ad6b459b0");
        assertEquals(SHA_256.passwordToHash(sentences.get(4)),"9484e7f667c723073a53b4b4925e9da85f72fcc7c4f7295cc81f9190a8ced56d");
        assertEquals(SHA_256.passwordToHash(sentences.get(5)),"3b071514aaa07c1b3e984e59c522c0e8eec79b642fd7cdc47673a33fec781ce3");


    }
}
