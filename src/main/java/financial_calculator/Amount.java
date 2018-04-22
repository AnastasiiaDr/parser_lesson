package financial_calculator;

public class Token {

    private String value;
    private TokenType type;

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }

    public Token() {
    }

    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(TokenType type) {
        this.type = type;
    }
}
