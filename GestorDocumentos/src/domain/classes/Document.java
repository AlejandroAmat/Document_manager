package domain.classes;
import domain.exceptions.InvalidDocumentFormat;
import domain.exceptions.InvalidWordFormat;

import java.util.*;

public class Document
{
    private String author;
    private String title;
    private Content content;
    String format;

    public Document(String author, String title, LinkedList<String> sentences, String format) throws InvalidDocumentFormat
    {
        if(!isFormatValid(format))
            throw new InvalidDocumentFormat(format);

        this.author = author;
        this.title = title;
        this.content = new Content(sentences);
        this.format = format;
    }

    public static boolean isFormatValid(String format)
    {
        if(!format.equals("xml") && !format.equals("txt") && !format.equals("prop"))
            return false;
        return true;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getFormat() {
        return format;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(LinkedList<String> sentences) {
        this.content = new Content(sentences);
    }

    //Pre: format is valid
    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isWord(String word)
    {
        return content.isWord(word);
    }

    public String[] getWords()
    {
        return content.getWords();
    }

    public void updateWeight(String word, float idf)
    {
        content.updateWeight(word, idf);
    }

    //Pre: only a-z characters allowed in word, InvalidWordFormat otherwise
    public float getWeight1(String word) throws InvalidWordFormat
    {
        return content.getWeight1(word);
    }

    public float getWeight2(String word) throws InvalidWordFormat
    {
        return content.getWeight2(word);
    }

    //Pre: only a-z characters allowed in word, InvalidWordFormat otherwise
    public float getTf(String word) throws  InvalidWordFormat
    {
        return content.getTf(word);
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == this)
            return true;

        if(!(o instanceof Document))
            return false;

        Document doc = (Document) o;

        return this.author.equals(doc.author) && this.title.equals(doc.title);
    }

    public LinkedList<String> getSentences()
    {
        return content.getSentences();
    }
}