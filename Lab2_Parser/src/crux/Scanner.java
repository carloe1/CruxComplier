package crux;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class Scanner implements Iterable<Token> {
	public static String studentName = "Carlos Estrada";
	public static String studentID = "58042643";
	public static String uciNetID = "carloe1";
	
	private int lineNum;  		// current line count
	private int charPos;  		// character offset for current line
	private int currentChar;	// contains the current char
	private int nextChar; 		// contains the next char (-1 == EOF)
	private Reader input;		// contains the file contents, .read() to read next char
	
	private boolean fileEnded = false; // set to true when file ends, returns a null token
	
	Scanner(Reader reader) throws IOException
	{
		input = reader;
		lineNum = 1; 
		charPos = 1;
		currentChar = input.read();
		nextChar = input.read();
	}		

	private void scanNext() throws IOException
	{
		charPos++;
		currentChar = nextChar;
		nextChar = input.read();
	}
	private void newLine() throws IOException
	{
		charPos = 0;
		lineNum++;
		scanNext();
	}
	
	public void ignoreSpaceAndNewLines() throws IOException
	{
		while (currentChar == 32){
				scanNext();
			}
		ignoreComments();
		if (currentChar == 10){
			newLine();
			ignoreSpaceAndNewLines();
		}
	}
	
	public void ignoreComments() throws IOException
	{
		if (isComment(currentChar) && isComment(nextChar)){
			while(currentChar != 10){
				scanNext();
				if (currentChar == -1){
					return;
				}
			}
			newLine();
		}
	}
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
	public boolean isCharOrUnderScore(int c)
	{
		return ((65 <= c)&&(c <= 90))||((97<=c)&&(c<=122)) || (c==95);
	}
	
	public boolean isInt(int i)
	{
		return (48<=i) && (i<=57);
	}
	
	public boolean isBool(int b)
	{
		return (b==60) || (b==61) || (b==62) || (b==33);
	}
	public boolean isComment(int i)
	{
		return i == 47;
	}
	public boolean isColon(int i)
	{
		return i == 58;
	}
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
	public Token stringToken() throws IOException
	{
		int startPos = charPos;
		String lexeme = "" + (char) currentChar;
		
		while (isCharOrUnderScore(nextChar) || isInt(nextChar)){
			scanNext();
			lexeme += (char) currentChar;
		}
		
		if (Token.Kind.matches(lexeme)){
			return new Token(lexeme, lineNum, startPos);
		}
		
		return Token.IDENTIFIER(lexeme, lineNum, startPos);
	}
	
	public Token integerToken(String lexeme) throws IOException
	{	
		boolean floatFlag = false;
		int startPos = charPos;
		scanNext();
		
		while(isInt(currentChar) || currentChar == 46){
			if (currentChar == 46 && !floatFlag){
				floatFlag = true;
			}
			else if ((currentChar == 46 && !isInt(nextChar)) || (currentChar == 46 &&floatFlag)){
				break;
			}
			lexeme += (char) currentChar;
			scanNext();
		}
		
		if (floatFlag){
			return Token.FLOAT(lexeme,lineNum, startPos);
		}
		return Token.INTEGER(lexeme,lineNum, startPos);
	}
	
	public Token boolToken() throws IOException
	{
		int startPos = charPos;
		String lexeme = "" + (char)currentChar;
		
		if (nextChar == 61){
			scanNext();
			lexeme += (char) currentChar;
		}
		
		return new Token(lexeme, lineNum, startPos);
	}
	
	public Token callOrColon(String lexeme) throws IOException
	{
		int startPos = charPos;
		if (isColon(nextChar)){
			scanNext();
			lexeme += (char) currentChar;
			return new Token(lexeme, lineNum, startPos);
		}
		
		return new Token(lexeme, lineNum, startPos);
	}
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
	
	public Token next() throws IOException 
	{
		
		Token t = null;
		ignoreSpaceAndNewLines();
		
		String lexeme = "" + (char)currentChar;
		
		if (fileEnded){
			return t;
		}
		else if ( isCharOrUnderScore(currentChar) || (currentChar==95)){
			t = stringToken();
		}
		else if (isInt(currentChar)){
			t = integerToken(lexeme);
			return t;
		}
		else if ( isBool(currentChar)){
			t = boolToken();
		}
		else if (currentChar==-1){
			fileEnded = true;
			t = Token.EOF(lineNum, charPos);
		}
		else if (isColon(currentChar)){
			t = callOrColon(lexeme);
		}
		else{
			t = new Token(lexeme, lineNum, charPos);
		}
		
		scanNext();
		return t;
	}

	public Iterator<Token> iterator()
	{
		// TODO Auto-generated method stub
		return null;
	}
}