package lexer;

import static lexer.TokenType.FALSE;
import static lexer.TokenType.ID;
import static lexer.TokenType.INT;
import static lexer.TokenType.SHARP;
import static lexer.TokenType.TRUE;
import static lexer.TransitionOutput.GOTO_ACCEPT_ID;
import static lexer.TransitionOutput.GOTO_ACCEPT_INT;
import static lexer.TransitionOutput.GOTO_EOS;
import static lexer.TransitionOutput.GOTO_FAILED;
import static lexer.TransitionOutput.GOTO_BOOL;
import static lexer.TransitionOutput.GOTO_MATCHED;
import static lexer.TransitionOutput.GOTO_SIGN;
import static lexer.TransitionOutput.GOTO_START;

enum State {

	START {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			case LETTER:
				context.append(v);
				return GOTO_ACCEPT_ID;
			case DIGIT:
				context.append(v);
				return GOTO_ACCEPT_INT;
			case SPECIAL_CHAR:
				context.append(v);
				if (v == '#') {
					return GOTO_BOOL;
				}else if (v == '+' || v == '-') {
					return GOTO_SIGN;
				}else {
					return GOTO_MATCHED(TokenType.fromSpecialCharactor(v), context.getLexime());
				}

			case WS:
				return GOTO_START;
			case END_OF_STREAM:
				return GOTO_EOS;

			default:
				throw new AssertionError();
			}
		}
	}

	,
	ACCEPT_ID {
		@Override
		public TransitionOutput transit(ScanContext context) {
			String Box;
			Char ch = context.getCharStream().nextChar();

			char v = ch.value();
			switch (ch.type()) {
			case LETTER:
			case DIGIT:
				context.append(v);
				return GOTO_ACCEPT_ID;
			case SPECIAL_CHAR:
				context.append(v);
				Box = context.getLexime();
				if (Box.equals(Token.ofName(Box).lexme())) {
					return GOTO_MATCHED(Token.ofName(Box).type(), Box);
				}
				return GOTO_FAILED;
			case WS:
			case END_OF_STREAM:
				Box = context.getLexime();
				if (Box.equals(Token.ofName(Box).lexme())) { 
					return GOTO_MATCHED(Token.ofName(Box).type(), Box);
				}
				return GOTO_MATCHED(ID, Box); 
			default:
				throw new AssertionError();
			}
		}
	},
	ACCEPT_INT {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			switch (ch.type()) {
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				context.append(ch.value());
				return GOTO_ACCEPT_INT;
			case SPECIAL_CHAR:
				return GOTO_FAILED;
			case WS:
			case END_OF_STREAM:
				return GOTO_MATCHED(INT, context.getLexime());
			default:
				throw new AssertionError();
			}
		}
	},
	SIGN {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();

			switch (ch.type()) {
			case LETTER:
				return GOTO_FAILED;
			case DIGIT:
				context.append(v);
				return GOTO_ACCEPT_INT;
			case WS:
			case END_OF_STREAM:
				String Box = context.getLexime();
				if (Box.equals("+")) {
					return GOTO_MATCHED(TokenType.PLUS, Box);
				} else if (Box.equals("-")) {
					return GOTO_MATCHED(TokenType.MINUS, Box);
				}
				return GOTO_FAILED;
			default:
				throw new AssertionError();
			}
		}
	},
	//T와F를 표시하는 상태인 BOOL
	BOOL {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch (ch.type()) {
			case LETTER:
				if (v == 'F') {
					context.append(v);
					return GOTO_MATCHED(FALSE, context.getLexime());
				} else if (v == 'T') {
					context.append(v);
					return GOTO_MATCHED(TRUE, context.getLexime());
				} else {
					return GOTO_FAILED;
				}
			default:
				throw new AssertionError();
			}
		}
	},
	MATCHED {
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	FAILED {
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	EOS {
		@Override
		public TransitionOutput transit(ScanContext context) {
			return GOTO_EOS;
		}
	};

	abstract TransitionOutput transit(ScanContext context);

}
