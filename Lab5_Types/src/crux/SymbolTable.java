package crux;
import java.util.*;

/*
 * "Conceptually, the SymbolTable forms a liked list of Map<String, Symbol>s. Each table
 * has a pointer to a parent table, representing the outer scope. Valid Crux source code, 
 * contains balanced curly braces; for every open brace there is a close brace."
 */

public class SymbolTable {
	
	private int depth;							// Keeps track of the scope
	private SymbolTable parent;					// null if it's the most outer scope
	private Map<String, Symbol> sTable;			// The table keeping track of all the declarations
	
	public SymbolTable()
	{
		this.parent = null;
		this.depth = 0;
		this.sTable = new LinkedHashMap<String, Symbol>();
	}
	
    public SymbolTable( SymbolTable parent)
    {
    	this.parent = parent;
    	if (this.parent != null){
    		this.depth = parent.getDepth() + 1;
    	}
    	this.sTable = new LinkedHashMap<String, Symbol>();
    }
    
    public int getDepth()
    {
    	return this.depth;
    }
    
    public SymbolTable getParent()
    {
    	return this.parent;
    }
    
    // "The lookup(String name) method recursively walked the list of tables,
    // proceeding from the innermost scope to outermost scope. The method returns
    // the first Symbol matching name, otherwise it signals that no such symbol exits."
    //
    public Symbol lookup(String name) throws SymbolNotFoundError
    {
    	Symbol foundSymbol = this.getSymbol(name);
    	
    	if (foundSymbol != null ){
    		return foundSymbol;
    	}
    	
    	throw new SymbolNotFoundError(name);
    }
    
    public Symbol getSymbol(String name)
    {
    	Symbol foundSymbol = this.sTable.get(name);
    	
    	if (foundSymbol != null){
    		return foundSymbol;
    	}
    	else if (this.parent == null){
			return null;
		}
		
		return this.parent.getSymbol(name);
    }
    
    // First search the table to see if the variable has been declared first
    // If found a redelcaration is thrown, otherwise continue with the insertion.
    //
    public Symbol insert(String name) throws RedeclarationError
    {
    	Symbol declaration = this.sTable.get(name);
    	
    	if (declaration != null){
    		throw new RedeclarationError(declaration);
    	}
    	
    	declaration = new Symbol(name);
    	sTable.put(name, declaration);
    	
    	return declaration;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if ( this.parent != null)
            sb.append(parent.toString());
        
        String indent = new String();
        for (int i = 0; i < depth; i++) {
            indent += "  ";
        }
        
        for ( Symbol s: sTable.values())
        {
            sb.append(indent + s.toString() + "\n");
        }
        return sb.toString();
    }
}

class SymbolNotFoundError extends Error
{
    private static final long serialVersionUID = 1L;
    private String name;
    
    SymbolNotFoundError(String name)
    {
        this.name = name;
    }
    
    public String name()
    {
        return name;
    }
}

class RedeclarationError extends Error
{
    private static final long serialVersionUID = 1L;

    public RedeclarationError(Symbol sym)
    {
        super("Symbol " + sym + " being redeclared.");
    }
}
