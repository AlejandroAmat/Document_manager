package domain.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class StopWordsReader
{
    final static String spanishPath = "font/spanish.txt";
    final static String catalanPath = "font/catalan.txt";
    final static String englishPath = "font/english.txt";

    private static HashSet<String> stopWordsSpanish = readStopWords(spanishPath);
    private static HashSet<String> stopWordsEnglish = readStopWords(englishPath);
    private static HashSet<String> stopWordsCatalan = readStopWords(catalanPath);


    private static HashSet<String> readStopWords(String path)
    {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        HashSet<String> hashSet = new HashSet<>();

        while(scanner.hasNextLine())
        {
            hashSet.add(scanner.nextLine());
        }
        scanner.close();

        return hashSet;
    }


    public static boolean contains(String word)
    {
        return stopWordsSpanish.contains(word);
    }
}
