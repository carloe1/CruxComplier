package types;

import java.util.HashMap;
import java.util.List;
import crux.Symbol;
import ast.*;

public class TypeChecker implements CommandVisitor {
    
    private HashMap<Command, Type> typeMap;
    private String currentFunctionName;
    private Type currentReturnType;
    
    public TypeChecker()
    {
        typeMap = new HashMap<Command, Type>();
        errorBuffer = new StringBuffer();
    }

// Helper Methods ================================================

    private void put(Command node, Type type)
    {
        if (type instanceof ErrorType) {
            reportError(node.lineNumber(), node.charPosition(), ((ErrorType)type).getMessage());
        }
        typeMap.put(node, type);
    }
    
    public Type getType(Command node)
    {
        return typeMap.get(node);
    }
    
    public boolean check(Command ast)
    {
        ast.accept(this);
        return !hasError();
    }
    
    private Type checkAndGetType(Command node)
    {
    	this.check(node);
    	return getType(node);
    }
    
// Error Report ===============================================
    
    private StringBuffer errorBuffer;
    
    String INVALID_SIGNATURE = "Function main has invalid signature.";
    
    private ErrorType INVALID_RETURN_TYPE(Type retType)
    {
    	String errorString = "Function " + currentFunctionName + " returns " + currentReturnType + " not " + retType + ".";
    	return new ErrorType(errorString);
    }
    
    private ErrorType VOID_ARGUMENT(int pos, String funcName)
    {
    	String errorString = "Function " + funcName + " has a void argument in position " + pos + ".";
    	return new ErrorType(errorString);
    }
    
    private ErrorType ARGUMENT_ERROR(int pos, String funcName, ErrorType error)
    {
    	String errorString = "Function " + funcName + " has an error in argument in position " + pos + ": " + error.getMessage();
    	return new ErrorType(errorString);
    }
    
    private ErrorType IF_ERROR(Type condType)
    {
    	String errorString = "IfElseBranch requires bool condition not " + condType + ".";
    	return new ErrorType(errorString);
    }
    
    private ErrorType WHILE_ERROR(Type condType)
    {
    	String errorString = "WhileLoop requires bool condition not " + condType + ".";
    	return new ErrorType(errorString);
    }
    
    /* Useful error strings:
     *
     * "Not all paths in function " + currentFunctionName + " have a return."
     *
     * 
     * "WhileLoop requires bool condition not " + condType + "." 
     *
     * "Variable " + varName + " has invalid type " + varType + "."
     * "Array " + arrayName + " has invalid base type " + baseType + "."
     */
    
    public boolean hasError()
    {
        return errorBuffer.length() != 0;
    }
    
    public String errorReport()
    {
        return errorBuffer.toString();
    }
    
    private void reportError(int lineNum, int charPos, String message)
    {
        errorBuffer.append("TypeError(" + lineNum + "," + charPos + ")");
        errorBuffer.append("[" + message + "]" + "\n");
    }

// Visit Methods ================================================
    
    @Override
    public void visit(ExpressionList node)
    {
    	TypeList typelist = new TypeList();
    	for (Expression expression : node){
    		typelist.append(this.checkAndGetType((Command) expression));
    	}
    	this.put(node, typelist);
    }

    @Override
    public void visit(DeclarationList node)
    {    
    	for (Declaration declaration : node){
    		declaration.accept(this);
    	}
    }

    @Override
    public void visit(StatementList node)
    {	
    	for (Statement statement : node){
    		statement.accept(this);
    	}
    }

    @Override
    public void visit(AddressOf node) 
    {
    	Type nodeType = node.symbol().type();
    	this.put(node, new AddressType(nodeType));
    }

    @Override
    public void visit(LiteralBool node) 
    {
    	this.put(node, new BoolType());
    }

    @Override
    public void visit(LiteralFloat node)
    {    
    	this.put(node, new FloatType());
    }

    @Override
    public void visit(LiteralInt node)
    {
    	this.put(node, new IntType());
    }

    @Override
    public void visit(VariableDeclaration node) {
        Symbol variable = node.symbol();
        Type variableType = Type.getBaseType(variable.type().toString());
        
        this.put(node, variableType);
    }

    @Override
    public void visit(ArrayDeclaration node) {
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(FunctionDefinition node) 
    { 
    	StatementList body = node.body();
    	Symbol functionSymbol = node.function();
    	List<Symbol> arguments = node.arguments();
    	String functionName = functionSymbol.name();
    	Type returnType = ((FuncType) functionSymbol.type()).returnType();
    	
    	if (functionName.equals("main")){
    			if(!functionSymbol.toString().equals("Symbol(main:func(TypeList()):void)")){
		    		this.put(node, new ErrorType(this.INVALID_SIGNATURE));
		    		return;
    			}
    	}
    	else{
    		int position = 0;
    		for (Symbol argument : arguments){
    			if (argument.type() instanceof ErrorType ){
    				this.put(node, this.ARGUMENT_ERROR(position, functionName, (ErrorType)argument.type())); 
    				return;
    			}
    			else if (argument.type() instanceof VoidType){
    				this.put(node, this.VOID_ARGUMENT(position, functionName)); 
    				return;
    			}
    			position++;
    		}
    	}
    	
    	this.currentFunctionName = functionName;
    	this.currentReturnType = returnType;
    	
    	body.accept(this);
    	
    	put(node, returnType);
    }

    @Override
    public void visit(Comparison node) {
        Type left = this.checkAndGetType((Command) node.leftSide());
        Type right = this.checkAndGetType((Command) node.rightSide());
        
        Type resultType = left.compare(right);
        this.put(node, resultType);
    }
    
    @Override
    public void visit(Addition node) {
        Type left = this.checkAndGetType((Command) node.leftSide());
        Type right = this.checkAndGetType((Command) node.rightSide());
        
        Type resultType = left.add(right);
        this.put(node, resultType);
    }
    
    @Override
    public void visit(Subtraction node) {
        Type left = this.checkAndGetType( (Command) node.leftSide());
        Type right = this.checkAndGetType( (Command) node.rightSide());
        
        Type resultType = left.sub(right);
        this.put(node, resultType);
    }
    
    @Override
    public void visit(Multiplication node) {
        Type left = this.checkAndGetType((Command) node.leftSide());
        Type right = this.checkAndGetType((Command) node.rightSide());
        
        Type resultType = left.mul(right);
        this.put(node, resultType);
    }
    
    @Override
    public void visit(Division node) {
        Type left = this.checkAndGetType( (Command) node.leftSide());
        Type right = this.checkAndGetType( (Command) node.rightSide());
        
        Type resultType = left.div(right);
        this.put(node, resultType);
    }
    
    @Override
    public void visit(LogicalAnd node) {
        Type left = this.checkAndGetType((Command) node.leftSide());
        Type right = this.checkAndGetType((Command) node.rightSide());
        
        Type resultType = left.and(right);
        this.put(node, resultType);
    }

    @Override
    public void visit(LogicalOr node) {
        Type left = this.checkAndGetType( (Command) node.leftSide());
        Type right = this.checkAndGetType( (Command) node.rightSide());
        
        Type resultType = left.or(right);
        this.put(node, resultType);
    }

    @Override
    public void visit(LogicalNot node) {
        Type bool = this.checkAndGetType( (Command) node.expression());
        bool = bool.not();
        
        this.put(node, bool);
    }
    
    @Override
    public void visit(Dereference node) {
        
    	AddressOf dereferenceExpression = (AddressOf) node.expression();
    	Type referenceType = dereferenceExpression.symbol().type();
    	dereferenceExpression.accept(this);
    	put(node, referenceType);
    }

    @Override
    public void visit(Index node) {
        //throw new RuntimeException("Implement this");
    }

    @Override
    public void visit(Assignment node) 
    {    
    	Type destType = checkAndGetType((Command) node.destination());
    	Type sourceType = checkAndGetType((Command) node.source());
    	
    	this.put(node, destType.assign(sourceType) );
    }

    @Override
    public void visit(Call node) {
    	
    	Type argType;
    	TypeList args = new TypeList();
    	for (Expression expression : node.arguments()){
    		
    		argType = this.checkAndGetType((Command)expression);
    		args.append(argType);
    	}
    	
    	Type callType = node.function().type();
    	this.put(node, callType.call(args));
    }

	@Override
    public void visit(IfElseBranch node) {
        Type conditionType = this.checkAndGetType((Command)node.condition());
        if (!(conditionType instanceof BoolType)){
        	this.put(node, IF_ERROR(conditionType));
        }
        
	
	}

    @Override
    public void visit(WhileLoop node) {
        Type conditionType = this.checkAndGetType((Command) node.condition());
        if  (!(conditionType instanceof BoolType)){
        	this.put(node, WHILE_ERROR(conditionType));
        }
    }

    @Override
    public void visit(Return node) {
    	
    	Expression returnExpression = node.argument();
    	Type returnType = this.checkAndGetType((Command)returnExpression);
    	
    	if (currentReturnType.equals("void") || returnType == null){
    	}
    	else if (!returnType.equivalent(this.currentReturnType)){
    		this.put(node, this.INVALID_RETURN_TYPE(returnType));
    		return;
    	}
    	
    	this.put(node, returnType);
    }

    @Override
    public void visit(ast.Error node) {
        put(node, new ErrorType(node.message()));
    }
}
