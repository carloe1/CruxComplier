package mips;

import java.util.HashMap;

import crux.Symbol;
import types.*;

public class ActivationRecord
{
    private static int fixedFrameSize = 2*4;
    private ast.FunctionDefinition func;
    private ActivationRecord parent;
    private int stackSize;
    private HashMap<Symbol, Integer> locals;
    private HashMap<Symbol, Integer> arguments;
    
    public static ActivationRecord newGlobalFrame()
    {
        return new GlobalFrame();
    }
    
    protected static int numBytes(Type type)
    {
    	if (type instanceof BoolType)
    		return 4;
        if (type instanceof IntType)
            return 4;
        if (type instanceof FloatType)
            return 4;
        if (type instanceof ArrayType) {
            ArrayType aType = (ArrayType)type;
            return aType.extent() * numBytes(aType.base());
        }
        throw new RuntimeException("No size known for " + type);
    }
    
    protected ActivationRecord()
    {
        this.func = null;
        this.parent = null;
        this.stackSize = 0;
        this.locals = null;
        this.arguments = null;
    }
    
    public ActivationRecord(ast.FunctionDefinition fd, ActivationRecord parent)
    {
        this.func = fd;
        this.parent = parent;
        this.stackSize = 0;
        this.locals = new HashMap<Symbol, Integer>();
        
        // map this function's parameters
        this.arguments = new HashMap<Symbol, Integer>();
        int offset = 0;
        for (int i=fd.arguments().size()-1; i>=0; --i) {
            Symbol arg = fd.arguments().get(i);
            arguments.put(arg, offset);
            offset += numBytes(arg.type());
        }
    }
    
    public String name()
    {
        return func.symbol().name();
    }
    
    public ActivationRecord parent()
    {
        return parent;
    }
    
    public int stackSize()
    {
        return stackSize;
    }
    
    public void add(Program prog, ast.VariableDeclaration var)
    {
        this.stackSize = numBytes(var.symbol().type());
        this.locals.put(var.symbol(),  -stackSize);
    }
    
    public void add(Program prog, ast.ArrayDeclaration array)
    {
        throw new RuntimeException("implement adding array to local function space");
    }
    
    public void getAddress(Program prog, String reg, Symbol sym)
    {
    	// First look at local variables
        if (this.locals.containsKey(sym)){
        	int offset = this.locals.get(sym);
        	String instruction = "	addi " + reg + ", $fp, " + (offset - fixedFrameSize);
        	prog.appendInstruction(instruction);
        }
        // Then look at function arguments
        else if (this.arguments.containsKey(sym)){
        	int offset = this.arguments.get(sym);
        	String instruction = "	addi " + reg + ", $fp, " + offset;
        	prog.appendInstruction(instruction);
        }
        // Look at some of the earlier scopes otherwise
        else if (this.parent != null){
        	this.parent.getAddress(prog, reg, sym);
        }
    }
}

class GlobalFrame extends ActivationRecord
{
    public GlobalFrame()
    {
    }
    
    private String mangleDataname(String name)
    {
        return "cruxdata." + name;
    }
    
    @Override
    public void add(Program prog, ast.VariableDeclaration var)
    {
    	Type varType = var.symbol().type();
        String variableName = mangleDataname(var.symbol().name());
        prog.appendData(variableName + ": .space " + numBytes(varType));
    }    
    
    @Override
    public void add(Program prog, ast.ArrayDeclaration array)
    {
    	Type arrayType = array.symbol().type();
        String arrayName = mangleDataname(array.symbol().name());
        prog.appendData(arrayName + ": .space " + numBytes(arrayType));
    }
        
    @Override
    public void getAddress(Program prog, String reg, Symbol sym)
    {
        prog.appendInstruction("	la " + reg + ", " + this.mangleDataname(sym.name()));
    }
}
