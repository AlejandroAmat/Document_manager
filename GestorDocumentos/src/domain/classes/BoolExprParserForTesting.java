package domain.classes;

import domain.exceptions.InvalidExpression;

import java.util.LinkedList;
import java.util.Stack;

public class BoolExprParserForTesting extends BoolExprParser {
//-----------------------------------------------------------------------------------

    public BoolExprParserForTesting() {
        opStack = new Stack<>();
        outputQueue = new LinkedList<>();
        outputQueueWords = new LinkedList<>();
    }

    /**
     * Saves, tokenizes and converts expr to postfix.
     * @param expr: Expression we want to save and compare sentences to.
     */
    public BoolExprParserForTesting(String expr, BoolExprTokenizer t) throws InvalidExpression {
        expression = expr;
        tokenizer = t;

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
        parseInfixToPostfix();
    }

}
