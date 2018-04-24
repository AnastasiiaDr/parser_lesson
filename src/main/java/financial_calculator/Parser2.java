package financial_calculator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static financial_calculator.TokenType.*;
import static financial_calculator.Utils.handleError;

public class Parser {

    private int index;
    private String token;
    private TokenType tokenType;
    private TokenType currentTokenType;
    private String expression;

    TokenType currentCurrency;

    public Parser(String expression) {
        this.expression = expression.trim();
        token = "";
        index = 0;
    }

    public String evaluate() throws ParserException {
        getToken();

        Amount result = evalAdditionAndSubtraction();

        if (!isEndOfExpression()) {
            handleError(ErrorType.SYNTAX_ERROR);
        }

        return result.getValue() + " " + result.getCurrency();
    }

    private Amount evalAdditionAndSubtraction() throws ParserException {
        Amount result = new Amount();
        Amount partialResult = new Amount();

        char op;

        //currentCurrency = tokenType;
        result.setCurrency(tokenType);
        result.setValue(evalConvertOperations().getValue());

      //  getToken();
        while (index < expression.length() && ((op = token.charAt(0)) == '+' || op == '-')) {
            getToken();
            partialResult = evalConvertOperations();
            if (!isTheSameCurrency(result.getCurrency(), partialResult.getCurrency())) {
                handleError(ErrorType.CURRENCY_ERROR);
            }
            switch (op) {
                case '+':
                    result.setValue(result.getValue() + partialResult.getValue());
                    break;
                case '-':
                    result.setValue(result.getValue() - partialResult.getValue());
                    break;
            }
          //  getToken();
        }
        return result;
    }

    private Amount evalConvertOperations() throws ParserException {
        Amount result;
        Amount partialResult;
        String op = "";
        String operation;
//        if (tokenType.equals(CONVERT_OPERATION)) {
//            operation = token;
//            getToken();
//        }
    //    if (!tokenType.equals(CONVERT_OPERATION)) {
            result = checkParentheses();
     //   }
        while (index < expression.length() && tokenType.equals(CONVERT_OPERATION)) {
       // if (tokenType.equals(CONVERT_OPERATION)) {
            currentTokenType = tokenType;
            operation = token;
            getToken();
            result = checkParentheses();
            //getToken();
            if (operation.equals("toDollar")) {
                if (currentTokenType.equals(EURO)) {
                    getToken();
                } else if (currentTokenType.equals(DOLLAR)) {
                    handleError(ErrorType.TO_DOLLAR_CONVERT_ERROR);
                }
            } else if (operation.equals("toEuro")) {
                if (currentTokenType.equals(DOLLAR)) {
                    getToken();
                } else if (currentTokenType.equals(EURO)){
                    handleError(ErrorType.TO_EURO_CONVERT_ERROR);
                }
            } else {
                handleError(ErrorType.SYNTAX_ERROR);
            }
        }

        return result;
    }

    private Amount checkParentheses() throws ParserException {
        Amount result = null;

        if (token.equals("(")) {
            getToken();
            result = evalAdditionAndSubtraction();
       //     currentTokenType = tokenType;
//            getToken();
            if (!token.equals(")")) {
                handleError(ErrorType.PARENTHESES_ERROR);
            }
            getToken();
        } else {
            result = getAtom();
        }
        return result;
    }

    private Amount getAtom() throws ParserException {
        Amount result = null;
        if (tokenType != DOLLAR && tokenType != EURO) {
            handleError(ErrorType.CURRENCY_ERROR);
        }
        try {
            result = new Amount(Double.parseDouble(token), tokenType);
           // result = Double.parseDouble(token);
            getToken();
        } catch (NumberFormatException e) {
            handleError(ErrorType.NUMBER_FORMAT_ERROR);
        }
        return result;
    }




    public List<String> getTokens() throws ParserException {
        List<String> tokens = new ArrayList<String>();
        while (index < expression.length()) {
            getToken();
            tokens.add(token);
        }
        return tokens;
    }

    private void getToken() throws ParserException {
        token = "";
        tokenType = null;

        if (isEndOfExpression()) {
            setEndOfExpressionToken();
            return;
        }

        skipSpaces();

        if (isEndOfExpression()) {
            setEndOfExpressionToken();
            return;
        }

        char op = expression.charAt(index);
        if (isDelimiter(op)) {
            token = "" + op;
            tokenType = DELIMITER;
            index++;
        } else if (Character.isLetter(op)) {
            while (index < expression.length() && (Character.isLetter(op = expression.charAt(index)))) {
                token += op;
                index++;
            }
            if (isConvertOperation(token)) {
                tokenType = CONVERT_OPERATION;
            } else {
                handleError(ErrorType.SYNTAX_ERROR);
            }
        } else if (op == '$') {
            index++;
            while (index < expression.length() && (Character.isDigit(op = expression.charAt(index)))) {
                token += op;
                index++;
            }
            if (!token.equals("")) {
                tokenType = DOLLAR;
            } else {
                handleError(ErrorType.SYNTAX_ERROR);
            }
        } else if (Character.isDigit(op)) {
            while (index < expression.length() && Character.isDigit(op = expression.charAt(index))) {
                token += op;
                index++;
            }
            String tmp = "";
            while (index < expression.length() && (Character.isLetter(op = expression.charAt(index)))) {
                tmp += op;
                index++;
            }
            if (tmp.equalsIgnoreCase("eur")) {
                tokenType = EURO;
            } else {
                handleError(ErrorType.SYNTAX_ERROR);
            }
        } else {
            handleError(ErrorType.SYNTAX_ERROR);
        }
    }

    private boolean isConvertOperation(String element) {
        return element.equalsIgnoreCase("toDollar") || element.equalsIgnoreCase("toEuro");
    }

    private boolean isDelimiter(char element) {
        Set<Character> delimiters = new HashSet<Character>();
        delimiters.add('(');
        delimiters.add(')');
        delimiters.add(' ');
        delimiters.add('+');
        delimiters.add('-');
        return delimiters.contains(element);
    }

    private boolean isEndOfExpression() {
        return index == expression.length();
    }

    private void setEndOfExpressionToken() {
        token = "\n";
    }

    private void skipSpaces() {
        while (index < expression.length() && expression.charAt(index) == ' ') {
            index++;
        }
    }

    private boolean isTheSameCurrency(TokenType currentCurrency, TokenType newValueCurrency) {
        return currentCurrency.equals(newValueCurrency);
    }
}
