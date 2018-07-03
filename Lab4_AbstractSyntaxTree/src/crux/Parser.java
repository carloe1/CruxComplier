package crux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ast.Command;
import ast.Expression;
import ast.Statement;


public class Parser {
    public static String studentName = "Carlos Estrada";
    public static String studentID = "58042643";
    public static String uciNetID = "carloe1";
    
// Parser ==========================================
   
    private Scanner scanner;
    private Token currentToken;
    
    public Parser(Scanner scanner) throws IOException
    {
    	this.scanner = scanner;
    	currentToken = this.scanner.next();
    	this.symbolTable = new SymbolTable();
    	
    }
    
    public ast.Command parse() throws IOException
    {
        initSymbolTable();
        try {
            return program();
        } catch (QuitParseException q) {
            return new ast.Error(lineNumber(), charPosition(), "Could not complete parsing.");
        }
    }

/*
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
*/
    
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
    
    private void enterScope()
    {
    	SymbolTable newChild = new SymbolTable(symbolTable);
    	symbolTable = newChild;
    }
    
    private void exitScope()
    {
    	symbolTable = symbolTable.getParent();
    }
    
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
    
// Helper Methods ==========================================
    private boolean have(Token.Kind kind)
    {
        return currentToken.is(kind);
    }
    
    private boolean accept(Token.Kind kind) throws IOException
    {
        if (have(kind)) {
            currentToken = scanner.next();
            return true;
        }
        return false;
    }    
    
    private boolean expect(Token.Kind kind) throws IOException
    {
        if (accept(kind))
            return true;
        String errormessage = reportSyntaxError(kind);
        throw new QuitParseException(errormessage);
        //return false;
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
    
// Grammar Rules =====================================================
    
    // literal := INTEGER | FLOAT | TRUE | FALSE .
    public ast.Expression literal() throws IOException
    {
    	//enterRule(NonTerminal.LITERAL);
        String kindName = currentToken.kind();
        Token tok = new Token();
        if (kindName == "INTEGER"){
        	tok = expectRetrieve(Token.Kind.INTEGER);
        }
        else if (kindName == "FLOAT"){
        	tok = expectRetrieve(Token.Kind.FLOAT);
        }
        else if (kindName == "TRUE"){
        	tok = expectRetrieve(Token.Kind.TRUE);
        }
        else if (kindName == "FALSE"){
        	tok = expectRetrieve(Token.Kind.FALSE);
        }
        else {
        	String errorMessage = reportSyntaxError(NonTerminal.LITERAL);
            throw new QuitParseException(errorMessage);
        }
        
        Expression expr = Command.newLiteral(tok);
        //exitRule(NonTerminal.LITERAL);
        return expr;
    }
    
    // designator := IDENTIFIER { "[" expression0 "]" } .
    public Expression designator() throws IOException
    {
    	//enterRule(NonTerminal.DESIGNATOR);
        
    	Token addressOf = expectRetrieve(Token.Kind.IDENTIFIER);
        Symbol sym = tryResolveSymbol(addressOf);
        
        ast.Expression base = new ast.AddressOf(addressOf.lineNumber(), addressOf.charPosition(), sym);
        
        while (accept(Token.Kind.OPEN_BRACKET)) {
        	Token indexToken = currentToken;
            ast.Expression ammount =  expression0();
            expect(Token.Kind.CLOSE_BRACKET);
            base = new ast.Index(indexToken.lineNumber(), indexToken.charPosition(), base, ammount);
        }
        //exitRule(NonTerminal.DESIGNATOR);
        
        return base;
    }
    
    // type := IDENTIFIER .
    public void type() throws IOException
    {
    	//enterRule(NonTerminal.TYPE);
		expect(Token.Kind.IDENTIFIER);
    	//exitRule(NonTerminal.TYPE);
    }
    
    // op0 := ">=" | "<=" | "!=" | "==" | ">" | "<" .
    public Token op0() throws IOException
    {	
    	//enterRule(NonTerminal.OP0);
    	String lex = currentToken.lexeme();
    	Token tok;
    	
    	if (lex.equals(">=")){
    		tok = expectRetrieve(Token.Kind.GREATER_EQUAL);
    	}
    	else if (lex.equals("<=")){
    		tok = expectRetrieve(Token.Kind.LESSER_EQUAL);
    	}
    	else if (lex.equals("!=")){
    		tok = expectRetrieve(Token.Kind.NOT_EQUAL);
    	}
    	else if (lex.equals("==")){
    		tok = expectRetrieve(Token.Kind.EQUAL);
    	}
    	else if (lex.equals("<")){
    		tok = expectRetrieve(Token.Kind.LESS_THAN);
    	}
    	else if (lex.equals(">")){
    		tok = expectRetrieve(Token.Kind.GREATER_THAN);
    	}
    	else{
    		String errorMessage = reportSyntaxError(NonTerminal.OP0);
            throw new QuitParseException(errorMessage);
    	}
    	
    	//exitRule(NonTerminal.OP0);
    	return tok;
    }
    
    // op1 := "+" | "-" | "or" .
    public Token op1() throws IOException
    {
    	//enterRule(NonTerminal.OP1);
    	String lex = currentToken.lexeme();
    	
    	Token tok;
    	
    	if (lex.equals("+")){
    		tok = expectRetrieve(Token.Kind.ADD);
    	}
    	else if (lex.equals("-")){
    		tok = expectRetrieve(Token.Kind.SUB);
    	}
    	else if (lex.equals("or")){
    		tok = expectRetrieve(Token.Kind.OR);
    	}
    	else{
    		String errorMessage = reportSyntaxError(NonTerminal.OP1);
            throw new QuitParseException(errorMessage);
    	}
    	//exitRule(NonTerminal.OP1);
    	return tok;
    }
    
    // op2 := "*" | "/" | "and" .
    public Token op2() throws IOException
    {
    	//enterRule(NonTerminal.OP2);
    	String lex = currentToken.lexeme();
    	
    	Token tok;
    	
    	if (lex.equals("*")){
    		tok = expectRetrieve(Token.Kind.MUL);
    	}
    	else if (lex.equals("/")){
    		tok = expectRetrieve(Token.Kind.DIV);
    	}
    	else if (lex.equals("and")){
    		tok = expectRetrieve(Token.Kind.AND);
    	}
    	else{
    		String errorMessage = reportSyntaxError(NonTerminal.OP2);
            throw new QuitParseException(errorMessage);
    	}
    	//exitRule(NonTerminal.OP2);
    	return tok;
    }
    
    // expression0 := expression1 [ op0 expression1 ] .
    public ast.Expression expression0() throws IOException
    {
    	//enterRule(NonTerminal.EXPRESSION0);
    	ast.Expression expr = expression1();
    	
    	if (isOp0(currentToken.lexeme())){
    		Token op = op0();
    		ast.Expression rightSide = expression1();
    		expr = ast.Command.newExpression(expr, op, rightSide);
    	}
    	//exitRule(NonTerminal.EXPRESSION0);
    	return expr;
    }
    
    // expression1 := expression2 { op1  expression2 } .
    public ast.Expression expression1() throws IOException
    {
    	//enterRule(NonTerminal.EXPRESSION1);
    	ast.Expression expr = expression2();
    	
    	while (isOp1(currentToken.lexeme())){
    		Token op = op1();
    		ast.Expression rightSide = expression2();
    		expr = ast.Command.newExpression(expr, op, rightSide);
    	}
    	
    	//exitRule(NonTerminal.EXPRESSION1);
    	return expr;
    }
    
    // expression2 := expression3 { op2 expression3 } .
    public ast.Expression expression2() throws IOException
    {
    	//enterRule(NonTerminal.EXPRESSION2);
    	ast.Expression expr = expression3();
    	
    	while (isOp2(currentToken.lexeme())){
    		Token op = op2();
    		ast.Expression rightSide = expression3();
    		expr = ast.Command.newExpression(expr, op, rightSide);
    	}
    	//exitRule(NonTerminal.EXPRESSION2);
    	return expr;
    }
    
    // expression3 := "not" expression3
    //        | "(" expression0 ")"
    //        | designator
    //        | call-expression
    //        | literal .
    public ast.Expression expression3() throws IOException
    {
    	//enterRule(NonTerminal.EXPRESSION3);
    	ast.Expression expr = null;
    	
    	if (have(Token.Kind.NOT)){
    		Token notToken = expectRetrieve(Token.Kind.NOT);
    		expr = expression3();
    		expr = new ast.LogicalNot(notToken.lineNumber(), notToken.charPosition(), expr);
    	}
    	else if (have(Token.Kind.OPEN_PAREN)){
    		expect(Token.Kind.OPEN_PAREN);
    		expr = expression0();
    		expect(Token.Kind.CLOSE_PAREN);
    	}
    	else if (have(Token.Kind.IDENTIFIER)){
    		Token designatorToken = this.currentToken;
    		expr = designator();
    		expr = new ast.Dereference(designatorToken.lineNumber(), designatorToken.charPosition(), expr);
    	}
    	else if (have(Token.Kind.CALL)){
    		expr = call_expression();
    	}
    	else{
    		expr = literal();
    	}
    	
    	//exitRule(NonTerminal.EXPRESSION3);
    	return expr;
    }
    
    // expression-list := [ expression0 { "," expression0 } ] .
    public ast.ExpressionList expression_list() throws IOException
    {
    	//enterRule(NonTerminal.EXPRESSION_LIST);
    	ast.ExpressionList expressionList = new ast.ExpressionList(lineNumber(), charPosition());
    	
    	if (!have(Token.Kind.CLOSE_PAREN)){
    		expressionList.add(expression0());
    		while (have(Token.Kind.COMMA)){
    			expectRetrieve(Token.Kind.COMMA);
    			expressionList.add(expression0());
    		}
    	}
    	
    	//exitRule(NonTerminal.EXPRESSION_LIST);
    	return expressionList;
    }
    
    // call-expression := "::" IDENTIFIER "(" expression-list ")" .
    public ast.Call call_expression() throws IOException
    {
    	//enterRule(NonTerminal.CALL_EXPRESSION);
    	Token call = expectRetrieve(Token.Kind.CALL);
    	
    	Token identifier = expectRetrieve(Token.Kind.IDENTIFIER);
    	Symbol sym =tryResolveSymbol(identifier);
    	
    	expect(Token.Kind.OPEN_PAREN);
    	ast.ExpressionList args = expression_list();
    	
    	expect(Token.Kind.CLOSE_PAREN);
    	//exitRule(NonTerminal.CALL_EXPRESSION);
    	
    	ast.Call callExpression = new ast.Call(call.lineNumber(), call.charPosition(), sym, args);
    	return callExpression;
    }
    
    // parameter := IDENTIFIER ":" type .
    public Symbol parameter() throws IOException
    {
    	//enterRule(NonTerminal.PARAMETER);
    	
    	Token identifier = expectRetrieve(Token.Kind.IDENTIFIER);
    	Symbol symbol = tryDeclareSymbol(identifier);
    	expect(Token.Kind.COLON);
    	type();
    	
    	//exitRule(NonTerminal.PARAMETER);
    	return symbol;
    }
    
    // parameter-list := [ parameter { "," parameter } ] .
    public List<Symbol> parameter_list() throws IOException
    { 	
    	//enterRule(NonTerminal.PARAMETER_LIST);
    	List<Symbol> parameters = new ArrayList<Symbol>();
    	
    	if (!have(Token.Kind.CLOSE_PAREN)){
    		
    		parameters.add(parameter());
	    	
    		while(have(Token.Kind.COMMA)){
	    		expectRetrieve(Token.Kind.COMMA);
	    		parameters.add(parameter());
	    	}
    	}
    	//exitRule(NonTerminal.PARAMETER_LIST);
    	return parameters;
    }
    
    // variable-declaration := "var" IDENTIFIER ":" type ";" .
    public ast.VariableDeclaration variable_declaration() throws IOException
    {
    	//enterRule(NonTerminal.VARIABLE_DECLARATION);
    	Token varToken = expectRetrieve(Token.Kind.VAR);
    	Token identifier = expectRetrieve(Token.Kind.IDENTIFIER);
    	Symbol symbol = tryDeclareSymbol(identifier);
    	
    	expect(Token.Kind.COLON);
    	type();
    	expect(Token.Kind.SEMICOLON);
    	
    	//exitRule(NonTerminal.VARIABLE_DECLARATION);
    	ast.VariableDeclaration variable = new ast.VariableDeclaration(varToken.lineNumber(), varToken.charPosition(), symbol);
    	return variable;
    }
    
    //array-declaration := "array" IDENTIFIER ":" type "[" INTEGER "]" { "[" INTEGER "]" } ";" .
    public ast.ArrayDeclaration array_declaration() throws IOException
    {
    	//enterRule(NonTerminal.ARRAY_DECLARATION);
    	
    	Token arrayToken = expectRetrieve(Token.Kind.ARRAY);
    	Token identifier = expectRetrieve(Token.Kind.IDENTIFIER);
    	Symbol sym = tryDeclareSymbol(identifier);
    	
    	expect(Token.Kind.COLON);
    	type();
    	
    	do {
    		expect(Token.Kind.OPEN_BRACKET);
    		expectRetrieve(Token.Kind.INTEGER);
    		expect(Token.Kind.CLOSE_BRACKET);} while (!have(Token.Kind.SEMICOLON));
    	
    	expect(Token.Kind.SEMICOLON);
    	
    	//exitRule(NonTerminal.ARRAY_DECLARATION);
    	ast.ArrayDeclaration arrayDeclaration = new ast.ArrayDeclaration(arrayToken.lineNumber(), arrayToken.charPosition(), sym);
    	return arrayDeclaration;
    }
    
    // function-definition := "func" IDENTIFIER "(" parameter-list ")" ":" type statement-block .
    public ast.FunctionDefinition function_definition() throws IOException
    {
    	//enterRule(NonTerminal.FUNCTION_DEFINITION);
    	
		Token funcToken = expectRetrieve(Token.Kind.FUNC);
    	Token identifier = expectRetrieve(Token.Kind.IDENTIFIER);
    	Symbol func = tryDeclareSymbol(identifier);
    	
    	expect(Token.Kind.OPEN_PAREN);
    	enterScope();
    	
    	List<Symbol> args = parameter_list();
    	
    	expect(Token.Kind.CLOSE_PAREN);
    	expect(Token.Kind.COLON);
    	
    	type();
    	ast.StatementList statementBlock = statement_block();
    	exitScope();
    	
    	//exitRule(NonTerminal.FUNCTION_DEFINITION);
    	return new ast.FunctionDefinition(funcToken.lineNumber(), funcToken.charPosition(), func, args, statementBlock);
    }
    
    // declaration := variable-declaration | array-declaration | function-definition .
    public ast.Declaration declaration() throws IOException
    {
    	//enterRule(NonTerminal.DECLARATION);
    	
    	Token tok;
    	
    	ast.Declaration command = null;
    	
    	if (have(Token.Kind.VAR)){
    		command = variable_declaration();
    	}
    	else if (have(Token.Kind.ARRAY)){
    		command = array_declaration();
    	}
    	else if (have(Token.Kind.FUNC)){
    		command = function_definition();
    	}
    	else{
    		String errorMessage = reportSyntaxError(currentToken.kind);
            throw new QuitParseException(errorMessage);
    	}
    	
    	//exitRule(NonTerminal.DECLARATION);
		return command;
    }
    
    // declaration-list := { declaration } .
    public ast.DeclarationList declaration_list() throws IOException
    {
    	//enterRule(NonTerminal.DECLARATION_LIST);
    	ast.DeclarationList declarationList = new ast.DeclarationList(lineNumber(), charPosition());
    	
    	while (!have(Token.Kind.EOF)){
    		declarationList.add(declaration());
    	}
    	
    	//exitRule(NonTerminal.DECLARATION_LIST);
    	return declarationList;
    }
    
    // assignment-statement := "let" designator "=" expression0 ";" .
    public ast.Assignment assignment_statement() throws IOException
    {
    	//enterRule(NonTerminal.ASSIGNMENT_STATEMENT);
    	
    	Token letToken = expectRetrieve(Token.Kind.LET);
    	ast.Expression dest = designator();
    	expect(Token.Kind.ASSIGN);
    	ast.Expression source = expression0();
    	expect(Token.Kind.SEMICOLON);
    	
    	//exitRule(NonTerminal.ASSIGNMENT_STATEMENT);
    	
    	ast.Assignment assignment = new ast.Assignment(letToken.lineNumber(), letToken.charPosition(), dest, source);
    	return assignment;
    }
    
    // call-statement := call-expression ";" .
    public Statement call_statement() throws IOException
    {
    	//enterRule(NonTerminal.CALL_STATEMENT);
    	
    	ast.Call statement = call_expression();
    	expect(Token.Kind.SEMICOLON);
    	
    	
    	//exitRule(NonTerminal.CALL_STATEMENT);
    	return statement;
    }
    
    // if-statement := "if" expression0 statement-block [ "else" statement-block ] .
    public ast.IfElseBranch if_statement() throws IOException
    {
    	//enterRule(NonTerminal.IF_STATEMENT);
    	
    	Token ifToken = expectRetrieve(Token.Kind.IF);
    	ast.Expression cond = expression0();
    	
    	enterScope();
    	ast.StatementList thenBlock = statement_block();
    	ast.StatementList elseBlock = new ast.StatementList(lineNumber(), charPosition());
    	exitScope();
    	
    	if (have(Token.Kind.ELSE)){
    		expect(Token.Kind.ELSE);
    		enterScope();
    		thenBlock = statement_block();
    		exitScope();
    	}
    	
    	//exitRule(NonTerminal.IF_STATEMENT);
    	
    	ast.IfElseBranch ifElse = new ast.IfElseBranch(ifToken.lineNumber(), ifToken.charPosition(), cond, thenBlock, elseBlock);
    	return ifElse;
    }
    
    // while-statement := "while" expression0 statement-block .
    public ast.WhileLoop while_statement() throws IOException
    {
    	//enterRule(NonTerminal.WHILE_STATEMENT);
    	
    	Token tok = expectRetrieve(Token.Kind.WHILE);
    	ast.Expression cond = expression0();
    	enterScope();
    	ast.StatementList body = statement_block();
    	
    	exitScope();
    	//exitRule(NonTerminal.WHILE_STATEMENT);
    	
    	ast.WhileLoop whileStatement = new ast.WhileLoop(tok.lineNumber(), tok.charPosition(), cond, body);
    	return whileStatement;
    }
    
    // return-statement := "return" expression0 ";" .
    public ast.Return return_statement() throws IOException
    {
    	//enterRule(NonTerminal.RETURN_STATEMENT);
    	
    	Token returnToken = expectRetrieve(Token.Kind.RETURN);
    	ast.Expression arg = expression0();
    	expect(Token.Kind.SEMICOLON);
    	
    	//exitRule(NonTerminal.RETURN_STATEMENT);
    	ast.Return returnStatement = new ast.Return(returnToken.lineNumber(), returnToken.charPosition(), arg);
    	
    	return returnStatement;
    }
    
    // statement := variable-declaration
    //         | array-declaration
    //         | call-statement
    //         | assignment-statement
    //         | if-statement
    //         | while-statement
    //         | return-statement .
    public ast.Statement statement() throws IOException
    {
    	//enterRule(NonTerminal.STATEMENT);
    	
    	ast.Statement statement = null;
    	
    	if (have(Token.Kind.VAR)){
    		statement = variable_declaration();
    	}
    	else if (have(Token.Kind.ARRAY)){
    		statement = array_declaration();
    	}
    	else if (have(Token.Kind.CALL)){
    		statement = call_statement();
    	}
    	else if (have(Token.Kind.LET)){
    		statement = assignment_statement();
    	}
    	else if (have(Token.Kind.IF)){
    		statement = if_statement();
    	}
    	else if (have(Token.Kind.WHILE)){
    		statement = while_statement();
    	}
    	else if (have(Token.Kind.RETURN)){
    		statement = return_statement();
    	}
    	else{
    		expect(Token.Kind.CLOSE_BRACE);
    		statement = new ast.Error(lineNumber(), charPosition(), "No statement");
    	}
    	
    	//exitRule(NonTerminal.STATEMENT);
    	return statement;
    }
    
    // statement-list := { statement } .
    public ast.StatementList statement_list() throws IOException
    {
    	//enterRule(NonTerminal.STATEMENT_LIST);
    	
    	ast.StatementList statementList = new ast.StatementList(lineNumber(), charPosition());    	
    	while (!have(Token.Kind.CLOSE_BRACE)){
        	statementList.add(statement());
        }
        
        //exitRule(NonTerminal.STATEMENT_LIST);
        return statementList;
    }
    
    // statement-block := "{" statement-list "}" .
    public ast.StatementList statement_block() throws IOException
    {	
    	//enterRule(NonTerminal.STATEMENT_BLOCK);
    	
    	expect(Token.Kind.OPEN_BRACE);
    	ast.StatementList statementList = statement_list();
    	expect(Token.Kind.CLOSE_BRACE);
    	
    	//exitRule(NonTerminal.STATEMENT_BLOCK);
    	return statementList;
    }
    
    // program := declaration-list EOF .
    public ast.DeclarationList program() throws IOException
    {
    	// enterRule(NonTerminal.PROGRAM);
    	ast.DeclarationList declarationList = declaration_list();
    	expect(Token.Kind.EOF);
    	
    	// exitRule(NonTerminal.PROGRAM);
    	return declarationList;
    }
    
}
