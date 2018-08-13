package parser.ast;

import java.util.HashMap;
import java.util.Map;
import lexer.*;

public class FunctionNode implements Node {
	public enum FunctionType { // 각 FunctionNode에 대해서 TokenType을 반환
		ATOM_Q {
			TokenType tokenType() {
				return TokenType.ATOM_Q;
			}
		},
		CAR {
			TokenType tokenType() {
				return TokenType.CAR;
			}
		},
		CDR {
			TokenType tokenType() {
				return TokenType.CDR;
			}
		},
		COND {
			TokenType tokenType() {
				return TokenType.COND;
			}
		},
		CONS {
			TokenType tokenType() {
				return TokenType.CONS;
			}
		},
		DEFINE {
			TokenType tokenType() {
				return TokenType.DEFINE;
			}
		},
		EQ_Q {
			TokenType tokenType() {
				return TokenType.EQ_Q;
			}
		},
		LAMBDA {
			TokenType tokenType() {
				return TokenType.LAMBDA;
			}
		},
		NOT {
			TokenType tokenType() {
				return TokenType.NOT;
			}
		},
		NULL_Q {
			TokenType tokenType() {
				return TokenType.NULL_Q;
			}
		};
		// TokenType에 FunctionType을 넣어서 사용할 자료형인 Map
		private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType, FunctionType>();
		static { // Map에 TokenType등을 넣어준다.
			for (FunctionType fType : FunctionType.values()) {
				fromTokenType.put(fType.tokenType(), fType);
			}
		}

		static FunctionType getFunType(TokenType tType) {
			return fromTokenType.get(tType);
		}

		abstract TokenType tokenType();
	}

	public FunctionType value;

	@Override
	public String toString() { // FunctionType은 출력시 내용만 출력되면 되므로 value를 사용해서 출력을
								// 해준다.
		return "Func: " + value.name();
	}

	public FunctionNode(TokenType tType) { // Type을 이용해서 value를 저장
		FunctionType fType = FunctionType.getFunType(tType);
		value = fType;
	}

}