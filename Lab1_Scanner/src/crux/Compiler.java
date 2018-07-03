package crux;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Compiler {
    public static String studentName = "Carlos Estrada";
    public static String studentID = "58042643";
    public static String uciNetID = "carloe1";
	
	public static void main(String[] args) throws IOException
	{
        String sourceFile = args[0];
		
        Scanner s = null;
        
        try {
            s = new Scanner(new FileReader(sourceFile));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error accessing the source file: \"" + sourceFile + "\"");
            System.exit(-2);
        }
        
        Token t = s.next();
        while ( t != null) {
                System.out.println(t);
                t = s.next();
        }
    }
}
