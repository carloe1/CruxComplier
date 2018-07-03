package crux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    public static String studentName = "Carlos Estrada";
    public static String studentID = "58042643";
    public static String uciNetID = "carloe1";
    
    
// Grammar Rule Reporting ==========================================
    
    private int parseTreeRecursionDepth = 0;
    private StringBuffer parseTreeBuffer = new StringBuffer();

    public void enterRule(NonTerminal nonTerminal) {
        String lineData = new String();
        for(int i = 0; i < parseTreeRecursionDepth; i++)
        {
            lineData += "  ";
        }
        lineData += nonTerminal.name();
        //System.out.println("descending " + lineData);
        parseTreeBuffer.append(lineData + "\n");
        parseTreeRecursionDepth++;
    }
    
    private void exitRule(NonTerminal nonTerminal)
    {
        parseTreeRecursionDepth--;
    }
    
    public String parseTreeReport()
    {
        return parseTreeBuffer.toString();
    }
    
// SymbolTable Management ==============================================
    private SymbolTable symbolTable;
    
    
    private void initSymbolTable()
    {
    	// readInt() : int, Prompts the user for an integer.
    	this.symbolTable.insert("readInt");
    	
    	// readFloat() : float, Prompts the user for an integer.
    	this.symbolTable.insert("readFloat");
    	
    	//printBool(arg:bool) : void, Prints a bool value to the screen.
    	this.symbolTable.insert("printBool");
    	
    	// printInt(arg:int) : void, Prints an integer value to the screen.
    	this.symbolTable.insert("printInt");
    	
    	// printFloat(arg:float) : void, Prints a float value to the screen.
    	this.symbolTable.insert("printFloat");
    	
    	// println() : void, Prints newline character to the screen.
    	this.symbolTable.insert("println");
    }
    
    // "Semantically, the crux has a layer of scope for each function. According to the Crux Grammar, 
    // curly braces only occur in a statement-block...
    // 
    // Conceptually, each time the Parser encounters and statement-block it introduces a new scope to
    // contain any newly declared Symbols (variables and functions). This rule is loosened somewhat
    // because Curx's symbol semantics specify that parameters of a function are also scoped with the
    // function body."
    //
    private void enterScope()
    {
    	SymbolTable newChild = new SymbolTable(symbolTable);
    	symbolTable = newChild;
    }
    
    // pop
    //
    private void exitScope()
    {
    	symbolTable = symbolTable.getParent();
    }
    
    // "When the parser encounters a variable declaration or function definition, it calls 
    // tryDeclareSymbol to perform an insertion in the currentSymbolTable. If the signal a failure, 
    // then tryDeclareSymbol logs a "DeclareSymbolError. Otherwise, insertion succeeded and tryDeclareSymbol
    // returns the newly created Symbol so that the Parser may resume parsing."
    //
    private Symbol tryResolveSymbol(Token ident)
    {
        assert(ident.is(Token.Kind.IDENTIFIER));
        String name = ident.lexeme();
        try {
            return symbolTable.lookup(name);
        } catch (SymbolNotFoundError e) {
            String message = reportResolveSymbolError(name, ident.lineNumber(), ident.charPosition());
            return new ErrorSymbol(message);
        }
    }

    private String reportResolveSymbolError(String name, int lineNum, int charPos)
    {
        String message = "ResolveSymbolError(" + lineNum + "," + charPos + ")[Could not find " + name + ".]";
        errorBuffer.append(message + "\n");
        errorBuffer.append(symbolTable.toString() + "\n");
        return message;
    }

    private Symbol tryDeclareSymbol(Token ident)
    {
        assert(ident.is(Token.Kind.IDENTIFIER));
        String name = ident.lexeme();
        try {
            return symbolTable.insert(name);
        } catch (RedeclarationError re) {
            String message = reportDeclareSymbolError(name, ident.lineNumber(), ident.charPosition());
            return new ErrorSymbol(message);
        }
    }

    private String reportDeclareSymbolError(String name, int lineNum, int charPos)
    {
        String message = "DeclareSymbolError(" + lineNum + "," + charPos + ")[" + name + " already exists.]";
        errorBuffer.append(message + "\n");
        errorBuffer.append(symbolTable.toString() + "\n");
        return message;
    }    

// Error Reporting =========================================================================
    
    private StringBuffer errorBuffer = new StringBuffer();
    
    private String reportSyntaxError(NonTerminal nt)
    {
        String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[expectRetrieveed a token from " + nt.name() + " but got " + currentToken.kind() + ".]";
        errorBuffer.append(message + "\n");
        return message;
    }
     
    private String reportSyntaxError(Token.Kind kind)
    {
        String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[expectRetrieveed " + kind + " but got " + currentToken.kind() + ".]";
        errorBuffer.append(message + "\n");
        return message;
    }
    
    public String errorReport()
    {
        return errorBuffer.toString();
    }
    
    public boolean hasError()
    {
        return errorBuffer.length() != 0;
    }
    
    private class QuitParseException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        public QuitParseException(String errorMessage) {
            super(errorMessage);
        }
    }
    
    private int lineNumber()
    {
        return currentToken.lineNumber();
    }
    
    private int charPosition()
    {
        return currentToken.charPosition();
    }
    
// Helper Methods ===============================================================
    
    private boolean have(Token.Kind kind)
    {
        return currentToken.is(kind);
    }
    
    private boolean have(NonTerminal nt)
    {
        return nt.firstSet().contains(currentToken.kind());
    }

    private boolean accept(Token.Kind kind) throws IOException
    {
        if (have(kind)) {
            currentToken = scanner.next();
            return true;
        }
        return false;
    }    
    
    private boolean accept(NonTerminal nt) throws IOException
    {
        if (have(nt)) {
            currentToken = scanner.next();
            return true;
        }
        return false;
    }
    
    private Token expectRetrieve(Token.Kind kind) throws IOException
    {
        Token tok = currentToken;
        if (accept(kind))
            return tok;
        String errorMessage = reportSyntaxError(kind);
        throw new QuitParseException(errorMessage);
        //return ErrorToken(errorMessage);
    }
        
    private Token expectRetrieve(NonTerminal nt) throws IOException
    {
        Token tok = currentToken;
        if (accept(nt))
            return tok;
        String errorMessage = reportSyntaxError(nt);
        throw new QuitParseException(errorMessage);
        //return ErrorToken(errorMessage);
    }
    
    private boolean isOp0(String lex)
    {
    	return lex.equals(">=") || lex.equals("<=") || lex.equals("!=") || lex.equals("==") || lex.equals(">") || lex.equals("<");
    }
    
    private boolean isOp1(String lex)
    {
    	return lex.equals("+") || lex.equals("-") || lex.equals("or");
    }
    
    private boolean isOp2(String lex)
    {
    	return lex.equals("*") || lex.equals("/") || lex.equals("and");
    }
    
// Parser ================================================================
    
    private Scanner scanner;
    private Token currentToken;
    
    public Parser(Scanner scanner) throws IOException
    {
    	this.scanner = scanner;
    	currentToken = this.scanner.next();
    	this.symbolTable = new SymbolTable();
    	
    }
    
    public void parse() throws IOException
    {
        initSymbolTable();
        try {
            program();
        } catch (QuitParseException q) {
            errorBuffer.append("SyntaxError(" + lineNumber() + "," + charPosition() + ")");
            errorBuffer.append("[Could not complete parsing.]");
        }
    }
   
// Grammar Rules =====================================================
    
    // literal := INTEGER | FLOAT | TRUE | FALSE .
    public void literal() throws IOException
    {
    	enterRule(NonTerminal.LITERAL);
        String kindName = currentToken.kind.name();
        
        if (kindName == "INTEGER"){
        	expectRetrieve(Token.Kind.INTEGER);
        }
        else if (kindName == "FLOAT"){
        	expectRetrieve(Token.Kind.FLOAT);
        }
        else if (kindName == "TRUE"){
        	expectRetrieve(Token.Kind.TRUE);
        }
        else if (kindName == "FALSE"){
        	expectRetrieve(Token.Kind.FALSE);
        }
        else {
        	String errorMessage = reportSyntaxError(NonTerminal.LITERAL);
            throw new QuitParseException(errorMessage);
        }
        exitRule(NonTerminal.LITERAL);
    }
    
    // designator := IDENTIFIER { "[" expression0 "]" } .
    public void designator() throws IOException
    {
    	enterRule(NonTerminal.DESIGNATOR);
        Token identifier = expectRetrieve(Token.Kind.IDENTIFIER);
        this.tryResolveSymbol(identifier);
        while (accept(Token.Kind.OPEN_BRACKET)) {
            expression0();
            expectRetrieve(Token.Kind.CLOSE_BRACKET);
        }
        exitRule(NonTerminal.DESIGNATOR);
    }
    
    // type := IDENTIFIER .
    public void type() throws IOException
    {
    	enterRule(NonTerminal.TYPE);
    	Token identifier = expectRetrieve(Token.Kind.IDENTIFIER);
    	exitRule(NonTerminal.TYPE);
    }
    
    // op0 := ">=" | "<=" | "!=" | "==" | ">" | "<" .
    public void op0() throws IOException
    {	
    	enterRule(NonTerminal.OP0);
    	String lex = currentToken.lexeme();
    	
    	if (lex.equals(">=")){
    		expectRetrieve(Token.Kind.GREATER_EQUAL);
    	}
    	else if (lex.equals("<=")){
    		expectRetrieve(Token.Kind.LESSER_EQUAL);
    	}
    	else if (lex.equals("!=")){
    		expectRetrieve(Token.Kind.NOT_EQUAL);
    	}
    	else if (lex.equals("==")){
    		expectRetrieve(Token.Kind.EQUAL);
    	}
    	else if (lex.equals("<")){
    		expectRetrieve(Token.Kind.LESS_THAN);
    	}
    	else if (lex.equals(">")){
    		expectRetrieve(Token.Kind.GREATER_THAN);
    	}
    	else{
    		String errorMessage = reportSyntaxError(NonTerminal.OP0);
            throw new QuitParseException(errorMessage);
    	}
    	exitRule(NonTerminal.OP0);
    }
    
    // op1 := "+" | "-" | "or" .
    public void op1() throws IOException
    {
    	enterRule(NonTerminal.OP1);
    	String lex = currentToken.lexeme();
    	
    	if (lex.equals("+")){
    		expectRetrieve(Token.Kind.ADD);
    	}
    	else if (lex.equals("-")){
    		expectRetrieve(Token.Kind.SUB);
    	}
    	else if (lex.equals("or")){
    		expectRetrieve(Token.Kind.OR);
    	}
    	else{
    		String errorMessage = reportSyntaxError(NonTerminal.OP1);
            throw new QuitParseException(errorMessage);
    	}
    	exitRule(NonTerminal.OP1);
    }
    
    // op2 := "*" | "/" | "and" .
    public void op2() throws IOException
    {
    	enterRule(NonTerminal.OP2);
    	String lex = currentToken.lexeme();
    	
    	if (lex.equals("*")){
    		expectRetrieve(Token.Kind.MUL);
    	}
    	else if (lex.equals("/")){
    		expectRetrieve(Token.Kind.DIV);
    	}
    	else if (lex.equals("and")){
    		expectRetrieve(Token.Kind.AND);
    	}
    	else{
    		String errorMessage = reportSyntaxError(NonTerminal.OP2);
            throw new QuitParseException(errorMessage);
    	}
    	exitRule(NonTerminal.OP2);
    }
    
    // expression0 := expression1 [ op0 expression1 ] .
    public void expression0() throws IOException
    {
    	enterRule(NonTerminal.EXPRESSION0);
    	
    	expression1();
    	if (isOp0(currentToken.lexeme())){
    		op0();
    		expression1();
    	}
    	
    	exitRule(NonTerminal.EXPRESSION0);
    }
    
    // expression1 := expression2 { op1  expression2 } .
    public void expression1() throws IOException
    {
    	enterRule(NonTerminal.EXPRESSION1);
    	
    	expression2();
    	while (isOp1(currentToken.lexeme())){
    		op1();
    		expression2();
    	}
    	
    	exitRule(NonTerminal.EXPRESSION1);
    }
    
    // expression2 := expression3 { op2 expression3 } .
    public void expression2() throws IOException
    {
    	enterRule(NonTerminal.EXPRESSION2);
    	
    	expression3();
    	while (isOp2(currentToken.lexeme())){
    		op2();
    		expression3();
    	}
    	
    	exitRule(NonTerminal.EXPRESSION2);
    }
    
    // expression3 := "not" expression3
    //        | "(" expression0 ")"
    //        | designator
    //        | call-expression
    //        | literal .
    public void expression3() throws IOException
    {
    	enterRule(NonTerminal.EXPRESSION3);
    	
    	if (have(Token.Kind.NOT)){
    		expectRetrieve(Token.Kind.NOT);
    		expression3();
    	}
    	else if (have(Token.Kind.OPEN_PAREN)){
    		expectRetrieve(Token.Kind.OPEN_PAREN);
    		expression0();
    		expectRetrieve(Token.Kind.CLOSE_PAREN);
    	}
    	else if (have(Token.Kind.IDENTIFIER)){
    		designator();
    	}
    	else if (have(Token.Kind.CALL)){
    		call_expression();
    	}
    	else{
    		literal();
    	}
    	
    	exitRule(NonTerminal.EXPRESSION3);
    }
    
    // call-expression := "::" IDENTIFIER "(" expression-list ")" .
    public void call_expression() throws IOException
    {
    	enterRule(NonTerminal.CALL_EXPRESSION);
    	
    	expectRetrieve(Token.Kind.CALL);
    	Token identifier = expectRetrieve(Token.Kind.IDENTIFIER);
    	tryResolveSymbol(identifier);
    	expectRetrieve(Token.Kind.OPEN_PAREN);
    	expression_list();
    	expectRetrieve(Token.Kind.CLOSE_PAREN);
    	
    	exitRule(NonTerminal.CALL_EXPRESSION);
    }
    
    // expression-list := [ expression0 { "," expression0 } ] .
    public void expression_list() throws IOException
    {
    	enterRule(NonTerminal.EXPRESSION_LIST);
    	
    	if (!have(Token.Kind.CLOSE_PAREN)){
    		expression0();
    		while (have(Token.Kind.COMMA)){
    			expectRetrieve(Token.Kind.COMMA);
    			expression0();
    		}
    	}
    	
    	exitRule(NonTerminal.EXPRESSION_LIST);
    }
    
    // parameter := IDENTIFIER ":" type .
    public void parameter() throws IOException
    {
    	enterRule(NonTerminal.PARAMETER);
    	
    	Token identifier = expectRetrieve(Token.Kind.IDENTIFIER);
    	tryDeclareSymbol(identifier);
    	expectRetrieve(Token.Kind.COLON);
    	type();
    	
    	exitRule(NonTerminal.PARAMETER);
    }
    
    // parameter-list := [ parameter { "," parameter } ] .
    public void parameter_list() throws IOException
    { 	
    	enterRule(NonTerminal.PARAMETER_LIST);
    	
    	if (!have(Token.Kind.CLOSE_PAREN)){
    		parameter();
	    	while(have(Token.Kind.COMMA)){
	    		expectRetrieve(Token.Kind.COMMA);
	    		parameter();
	    	}
    	}
    	
    	exitRule(NonTerminal.PARAMETER_LIST);
    }
    
    // variable-declaration := "var" IDENTIFIER ":" type ";" .
    public void variable_declaration() throws IOException
    {
    	enterRule(NonTerminal.VARIABLE_DECLARATION);
    	
    	expectRetrieve(Token.Kind.VAR);
    	Token identifier = expectRetrieve(Token.Kind.IDENTIFIER);
    	tryDeclareSymbol(identifier);
    	expectRetrieve(Token.Kind.COLON);
    	type();
    	expectRetrieve(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.VARIABLE_DECLARATION);
    }
    
    //array-declaration := "array" IDENTIFIER ":" type "[" INTEGER "]" { "[" INTEGER "]" } ";" .
    public void array_declaration() throws IOException
    {
    	enterRule(NonTerminal.ARRAY_DECLARATION);
    	
    	expectRetrieve(Token.Kind.ARRAY);
    	Token identifier = expectRetrieve(Token.Kind.IDENTIFIER);
    	tryDeclareSymbol(identifier);
    	expectRetrieve(Token.Kind.COLON);
    	type();
    	
    	do {
    		expectRetrieve(Token.Kind.OPEN_BRACKET);
    		expectRetrieve(Token.Kind.INTEGER);
    		expectRetrieve(Token.Kind.CLOSE_BRACKET);} while (!have(Token.Kind.SEMICOLON));
    	
    	expectRetrieve(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.ARRAY_DECLARATION);
    }
    
    // function-definition := "func" IDENTIFIER "(" parameter-list ")" ":" type statement-block .
    public void function_definition() throws IOException
    {
    	enterRule(NonTerminal.FUNCTION_DEFINITION);
    	
    	@SuppressWarnings("unused")
		Token func = expectRetrieve(Token.Kind.FUNC);
    	Token identifier = expectRetrieve(Token.Kind.IDENTIFIER);
    	tryDeclareSymbol(identifier);
    	
    	expectRetrieve(Token.Kind.OPEN_PAREN);
    	enterScope();
    	
    	parameter_list();
    	
    	expectRetrieve(Token.Kind.CLOSE_PAREN);
    	
    	expectRetrieve(Token.Kind.COLON);
    	
    	type();
    	statement_block();
    	exitScope();
    	
    	exitRule(NonTerminal.FUNCTION_DEFINITION);
    }
    
    // declaration := variable-declaration | array-declaration | function-definition .
    public void declaration() throws IOException
    {
    	enterRule(NonTerminal.DECLARATION);
    	
    	if (have(Token.Kind.VAR)){
    		variable_declaration();
    	}
    	else if (have(Token.Kind.ARRAY)){
    		array_declaration();
    	}
    	else if (have(Token.Kind.FUNC)){
    		function_definition();
    	}
    	else{
    		String errorMessage = reportSyntaxError(currentToken.kind);
            throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.DECLARATION);
    }
    
    // declaration-list := { declaration } .
    public void declaration_list() throws IOException
    {
    	enterRule(NonTerminal.DECLARATION_LIST);
    	
    	while (!have(Token.Kind.EOF)){
    		declaration();
    	}
    	
    	exitRule(NonTerminal.DECLARATION_LIST);
    }
    
    // assignment-statement := "let" designator "=" expression0 ";" .
    public void assignment_statement() throws IOException
    {
    	enterRule(NonTerminal.ASSIGNMENT_STATEMENT);
    	
    	expectRetrieve(Token.Kind.LET);
    	designator();
    	expectRetrieve(Token.Kind.ASSIGN);
    	expression0();
    	expectRetrieve(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.ASSIGNMENT_STATEMENT);
    }
    
    // call-statement := call-expression ";" .
    public void call_statement() throws IOException
    {
    	enterRule(NonTerminal.CALL_STATEMENT);
    	
    	call_expression();
    	expectRetrieve(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.CALL_STATEMENT);
    }
    
    // if-statement := "if" expression0 statement-block [ "else" statement-block ] .
    public void if_statement() throws IOException
    {
    	enterRule(NonTerminal.IF_STATEMENT);
    	
    	expectRetrieve(Token.Kind.IF);
    	expression0();
    	
    	enterScope();
    	statement_block();
    	exitScope();
    	
    	if (have(Token.Kind.ELSE)){
    		expectRetrieve(Token.Kind.ELSE);
    		enterScope();
    		statement_block();
    		exitScope();
    	}
    	
    	exitRule(NonTerminal.IF_STATEMENT);
    }
    
    // while-statement := "while" expression0 statement-block .
    public void while_statement() throws IOException
    {
    	enterRule(NonTerminal.WHILE_STATEMENT);
    	
    	expectRetrieve(Token.Kind.WHILE);
    	expression0();
    	enterScope();
    	statement_block();
    	exitScope();
    	
    	exitRule(NonTerminal.WHILE_STATEMENT);
    }
    
    // return-statement := "return" expression0 ";" .
    public void return_statement() throws IOException
    {
    	enterRule(NonTerminal.RETURN_STATEMENT);
    	
    	expectRetrieve(Token.Kind.RETURN);
    	expression0();
    	expectRetrieve(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.RETURN_STATEMENT);
    }
    
    // statement := variable-declaration
    //         | call-statement
    //         | assignment-statement
    //         | if-statement
    //         | while-statement
    //         | return-statement .
    public void statement() throws IOException
    {
    	enterRule(NonTerminal.STATEMENT);
    	
    	if (have(Token.Kind.VAR)){
    		variable_declaration();
    	}
    	else if (have(Token.Kind.LET)){
    		assignment_statement();
    	}
    	else if (have(Token.Kind.CALL)){
    		call_statement();
    	}
    	else if (have(Token.Kind.IF)){
    		if_statement();
    	}
    	else if (have(Token.Kind.WHILE)){
    		while_statement();
    	}
    	else if (have(Token.Kind.RETURN)){
    		return_statement();
    	}
    	else{
    		expectRetrieve(Token.Kind.CLOSE_BRACE);
    	}
    	
    	exitRule(NonTerminal.STATEMENT);
    }
    
    // statement-list := { statement } .
    public void statement_list() throws IOException
    {
    	enterRule(NonTerminal.STATEMENT_LIST);
    	
        while (!have(Token.Kind.CLOSE_BRACE)){
        	statement();
        }
        
        exitRule(NonTerminal.STATEMENT_LIST);
    }
    
    // statement-block := "{" statement-list "}" .
    public void statement_block() throws IOException
    {	
    	enterRule(NonTerminal.STATEMENT_BLOCK);
    	
    	expectRetrieve(Token.Kind.OPEN_BRACE);
    	statement_list();
    	expectRetrieve(Token.Kind.CLOSE_BRACE);
    	
    	exitRule(NonTerminal.STATEMENT_BLOCK);
    }
    
    // program := declaration-list EOF .
    public void program() throws IOException
    {
    	enterRule(NonTerminal.PROGRAM);
    	
    	declaration_list();
    	expectRetrieve(Token.Kind.EOF);
    	
    	exitRule(NonTerminal.PROGRAM);
    }
}

