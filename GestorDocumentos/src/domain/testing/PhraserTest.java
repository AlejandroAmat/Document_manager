package domain.testing;
import org.junit.*;

import java.util.LinkedList;

import static domain.utils.Phraser.getPhrases;
import static domain.utils.Phraser.mergePhrases;
import static org.junit.Assert.assertEquals;

/**
 * Classe de test de la classe domain/utils/Phraser. Testejem les diferents capacitats i en descobrim les possibles febleses.
 * En total disposem de 15 tests:
 *  - 14 de la funció estàtica getPhrases(). El seu funcionament és més complex i la casuística més amplia. Cal testejar-la a fons.
 *  - 1 de la funció estàtica mergePhrases(). El seu funcionament és senzill i trivial. Amb comprovar que funciona, és suficient.
 */
public class PhraserTest {
    /**
     * Test general on proven les capacitats de getPhraser().
     */
    @Test
    public void TestGeneral() {
        String c = "Esta es una prueba general de las capacidades de InputFormatCtrl. En caso que sea correcto, esto debería devolver 3 frases! Esta es la tercera frase?";
        int expectedLines = 3;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("Esta es una prueba general de las capacidades de InputFormatCtrl. ");
        expectedList.add("En caso que sea correcto, esto debería devolver 3 frases! ");
        expectedList.add("Esta es la tercera frase?");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test per a comprovar que el canvi de majúscules i minúscules al principi i final de frases no afecta el comportament del mètode.
     */
    @Test
    public void TestMayusculasMinusculas() {
        String c = "CoMpRoBaMoS QuE eL CaMbIo De MaYuScUlAs y MiNuScCULAS. NO LE AFECTA. o si?";
        int expectedLines = 3;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("CoMpRoBaMoS QuE eL CaMbIo De MaYuScUlAs y MiNuScCULAS. ");
        expectedList.add("NO LE AFECTA. ");
        expectedList.add("o si?");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test de diferents caràcters poc comuns per a provar si influeixen en les frases que retorna.
     */
    @Test
    public void TestTildesICarateresPocoComunes () {
        String c = "Ahora miramos si soportamos las tildes. Esdrújula. Francés. Bàsquet. Optimització. Facultat d'Informàtica de Barcelona. àèìòùÀÈÌÒÙ, áéíóúÁÉÍÓÚ, äëïöüÄËÏÖÜ, `´¨'_-{}[]()<>ç+*^.";
        int expectedLines = 7;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("Ahora miramos si soportamos las tildes. ");
        expectedList.add("Esdrújula. ");
        expectedList.add("Francés. ");
        expectedList.add("Bàsquet. ");
        expectedList.add("Optimització. ");
        expectedList.add("Facultat d'Informàtica de Barcelona. ");
        expectedList.add("àèìòùÀÈÌÒÙ, áéíóúÁÉÍÓÚ, äëïöüÄËÏÖÜ, `´¨'_-{}[]()<>ç+*^.");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test per veure si els caràcters especials com \n afecten el funcionament.
     */
    @Test
    public void TestNextLine() {
        String c = "Hola\n Esto es una prueba.";
        int expectedLines = 1;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("Hola\n Esto es una prueba.");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test que comprova el correcte funcionament quan la string d'entrada no porta punt i final.
     */
    @Test
    public void TestNoPuntoFinal () {
        String c = "Que ocure si ponemos la ultima frase sin punto final? Esto ocurre: que la frase se sigue detectando entera";
        int expectedLines = 2;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("Que ocure si ponemos la ultima frase sin punto final? ");
        expectedList.add("Esto ocurre: que la frase se sigue detectando entera");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test que comprova si els espais posteriors a un end of line els inclou a la frase anterior correctament.
     */
    @Test
    public void TestMultiplesEspaciosTrasFrase () {
        String c = "Muchos espacios.         Muchisimos    .                    ";
        int expectedLines = 2;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("Muchos espacios.         ");
        expectedList.add("Muchisimos    .                    ");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test que prova com separa múltiples punts
     */
    @Test
    public void TestPuntosSuspensivos () {
        String c = "Pero.. Y los puntos suspensivos... Pasa algo..........";
        int expectedLines = 3;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("Pero.. ");
        expectedList.add("Y los puntos suspensivos... ");
        expectedList.add("Pasa algo..........");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test que prova com separa múltiples interrogants
     */
    @Test
    public void TestInterrogantes () {
        String c = "Pero?? Y los interrogantes??? Pasa algo???????????";
        int expectedLines = 3;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("Pero?? ");
        expectedList.add("Y los interrogantes??? ");
        expectedList.add("Pasa algo???????????");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test que prova com separa múltiples exclamacions
     */
    @Test
    public void TestExclamaciones () {
        String c = "Pero!! Y las exclamaciones!!! Pasa algo!!!!!!!!!!!";
        int expectedLines = 3;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("Pero!! ");
        expectedList.add("Y las exclamaciones!!! ");
        expectedList.add("Pasa algo!!!!!!!!!!!");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test que prova com separa múltiples end of line sense espais
     */
    @Test
    public void TestEndOfPhrasesJuntos () {
        String c = "..?!.!?.!.!?.!..?!";
        int expectedLines = 1;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("..?!.!?.!.!?.!..?!");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test que prova com separa múltiples end of line amb espais
     */
    @Test
    public void TestEndOfPhraseSeparados () {
        String c = ". . ! ? ? . . ! ? ";
        int expectedLines = 9;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add(". ");
        expectedList.add(". ");
        expectedList.add("! ");
        expectedList.add("? ");
        expectedList.add("? ");
        expectedList.add(". ");
        expectedList.add(". ");
        expectedList.add("! ");
        expectedList.add("? ");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test que prova com separa múltiples end of line amb i sense espais.
     */
    @Test
    public void TestEndOfPhraseJuntosYSeparados () {
        String c = "..? !.!? .! ";
        int expectedLines = 3;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("..? ");
        expectedList.add("!.!? ");
        expectedList.add(".! ");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test que prova com separa els acrònims delimitats per punts.
     */
    @Test
    public void TestAcronimos () {
        String c = "F.C.B.";
        int expectedLines = 3;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("F.");
        expectedList.add("C.");
        expectedList.add("B.");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }

    /**
     * Test que comprova com se separen les abreviacions acabades en punt.
     */
    @Test
    public void TestAbreviaciones () {
        String c = "Dr. Chivite";
        int expectedLines = 2;
        LinkedList<String> expectedList = new LinkedList<>();
        expectedList.add("Dr. ");
        expectedList.add("Chivite");

        LinkedList<String> result = getPhrases(c);
        System.out.println(result);
        assertEquals(expectedLines, result.size(), 0);
        assertEquals(expectedList, result);
    }


    /**
     * Únic test de la funció mergePhrases(). Comprova si, donada una llista de frases, en retorna la seva concatenació.
     */
    @Test
    public void TestPhrasesMerger() {
        String expectedContent = "Hola, bon dia. Esta és una prova de la funció mergePhrases(). És una funció trivial, i no hauria de tenir gens de complicació. Per tant, fem únicament 1 sol test.";
        LinkedList<String> sentences = new LinkedList<>();
        sentences.add("Hola, bon dia. ");
        sentences.add("Esta és una prova de la funció mergePhrases(). ");
        sentences.add("És una funció trivial, i no hauria de tenir gens de complicació. ");
        sentences.add("Per tant, fem únicament 1 sol test.");

        String result = mergePhrases(sentences);
        System.out.println(result);
        assertEquals(expectedContent, result);
    }
}
