package mips;

import java.util.regex.Pattern;
import crux.Symbol;
import ast.*;
import types.*;

public class CodeGen implements ast.CommandVisitor {
    
    private StringBuffer errorBuffer = new StringBuffer();
    private TypeChecker tc;
    private Program program;
    private ActivationRecord currentFunction;
    private String funcLabel;

    public CodeGen(TypeChecker tc)
    {
        this.tc = tc;
        this.program = new Program();
    }

// Error Report Methods ====================================================
    
    public boolean hasError()
    {
        return errorBuffer.length() != 0;
    }
    
    public String errorReport()
    {
        return errorBuffer.toString();
    }

    private class CodeGenException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        public CodeGenException(String errorMessage) {
            super(errorMessage);
        }
    }

// Helper Methods ==========================================================
    
    public boolean generate(Command ast)
    {
        try {
            currentFunction = ActivationRecord.newGlobalFrame();
            ast.accept(this);
            return !hasError();
        } catch (CodeGenException e) {
            return false;
        }
    }
    
    public Program getProgram()
    {
        return program;
    }
    
    public void addComment(String comment){
    	this.program.appendInstruction("## " + comment + " ##");
    }
    
    public void performOperation(Type type, String operation){
    	
    	if (type instanceof IntType){
        	this.program.popInt("$t1");
        	this.program.popInt("$t0");
        	this.program.appendInstruction("	" + operation + " $t0, $t0, $t1");
        	this.program.pushInt("$t0");
        }
        else if (type instanceof FloatType){
        	this.program.popFloat("$f1");
        	this.program.popFloat("$f0");
        	this.program.appendInstruction("	" + operation+ ".s $f0, $f0, $f1");
        	this.program.pushFloat("$f0");
        }
    }
    
// Visit Methods ===========================================================
    @Override
    public void visit(ExpressionList node) {
        for (Expression expression : node){
    		expression.accept(this);
    	}
    }

    @Override
    public void visit(DeclarationList node) {
    	for (Declaration declaration : node){
    		declaration.accept(this);
    	}
    }

    @Override
    public void visit(StatementList node) {
    	for (Statement statement: node){
    		statement.accept(this);
    		if (statement instanceof Call){
    			Type returnType = this.tc.getType((Call)statement);
    			if (returnType.equivalent(new FloatType())){
    				this.program.popFloat("$t0");
    			}
    			else if (returnType.equivalent(new IntType()) || returnType.equivalent(new BoolType())){
    				this.program.popInt("$t0");
    			}
    		}
    	}
    }

    @Override
    public void visit(AddressOf node) {
    	this.addComment("Getting Address Of: " + node.symbol().name());
        
    	this.currentFunction.getAddress(this.program, "$t0", node.symbol());
        this.program.pushInt("$t0");
    }

    @Override
    public void visit(LiteralBool node) {
    	this.addComment("LiteralBool: " + node.value());
    	
        int boolData = 0;	// set to false originally
        if (node.value() == ast.LiteralBool.Value.TRUE){
        	boolData = 1;
        }

        this.program.appendInstruction("	li $t0, " + boolData);
        this.program.pushInt("$t0");
    }

    @Override
    public void visit(LiteralFloat node) {
    	this.addComment("LiteralFloat: " + node.value());
    	
        float floatValue = node.value();
        String instruction = "	li.s $f0, " + floatValue;
        this.program.appendInstruction(instruction);
        this.program.pushFloat("$f0");
    }

    @Override
    public void visit(LiteralInt node) {
    	this.addComment("LiteralInt:" + node.value());
    	
    	int intValue = node.value();
    	String instruction = "	li $t0, " + intValue;
    	this.program.appendInstruction(instruction);
    	this.program.pushInt("$t0");
    }

    @Override
    public void visit(VariableDeclaration node) {
    	this.addComment("Variable Declaration: " + node.symbol().name());
        this.currentFunction.add(this.program, node);
    }

    @Override
    public void visit(ArrayDeclaration node) {
    	this.addComment("ArrayDeclaration: " + node.symbol().name());
        this.currentFunction.add(this.program, node);
    }

    @Override
    public void visit(FunctionDefinition node) {
    	
    	String funcName = node.symbol().name();
    	this.funcLabel = this.program.newLabel();
    	Type returnType = this.tc.getType(node);
    	
    	if (!funcName.equals("main")){
    		funcName = "cruxfunc." + funcName;
    	}
    	
    	this.currentFunction = new ActivationRecord(node, currentFunction);
    	int startPos = this.program.appendInstruction(funcName + ":");
    	node.body().accept(this);
    	
		program.appendInstruction(this.funcLabel + ":");
    	this.program.insertPrologue(startPos+1, this.currentFunction.stackSize());
    	
    	if (!funcName.equals("main")){	
        	if (returnType.equivalent(new FloatType())){
	    		this.program.popFloat("$v0");
	    	}
	    	else if (returnType.equivalent(new IntType()) || returnType.equivalent(new BoolType())){
	    		this.program.popInt("$v0");
	    	}
	    	this.program.appendEpilogue(this.currentFunction.stackSize());
    	}
    	else{
    		if (this.currentFunction.stackSize() > 0){
    			this.program.appendInstruction("	addu $sp, $sp " + this.currentFunction.stackSize());
    		}
    		this.program.appendExitSequence();
    	}
    	
    	this.currentFunction = this.currentFunction.parent();
    }

    @Override
    public void visit(Addition node) {
    	this.addComment("Addition: " + node.leftSide() + " + " + node.rightSide());
    	
        node.leftSide().accept(this);
        node.rightSide().accept(this);
        
        Type type = this.tc.getType(node);
        this.performOperation(type, "add");
    }

    @Override
    public void visit(Subtraction node) {
    	this.addComment("Subtraction: " + node.leftSide() + " - " + node.rightSide());
    	node.leftSide().accept(this);
        node.rightSide().accept(this);
        
        Type type = this.tc.getType(node);
        this.performOperation(type, "sub");
    }

    @Override
    public void visit(Multiplication node) {
    	this.addComment("Multiplication: " + node.leftSide() + " - " + node.rightSide());
        node.leftSide().accept(this);
        node.rightSide().accept(this);
        
        Type type = this.tc.getType(node);
        this.performOperation(type, "mul");
    }

    @Override
    public void visit(Division node) {
    	this.addComment("Division: " + node.leftSide() + " - " + node.rightSide());
    	node.leftSide().accept(this);
        node.rightSide().accept(this);
        
        Type type = this.tc.getType(node);
        this.performOperation(type, "div");
    }

    @Override
    public void visit(LogicalAnd node) {
        node.leftSide().accept(this);
        node.rightSide().accept(this);
        
        this.program.popInt("$t1");
        this.program.popInt("$t0");
        this.program.appendInstruction("	and $t0, $t0, $t1");
        program.pushInt("$t0");
    }

    @Override
    public void visit(LogicalOr node) {
    	 node.leftSide().accept(this);
         node.rightSide().accept(this);
         
         this.program.popInt("$t1");
         this.program.popInt("$t0");
         this.program.appendInstruction("	or $t0, $t0, $t1");
         program.pushInt("$t0");
         
    }
    
    @Override
    public void visit(LogicalNot node) {
        node.expression().accept(this);
    }

    @Override
    public void visit(Comparison node) {
        node.leftSide().accept(this);
        node.rightSide().accept(this);
        
        String operation = new String("");
        Type comparisonType = this.tc.getType((Command)node.leftSide());
        if (comparisonType instanceof IntType){
        	this.program.popInt("$t1");
        	this.program.popInt("$t0");
        	
        	switch (node.operation()){
            	case LT: operation = "slt"; break;
            	case LE: operation = "sle"; break;
            	case EQ: operation = "seq"; break;
            	case NE: operation = "sne"; break;
            	case GE: operation = "sgt"; break;
            	case GT: operation = "sge"; break;
        	}
        	this.program.appendInstruction("\t" + operation + " $t0, $t0, $t1");
        	this.program.pushInt("$t0");
        }
        else if (comparisonType instanceof FloatType){
        	this.program.popFloat("$f2");
        	this.program.popFloat("$f0");
        	switch (node.operation()){
	        	case LT: operation = "c.lt.s"; break;
	        	case LE: operation = "c.le.s"; break;
	        	case EQ: operation = "c.eq.s"; break;
	        	case NE: operation = "c.eq.s"; break;
	        	case GE: operation = "c.lt.s"; break;
	        	case GT: operation = "c.le.s"; break;
        	}
        	this.program.appendInstruction("\t" + operation + " $f0, $f2");
        	this.program.pushFloat("$f2");
        }
    }

    @Override
    public void visit(Dereference node) {
    	node.expression().accept(this);
        this.program.popInt("$t0");
        this.program.appendInstruction("	lw $t0, 0($t0)");
        this.program.pushInt("$t0");
    }

    @Override
    public void visit(Index node) {
        node.amount().accept(this);
        node.base().accept(this);
        
        this.program.popInt("$t1");
        this.program.popInt("$t0");
        
        Type type =  this.tc.getType((Command)node.amount());
        
        this.program.appendInstruction("	li $t2, " + ActivationRecord.numBytes(type));
        this.program.appendInstruction("	mul $t1, $t1, $t2");
        this.program.appendInstruction("	add $t0, $t0, $t1");
        this.program.pushInt("$t1");
    }

    @Override
    public void visit(Assignment node) {
    	this.addComment("Assignment: " + node.destination());
    	node.destination().accept(this);
    	node.source().accept(this);
    	
    	this.program.popInt("$t0");
    	this.program.popInt("$t1");
    	this.program.appendInstruction("	sw $t0, 0($t1)\t\t\t\t# Assignment");
    }

    @Override
    public void visit(Call node) {
    	
    	String funcName = "func." + node.function().name();
    	node.arguments().accept(this);
    	
    	// Check to see if the function is predefined or not
    	if (!funcName.matches("func.print(Bool|Int|Float|ln)|func.read(Bool|Int|Float)")){
    		funcName = "crux" + funcName;
    	}
    	
    	// Jump to function
    	this.program.appendInstruction("	jal " + funcName + "\t\t\t # Function Call");
    	if (node.arguments().size() > 0){
	    	int argSize = 0;
	    	for (Expression expression : node.arguments()){
	    		Type type = this.tc.getType((Command) expression);
	    		argSize += ActivationRecord.numBytes(type);
	    	}
	    	this.program.appendInstruction("	addi $sp, $sp, " + argSize);
    	}
    	// Return value if function is not void type
    	Type type = this.tc.getType(node);
    	if (!type.equivalent(new VoidType())){
    		this.program.appendInstruction(" 	subu $sp, $sp, 4");
    		this.program.appendInstruction("	sw $v0, 0($sp)");
    	}
    }

    @Override
    public void visit(IfElseBranch node) {
        String elseLabel = this.program.newLabel();
        String joinLabel = this.program.newLabel();
        
        node.condition().accept(this);
        this.program.popInt("$t1");
        
        // Check to see if it condition is true, else jump to else label
        program.appendInstruction("	beqz $t1, " + elseLabel);
        node.thenBlock().accept(this);
        
        // Once if or else statements are done, they meet at join label
        this.program.appendInstruction("	j " + joinLabel);
        this.program.appendInstruction(elseLabel + ":");
        node.elseBlock().accept(this);
        this.program.appendInstruction(joinLabel + ":");
    }

    @Override
    public void visit(WhileLoop node) {
        String condLabel = this.program.newLabel();
        String joinLabel = this.program.newLabel();
        
        program.appendInstruction(condLabel + ":");
        node.condition().accept(this);
        program.popInt("$t1");
        // If condition is true, jump to join label
        program.appendInstruction("	beqz $t1, " + joinLabel);
        node.body().accept(this);
        program.appendInstruction("	j " + condLabel);
        // End of while loop
        program.appendInstruction(joinLabel + ":");
        
    }

    @Override
    public void visit(Return node) {
    	node.argument().accept(this);
    	
    	// if we have a type that isn't void, return value should be on $v0 reg
    	Type returnType = this.tc.getType((Command) node.argument());
    	if (!returnType.equivalent(new VoidType())){
			program.popInt("$v0");
    	}
		program.appendInstruction("	j " + this.funcLabel);
    }

    @Override
    public void visit(ast.Error node) {
        String message = "CodeGen cannot compile a " + node;
        errorBuffer.append(message);
        throw new CodeGenException(message);
    }
}
