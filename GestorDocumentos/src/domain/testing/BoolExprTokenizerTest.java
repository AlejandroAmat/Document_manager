package domain.testing;

import domain.classes.BoolExprTokenizer;
import domain.exceptions.InvalidExpression;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class BoolExprTokenizerTest {

    private static BoolExprTokenizer tokenizer;
    private String expression;

    private static void listTokens(BoolExprTokenizer tz) {
        BoolExprTokenizer.Token token;
        for (int i = 0; i < tz.getTokensSize(); ++i) {
            token = tz.getToken(i);
            String word = "";
            if (token == BoolExprTokenizer.Token.WRD || token == BoolExprTokenizer.Token.STR) {
                word = tz.getWordSeq(i);
            }

            System.out.println(token.toString() + " " + word);
        }
    }

    public static void setAndTokenize(BoolExprTokenizer tz, String expr) throws InvalidExpression {
        tz.setExpression(expr);
        tz.tokenize();
    }

    @BeforeClass
    public static void init() {
        tokenizer = new BoolExprTokenizer();
    }

    @Test
    public void emptyExpression() throws InvalidExpression {
        expression = "";
        setAndTokenize(tokenizer, expression);
        listTokens(tokenizer);
        assertEquals(0, tokenizer.getTokensSize());
    }

    @Test
    public void onlySpaces() throws InvalidExpression {
        expression = "                          ";
        setAndTokenize(tokenizer, expression);
        assertEquals(0, tokenizer.getTokensSize());
    }

    @Test
    public void onlySets() throws InvalidExpression {
        expression = "{hello world} & {world hello} | ({p1 p2})";
        setAndTokenize(tokenizer, expression);
        //listTokens(tokenizer);
        assertEquals("[LP, WRD, AND, WRD, RP, AND, LP, WRD, AND, WRD, RP, OR, LP, LP, WRD, AND, WRD, RP, RP]", tokenizer.getTokens().toString());
    }

    @Test
    public void heavySets() throws InvalidExpression {
        expression = "{hello world} & {world hello table computer window chair} | ({p1 p2}) & {w1 w2} & {w3 w4}";
        setAndTokenize(tokenizer, expression);
        //listTokens(tokenizer);
        assertEquals("[LP, WRD, AND, WRD, RP, AND, LP, WRD, AND, WRD, AND, WRD, AND, WRD, AND, WRD, AND, WRD, " +
                "RP, OR, LP, LP, WRD, AND, WRD, RP, RP, AND, LP, WRD, AND, WRD, RP, AND, LP, WRD, AND, WRD, RP]", tokenizer.getTokens().toString());
    }

    @Test
    public void onlySequences() throws InvalidExpression {
        expression = "\"hello world\" & (\"i'm doing great YES!\") | !\"not!\"";
        setAndTokenize(tokenizer, expression);
        assertEquals("[LP, STR, RP, AND, LP, LP, STR, RP, RP, OR, NOT, LP, STR, RP]", tokenizer.getTokens().toString());
    }

    @Test
    public void extraSpaces() throws InvalidExpression {
        expression = "{    p1 p2     } | p3            & !p4     ";
        setAndTokenize(tokenizer, expression);
        assertEquals("[LP, WRD, AND, WRD, RP, OR, WRD, AND, NOT, WRD]", tokenizer.getTokens().toString());
    }

    @Test (expected = InvalidExpression.class)
    public void invalidSet() throws InvalidExpression {
        expression = "{ hello & word }";
        setAndTokenize(tokenizer, expression);
    }

    @Test (expected = InvalidExpression.class)
    public void invalidSet2() throws InvalidExpression {
        expression = "{ hello & word ";
        setAndTokenize(tokenizer, expression);
    }

    @Test (expected = InvalidExpression.class)
    public void invalidSet3() throws InvalidExpression {
        expression = " hello & word }";
        setAndTokenize(tokenizer, expression);
    }

    @Test (expected = InvalidExpression.class)
    public void invalidSequence() throws InvalidExpression {
        expression = "\"hello world!   ";
        setAndTokenize(tokenizer, expression);
    }

    @Test (expected = InvalidExpression.class)
    public void invalidSequence2() throws InvalidExpression {
        expression = "hello world\"";
        setAndTokenize(tokenizer, expression);
    }
}
