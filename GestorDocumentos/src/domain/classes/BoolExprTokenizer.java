package domain.classes;

import domain.exceptions.InvalidExpression;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Character.isWhitespace;

public class BoolExprTokenizer {
    public enum Token {LP, RP, LConj, RConj, SEQ, WRD, STR, AND, NOT, OR, TRUE, FALSE}

    private String expression;
    private int pos = 0;

    private ArrayList<Token> tokens = new ArrayList<>();
    private HashMap<Integer, String> words_seq = new HashMap<>();
    private int listPos = -1;

    private boolean inSequence = false;
    private boolean inConj = false;

    private boolean isToken(char c) {
        return c == '(' || c == ')' || c == '{' || c == '}' || c == '"' || c == '&' || c == '|' || c == '!';
    }

    private void checkIfSequence() {
        if (inSequence) {
            //error
        }
    }

    private void checkIfConj() {
        if (inConj) {
            //error
        }
    }

    //------------------------------------------------------------------------------
    public BoolExprTokenizer() {

    }

    public BoolExprTokenizer(String expr) {
        expression = expr;
    }


    public String getExpression() {
        return expression;
    }
    public void setExpression(String expr) {
        expression = expr;
        pos = 0;
        listPos = -1;
        tokens.clear();
        words_seq.clear();
        inConj = false;
        inSequence = false;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public HashMap<Integer, String> getWordsSeqHash() {
        return words_seq;
    }

    public static boolean isOperator(Token t) {
        return t == Token.AND || t == Token.OR || t == Token.NOT || t == Token.LP;
    }

    /**
     * Getter of precedence of param t.
     * @param t Token we want to get the precedence of.
     * @return Precedence of t.
     */
    public static int getTokenPrecedence(Token t) {
        if (t == Token.LP) return 0;
        if (t == Token.OR) return 1;
        if (t == Token.AND) return 2;
        if (t == Token.NOT) return 3;

        return -1;
    }

    /**
     * @return returns word or sequence (String) with key index in the words_seq HashMap.
     */
    public String getWordSeq(int index) {
        return words_seq.get(index);
    }

    /**
     * Getter of token at position index.
     * @param index Index of the token we want.
     * @return Token @ index.
     */
    public Token getToken(int index) {
        try {
            return tokens.get(index);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Incorrect index");
        }

        return null;
    }

    /**
     *
     * @return Size of the tokens list.
     */
    public int getTokensSize() {
        return tokens.size();
    }

    /**
     * Tokenizes the boolean expression String found at attribute expression:
     *      & --> AND
     *      | --> OR
     *      ! --> NOT
     *
     *      any string --> WRD
     *
     *      ( --> LP
     *      ) --> RP
     *
     *      { --> LP
     *          any string inside {} will be converted to LP WRD AND WRD AND....RP. [{p1 p2} --> (p1 & p2)]
     *      } --> RP
     *
     *      " --> SEQ
     *          Everything inside SEQ will be taken as a string literal. Tokens will not be tokenized here. Except, of
     *          course, the end of sequence token (").
     *          The tokenizer will convert the sequence to tokens: LP STR RP. ["hello world" --> ("hello world")]
     *
     */
    public void tokenize() throws InvalidExpression {
        while (pos < expression.length()) {
            while (isWhitespace(expression.charAt(pos))) { // skip spaces inside the string when they don't matter.
                ++pos;
                if (pos >= expression.length()) return;
            }

            if (pos >= expression.length()) { // self-explanatory
                return;
            }

            int start = pos++;

            switch (expression.charAt(start)) { // check for token in start (pos-1)
                case '(':
                    if (inSequence) throw new InvalidExpression();
                    if (inConj) throw new InvalidExpression();
                    tokens.add(Token.LP);
                    break;
                case ')':
                    if (inSequence) throw new InvalidExpression();
                    if (inConj) throw new InvalidExpression();
                    tokens.add(Token.RP);
                    break;
                case '{':
                    if (inSequence) throw new InvalidExpression();
                    if (inConj) throw new InvalidExpression();
                    inConj = true;
                    tokens.add(Token.LP);
                    break;
                case '}':
                    if (!inConj) throw new InvalidExpression();
                    if (inSequence) throw new InvalidExpression();
                    tokens.add(Token.RP);
                    inConj = false;
                    break;
                case '"':
                    if (inConj) throw new InvalidExpression();
                    if (!inSequence) {
                        tokens.add(Token.LP);
                    } else {
                        tokens.add(Token.RP);
                    }

                    inSequence = !inSequence;
                    break;
                case '&':
                    if (inSequence) throw new InvalidExpression();
                    if (inConj) throw new InvalidExpression();
                    tokens.add(Token.AND);
                    break;
                case '|':
                    if (inSequence) throw new InvalidExpression();
                    if (inConj) throw new InvalidExpression();
                    tokens.add(Token.OR);
                    break;
                case '!':
                    if (inSequence) throw new InvalidExpression();
                    if (inConj) throw new InvalidExpression();
                    tokens.add(Token.NOT);
                    break;
                default:
                    StringBuilder temp = new StringBuilder();
                    boolean endOfConj = false;

                    // if last token of {} is found, endConj is set to true to finish the {WRD WRD WRD} to (WRD AND WRD AND WRD) conversion.
                    if (inSequence || inConj) {
                        while (pos <= expression.length()) {
                            if (!inSequence && isWhitespace(expression.charAt(start))) break;
                            temp.append(expression.charAt(start));
                            if (pos == expression.length()) {
                                if (inSequence) {
                                    throw new InvalidExpression();
                                }
                                endOfConj = true;
                                break;
                            }
                            if (isToken(expression.charAt(pos))) {
                                if (expression.charAt(pos) == '}') {
                                    endOfConj = true;
                                    break;
                                }
                                if (expression.charAt(pos) == '"') break;
                            }
                            ++start;
                            ++pos;
                        }

                    } else {
                        // regular loop to add regular solo strings to the token list as just WRD because
                        // we're not in a sequence or in a set of words.
                        while (pos <= expression.length() && !isWhitespace(expression.charAt(start))) {
                            temp.append(expression.charAt(start));
                            if (pos == expression.length()) break;
                            if (isToken(expression.charAt(pos))) break;
                            ++start;
                            ++pos;
                        }
                    }

                    // self-explanatory
                    if (inSequence) {
                        tokens.add(Token.STR);
                        words_seq.put(listPos+1, temp.toString().toLowerCase());
                    } else {
                        tokens.add(Token.WRD);
                        words_seq.put(listPos+1, temp.toString().toLowerCase());
                        if (!endOfConj && inConj) {
                            int s = start;
                            boolean canAnd = true;
                            while (s <= expression.length()) {
                                if (isWhitespace(expression.charAt(s))) {
                                    ++s;
                                } else {
                                    if (expression.charAt(s) == '}') {
                                        canAnd = false;
                                    }
                                    break;
                                }
                            }

                            if (canAnd) {
                                ++listPos;
                                tokens.add(Token.AND);
                            }
                        }
                    }
                    break;
            }

            ++listPos;
        }

        if (inSequence || inConj) {
            throw new InvalidExpression();
        }
    }

}
