package domain.classes;
import domain.exceptions.InvalidWordFormat;
import domain.utils.Pair;

import java.text.Normalizer;
import java.util.*;
public class Content
{
    private LinkedList<String> sentences;
    private int nWords;
    private HashMap<String, Pair<Float, Float>> weights1; //Pair<weight, tf>: tf = #appearances/nWords
    private HashMap<String, Pair<Float, Float>> weights2; //Pair<weight, tf>: tf = 1 + log10(#appearances)


    public Content (LinkedList<String> sentences)
    {
        this.sentences = sentences;
        weights1 = new HashMap<>();
        weights2 = new HashMap<>();
        calculateTermFrequency();
    }

    private boolean isWordValid(String word)
    {
        if(word.matches("[a-z]+"))
            return true;
        return false;
    }

    //Post: returns TRUE if word is in the content, FALSE if not
    public boolean isWord(String word)
    {
        return weights1.containsKey(word);
    }

    //Pre: only a-z characters allowed in word, InvalidWordFormat otherwise
    //Post: gets the weight1 of word
    public float getWeight1(String word) throws InvalidWordFormat
    {
        if(!isWordValid(word))
            throw new InvalidWordFormat(word);

        if(weights1.get(word) == null)
            return 0;

        return weights1.get(word).getFirst();
    }

    //Pre: only a-z characters allowed in word, InvalidWordFormat otherwise
    //Post: gets the weight2 of word
    public float getWeight2(String word) throws InvalidWordFormat
    {
        if(!isWordValid(word))
            throw new InvalidWordFormat(word);

        if(weights2.get(word) == null)
            return 0;

        return weights2.get(word).getFirst();
    }

    //Pre: only a-z characters allowed in word, InvalidWordFormat otherwise
    public float getTf(String word) throws InvalidWordFormat
    {
        if(!isWordValid(word))
            throw new InvalidWordFormat(word);

        if(weights1.get(word) == null)
            return 0;

        return weights1.get(word).getSecond();
    }


    //Pre: sentences cannot be null, nWords is not 0, weights is initialized
    //Post:
    private void calculateTermFrequency()
    {
        ListIterator<String> it = sentences.listIterator(0);

        while(it.hasNext())
        {
            String s = it.next();
            String [] words = s.split("[\\s.?¿:;,\"\']");

            //F
            for(int i = 0; i < words.length; i++)
            {
                String w = words[i];

                //We convert the word w, to a lowercase word with no accents
                w = w.toLowerCase();
                w = Normalizer.normalize(w, Normalizer.Form.NFD);
                w = w.replaceAll("[^\\p{ASCII}]", "");

                if(!w.equals("") && !StopWordsReader.contains(w))
                {
                    nWords++;

                    //Increment weights1.value.second by 1
                    if (weights1.containsKey(w)) {
                        //Increment by 1 the word frequency
                        weights1.get(w).setSecond(weights1.get(w).getSecond() + 1f);
                    } else {
                        weights1.put(w, new Pair<Float, Float>(-1f, 1f));
                    }

                    //Increment weights2.value.second by 1
                    if(weights2.containsKey(w))
                        weights2.get(w).setSecond(weights2.get(w).getSecond() + 1f);
                    else
                        weights2.put(w, new Pair<Float, Float>(-1f, 1f));
                }
            }
        }
        //We divide every weight1.value.second by the total number of words
        for(Pair<Float, Float> p : this.weights1.values())
        {
            p.setSecond(p.getSecond()/nWords);
        }

        //We transform every weight2.value.second to 1 + log10(x)
        for(Pair<Float, Float> p : this.weights2.values())
        {
            p.setSecond(1f + (float)Math.log10(p.getSecond()));
        }
    }

    public String[] getWords()
    {
        String[] res = new String[weights1.size()];

        int i = 0;
        for(String w: weights1.keySet())
        {
            res[i] = w;
            i++;
        }

        return res;
    }

    public void updateWeight(String word, float idf)
    {
        //We update weights1
        if(weights1.containsKey(word))
            weights1.get(word).setFirst(weights1.get(word).getSecond() * idf);

        //We update weights2
        if(weights2.containsKey(word))
            weights2.get(word).setFirst(weights2.get(word).getSecond() * idf);
    }

    public LinkedList<String> getSentences()
    {
        return this.sentences;
    }
}