package parser.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import lexer.*;
import parser.ast.*;

import javax.naming.Reference;

public class CuteParser {
	private Iterator<Token> tokens;
	private static Node END_OF_LIST = new Node() {
	};

	public CuteParser(String str) {
		try {
			tokens = Scanner.scan(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Token getNextToken() {
		if (!tokens.hasNext())
			return null;
		return tokens.next();
	}

	public Node parseExpr() {
		Token t = getNextToken();
		if (t == null) {
			System.out.println("No more token");
			return null;
		}
		TokenType tType = t.type();
		String tLexeme = t.lexme();
		switch (tType) {
		case ID:
			return new IdNode(tLexeme);
		case INT:
			if (tLexeme == null)
				System.out.println("???");
			return new IntNode(tLexeme);
		// 새로 수정된 부분
		case DIV:
		case EQ:
		case MINUS:
		case GT:
		case PLUS:
		case TIMES:
		case LT:
			return new BinaryOpNode(tType); // 새로 수정된 부분
		case ATOM_Q:
		case CAR:
		case CDR:
		case COND:
		case CONS:
		case DEFINE:
		case EQ_Q:
		case LAMBDA:
		case NOT:
		case NULL_Q:
			return new FunctionNode(tType);// 새로 수정된 부분
		case FALSE:
			return BooleanNode.FALSE_NODE;
		case TRUE:
			return BooleanNode.TRUE_NODE;
		case L_PAREN:
			return parseExprList();
		case R_PAREN:
			return END_OF_LIST;
		case APOSTROPHE:
			return new QuoteNode(parseExpr());
		case QUOTE:
			return new QuoteNode(parseExpr());
		default:
			System.out.println("Parsing Error!");
			return null;
		}
	} // 새로 수정된 부분

	private ListNode parseExprList() {
		Node head = parseExpr();
		if (head == null)
			return null;
		if (head == END_OF_LIST) // if next token is RPAREN
			return ListNode.ENDLIST;
		ListNode tail = parseExprList();
		if (tail == null)
			return null;
		return ListNode.cons(head, tail);
	}
}