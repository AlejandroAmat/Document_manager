package domain.testing;
import domain.controllers.*;
import domain.classes.*;
import domain.exceptions.DocumentNotFound;
import domain.exceptions.InvalidExpression;
import domain.exceptions.InvalidWeightType;
import domain.exceptions.InvalidWordFormat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class DocumentCtrlForTesting extends DocumentCtrl
{
    public static boolean add(Document doc)
    {
        return DocumentCtrl.add(doc);
    }

    public static boolean documentContainsWord(String word, Document doc)
    {
        return words.get(word).contains(doc);
    }

    public static boolean existsDocInHashMap(Document doc)
    {
        return documents.containsKey(doc.getAuthor()) && documents.get(doc.getAuthor()).containsKey(doc.getTitle());
    }

    public static int getNDocs()
    {
        return nDocs;
    }

    public static int getDf(String word)
    {
        if(words.containsKey(word))
            return words.get(word).size();
        return 0;
    }

    public static LinkedList<Document> searchBySimilarity(String author, String title, domain.classes.Comparator<Document> comp, int weightType) throws InvalidWordFormat, DocumentNotFound, InvalidWeightType, InvalidExpression {
        if(!isWeightValid(weightType))
            throw new InvalidWeightType(weightType);

        return DocumentCtrl.searchBySimilarity(comp, getDocument(author, title));
    }

    //Exactly the same function of DocumentManager.searchByQuery
    // excepts that here Content and Comparator are passed as arguments (in order to use Mocks)
    public static LinkedList<Document> searchByQuery(Content queryContent, Comparator<Document> comp, int weightType) throws InvalidWordFormat, InvalidWeightType, InvalidExpression {
        if(!isWeightValid(weightType))
            throw new InvalidWeightType(weightType);

        //We calculate the weight of every word in query
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

    public static LinkedList<Document> searchByBooleanStatement(Comparator<Document> comp) throws InvalidWordFormat, InvalidExpression {
        for(HashMap<String, Document> h : documents.values())
        {
            for(Document doc : h.values())
            {
                comp.compare(doc);
            }
        }

        return comp.getList();
    }

    public static LinkedList<String> searchByAuthorPrefix(Comparator<String> comp, String prefix) throws InvalidWordFormat, InvalidExpression {
        if(prefix.equals(""))
            return new LinkedList<String>(documents.keySet());

        Set<String> authors = documents.keySet();

        for(String author : authors)
        {
            comp.compare(author);
        }

        return comp.getList();
    }

    public static void reset()
    {
        nDocs = 0;
        documents = new HashMap<>();
        words = new HashMap<>();
    }


}
