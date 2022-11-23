package domain.utils;
import static java.lang.Character.isWhitespace;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public class Phraser {

    public Phraser () {}

    private static boolean isEndOfPhrase(byte c) {
        return c == '.' || c == '!' || c == '?';
    }

    public static LinkedList<String> getPhrases (String s) {
        int i = 0, f = 0; // inici i final d'una frase
        byte[] chars = s.getBytes(StandardCharsets.UTF_8);
        LinkedList<String> ret = new LinkedList<>();

        while (f < chars.length) {
            if (isEndOfPhrase(chars[f])) {
                while (f+1 < chars.length && isEndOfPhrase(chars[f+1])) ++f; // Para el caso de los puntos suspensivos, múltiples puntuaciones (...., !!!!, ??????), etc.
                while (f+1 < chars.length && isWhitespace(chars[f+1])) ++f;  // Para el caso de tener múltiples espacios en blanco tras el final de una frase.
                ret.add(new String(Arrays.copyOfRange(chars, i, f+1))); // get substring que contiene la frase
                i = ++f;
                continue;
            }
            ++f;
        }

        if (i < chars.length) //in case the last phrase did not have a point
            ret.add(new String(Arrays.copyOfRange(chars, i, chars.length))); // get substring que contiene la frase
        return ret;
    }

    public static String mergePhrases(LinkedList<String> phrases) {
        String result = "";
        for (String a : phrases) {
            result += a;
        }
        return result;
    }
}
