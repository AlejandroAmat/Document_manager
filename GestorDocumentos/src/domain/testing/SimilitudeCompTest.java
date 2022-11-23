package domain.testing;

import domain.classes.Document;
import domain.classes.SimilitudeComp;
import domain.exceptions.InvalidWordFormat;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;



/**
 * Classe utilitzada per a testejar les funcionalitats de la classe domain/classes/SimilitudeComp.
 * Tenim un total de 16 tests, 8 per cada estratègia d'assignacions de pesos.
 * Usem la llibreria Mockito per a simular el comportament de la classe Document en els casos que necesitem.
 */
public class SimilitudeCompTest {

    /**
     * Vector de referència per a testejar la constructora amb aquest paràmetre.
     */
    public static HashMap<String, Float> vecRef;

    /**
     * Mock de Document de referència per a testejar la constructora amb aquest paràmetre.
     */
    @Mock
    public static Document dRef = Mockito.mock(Document.class);

    /**
     * Mocks de 4 Documents per a comparar amb les referències.
     * La seva inicialització es fa a @BeforeClass init()
     *  - d0: document idèntic al referent.
     *  - d1: document amb 1 paraula sobre 4 de diferència sobre el referent.
     *  - d2: document amb 2 paraules sobre 4 de diferència sobre el referent.
     *  - d3: document totalment diferent del referent.
     */
    @Mock
    public static Document d0 = Mockito.mock(Document.class);
    @Mock
    public static Document d1 = Mockito.mock(Document.class);
    @Mock
    public static Document d2 = Mockito.mock(Document.class);
    @Mock
    public static Document d3 = Mockito.mock(Document.class);

    /**
     * Llistat on guardem els diferents documents per a parametritzar els tests.
     */
    private static LinkedList<Document> testDocs;

    /**
     * Mètode executat abans de tots els tests per a inicialitzar les estructures de dades necesaries.
     * Donem també valors de retorn a les diferents casuístiques amb les quals es trobaran els Mocks.
     * @throws InvalidWordFormat
     */
    @BeforeClass
    public static void init() throws InvalidWordFormat {

        String[] cRef = {"documento", "referencia", "supercaliragilisticoespialidoso", "prop"};
        Float[] tfRef = {1f, 1f, 1f, 1f};
        Float dRefN = 4f;
        //Vector referencia
        vecRef = new HashMap<>();
        for (int i = 0; i < cRef.length; ++i) {
            vecRef.put(cRef[i], tfRef[i]/dRefN);
        }
        // Documento referencia
        when(dRef.getWords()).thenReturn(cRef);
        for (int i = 0; i < cRef.length; ++i) {
            when(dRef.getWeight1(cRef[i])).thenReturn(tfRef[i]/dRefN);
            when(dRef.getWeight2(cRef[i])).thenReturn(tfRef[i]);
        }
        when(dRef.getTitle()).thenReturn("Documento de referencia.");

        // Docmuento 100% similar (cosSim = 1f)
        String[] c1 = {"documento","referencia", "supercaliragilisticoespialidoso", "prop"};
        Float[] tf1 = {1f, 1f, 1f, 1f};
        Float d1N = 4f;
        when(d0.getWords()).thenReturn(c1);
        for (int i = 0; i < c1.length; ++i) {
            when(d0.getWeight1(c1[i])).thenReturn(tf1[i]/d1N);
            when(d0.getWeight2(c1[i])).thenReturn(tf1[i]);
        }
        when(d0.getTitle()).thenReturn("Documento 1.");

        //Documento muy similar
        String[] c2 = {"documento","referencia", "supercaliragilisticoespialidoso", "prop"};
        Float[] tf2 = {1f, 1f, 1f, 0f};
        Float d2N = 1f;
        when(d1.getWords()).thenReturn(c2);
        for (int i = 0; i < c2.length; ++i) {
            when(d1.getWeight1(c2[i])).thenReturn(tf2[i]/d2N);
            when(d1.getWeight2(c2[i])).thenReturn(tf2[i]);
        }
        when(d1.getTitle()).thenReturn("Documento 2.");

        //Documento poco similar
        String[] c3 = {"documento","referencia", "supercaliragilisticoespialidoso", "prop"};
        Float[] tf3 = {1f, 1f, 0f, 0f};
        Float d3N = 1f;
        when(d2.getWords()).thenReturn(c3);
        for (int i = 0; i < c3.length; ++i) {
            when(d2.getWeight1(c3[i])).thenReturn(tf3[i]/d3N);
            when(d2.getWeight2(c3[i])).thenReturn(tf3[i]);
        }
        when(d2.getTitle()).thenReturn("Documento 3.");

        //Documento sin similitud alguna (cosSim = 0f)
        String[] c4 = {"aaaaaa", "bbbbb", "ccccc", "uau", "kjaskjdjaksd", "añlskdjfka"};
        Float[] tf4 = {8f, 9f, 1f, 2f, 7f, 10f};
        Float d4N = 37f;
        when(d3.getWords()).thenReturn(c4);
        for (int i = 0; i < c4.length; ++i) {
            when(d3.getWeight1(c4[i])).thenReturn(tf4[i]/d4N);
            when(d3.getWeight1(c4[i])).thenReturn(tf4[i]);
        }
        when(d3.getTitle()).thenReturn("Documento 4.");

        testDocs = new LinkedList<>();
        testDocs.add(d0);
        testDocs.add(d1);
        testDocs.add(d2);
        testDocs.add(d3);
    }

    /**
     * Test general d'ordenació per similitud amb referència de document i estratègia d'assignació de pesos 1.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestGeneralDocRefWT1() throws InvalidWordFormat {
        SimilitudeComp simComp = new SimilitudeComp(dRef, 2, 1);
        simComp.compare(testDocs.get(0));
        simComp.compare(testDocs.get(1));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }
        assertEquals(d0, result.get(0));
        assertEquals(d1, result.get(1));
    }

    /**
     * Test general d'ordenació per similitud amb referència de document i estratègia d'assignació de pesos 2.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestGeneralDocRefWT2() throws InvalidWordFormat {
        SimilitudeComp simComp = new SimilitudeComp(dRef, 2, 2);
        simComp.compare(testDocs.get(0));
        simComp.compare(testDocs.get(1));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }
        assertEquals(d0, result.get(0));
        assertEquals(d1, result.get(1));
    }

    /**
     * Test general d'ordenació per similitud amb referència de vector i estratègia d'assignació de pesos 1.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestGeneralVecRefWT1() throws InvalidWordFormat {
        int k = 2;
        SimilitudeComp simComp = new SimilitudeComp(vecRef, k, 1);
        simComp.compare(testDocs.get(0));
        simComp.compare(testDocs.get(1));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }
        assertEquals(k, result.size());
        assertEquals(d0, result.get(0));
        assertEquals(d1, result.get(1));
    }

    /**
     * Test general d'ordenació per similitud amb referència de vector i estratègia d'assignació de pesos 2.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestGeneralVecRefWT2() throws InvalidWordFormat {
        int k = 2;
        SimilitudeComp simComp = new SimilitudeComp(vecRef, k, 2);
        simComp.compare(testDocs.get(0));
        simComp.compare(testDocs.get(1));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }
        assertEquals(k, result.size());
        assertEquals(d0, result.get(0));
        assertEquals(d1, result.get(1));
    }

    /**
     * Test d'ordenació de documents sense cap mena de similitud amb referència de document i estratègia d'assignació de pesos 1.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestDocumentoSinSimilitudDocRefWT1() throws InvalidWordFormat {
        int k = 2;
        SimilitudeComp simComp = new SimilitudeComp(dRef, k, 1);
        simComp.compare(testDocs.get(0));
        simComp.compare(testDocs.get(3));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }

        assertEquals(k, result.size());
        assertEquals(d0, result.get(0));
        assertEquals(d3, result.get(1));
    }

    /**
     * Test d'ordenació de documents sense cap mena de similitud amb referència de document i estratègia d'assignació de pesos 2.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestDocumentoSinSimilitudDocRefWT2() throws InvalidWordFormat {
        int k = 2;
        SimilitudeComp simComp = new SimilitudeComp(dRef, k, 2);
        simComp.compare(testDocs.get(0));
        simComp.compare(testDocs.get(3));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }

        assertEquals(k, result.size());
        assertEquals(d0, result.get(0));
        assertEquals(d3, result.get(1));
    }

    /**
     * Test d'ordenació de documents sense cap mena de similitud amb referència de vector i estratègia d'assignació de pesos 1.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestDocumentoSinSimilitudVecRefWT1() throws InvalidWordFormat {
        int k = 2;
        SimilitudeComp simComp = new SimilitudeComp(vecRef, k, 1);
        simComp.compare(testDocs.get(0));
        simComp.compare(testDocs.get(3));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }

        assertEquals(k, result.size());
        assertEquals(d0, result.get(0));
        assertEquals(d3, result.get(1));
    }

    /**
     * Test d'ordenació de documents sense cap mena de similitud amb referència de vector i estratègia d'assignació de pesos 2.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestDocumentoSinSimilitudVecRefWT2() throws InvalidWordFormat {
        int k = 2;
        SimilitudeComp simComp = new SimilitudeComp(vecRef, k, 2);
        simComp.compare(testDocs.get(0));
        simComp.compare(testDocs.get(3));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }

        assertEquals(k, result.size());
        assertEquals(d0, result.get(0));
        assertEquals(d3, result.get(1));
    }

    /**
     * Test d'ordenació de documents amb el paràmetre k < #comparacions.
     * Amb referència de document i estratègia d'assignació de pesos 1.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestKMenorQueCompsDocRefWT1() throws InvalidWordFormat {
        //La k será menor que el número de comparaciones.
        int k = 1;
        SimilitudeComp simComp = new SimilitudeComp(dRef, k, 1);
        simComp.compare(testDocs.get(0));
        simComp.compare(testDocs.get(1));
        simComp.compare(testDocs.get(2));
        simComp.compare(testDocs.get(3));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }

        assertEquals(k, result.size());
        assertEquals(d0, result.get(0));
    }

    /**
     * Test d'ordenació de documents amb el paràmetre k < #comparacions.
     * Amb referència de document i estratègia d'assignació de pesos 2.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestKMenorQueCompsDocRefWT2() throws InvalidWordFormat {
        //La k será menor que el número de comparaciones.
        int k = 1;
        SimilitudeComp simComp = new SimilitudeComp(dRef, k, 2);
        simComp.compare(testDocs.get(0));
        simComp.compare(testDocs.get(1));
        simComp.compare(testDocs.get(2));
        simComp.compare(testDocs.get(3));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }

        assertEquals(k, result.size());
        assertEquals(d0, result.get(0));
    }

    /**
     * Test d'ordenació de documents amb el paràmetre k > #comparacions.
     * Amb referència de document i estratègia d'assignació de pesos 1.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestKMayorQueCompsDocRefWT1() throws InvalidWordFormat {
        //La k será mayor que el número de comparaciones.
        //Por tanto, la lista que devuelve, será de size() = 1;
        int k = 4;
        SimilitudeComp simComp = new SimilitudeComp(dRef, k, 1);
        simComp.compare(testDocs.get(0));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }

        assertEquals(1, result.size());
        assertEquals(d0, result.get(0));
    }

    /**
     * Test d'ordenació de documents amb el paràmetre k > #comparacions.
     * Amb referència de document i estratègia d'assignació de pesos 2.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestKMayorQueCompsDocRefWT2() throws InvalidWordFormat {
        //La k será mayor que el número de comparaciones.
        //Por tanto, la lista que devuelve, será de size() = 1;
        int k = 4;
        SimilitudeComp simComp = new SimilitudeComp(dRef, k, 2);
        simComp.compare(testDocs.get(0));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }

        assertEquals(1, result.size());
        assertEquals(d0, result.get(0));
    }

    /**
     * Test d'ordenació de documents amb el paràmetre k < #comparacions.
     * Amb referència de vector i estratègia d'assignació de pesos 1.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestKMenorQueCompsVecRefWT1() throws InvalidWordFormat {
        //La k será menor que el número de comparaciones.
        int k = 1;
        SimilitudeComp simComp = new SimilitudeComp(vecRef, k, 1);
        simComp.compare(testDocs.get(0));
        simComp.compare(testDocs.get(1));
        simComp.compare(testDocs.get(2));
        simComp.compare(testDocs.get(3));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }

        assertEquals(k, result.size());
        assertEquals(d0, result.get(0));
    }

    /**
     * Test d'ordenació de documents amb el paràmetre k < #comparacions.
     * Amb referència de vector i estratègia d'assignació de pesos 2.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestKMenorQueCompsVecRefWT2() throws InvalidWordFormat {
        //La k será menor que el número de comparaciones.
        int k = 1;
        SimilitudeComp simComp = new SimilitudeComp(vecRef, k, 2);
        simComp.compare(testDocs.get(0));
        simComp.compare(testDocs.get(1));
        simComp.compare(testDocs.get(2));
        simComp.compare(testDocs.get(3));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }

        assertEquals(k, result.size());
        assertEquals(d0, result.get(0));
    }

    /**
     * Test d'ordenació de documents amb el paràmetre k > #comparacions.
     * Amb referència de vector i estratègia d'assignació de pesos 1.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestKMayorQueCompsVecRefWT1() throws InvalidWordFormat {
        //La k será mayor que el número de comparaciones.
        //Por tanto, la lista que devuelve, será de size() = 1;
        int k = 4;
        SimilitudeComp simComp = new SimilitudeComp(vecRef, k, 1);
        simComp.compare(testDocs.get(0));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }

        assertEquals(1, result.size());
        assertEquals(d0, result.get(0));
    }

    /**
     * Test d'ordenació de documents amb el paràmetre k > #comparacions.
     * Amb referència de vector i estratègia d'assignació de pesos 2.
     * @throws InvalidWordFormat
     */
    @Test
    public void TestKMayorQueCompsVecRefWT2() throws InvalidWordFormat {
        //La k será mayor que el número de comparaciones.
        //Por tanto, la lista que devuelve, será de size() = 1;
        int k = 4;
        SimilitudeComp simComp = new SimilitudeComp(vecRef, k, 2);
        simComp.compare(testDocs.get(0));
        LinkedList<Document> result = simComp.getList();
        for (Document a : result) {
            System.out.println(a.getTitle());
        }

        assertEquals(1, result.size());
        assertEquals(d0, result.get(0));
    }
}
