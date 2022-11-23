package domain.controllers;
import domain.classes.*;
import domain.classes.Comparator;
import domain.exceptions.*;

import java.util.*;

public class DocumentCtrl
{
    //map<author, map<title, Doc> >
    protected static HashMap<String, HashMap<String, Document> > documents = new HashMap<String, HashMap<String, Document> >();
    protected static HashMap<String, LinkedList<Document>> words = new HashMap<>();
    protected static int nDocs = 0;

    private static boolean existsDocument(String author, String title)
    {
        if(!documents.containsKey(author) || !documents.get(author).containsKey(title))
            return false;

        return true;
    }

    protected static boolean isWeightValid(int type)
    {
        return type == 1 || type == 2;
    }

    //Pre: the document does not exist in the hashmap
    private static void addDocumentToHashMap(Document doc)
    {
        if(documents.get(doc.getAuthor()) != null)
            documents.get(doc.getAuthor()).put(doc.getTitle(), doc);
        else
        {
            HashMap<String, Document> m = new HashMap<String, Document>();
            m.put(doc.getTitle(), doc);
            documents.put(doc.getAuthor(), m);
        }
    }
    protected static float calculateIdf(int df)
    {
        return (float)Math.log10((float)nDocs/df);
    }

    //It updates all the weights of all documents
    private static void updateWeights()
    {
        for(Map.Entry<String, LinkedList<Document>> entry : words.entrySet())
        {
            String word = entry.getKey();
            LinkedList<Document> list = entry.getValue();
            int df = list.size();

            ListIterator<Document> it = list.listIterator(0);
            while(it.hasNext())
            {
                Document doc = it.next();
                doc.updateWeight(word, calculateIdf(df));
            }
        }
    }

    protected static boolean add(Document doc)
    {
        if(existsDocument(doc.getAuthor(), doc.getTitle()))
            return false;

        nDocs++;
        //For each word of the document, we add or update the list of docs where it appears
        String[] docWords = doc.getWords();
        for(int i = 0; i < docWords.length; i++)
        {
            String w = docWords[i];
            if(words.containsKey(w))
            {
                //The word w already exists in other documents
                //we add the doc to the list of documents that contain w
                words.get(w).add(doc);

            }
            else
            {
                //The word w does not exist in any other document

                LinkedList<Document> l = new LinkedList<>();
                l.add(doc);
                words.put(w, l);

            }
        }

        //We add the document in documents
        addDocumentToHashMap(doc);

        updateWeights();

        return true;
    }

    //Pre: format is valid
    //Post: TRUE if the document has been added successfully, FALSE if there is already
    //a document with the same author and title
    public static boolean add(String author, String title, LinkedList<String> sentences, String format) throws InvalidDocumentFormat
    {
        Document doc = new Document(author, title, sentences, format);
        return add(doc);
    }

    //The document {author, title} must exist
    //Post: the document {author, title} has been removed
    public static void remove(String author, String title) throws DocumentNotFound
    {
        if(!existsDocument(author, title))
            throw new DocumentNotFound(author, title);
        nDocs--;

        //For each word of the document, we remove from the hashmap 'words' the document where it appears
        Document doc = documents.get(author).get(title);
        String[] docWords = doc.getWords();
        for(int i = 0; i < docWords.length; i++)
        {
            words.get(docWords[i]).remove(doc);

            //If it was the only document containing docWord[i], then we remove the word from 'words'
            if(words.get(docWords[i]).size() == 0)
                words.remove(docWords[i]);
        }

        //We remove the document from documents
        documents.get(author).remove(title);
        if(documents.get(author).isEmpty())
            documents.remove(author);

        updateWeights();

    }

    //Pre: The document must exist, author != newAuthor
    //Post: true if ok, false if the newAuthor has already a document with title 'title',
    public static boolean changeAuthor(String author, String title, String newAuthor) throws DocumentNotFound
    {
        if(!existsDocument(author, title))
            throw new DocumentNotFound(author, title);

        if(existsDocument(newAuthor, title))
            return false;

        //We pick up the document in doc, then we remove it from the documents of 'author'
        Document doc = documents.get(author).get(title);
        documents.get(author).remove(title);

        doc.setAuthor(newAuthor);

        //We add the document once again with the new author
        addDocumentToHashMap(doc);

        if(documents.get(author).isEmpty())
            documents.remove(author);

        return true;
    }

    //Post: TRUE if ok, false if author has already a document with title 'newTitle'
    public static boolean changeTitle(String author, String title, String newTitle) throws DocumentNotFound
    {
        if(!existsDocument(author, title))
            throw new DocumentNotFound(author, title);

        if(existsDocument(author, newTitle))
            return false;

        Document doc = documents.get(author).get(title);
        documents.get(author).remove(title);

        doc.setTitle(newTitle);
        addDocumentToHashMap(doc);

        return true;
    }

    //Pre: Document {author, title} must exist
    //Post: the content has been changed to 'sentences' in document {author, title}
    public static void changeContent(String author, String title, LinkedList<String> sentences) throws DocumentNotFound, InvalidDocumentFormat
    {
        if(!existsDocument(author, title))
            throw new DocumentNotFound(author, title);

        String format = documents.get(author).get(title).getFormat();

        remove(author, title);
        add(author, title, sentences, format);

    }

    //Pre: format must be valid, and document {author, title} must exist
    //Post: the format of document {author, title} has been changed to 'format'
    public static void changeFormat(String author, String title, String format) throws InvalidDocumentFormat, DocumentNotFound
    {
        if(!Document.isFormatValid(format))
        {
            throw new InvalidDocumentFormat(format);
        }

        if(!existsDocument(author, title))
            throw new DocumentNotFound(author, title);

        documents.get(author).get(title).setFormat(format);
    }

    //Pre: Document {author, title} must exist, weightType is 1 or 2
    public static LinkedList<Document> searchBySimilarity(String author, String title, int k, int weightType) throws InvalidWordFormat, InvalidWeightType, DocumentNotFound, InvalidExpression {
        if(!isWeightValid(weightType))
            throw new InvalidWeightType(weightType);

        if(!existsDocument(author, title))
            throw new DocumentNotFound(author, title);

        Document docReference = documents.get(author).get(title);
        Comparator<Document> comp = new SimilitudeComp(docReference, k, weightType);

        return searchBySimilarity(comp, docReference);
    }

    protected static LinkedList<Document> searchBySimilarity(Comparator<Document> comp, Document docReference) throws InvalidWordFormat, InvalidExpression {
        //iterate and compare through all documents, except the reference document
        for(HashMap<String, Document> h : documents.values())
        {
            for(Document doc : h.values())
            {
                if(!docReference.equals(doc))
                {
                    comp.compare(doc);
                }
            }
        }

        return comp.getList();
    }

    //Pre: query only contains words (in lowercase) without any punctuation mark. ex: hola que tal, weightType is 1 or 2
    //Post: throws exception if any of the words has any character other than a-z
    public static LinkedList<Document> searchByQuery(String query, int k, int weightType) throws InvalidWeightType, InvalidWordFormat, InvalidExpression {
        if(!isWeightValid(weightType))
            throw new InvalidWeightType(weightType);

        //We calculate the weight of every word in query
        LinkedList<String> l = new LinkedList<>();
        l.add(query);
        Content queryContent = new Content(l);

        HashMap<String, Float> queryVector = new HashMap<>();
        String[] queryWords = queryContent.getWords();
        for(String w : queryWords)
        {
            //Idf of the word
            int df = 0;
            if(words.containsKey(w))
                df = words.get(w).size();

            float idf = calculateIdf(df);

            queryContent.updateWeight(w, idf);

            if(weightType == 1)
                queryVector.put(w, queryContent.getWeight1(w));
            else
                queryVector.put(w, queryContent.getWeight2(w));
        }

        Comparator<Document> comp = new SimilitudeComp(queryVector, k, weightType);

        //iterate and compare through all documents
        for(HashMap<String, Document> h : documents.values())
        {
            for(Document doc : h.values())
            {
                comp.compare(doc);
            }
        }

        return comp.getList();
    }

    //Pre: Statement is valid

    public static LinkedList<Document> searchByBooleanStatement(String statement) throws InvalidWordFormat, InvalidExpression {
        Comparator<Document> comp = new BoolExpressionComp(statement);

        for(HashMap<String, Document> h : documents.values())
        {
            for(Document doc : h.values())
            {
                comp.compare(doc);
            }
        }

        return comp.getList();
    }

    //Post: exception if the author does not exist, otherwise the result is a list of the titles
    public static LinkedList<String> getTitlesByAuthor(String author) throws AuthorNotFound
    {
        if(!documents.containsKey(author))
            throw new AuthorNotFound(author);

        Set<String> set = documents.get(author).keySet();

        return new LinkedList<>(set);
    }

    //Post: if the document does not exist exception thrown, the document is returned otherwise
    public static Document getDocument(String author, String title) throws DocumentNotFound
    {
        if(!existsDocument(author, title))
            throw new DocumentNotFound(author, title);

        return documents.get(author).get(title);
    }


    //Post: prefix "" returns all the authors
    public static LinkedList<String> searchByAuthorPrefix(String prefix) throws InvalidWordFormat, InvalidExpression {

        if(prefix.equals(""))
            return new LinkedList<String>(documents.keySet());

        Comparator<String> comp = new PrefixComparator(prefix);

        Set<String> authors = documents.keySet();

        for(String author : authors)
        {
            comp.compare(author);
        }

        return comp.getList();
    }

}