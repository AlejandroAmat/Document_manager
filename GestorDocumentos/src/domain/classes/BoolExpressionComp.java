package domain.classes;

import domain.exceptions.InvalidExpression;

public class BoolExpressionComp extends Comparator<Document> {

    private String expression;
    private BoolExprParser parser;

//--------------------------------------------------------

    /**
     * CLASS CONSTRUCTOR: saves expression and initializes parser.
     * @param expr: Boolean expression that we want to compare documents with.
     */
    public BoolExpressionComp(String expr) throws InvalidExpression {
        expression = expr;
        parser = new BoolExprParser(expression);
    }

    /**
     * Getter for expression.
     * @return this.expression
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Sets expression to param expr and updates the parser's expression.
     * @param expr New expression
     */
    public void setExpression(String expr) throws InvalidExpression {
        expression = expr;
        parser = new BoolExprParser(expression);
    }


    /**
     * Checks if one of the sentences of document complies with the boolean expression.
     * @param document:
     * @return TRUE if at least one sentence complies with the expression. FALSE otherwise.
     */
    @Override
    public boolean compare(Document document) throws InvalidExpression {
            for (String sentence : document.getSentences()) {
                if (parser.eval(sentence)) {
                    addToList(document);
                    return true;
                }
            }

        return false;
    }

}
