package domain.classes;
import domain.exceptions.*;
import java.util.LinkedList;
import java.util.Stack;

public class BoolExprParser {

    protected String expression;
    protected BoolExprTokenizer tokenizer;
    protected BoolExprTokenizer.Token currentToken;
    protected LinkedList<BoolExprTokenizer.Token> outputQueue;
    protected LinkedList<String> outputQueueWords;
    protected Stack<BoolExprTokenizer.Token> opStack;

    /**
     * Compares a and b.
     * @param a Token a
     * @param b Token b
     * @return TRUE if a == b, FALSE otherwise.
     */
    protected boolean tokenEquals(BoolExprTokenizer.Token a, BoolExprTokenizer.Token b) {
        return a == b;
    }

    /**
     * Compares precedence of a and b.
     * @param a Token a
     * @param b Token b
     * @return TRUE if a's precedence if higher or equal to b's. FALSE otherwise.
     */
    protected boolean hasHigherOrEqualPrecedence(BoolExprTokenizer.Token a, BoolExprTokenizer.Token b) {
        return BoolExprTokenizer.getTokenPrecedence(a) - BoolExprTokenizer.getTokenPrecedence(b) >= 0;
    }

    /**
     * Converts the token list from infix notation (WRD AND WRD) to postfix notation (WRD WRD AND) taking
     * operator precedence into account to make the order of the operations correct.
     *
     *
     */
    protected void parseInfixToPostfix() throws InvalidExpression {
        for (int i = 0; i < tokenizer.getTokensSize(); ++i) {
            currentToken = tokenizer.getToken(i);

            switch (currentToken) {
                case WRD:
                case STR:
                    outputQueue.add(currentToken);
                    outputQueueWords.add(tokenizer.getWordSeq(i).toLowerCase());
                    break;
                case LP:
                    opStack.push(currentToken);
                    break;
                case RP:
                    while (!opStack.isEmpty() && opStack.peek() != BoolExprTokenizer.Token.LP) {
                        BoolExprTokenizer.Token token = opStack.pop();
                        outputQueue.add(token);
                    }

                    if (opStack.isEmpty()) {
                        throw new InvalidExpression();
                    }

                    // popeamos el LP
                    opStack.pop();
                    break;
                default:
                    // mientras lo que venga del stack tenga higher (o igual) precedence, se va aÃ±adiendo lo del stack a la queue.
                    while (!opStack.empty() && hasHigherOrEqualPrecedence(opStack.peek(), currentToken)) {
                        outputQueue.add(opStack.pop());
                    }

                    opStack.push(currentToken);
                    break;
            }

        }

        while (!opStack.empty()) {
            if (tokenEquals(opStack.peek(), BoolExprTokenizer.Token.LP)) {
                throw new InvalidExpression("maybe parenthesis?");
            }
            outputQueue.add(opStack.pop());
        }

    }

    /**
     * Evaluates if the sentence complies with the attribute expression.
     * @param sentence: String we want to compare the expression with.
     * @return TRUE if it complies, FALSE otherwise.
     */
    protected boolean postEvaluator(String sentence) throws InvalidExpression {
        if (expression.equals("")) return true;

        String sent = sentence.toLowerCase();

        LinkedList<BoolExprTokenizer.Token> outCopy = new LinkedList<>(outputQueue);
        LinkedList<String> outWordsCopy = new LinkedList<>(outputQueueWords);

        Stack<BoolExprTokenizer.Token> stack = new Stack<>();
        Stack<String> stack_words = new Stack<>();

        while (!outCopy.isEmpty()) {
            BoolExprTokenizer.Token token = outCopy.getFirst();
            if (!BoolExprTokenizer.isOperator(token)) {
                stack.push(token);
                outCopy.removeFirst();
                if (tokenEquals(token, BoolExprTokenizer.Token.WRD) || tokenEquals(token, BoolExprTokenizer.Token.STR)) {
                    stack_words.push(outWordsCopy.getFirst());
                    outWordsCopy.removeFirst();
                }
                continue;
            }

            if (stack.isEmpty()) {
                throw new InvalidExpression();
            }

            /**
             * Regular expression used here is .*?\bWRD\b.*?  It checks for a specific word inside a string.
             *      . matches any character
             *          *? is for zero or more times
             *      \b is for word boundary.
             * It is only used when evaluating individual words.
             * Regex not used when the STR is received, String's contains() is used instead. That's because we
             * want to check if the value of STR is inside the sentence in a literal form.
             */
            switch (token) {
                case NOT:
                    BoolExprTokenizer.Token t = stack.pop();
                    boolean resNot = false;
                    if (tokenEquals(t, BoolExprTokenizer.Token.WRD)) {
                        resNot = !sent.matches(".*?\\b" + stack_words.pop() + "\\b.*?");
                    } else if (tokenEquals(t, BoolExprTokenizer.Token.STR)) {
                        resNot = !sent.contains(stack_words.pop());
                    } else if (tokenEquals(t, BoolExprTokenizer.Token.TRUE)) {
                        resNot = false;
                    } else if (tokenEquals(t, BoolExprTokenizer.Token.FALSE)) {
                        resNot = true;
                    } else {
                        throw new InvalidExpression();
                    }

                    if (resNot) stack.push(BoolExprTokenizer.Token.TRUE);
                    else stack.push(BoolExprTokenizer.Token.FALSE);

                    outCopy.removeFirst();
                    break;
                default:
                    boolean op1 = false;
                    boolean op2 = false;

                    BoolExprTokenizer.Token token1 = stack.pop();
                    BoolExprTokenizer.Token token2 = stack.pop();

                    switch (token1) {
                        case WRD:
                            op1 = sent.matches(".*?\\b" + stack_words.pop() + "\\b.*?");
                            break;
                        case STR:
                            op1 = sent.contains(stack_words.pop());
                            break;
                        case TRUE:
                            op1 = true;
                            break;
                        case FALSE:
                            op1 = false;
                            break;
                    }

                    boolean op2isWord = tokenEquals(token2, BoolExprTokenizer.Token.WRD)
                            || tokenEquals(token2, BoolExprTokenizer.Token.STR);

                    if (tokenEquals(token, BoolExprTokenizer.Token.AND) && !op1) {
                        if (op2isWord) stack_words.pop();
                        stack.push(BoolExprTokenizer.Token.FALSE);
                        outCopy.removeFirst();
                        break;
                    }

                    if (tokenEquals(token, BoolExprTokenizer.Token.OR) && op1) {
                        if (op2isWord) stack_words.pop();
                        stack.push(BoolExprTokenizer.Token.TRUE);
                        outCopy.removeFirst();
                        break;
                    }

                    switch (token2) {
                        case WRD:
                            op2 = sent.matches(".*?\\b" + stack_words.pop() + "\\b.*?");
                            break;
                        case STR:
                            op2 = sent.contains(stack_words.pop());
                            break;
                        case TRUE:
                            op2 = true;
                            break;
                        case FALSE:
                            op2 = false;
                            break;
                    }

                    boolean res = false;
                    if (tokenEquals(token, BoolExprTokenizer.Token.AND)) {
                        res = op1 && op2;
                    } else if (tokenEquals(token, BoolExprTokenizer.Token.OR)) {
                        res = op1 || op2;
                    }

                    if (res) stack.push(BoolExprTokenizer.Token.TRUE);
                    else stack.push(BoolExprTokenizer.Token.FALSE);

                    outCopy.removeFirst();
                    break;
            }
        }
        if (stack.isEmpty()) {
            throw new InvalidExpression("cannot be evaluated");
        }

        BoolExprTokenizer.Token token = stack.pop();
        if (tokenEquals(token, BoolExprTokenizer.Token.TRUE)) return true;
        if (tokenEquals(token, BoolExprTokenizer.Token.FALSE)) return false;
        if (tokenEquals(token, BoolExprTokenizer.Token.WRD)) return sent.matches(".*?\\b" + stack_words.pop() + "\\b.*?");
        if (tokenEquals(token, BoolExprTokenizer.Token.STR)) return sent.contains(stack_words.pop());
        else {
            throw new InvalidExpression();
        }

        //error
    }
//-----------------------------------------------------------------------------------

    public BoolExprParser() {
        opStack = new Stack<>();
        outputQueue = new LinkedList<>();
        outputQueueWords = new LinkedList<>();
    }

    /**
     * Saves, tokenizes and converts expr to postfix.
     * @param expr: Expression we want to save and compare sentences to.
     */
    public BoolExprParser(String expr) throws InvalidExpression {
        expression = expr;
        tokenizer = new BoolExprTokenizer(expr);
        tokenizer.tokenize();

        opStack = new Stack<>();
        outputQueue = new LinkedList<>();
        outputQueueWords = new LinkedList<>();
        parseInfixToPostfix();
    }

    /**
     * Updates expression, tokenizes and converts it to postfix.
     * @param expr: new expression.
     */
    public void setExpression(String expr) throws InvalidExpression {
        expression = expr;
        tokenizer.setExpression(expr);
        parseInfixToPostfix();
    }

    /**
     * Evaluates the sentence to see if it complies with the expression.
     * @param sentence: String we want to compare the expression with.
     * @return TRUE if it complies with the expression, FALSE otherwise.
     */
    public boolean eval(String sentence) throws InvalidExpression {
        return postEvaluator(sentence);
    }


}