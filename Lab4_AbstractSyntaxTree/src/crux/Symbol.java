package crux;

/*
 * Symbol Semantics
 * 
 * - An identifier must be declared before use. Not that this rule means crux does not
 * 	support mutual recursion, but it does support direct recursion.
 * 
 * - Identifier lookup is based on name only (not name and type).
 * 
 * - Only unique names may exit within any one scope.
 * 
 * - Symbols in an inner scope shadow symbols in outer scopes with the same name. Crux 
 *  offers no syntax for accessing names in an outer scope.
 * 
 * - Each scope (roughly) corresponds to a set of matching curly braces.
 * 
 * - Function parameters are scoped with the function body.
 */

public class Symbol {
    
    private String name;

    public Symbol(String name) {
        this.name = name;
    }
    
    public String name()
    {
        return this.name;
    }
    
    public String toString()
    {
        return "Symbol(" + name + ")";
    }

    public static Symbol newError(String message) {
        return new ErrorSymbol(message);
    }
}

class ErrorSymbol extends Symbol
{
    public ErrorSymbol(String message)
    {
        super(message);
    }
}
