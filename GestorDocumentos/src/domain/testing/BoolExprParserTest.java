package domain.testing;

import domain.classes.BoolExprParserForTesting;
import domain.classes.BoolExprTokenizer;
import domain.exceptions.InvalidExpression;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class BoolExprParserTest {

    private static ArrayList<String> sentences = new ArrayList<>();
    @Mock
    BoolExprTokenizer tokenizer = Mockito.mock(BoolExprTokenizer.class);

    @BeforeClass
    public static void init() {

    }

    @Test
    public void simple() throws InvalidExpression {
        String expression = "hola & tio";
        if (sentences.size() > 0) sentences.clear();

        when(tokenizer.getTokensSize()).thenReturn(3);

        when(tokenizer.getToken(0)).thenReturn(BoolExprTokenizer.Token.WRD);
        when(tokenizer.getWordSeq(0)).thenReturn("hola");

        when(tokenizer.getToken(1)).thenReturn(BoolExprTokenizer.Token.AND);

        when(tokenizer.getToken(2)).thenReturn(BoolExprTokenizer.Token.WRD);
        when(tokenizer.getWordSeq(2)).thenReturn("tio");

        BoolExprParserForTesting parser = new BoolExprParserForTesting(expression, tokenizer);

        sentences.add("hola tio como estas estoy de perfecto hermano como mola estar vivo.");
        sentences.add("ojala acabar prop, mis amigos joan y marc estan cansados.");

        boolean found = false;
        int size = sentences.size();

        for (int i = 0; i < size && !found; ++i) {
            found = parser.eval(sentences.get(0));
        }

        assertTrue(found);
    }

    @Test
    public void notAsSimple() throws InvalidExpression{
        String expression = "hello & world";
        if (sentences.size() > 0) sentences.clear();

        when(tokenizer.getTokensSize()).thenReturn(3);

        when(tokenizer.getToken(0)).thenReturn(BoolExprTokenizer.Token.WRD);
        when(tokenizer.getWordSeq(0)).thenReturn("hello");

        when(tokenizer.getToken(1)).thenReturn(BoolExprTokenizer.Token.AND);

        when(tokenizer.getToken(2)).thenReturn(BoolExprTokenizer.Token.WRD);
        when(tokenizer.getWordSeq(2)).thenReturn("world");

        BoolExprParserForTesting parser = new BoolExprParserForTesting(expression, tokenizer);

        sentences.add("hello world!!");

        boolean found = false;
        int size = sentences.size();

        for (int i = 0; i < size && !found; ++i) {
            found = parser.eval(sentences.get(0));
        }

        assertTrue(found);
    }

    @Test
    public void kindaComplex() throws InvalidExpression{
        String expression = "({hello world} & (!fish & !boat)) | {love programming}";
        ArrayList<BoolExprTokenizer.Token> tokens = new ArrayList<>(Arrays.asList(
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.WRD, // 2 --> hello
                BoolExprTokenizer.Token.AND,
                BoolExprTokenizer.Token.WRD, // 4 --> world
                BoolExprTokenizer.Token.RP,
                BoolExprTokenizer.Token.AND,
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.NOT,
                BoolExprTokenizer.Token.WRD, // 9 --> fish
                BoolExprTokenizer.Token.AND,
                BoolExprTokenizer.Token.NOT,
                BoolExprTokenizer.Token.WRD, // 12 --> boat
                BoolExprTokenizer.Token.RP,
                BoolExprTokenizer.Token.RP,
                BoolExprTokenizer.Token.OR,
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.WRD, // 17 --> love
                BoolExprTokenizer.Token.AND,
                BoolExprTokenizer.Token.WRD, // 19 --> programming
                BoolExprTokenizer.Token.RP
        ));
        if (sentences.size() > 0) sentences.clear();

        when(tokenizer.getTokensSize()).thenReturn(tokens.size());

        for (int i = 0; i < tokens.size(); ++i) {
            when(tokenizer.getToken(i)).thenReturn(tokens.get(i));
        }

        when(tokenizer.getWordSeq(2)).thenReturn("hello");
        when(tokenizer.getWordSeq(4)).thenReturn("world");
        when(tokenizer.getWordSeq(9)).thenReturn("fish");
        when(tokenizer.getWordSeq(12)).thenReturn("boat");
        when(tokenizer.getWordSeq(17)).thenReturn("love");
        when(tokenizer.getWordSeq(19)).thenReturn("programming");
        BoolExprParserForTesting parser = new BoolExprParserForTesting(expression, tokenizer);

        sentences.add("Hello guys! I love eating fish on my boat.");
        sentences.add("hello world!! i love programming and hate fishing.");

        boolean found = false;
        int size = sentences.size();

        for (int i = 0; i < size && !found; ++i) {
            found = parser.eval(sentences.get(i));
        }

        assertTrue(found);
    }

    @Test
    public void complex() throws InvalidExpression {
        String expression = "palms & !dad & (heavy | (spaghetti & ({sweaty knees mom} | !(rap & \"eminem is bad\"))))";
        ArrayList<BoolExprTokenizer.Token> tokens = new ArrayList<>(Arrays.asList(
                BoolExprTokenizer.Token.WRD, // 0 --> palms
                BoolExprTokenizer.Token.AND,
                BoolExprTokenizer.Token.NOT,
                BoolExprTokenizer.Token.WRD, // 3 --> dad
                BoolExprTokenizer.Token.AND,
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.WRD, // 6 --> heavy
                BoolExprTokenizer.Token.OR,
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.WRD, // 9 --> spaghetti
                BoolExprTokenizer.Token.AND,
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.WRD, // 13 --> sweaty
                BoolExprTokenizer.Token.AND,
                BoolExprTokenizer.Token.WRD, // 15 --> knees
                BoolExprTokenizer.Token.AND,
                BoolExprTokenizer.Token.WRD, // 17 --> mom
                BoolExprTokenizer.Token.RP,
                BoolExprTokenizer.Token.OR,
                BoolExprTokenizer.Token.NOT,
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.WRD, // 22 --> rap
                BoolExprTokenizer.Token.AND,
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.STR, // 25 --> eminem is bad
                BoolExprTokenizer.Token.RP,
                BoolExprTokenizer.Token.RP,
                BoolExprTokenizer.Token.RP,
                BoolExprTokenizer.Token.RP,
                BoolExprTokenizer.Token.RP
        ));
        if (sentences.size() > 0) sentences.clear();

        when(tokenizer.getTokensSize()).thenReturn(tokens.size());

        for (int i = 0; i < tokens.size(); ++i) {
            when(tokenizer.getToken(i)).thenReturn(tokens.get(i));
        }

        when(tokenizer.getWordSeq(0)).thenReturn("palms");
        when(tokenizer.getWordSeq(3)).thenReturn("dad");
        when(tokenizer.getWordSeq(6)).thenReturn("heavy");
        when(tokenizer.getWordSeq(9)).thenReturn("spaghetti");
        when(tokenizer.getWordSeq(13)).thenReturn("sweaty");
        when(tokenizer.getWordSeq(15)).thenReturn("knees");
        when(tokenizer.getWordSeq(17)).thenReturn("mom");
        when(tokenizer.getWordSeq(22)).thenReturn("rap");
        when(tokenizer.getWordSeq(25)).thenReturn("eminem is bad");
        BoolExprParserForTesting parser = new BoolExprParserForTesting(expression, tokenizer);

        sentences.add("His palms are sweaty, knees weak arms are heavy, there's vomit on his sweater already, mom's spaghetti. ");
        sentences.add("He's nervous, but on the surface he looks calm and ready.");
        sentences.add("To drop bombs, but he keeps on forgetting what he wrote down.");

        boolean found = false;
        int size = sentences.size();

        for (int i = 0; i < size && !found; ++i) {
            found = parser.eval(sentences.get(i));
        }

        assertTrue(found);
    }

    @Test (expected = InvalidExpression.class)
    public void invalidParenthesis() throws InvalidExpression {
        String expression = "(p1 & p2))";
        ArrayList<BoolExprTokenizer.Token> tokens = new ArrayList<>(Arrays.asList(
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.WRD,
                BoolExprTokenizer.Token.AND,
                BoolExprTokenizer.Token.WRD,
                BoolExprTokenizer.Token.RP,
                BoolExprTokenizer.Token.RP
        ));
        if (sentences.size() > 0) sentences.clear();

        when(tokenizer.getTokensSize()).thenReturn(tokens.size());

        for (int i = 0; i < tokens.size(); ++i) {
            when(tokenizer.getToken(i)).thenReturn(tokens.get(i));
        }

        when(tokenizer.getWordSeq(1)).thenReturn("p1");
        when(tokenizer.getWordSeq(3)).thenReturn("p2");
        BoolExprParserForTesting parser = new BoolExprParserForTesting(expression, tokenizer);

        sentences.add("His palms are sweaty, knees weak arms are heavy, there's vomit on his sweater already, mom's spaghetti. ");

        boolean found = false;
        int size = sentences.size();

        for (int i = 0; i < size && !found; ++i) {
            found = parser.eval(sentences.get(i));
        }
    }

    @Test (expected = InvalidExpression.class)
    public void notComputable() throws InvalidExpression, InvalidExpression {
        String expression = "(())";
        ArrayList<BoolExprTokenizer.Token> tokens = new ArrayList<>(Arrays.asList(
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.LP,
                BoolExprTokenizer.Token.RP,
                BoolExprTokenizer.Token.RP
        ));
        if (sentences.size() > 0) sentences.clear();

        when(tokenizer.getTokensSize()).thenReturn(tokens.size());

        for (int i = 0; i < tokens.size(); ++i) {
            when(tokenizer.getToken(i)).thenReturn(tokens.get(i));
        }

        BoolExprParserForTesting parser = new BoolExprParserForTesting(expression, tokenizer);

        sentences.add("Stubs son los reyes magos.");

        boolean found = false;
        int size = sentences.size();

        for (int i = 0; i < size && !found; ++i) {
            found = parser.eval(sentences.get(i));
        }
    }
}