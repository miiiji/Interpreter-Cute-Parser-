package lexer;

class Char {
	private final char value;
	private final CharacterType type;

	enum CharacterType { // 기존의 Charcter Type에 true,false를 처리하기 위해서 #상태를 추가
		LETTER, DIGIT, SPECIAL_CHAR, WS, END_OF_STREAM;
	}

	static Char of(char ch) {
		return new Char(ch, getType(ch));
	}

	static Char end() {
		return new Char(Character.MIN_VALUE, CharacterType.END_OF_STREAM);
	}

	private Char(char ch, CharacterType type) {
		this.value = ch;
		this.type = type;
	}

	char value() {
		return this.value;
	}

	CharacterType type() {
		return this.type;
	}

	private static CharacterType getType(char ch) {
		int code = (int) ch; // 읽어온 문자가 알파벳인 경우 letter 타입 반환
		if ((code >= (int) 'A' && code <= (int) 'Z') || (code >= (int) 'a' && code <= (int) 'z')) {
			return CharacterType.LETTER;
		}

		if (Character.isDigit(ch)) { // 숫자인 경우 digit반환
			return CharacterType.DIGIT;
		}

		switch (ch) { // sepcial_char인 경우 switch문으로 반환
		case '(':
			return CharacterType.SPECIAL_CHAR;
		case ')':
			return CharacterType.SPECIAL_CHAR;
		case '+':
			return CharacterType.SPECIAL_CHAR;
		case '-':
			return CharacterType.SPECIAL_CHAR;
		case '*':
			return CharacterType.SPECIAL_CHAR;
		case '/':
			return CharacterType.SPECIAL_CHAR;
		case '<':
			return CharacterType.SPECIAL_CHAR;
		case '>':
			return CharacterType.SPECIAL_CHAR;
		case '=':
			return CharacterType.SPECIAL_CHAR;
		case '\'':
			return CharacterType.SPECIAL_CHAR;
		case '`':
			return CharacterType.SPECIAL_CHAR;
		case '?':
			return CharacterType.LETTER;
		case '#':
			return CharacterType.SPECIAL_CHAR;
		}

		if (Character.isWhitespace(ch)) {
			return CharacterType.WS;
		}
		if (ch == '\'') {
			return CharacterType.END_OF_STREAM;
		}
		throw new IllegalArgumentException("input=" + ch);
	}
}
