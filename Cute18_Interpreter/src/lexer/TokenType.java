package lexer;

public enum TokenType {
	ID, INT, QUESTION, TRUE, FALSE, NOT, PLUS, MINUS, TIMES, DIV, LT, GT, EQ, APOSTROPHE, L_PAREN, R_PAREN, DEFINE, LAMBDA, COND, QUOTE, CAR, CDR, CONS, SHARP, ATOM_Q, NULL_Q, EQ_Q;

	static TokenType fromSpecialCharactor(char ch) {
		switch (ch) {
		case '(':
			return L_PAREN;
		case ')':
			return R_PAREN;
		case '+':
			return PLUS;
		case '-':
			return MINUS;
		case '*':
			return TIMES;
		case '/':
			return DIV;
		case '<':
			return LT;
		case '>':
			return GT;
		case '=':
			return EQ;
		case '\'':
			return APOSTROPHE;
		case '#':
			return SHARP;
		case '?':
			return QUESTION;
		case '`':
			return QUOTE;
		

		default:
			throw new IllegalArgumentException("unregistered char: " + ch);
		}
	}
}
