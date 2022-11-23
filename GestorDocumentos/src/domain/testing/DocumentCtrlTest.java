package domain.testing;

import domain.classes.Comparator;
import domain.classes.Content;
import domain.classes.Document;
import domain.controllers.DocumentCtrl;
import domain.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class DocumentCtrlTest {

    Document doc1, doc2;
    domain.classes.Comparator<Document> documentComparator;

    private static Document createDocumentMock(String author, String title, LinkedList<String> sentences, String format) {
        Document doc = Mockito.mock(Document.class);

        when(doc.getAuthor()).thenReturn(author);
        when(doc.getTitle()).thenReturn(title);
        when(doc.getSentences()).thenReturn(sentences);
        when(doc.getFormat()).thenReturn(format);

        setAuthorCoherence(doc);
        setTitleCoherence(doc);
        setFormatCoherence(doc);

        return doc;
    }

    private static void setAuthorCoherence(Document doc) {
        doAnswer(i -> {
            String newAuthor = i.getArgument(0);
            when(doc.getAuthor()).thenReturn(newAuthor);
            return null;
        }).when(doc).setAuthor(Mockito.anyString());
    }

    private static void setTitleCoherence(Document doc) {
        doAnswer(i -> {
            String newTitle = i.getArgument(0);
            when(doc.getTitle()).thenReturn(newTitle);
            return null;
        }).when(doc).setTitle(Mockito.anyString());
    }

    private static void setFormatCoherence(Document doc) {
        doAnswer(i -> {
            String newFormat = i.getArgument(0);
            when(doc.getFormat()).thenReturn(newFormat);
            return null;
        }).when(doc).setFormat(Mockito.anyString());
    }

    @Before
    public void init() {
        LinkedList<String> l1 = new LinkedList<>();
        l1.add("Me llamo Juan");
        doc1 = createDocumentMock("Juan", "Hola", l1, "txt");
        when(doc1.getWords()).thenReturn(new String[]{"me", "llamo", "juan"});

        LinkedList<String> l2 = new LinkedList<>();
        l2.add("Hola yo me llamo Pedro, me gusta prop");
        l2.add("Hace mucho calor.");
        doc2 = createDocumentMock("Pedro", "Hola", l2, "txt");
        when(doc2.getWords()).thenReturn(new String[]{"hola", "yo", "me", "llamo", "pedro", "gusta", "prop", "hace", "mucho", "calor"});

        DocumentCtrlForTesting.add(doc1);
        DocumentCtrlForTesting.add(doc2);
    }

    @After
    public void end() {
        DocumentCtrlForTesting.reset();
    }

    @Test
    public void addTest() {
        //Content of doc:
        //Hola que tal estás?
        //Que yo bien, gracias.

        Document doc = Mockito.mock(Document.class);

        when(doc.getAuthor()).thenReturn("Arnau");
        when(doc.getTitle()).thenReturn("Hello");
        when(doc.getWords()).thenReturn(new String[]{"hola", "que", "tal", "estas", "yo", "bien", "gracias"});

        boolean added = DocumentCtrlForTesting.add(doc);

        assertEquals(3, DocumentCtrlForTesting.getNDocs());
        assertTrue(added);

        //Let's check that DocumentCtrl.words is coherent with the document that has been added
        for (String w : doc.getWords())
            assertTrue(DocumentCtrlForTesting.documentContainsWord(w, doc));

        //Let's check if the document exists in the hashmap
        DocumentCtrlForTesting.existsDocInHashMap(doc);

        //Let's add a document that already exists
        assertFalse(DocumentCtrlForTesting.add(doc));
        assertEquals(3, DocumentCtrlForTesting.getNDocs());
        //

        when(doc.getTitle()).thenReturn("Hola2");
        assertTrue(DocumentCtrlForTesting.add(doc));
        assertEquals(4, DocumentCtrlForTesting.getNDocs());


        when(doc.getTitle()).thenReturn("Hola");
        when(doc.getAuthor()).thenReturn("Pepe");
        assertTrue(DocumentCtrlForTesting.add(doc));
        assertEquals(5, DocumentCtrlForTesting.getNDocs());
    }

    @Test
    public void removeTest1() throws DocumentNotFound
    {
        assertEquals(2, DocumentCtrlForTesting.getNDocs());
        assertEquals(2, DocumentCtrlForTesting.getDf("me"));
        assertEquals(1, DocumentCtrlForTesting.getDf("juan"));
        assertEquals(1, DocumentCtrlForTesting.getDf("yo"));

        DocumentCtrl.remove("Juan", "Hola");
        assertEquals(1, DocumentCtrlForTesting.getNDocs());
        assertEquals(0, DocumentCtrlForTesting.getDf("juan"));
        assertEquals(1, DocumentCtrlForTesting.getDf("me"));
        assertEquals(0, DocumentCtrlForTesting.getDf("jbjb"));

        //We check that the author Juan no longer exists
        try
        {
            DocumentCtrl.getTitlesByAuthor("Juan");
            assertTrue(false);
        }
        catch(AuthorNotFound e)
        {

        }
    }

    @Test (expected = DocumentNotFound.class)
    public void removeTest2() throws DocumentNotFound
    {
        DocumentCtrl.remove("Juan", "Adios");
    }

    @Test
    public void changeAuthorTest1() throws DocumentNotFound, AuthorNotFound {
        Document doc = DocumentCtrl.getDocument("Juan", "Hola");

        assertFalse(DocumentCtrl.changeAuthor("Juan", "Hola", "Pedro"));

        assertTrue(DocumentCtrl.changeAuthor("Juan", "Hola", "Pepe"));

        //We check if the author does not exist because he has no more documents
        try
        {
            DocumentCtrl.getTitlesByAuthor("Juan");
            assertTrue(false);
        }
        catch(AuthorNotFound e)
        {}

        assertEquals(doc, DocumentCtrl.getDocument("Pepe", "Hola"));

        assertFalse(DocumentCtrl.changeAuthor("Pepe", "Hola", "Pedro"));

    }

    @Test(expected = DocumentNotFound.class)
    public void changeAuthorTest2() throws DocumentNotFound {
        DocumentCtrl.changeAuthor("Juan", "Hola", "Pepe");
        DocumentCtrl.getDocument("Juan", "Hola");
    }

    @Test(expected = DocumentNotFound.class)
    public void changeTitleTest1() throws DocumentNotFound {
        assertTrue(DocumentCtrl.changeTitle("Juan", "Hola", "Adios"));
        DocumentCtrl.getDocument("Juan", "Hola");
    }

    @Test
    public void changeTitleTest2() throws DocumentNotFound {
        Document doc = DocumentCtrl.getDocument("Juan", "Hola");

        assertTrue(DocumentCtrl.changeTitle("Juan", "Hola", "Adios"));
        assertEquals(doc, DocumentCtrl.getDocument("Juan", "Adios"));

        assertTrue(DocumentCtrl.changeAuthor("Juan", "Adios", "Pedro"));

        assertFalse(DocumentCtrl.changeTitle("Pedro", "Adios", "Hola"));

    }

    @Test
    public void changeContentTest1() throws InvalidDocumentFormat, DocumentNotFound
    {
        LinkedList<String> l = new LinkedList<>();
        l.add("Frase 1 cambiada.");
        l.add("Frase 2 cambiada.");
        DocumentCtrl.changeContent("Juan", "Hola", l);
        assertEquals(2, DocumentCtrlForTesting.getNDocs());
        assertEquals(1, DocumentCtrlForTesting.getDf("me"));
        assertEquals(0, DocumentCtrlForTesting.getDf("Juan"));
    }

    @Test (expected = DocumentNotFound.class)
    public void changeContentTest2() throws InvalidDocumentFormat, DocumentNotFound
    {
        DocumentCtrl.changeContent("Juan", "Adios", new LinkedList<>());
    }

    @Test(expected = InvalidDocumentFormat.class)
    public void changeFormatTest1() throws InvalidDocumentFormat, DocumentNotFound {
        DocumentCtrl.changeFormat("Juan", "Hola", "xmll");
    }

    @Test
    public void changeFormatTest2() throws InvalidDocumentFormat, DocumentNotFound {
        //Let's test that no exception is launched
        //with the three valid formats
        DocumentCtrl.changeFormat("Juan", "Hola", "xml");
        DocumentCtrl.changeFormat("Juan", "Hola", "txt");
        DocumentCtrl.changeFormat("Juan", "Hola", "prop");

        //We change the format by a valid one, and check if the change has been done successfully
        DocumentCtrl.changeFormat("Juan", "Hola", "prop");
        assertEquals("prop", DocumentCtrl.getDocument("Juan", "Hola").getFormat());
    }

    @Test (expected = DocumentNotFound.class)
    public void changeFormatTest3() throws InvalidDocumentFormat, DocumentNotFound
    {
        //We check that the Exception DocumentNotFound is thrown if we want to modify the content of a non-existent document
        DocumentCtrl.changeFormat("Juan", "Adios", "txt");
    }

    private static Comparator<Document> createDocumentComparatorMock() throws InvalidWordFormat, InvalidExpression {
        Comparator<Document> comp = Mockito.mock(Comparator.class);
        when(comp.getList()).thenReturn(new LinkedList<>());
        doAnswer(i -> {
            Document doc = i.getArgument(0);
            System.out.println("Document {" + doc.getAuthor() + ", "+ doc.getTitle() + "} compared.");
            return null;
        }).when(comp).compare(Mockito.any(Document.class));
        return comp;
    }

    @Test
    public void searchBySimilarityTest1() throws InvalidWordFormat, DocumentNotFound, InvalidWeightType, InvalidExpression {

        System.out.println("SearchBySimilarity Test: \n");
        documentComparator = createDocumentComparatorMock();

        //We search by similarity taking as reference the document {Juan, Hola}
        //and check that all the documents (except the reference) have been compared.
        //The weigh type is 1, so no InvalidWeightType exception should be thrown
        System.out.println("Comparison with reference {Juan, Hola}:");
        DocumentCtrlForTesting.searchBySimilarity("Juan", "Hola", documentComparator, 1);

        verify(documentComparator, times(1)).compare(Mockito.any(Document.class));
        verify(documentComparator, times(1)).getList();

        //The same case as before, but not with weight type 2. No eception should be thrown.
        documentComparator = createDocumentComparatorMock();
        System.out.println("Comparison with reference {Juan, Hola}:");
        DocumentCtrlForTesting.searchBySimilarity("Juan", "Hola", documentComparator, 2);

        verify(documentComparator, times(1)).compare(Mockito.any(Document.class));
        verify(documentComparator, times(1)).getList();

        //Now with more than one document to compare
        Document doc = createDocumentMock("Arnau", "Prueba", new LinkedList<>(), "xml");
        when(doc.getWords()).thenReturn(new String[]{});
        DocumentCtrlForTesting.add(doc);

        System.out.println("\n Added document {Arnau, Prueba}\n");

        documentComparator = createDocumentComparatorMock();
        System.out.println("Comparison with reference {Pedro, Hola}:");
        DocumentCtrlForTesting.searchBySimilarity("Pedro", "Hola", documentComparator, 1);

        verify(documentComparator, times(2)).compare(Mockito.any(Document.class));
        verify(documentComparator, times(1)).getList();

    }

    @Test (expected = DocumentNotFound.class)
    public void searchBySimilarityTest2() throws InvalidWordFormat, DocumentNotFound, InvalidWeightType, InvalidExpression {
        documentComparator = createDocumentComparatorMock();
        DocumentCtrlForTesting.searchBySimilarity("Pedro", "Adios", documentComparator,1);
    }

    @Test (expected = InvalidWeightType.class)
    public void searchBySimilarityTest3() throws InvalidWordFormat, InvalidWeightType, DocumentNotFound, InvalidExpression {
        //Weight type 3 does not exist, so we check that InvalidWeightType exception is thrown
        documentComparator = createDocumentComparatorMock();
        DocumentCtrlForTesting.searchBySimilarity("Juan", "Hola", documentComparator, 3);
    }

    @Test (expected = InvalidWeightType.class)
    public void searchBySimilarityTest4() throws InvalidWordFormat, InvalidWeightType, DocumentNotFound, InvalidExpression {
        //Weight type 0 does not exist, so we check that InvalidWeightType exception is thrown
        documentComparator = createDocumentComparatorMock();
        DocumentCtrlForTesting.searchBySimilarity("Juan", "Hola", documentComparator, 0);
    }

    @Test
    public void searchByQueryTest1() throws InvalidWordFormat, InvalidWeightType, InvalidExpression {
        //Query: me gusta prop
        System.out.println("SearchByQuery Test:");
        Content queryContent = Mockito.mock(Content.class);
        when(queryContent.getWords()).thenReturn(new String[]{"me", "gusta", "prop"});
        when(queryContent.getWeight1("me")).thenReturn(0f);
        when(queryContent.getWeight1("gusta")).thenReturn(1f);
        when(queryContent.getWeight1("prop")).thenReturn(1f);

        documentComparator = createDocumentComparatorMock();

        //We search by the query and check that all documents have been compared, that the vector of the query has been created. No exception should be thrown.
        DocumentCtrlForTesting.searchByQuery(queryContent, documentComparator, 1);
        verify(queryContent, times(3)).getWeight1(Mockito.anyString());
        verify(queryContent, times(0)).getWeight2(Mockito.anyString());
        verify(documentComparator, times(2)).compare(Mockito.any(Document.class));
        verify(documentComparator, times(1)).compare(doc1);
        verify(documentComparator, times(1)).compare(doc2);
        verify(documentComparator, times(1)).getList();

        verify(queryContent, times(3)).updateWeight(Mockito.anyString(), Mockito.anyFloat());
        verify(queryContent, times(1)).updateWeight("me", 0);
        verify(queryContent, times(1)).updateWeight("gusta", (float)Math.log10(2f/1f));
        verify(queryContent, times(1)).updateWeight("prop", (float)Math.log10(2f/1f));
        verify(queryContent, times(1)).getWeight1("me");
        verify(queryContent, times(1)).getWeight1("gusta");
        verify(queryContent, times(1)).getWeight1("prop");



        documentComparator = createDocumentComparatorMock();
        queryContent = Mockito.mock(Content.class);
        when(queryContent.getWords()).thenReturn(new String[]{"me", "gusta", "prop"});
        when(queryContent.getWeight1("me")).thenReturn(0f);
        when(queryContent.getWeight1("gusta")).thenReturn(1f);
        when(queryContent.getWeight1("prop")).thenReturn(1f);

        DocumentCtrlForTesting.searchByQuery(queryContent, documentComparator, 2);
        verify(queryContent, times(0)).getWeight1(Mockito.anyString());
        verify(queryContent, times(3)).getWeight2(Mockito.anyString());
        verify(documentComparator, times(2)).compare(Mockito.any(Document.class));
        verify(documentComparator, times(1)).compare(doc1);
        verify(documentComparator, times(1)).compare(doc2);
        verify(documentComparator, times(1)).getList();

        verify(queryContent, times(3)).updateWeight(Mockito.anyString(), Mockito.anyFloat());
        verify(queryContent, times(1)).updateWeight("me", 0);
        verify(queryContent, times(1)).updateWeight("gusta", (float)Math.log10(2f/1f));
        verify(queryContent, times(1)).updateWeight("prop", (float)Math.log10(2f/1f));
        verify(queryContent, times(1)).getWeight2("me");
        verify(queryContent, times(1)).getWeight2("gusta");
        verify(queryContent, times(1)).getWeight2("prop");
    }

    @Test (expected = InvalidWeightType.class)
    public void searchByQueryTest2() throws InvalidWordFormat, InvalidWeightType, InvalidExpression {

        //We test that InvalidWeightType is thrown correctly with weight type 3
        Content queryContent = Mockito.mock(Content.class);
        when(queryContent.getWords()).thenReturn(new String[]{"me", "gusta", "prop"});
        when(queryContent.getWeight1("me")).thenReturn(0f);
        when(queryContent.getWeight1("gusta")).thenReturn(1f);
        when(queryContent.getWeight1("prop")).thenReturn(1f);

        documentComparator = createDocumentComparatorMock();

        DocumentCtrlForTesting.searchByQuery(queryContent, documentComparator, 3);
    }

    @Test (expected = InvalidWeightType.class)
    public void searchByQueryTest3() throws InvalidWordFormat, InvalidWeightType, InvalidExpression {

        //We test that InvalidWeightType is thrown correctly with weight type 0
        Content queryContent = Mockito.mock(Content.class);
        when(queryContent.getWords()).thenReturn(new String[]{"me", "gusta", "prop"});
        when(queryContent.getWeight1("me")).thenReturn(0f);
        when(queryContent.getWeight1("gusta")).thenReturn(1f);
        when(queryContent.getWeight1("prop")).thenReturn(1f);

        documentComparator = createDocumentComparatorMock();

        DocumentCtrlForTesting.searchByQuery(queryContent, documentComparator, 0);
    }

    @Test
    public void searchByBooleanStatementTest() throws InvalidWordFormat, InvalidExpression {
        System.out.println("SearchByBooleanStatement Test:");
        documentComparator = createDocumentComparatorMock();

        DocumentCtrlForTesting.searchByBooleanStatement(documentComparator);

        verify(documentComparator, times(2)).compare(Mockito.any(Document.class));
        verify(documentComparator, times(1)).compare(doc1);
        verify(documentComparator, times(1)).compare(doc2);
        verify(documentComparator, times(1)).getList();
    }

    @Test (expected = AuthorNotFound.class)
    public void getTitlesByAuthorTest1() throws AuthorNotFound
    {
        DocumentCtrl.getTitlesByAuthor("Noexiste");
    }

    @Test
    public void getTitlesByAuthorTest() throws AuthorNotFound, DocumentNotFound
    {
        assertEquals(1, DocumentCtrl.getTitlesByAuthor("Juan").size());
        assertEquals("Hola", DocumentCtrl.getTitlesByAuthor("Juan").get(0));

        DocumentCtrl.changeTitle("Juan", "Hola", "Buenas");
        DocumentCtrl.changeAuthor("Juan", "Buenas", "Pedro");
        assertEquals(2, DocumentCtrl.getTitlesByAuthor("Pedro").size());
        assertTrue(DocumentCtrl.getTitlesByAuthor("Pedro").contains("Buenas"));
        assertTrue(DocumentCtrl.getTitlesByAuthor("Pedro").contains("Hola"));

        Document doc = createDocumentMock("Pedro", "Prueba", new LinkedList<>(), "prop");
        when(doc.getWords()).thenReturn(new String[]{});
        DocumentCtrlForTesting.add(doc);

        assertEquals(3, DocumentCtrl.getTitlesByAuthor("Pedro").size());
        assertTrue(DocumentCtrl.getTitlesByAuthor("Pedro").contains("Hola"));
        assertTrue(DocumentCtrl.getTitlesByAuthor("Pedro").contains("Buenas"));
        assertTrue(DocumentCtrl.getTitlesByAuthor("Pedro").contains("Prueba"));
    }

    @Test (expected = DocumentNotFound.class)
    public void getDocumentTest1() throws DocumentNotFound
    {
        DocumentCtrl.getDocument("Juan", "Adios");
    }

    @Test (expected = DocumentNotFound.class)
    public void getDocumentTest2() throws DocumentNotFound
    {
        DocumentCtrl.getDocument("Pepe", "Hola");
    }

    @Test
    public void getDocumentTest3() throws  DocumentNotFound
    {
        assertEquals(doc1, DocumentCtrl.getDocument("Juan", "Hola"));
    }

    private static Comparator<String> createStringComparatorMock() throws InvalidWordFormat, InvalidExpression {
        Comparator<String> comp = Mockito.mock(Comparator.class);
        doAnswer(i -> {
            String pref = i.getArgument(0);
            System.out.println("Prefix '" + pref + "' compared.");
            return null;
        }).when(comp).compare(Mockito.anyString());
        return comp;
    }

    @Test
    public void searchByAuthorPrefixTest() throws InvalidWordFormat, InvalidExpression {
        Comparator<String> comp = createStringComparatorMock();
        LinkedList<String> l = comp.getList();
        System.out.println("SearchByAuthorPrefix Test:");

        assertEquals(2, DocumentCtrlForTesting.searchByAuthorPrefix(comp, "").size());

        System.out.println("Comparison of Juan and Pedro:");
        when(comp.getList()).thenReturn(new LinkedList<>(Arrays.asList("Juan")));
        assertEquals(1, DocumentCtrlForTesting.searchByAuthorPrefix(comp, "Ju").size());
        verify(comp, times(2)).compare(Mockito.anyString());

        System.out.println("\n Comparison with DocumentManager reset:");
        comp = createStringComparatorMock();
        DocumentCtrlForTesting.reset();
        when(comp.getList()).thenReturn(new LinkedList<>());
        assertEquals(0, DocumentCtrlForTesting.searchByAuthorPrefix(comp, "Pe").size());
        verify(comp, times(0)).compare(Mockito.anyString());

    }

}