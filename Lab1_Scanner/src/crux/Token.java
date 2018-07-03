package crux;

public class Token {
	
	// crux.Token.Kind: An enum of all possible token kinds
	public static enum Kind {
		AND("and"),
		OR("or"),
		NOT("not"),
		LET("let"),
		VAR("var"),
		ARRAY("array"),
		TRUE("true"),
		FALSE("false"),
		IF("if"),
		ELSE("else"),
		WHILE("while"),
		RETURN("return"),
		
		ADD("+"),
		SUB("-"),
		MUL("*"),
		DIV("/"),
		
		IDENTIFIER(),
		INTEGER(),
		FLOAT(),
		ERROR(),
		EOF(),
		
		OPEN_PAREN("("),
		CLOSE_PAREN(")"),
		OPEN_BRACE("{"),
		CLOSE_BRACE("}"),
		OPEN_BRACEKT("["),
		CLOSE_BRACKET("]"),
		GREATER_EQUAL(">="),
		LESSER_EQUAL("<="),
		NOT_EQUAL("!="),
		EQUAL("=="),
		GREATER_THAN(">"),
		LESS_THAN("<"),
		ASSIGN("="),
		COMMA(","),
		SEMICOLON(";"),
		COLON(":"),
		CALL("::");
		
		private String default_lexeme;
		
		Kind()
		{
			default_lexeme = "";
		}
		
		Kind(String lexeme)
		{
			default_lexeme = lexeme;
		}
		
		public boolean hasStaticLexeme()
		{
			return default_lexeme != null;
		}
		
		public static boolean matches(String lexeme)
		{
			
			for (Kind k : Kind.values()){
				if (lexeme.equals(k.default_lexeme)){
					return true;
				}
			}
			return false;
		}
		
		public String toString()
		{
			return default_lexeme;
		}
	}

	Kind kind; 					// The crux.Token.Kind
	private int lineNum; 		// The line number where this token occurs
	private int charPos; 		// The character position at which this token starts
	private String lexeme = ""; // The token's lexical contents
	          
	public static Token EOF(int linePos, int charPos){
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.EOF;
		tok.lexeme = "EOF";
		return tok;
	}
	
	public static Token IDENTIFIER(String lexeme,int linePos, int charPos){
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.IDENTIFIER;
		tok.lexeme = lexeme;
		return tok;
	}
	
	public static Token INTEGER(String lexeme, int linePos, int charPos){
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.INTEGER;
		tok.lexeme = lexeme;
		return tok;
	}
	
	public static Token FLOAT(String lexeme, int linePos, int charPos){
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.FLOAT;
		tok.lexeme = lexeme;
		return tok;
	}

	private Token(int lineNum, int charPos)
	{
		this.lineNum = lineNum;
		this.charPos = charPos;
		
		// if we don't match anything, signal error
		this.kind = Kind.ERROR;
		this.lexeme = "No Lexeme Given";
	}
	
	public Token(String lexeme, int lineNum, int charPos)
	{
		this.lineNum = lineNum;
		this.charPos = charPos;
		
		for (Kind k : Kind.values()){
			
			if (lexeme.equals(k.default_lexeme))
			{
				this.kind = k;
				this.lexeme = lexeme;
				return;
			}
		}
		
		if (this.lexeme == ""){
			// if we don't match anything, signal error
			this.kind = Kind.ERROR;
			this.lexeme = "Unrecognized lexeme: " + lexeme;
		}
	}
	
	public int lineNumber()
	{
		return lineNum;
	}
	public int charPosition()
	{
		return charPos;
	}
	public String lexeme()
	{
		return this.lexeme;
	}
	
	public boolean nameAndLexeme()
	{
		String name = kind.name();
		return name == "IDENTIFIER" || name == "INTEGER" || name == "FLOAT" || name == "ERROR";
	}
	
	public String toString()
	{
		if (nameAndLexeme()){
			return kind.name() + "(" + this.lexeme + ")(lineNum:" + lineNum + ", charPos:" + charPos + ")";
		}
		return this.kind.name() + "(lineNum:" + lineNum + ", charPos:" + charPos + ")";
	}
}